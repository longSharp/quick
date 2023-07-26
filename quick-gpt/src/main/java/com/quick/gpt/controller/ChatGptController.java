package com.quick.gpt.controller;

import cn.hutool.dfa.SensitiveUtil;
import cn.hutool.json.JSONUtil;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.quick.common.config.UserHolder;
import com.quick.common.dto.req.AppSession;
import com.quick.common.dto.resp.R;
import com.quick.common.enums.ResultCode;
import com.quick.common.service.ISessionCache;
import com.quick.common.utils.DESUtil;
import com.quick.gpt.config.ChatGptParamsConfig;
import com.quick.gpt.domain.po.SysUserPO;
import com.quick.gpt.domain.po.UseAccountPO;
import com.quick.gpt.domain.po.UserMemberPO;
import com.quick.gpt.domain.po.UserProblemLogPO;
import com.quick.gpt.enums.MemberMark;
import com.quick.gpt.enums.ProblemType;
import com.quick.gpt.enums.chat.RoleEnum;
import com.quick.gpt.enums.exception.ChatGPTErrorEnum;
import com.quick.gpt.exception.ChatGPTException;
import com.quick.gpt.feign.SysUserService;
import com.quick.gpt.feign.UseAccountService;
import com.quick.gpt.feign.UserMemberService;
import com.quick.gpt.service.UserProblemLogService;
import com.quick.gpt.util.OpenAiUtils;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/gpt")
@Validated
public class ChatGptController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private UserMemberService userMemberService;
    @Autowired
    private UseAccountService useAccountService;
    @Autowired
    private UserProblemLogService userProblemLogService;
    @Autowired
    private ISessionCache sessionRedisCache;
    @Autowired
    private ChatGptParamsConfig config;

    @PostMapping(value = "/sendStream")
    public void sendStream(@NotNull @RequestParam Long dialogId,@Valid @NotBlank @RequestBody String content, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionId = request.getHeader("sessionId");
        ServletOutputStream os = response.getOutputStream();
        if(StringUtils.isEmpty(sessionId)){
            os.write(ResultCode.SESSION_EMPTY.getMsg().getBytes(Charset.defaultCharset()));
            os.flush();
            os.close();
            return;
        }
        String[] userIds = sessionId.split("-");
        if(userIds.length<2){
            os.write(ResultCode.LOGIN_TIME_OUT.getMsg().getBytes(Charset.defaultCharset()));
            os.flush();
            os.close();
            return;
        }
        String remoteAddr = request.getRemoteAddr();
        AppSession sessionObj = sessionRedisCache.getSessionForHash(userIds[1], remoteAddr);
        if(sessionObj==null||!sessionObj.getId().equals(sessionId)){
            os.write(ResultCode.LOGIN_TIME_OUT.getMsg().getBytes(Charset.defaultCharset()));
            os.flush();
            os.close();
            return;
        }
        Long userId = Long.parseLong(userIds[1]);
        //1.查询用户是否是会员
        R<String> userResult = sysUserService.getById(userId);
        SysUserPO user = getData(userResult,SysUserPO.class,os);
        if(user==null) return;
        UserMemberPO userMemberPO = null;
        R<String> useAccountResult = useAccountService.queryUseAccountByUserId(userId);
        UseAccountPO useAccountPO = getData(useAccountResult, UseAccountPO.class, os);
        //2.如果不是会员，检查用户次数账户是否还有次数
        if(useAccountPO==null){
            os.write(ResultCode.NOT_FIND_USE_ACCOUNT.getMsg().getBytes(Charset.defaultCharset()));
            os.flush();
            os.close();
            return;
        }
        if(MemberMark.MEMBER.equals(user.getIsMember())){
            //3.如果是会员，检查当日会员使用次数是否使用完毕
            R<String> memberResult= userMemberService.queryMemberByUserId(userId);
            userMemberPO = getData(memberResult, UserMemberPO.class, os);
            if(userMemberPO==null){
                os.write(ResultCode.MEMBER_EXPIRED.getMsg().getBytes(Charset.defaultCharset()));
                os.flush();
                os.close();
                return;
            }
            Long todayBalanceCount = userMemberPO.getDialogBalance();
            if(useAccountPO.getBalanceCount()<=0&&todayBalanceCount<=0&&useAccountPO.getGiveCount()<=0){
                os.write(ResultCode.USE_ACCOUNT_BALANCE_INSUFFICIENT.getMsg().getBytes(Charset.defaultCharset()));
                os.flush();
                os.close();
                return;
            }
        }else{
            if(useAccountPO.getBalanceCount()<=0&&useAccountPO.getGiveCount()<=0){
                os.write(ResultCode.USE_ACCOUNT_BALANCE_INSUFFICIENT.getMsg().getBytes(Charset.defaultCharset()));
                os.flush();
                os.close();
                return;
            }
        }

        //4.从mongodb取出历史记录
        setResponse(response);
        List<UserProblemLogPO> userProblemLogPOS = userProblemLogService.queryAllByUserIdAndDiaId(userId,dialogId, ProblemType.TEXT);
        List<ChatMessage> chatMessages = new ArrayList<>();
        boolean isSens = SensitiveUtil.containsSensitive(content);
        if(isSens){
            os.write(ResultCode.SENSITIVE_ERROR.getMsg().getBytes(Charset.defaultCharset()));
            os.flush();
            os.close();
            return;
        }
        //是否token数达到上限
        Boolean isUpper=false;
        for (UserProblemLogPO item : userProblemLogPOS) {
            ChatMessage assistantMsg = new ChatMessage(RoleEnum.ASSISTANT.getRoleName(), item.getAnswer());
            ChatMessage userMsg = new ChatMessage(RoleEnum.USER.getRoleName(), item.getQuestion());
            if(item.getTokens()!=null){
                isUpper = true;
            }
            chatMessages.add(userMsg);
            chatMessages.add(assistantMsg);
        }
        ChatMessage userMsg = new ChatMessage(RoleEnum.USER.getRoleName(), content);
        chatMessages.add(userMsg);
        ChatMessage resultMsg = checkLength(isUpper,chatMessages, userId+dialogId + "", os);
        if(resultMsg==null) {
            return;
        }
        userProblemLogService.saveAndUpdate(dialogId,isUpper,userMemberPO, useAccountPO, resultMsg.getContent(), content, MemberMark.MEMBER.equals(user.getIsMember()));
    }

    @RequestMapping(value = "/getGptLog/{type}/{dialogId}")
    public R<String> getGptLog(@NotNull @PathVariable Integer type,@NotNull @PathVariable Long dialogId){
        Long userId = UserHolder.getUserId();
        List<UserProblemLogPO> userProblemLogPOS = userProblemLogService.queryAllByUserIdAndDiaId(userId,dialogId,ProblemType.valueOf(type));
        if(userProblemLogPOS!=null&&userProblemLogPOS.size()>=1&&userProblemLogPOS.get(0).getTopic()!=null&&userProblemLogPOS.get(0).getTopic()){
            userProblemLogPOS.remove(0);
        }
        List<ChatMessage> chatMessages = new ArrayList<>();
        for (UserProblemLogPO item : userProblemLogPOS) {
            ChatMessage assistantMsg = new ChatMessage(RoleEnum.ASSISTANT.getRoleName(), item.getAnswer());
            ChatMessage userMsg = new ChatMessage(RoleEnum.USER.getRoleName(), item.getQuestion());
            chatMessages.add(userMsg);
            chatMessages.add(assistantMsg);
        }
        return R.ok(chatMessages);
    }

    @RequestMapping(value = "/createImg")
    public void createImg(@NotBlank @RequestParam String content, HttpServletResponse response) {
        OpenAiUtils.downloadImage(content, response);
    }

    private <T> T getData(R<String> result,Class<T> clazz,ServletOutputStream os) throws IOException {
        if(!result.getCode().equals(ResultCode.REQUEST_SUCCESS.getCode())){
            os.write(ResultCode.REQUEST_FAIL.getMsg().getBytes(Charset.defaultCharset()));
            os.flush();
            os.close();
            return null;
        }
        String decode = DESUtil.decrypt(result.getData());
        return JSONUtil.toBean(decode, clazz);
    }

    private ChatMessage checkLength(Boolean isUpper,List<ChatMessage> messages,String userId, OutputStream os) throws IOException {
        ChatMessage resultMsg = null;
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding enc = registry.getEncoding(EncodingType.CL100K_BASE);
        String json = JSONUtil.toJsonStr(messages);
        List<ChatMessage> chatMessagesList = null;
        if(isUpper){
            int tokens = (int) (enc.encode(json).size()*0.85);
            int jk = tokens/config.getTokens();
            chatMessagesList = new ArrayList<>();
            chatMessagesList.add(messages.get(0));
            chatMessagesList.add(messages.get(1));
            for(int i=messages.size()/(jk+1);i<messages.size();i++){
                chatMessagesList.add(messages.get(i));
            }
        }

        try {
            resultMsg = OpenAiUtils.createStreamChatCompletionForResp(chatMessagesList==null?messages:chatMessagesList, userId + "", os);
            return resultMsg;
        }catch (ChatGPTException exception){
            if(ChatGPTErrorEnum.LENGTH_ERROR.getErrorCode().equals(exception.getErrorCode())){
                isUpper = true;
                if(messages.size()<2){
                    os.write(ResultCode.LENGTH_ERROR.getMsg().getBytes(Charset.defaultCharset()));
                    os.flush();
                    os.close();
                    return null;
                }
                ArrayList<ChatMessage> chatMessages = new ArrayList<>();
                for(int i=messages.size()/2;i<messages.size();i++){
                    chatMessages.add(messages.get(i));
                }
                return checkLength(isUpper,chatMessages,userId,os);
            }
            if(ChatGPTErrorEnum.FAILED_TO_GENERATE_ANSWER.getErrorCode().equals(exception.getErrorCode())){
                os.write(ResultCode.NETWORK_ERROR.getMsg().getBytes(Charset.defaultCharset()));
                os.flush();
                os.close();
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
            os.write(ResultCode.NETWORK_ERROR.getMsg().getBytes(Charset.defaultCharset()));
            os.flush();
            os.close();
            return null;
        }
        return resultMsg;
    }

    private void setResponse(HttpServletResponse response){
        // 需要指定response的ContentType为流式输出，且字符编码为UTF-8
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        // 禁用缓存
        response.setHeader("Cache-Control", "no-cache");
    }
}
