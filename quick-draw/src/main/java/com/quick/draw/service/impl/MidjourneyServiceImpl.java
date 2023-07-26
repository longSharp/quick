package com.quick.draw.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.common.constant.RedisKeyPrefixConstant;
import com.quick.common.enums.ResultCode;
import com.quick.common.enums.Status;
import com.quick.common.exception.BusinessException;
import com.quick.common.utils.TransApi;
import com.quick.draw.common.config.params.MidjourneyParamsConfig;
import com.quick.draw.common.constant.MidJourneyUrlConstant;
import com.quick.draw.common.enums.ImageJobStatus;
import com.quick.draw.common.enums.ImageJobType;
import com.quick.draw.dao.ImageJobMapper;
import com.quick.draw.dao.PromptLabelMapper;
import com.quick.draw.domain.dto.req.MidjourneyGenerateReqDTO;
import com.quick.draw.domain.dto.req.MidjourneyOtherReqDTO;
import com.quick.draw.domain.dto.req.MidjourneyResetReqDTO;
import com.quick.draw.domain.dto.req.MidjourneyUpsampleReqDTO;
import com.quick.draw.domain.dto.resp.BaiduTransRespDTO;
import com.quick.draw.domain.dto.resp.BaidutransResult;
import com.quick.draw.domain.dto.resp.DiscordSendMsgRespDTO;
import com.quick.draw.domain.dto.resp.DiscordUploadRespDTO;
import com.quick.draw.domain.po.ImageJobPO;
import com.quick.draw.domain.po.PromptLabelPO;
import com.quick.draw.service.MidjourneyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MidjourneyServiceImpl implements MidjourneyService {

    @Autowired
    private MidjourneyParamsConfig midjourneyParamsConfig;

    @Autowired
    private ImageJobMapper imageJobMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    public TransApi transApi;

    @Autowired
    private PromptLabelMapper promptLabelMapper;

    @Transactional
    @Override
    public void generateImages(MidjourneyGenerateReqDTO dto, Long userId) {
        ImageJobPO imageJobPO = new ImageJobPO();
        imageJobPO.setJobStatus(ImageJobStatus.GENERATING)
                .setUserId(userId)
                .setPrompt(dto.getPrompt())
                .setJobSchedule("0")
                .setJobType(ImageJobType.TEXT_GENERATE);
        List<Long> tags = dto.getTags();
        StringBuilder prompts = new StringBuilder();
        StringBuilder params = new StringBuilder();
        StringBuilder tagIds = new StringBuilder();
        if(tags!=null&&tags.size()>0){
            List<PromptLabelPO> tagsPO = promptLabelMapper.selectBatchIds(tags);
            for (PromptLabelPO tag : tagsPO) {
                tagIds.append(tag.getId());
                tagIds.append("|");
                if(StringUtils.isEmpty(tag.getParam())){
                    prompts.append(",");
                    prompts.append(tag.getTemplate());
                    continue;
                }
                params.append(" ");
                params.append(tag.getParam());
            }
        }
        imageJobPO.setTags(tagIds.toString());

        Long jobId = imageJobPO.getId();
        long random = RandomUtil.randomLong(0, 429496724);
        //中文转英文
        String transResult = transApi.getTransResult(dto.getPrompt(), "auto", "en");
        BaiduTransRespDTO baiduTransRespDTO = JSONUtil.toBean(transResult, BaiduTransRespDTO.class);
        if(baiduTransRespDTO==null||baiduTransRespDTO.getTrans_result()==null||baiduTransRespDTO.getTrans_result().size()<1){
            throw new BusinessException(ResultCode.REQUEST_FAIL);
        }
        BaidutransResult baidutransResult = baiduTransRespDTO.getTrans_result().get(0);
        String props = baidutransResult.getDst()+prompts+" "+"--seed "+random;
        int hashCode = props.hashCode();
        imageJobPO.setHash(hashCode);
        log.info("generateImages userId:"+userId);
        log.info("generateImages hashCode:"+hashCode);
//        redisTemplate.opsForHash().put(RedisKeyPrefixConstant.IMAGE_TASK+userId,jobId+"",JSONUtil.toJsonStr(imageJobPO));
//        redisTemplate.opsForValue().set(RedisKeyPrefixConstant.TASK_USER_ID+jobId,userId+"");

//        String promptTrans = "([Ignore this " +
//                jobId +
//                "])"+baidutransResult.getDst()+prompts+params;
        String promptTrans = props+params;
        imageJobMapper.insert(imageJobPO);
        redisTemplate.opsForHash().put(RedisKeyPrefixConstant.IMAGE_TASK+userId,hashCode+"",JSONUtil.toJsonStr(imageJobPO));
        redisTemplate.opsForValue().set(RedisKeyPrefixConstant.TASK_USER_ID+hashCode,userId+"");
        HttpRequest post = HttpUtil.createPost(MidJourneyUrlConstant.GET_IMAGES);
        Map<String, Object> data = new HashMap<>();
        data.put("type",1);
        data.put("version","1118961510123847772");
        data.put("id","938956540159881230");
        data.put("name","imagine");
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        obj.put("type",3);
        obj.put("name","prompt");
        obj.put("value", promptTrans);
        list.add(obj);
        data.put("options",list);
        Map<String, Object> body = midjourneyParamsConfig.getBody(2, data, null);
        Map<String, String> header = midjourneyParamsConfig.getHeader();
        execute(post, body, header,userId,jobId);
    }

    @Transactional
    @Override
    public void upsample(MidjourneyUpsampleReqDTO dto) {
        MidjourneyOtherReqDTO midjourneyOtherReqDTO = new MidjourneyOtherReqDTO();
        midjourneyOtherReqDTO.setType(ImageJobType.UPSCALE);
        midjourneyOtherReqDTO.setTaskId(dto.getTaskId());
        this.other(midjourneyOtherReqDTO,dto.getIndex());
    }

    @Override
    public void imageGenerateImage(MultipartFile file, MidjourneyGenerateReqDTO dto,Long userId) throws IOException {
        //TODO 1.上传图片
        String imgUrl = upload(file);
        if(imgUrl==null){
            throw new BusinessException(ResultCode.GENERATE_FAIL);
        }
        //TODO 2.生成图片
        ImageJobPO imageJobPO = new ImageJobPO();
        imageJobPO.setJobStatus(ImageJobStatus.GENERATING)
                .setUserId(userId)
                .setPrompt(dto.getPrompt())
                .setJobSchedule("0")
                .setJobType(ImageJobType.IMAGE_GENERATE);
        List<Long> tags = dto.getTags();
        StringBuilder prompts = new StringBuilder();
        StringBuilder params = new StringBuilder();
        StringBuilder tagIds = new StringBuilder();
        if(tags!=null&&tags.size()>0){
            List<PromptLabelPO> tagsPO = promptLabelMapper.selectBatchIds(tags);
            for (PromptLabelPO tag : tagsPO) {
                tagIds.append(tag.getId());
                tagIds.append("|");
                if(StringUtils.isEmpty(tag.getParam())){
                    prompts.append(",");
                    prompts.append(tag.getTemplate());
                    continue;
                }
                params.append(" ");
                params.append(tag.getParam());
            }
        }
        imageJobPO.setTags(tagIds.toString());
        //中文转英文
        String transResult = transApi.getTransResult(dto.getPrompt(), "auto", "en");
        BaiduTransRespDTO baiduTransRespDTO = JSONUtil.toBean(transResult, BaiduTransRespDTO.class);
        if(baiduTransRespDTO==null||baiduTransRespDTO.getTrans_result()==null||baiduTransRespDTO.getTrans_result().size()<1){
            throw new BusinessException(ResultCode.REQUEST_FAIL);
        }
        BaidutransResult baidutransResult = baiduTransRespDTO.getTrans_result().get(0);
        long random = RandomUtil.randomLong(0, 429496724);
        String props = baidutransResult.getDst()+prompts+" "+"--seed "+random;
        int hashCode = props.hashCode();
        log.info("generateImages userId:"+userId);
        log.info("generateImages jobId:"+hashCode);
        String promptTrans = imgUrl+props+params;
        imageJobMapper.insert(imageJobPO);
        Long jobId = imageJobPO.getId();
        redisTemplate.opsForHash().put(RedisKeyPrefixConstant.IMAGE_TASK+userId,hashCode+"",JSONUtil.toJsonStr(imageJobPO));
        redisTemplate.opsForValue().set(RedisKeyPrefixConstant.TASK_USER_ID+hashCode,userId+"");
        HttpRequest post = HttpUtil.createPost(MidJourneyUrlConstant.GET_IMAGES);
        Map<String, Object> data = new HashMap<>();
        data.put("type",1);
        data.put("version","1118961510123847772");
        data.put("id","938956540159881230");
        data.put("name","imagine");
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> obj = new HashMap<>();
        obj.put("type",3);
        obj.put("name","prompt");
        obj.put("value", promptTrans);
        list.add(obj);
        data.put("options",list);
        Map<String, Object> body = midjourneyParamsConfig.getBody(2, data, null);
        Map<String, String> header = midjourneyParamsConfig.getHeader();
        execute(post, body, header,userId,jobId);
    }

    @Transactional
    @Override
    public void other(MidjourneyOtherReqDTO dto, Integer index) {
        ImageJobPO rand = imageJobMapper.selectById(dto.getTaskId());
        ImageJobPO father = getFatherTask(rand);
        checkTask(father.getId());
        ImageJobPO imageJobPO = new ImageJobPO();
        int random = RandomUtil.randomInt(0, 90);
        imageJobPO.setJobStatus(ImageJobStatus.GENERATING)
                .setUserId(father.getUserId())
                .setPrompt(father.getPrompt())
                .setJobSchedule(""+random)
                .setJobType(dto.getType())
                .setJobFkID(father.getId())
                .setTags(father.getTags());
        imageJobMapper.insert(imageJobPO);
        Map<String, Object> data = new HashMap<>();

        switch (dto.getType()){
            case PAN_LEFT:
                data.put("custom_id","MJ::JOB::pan_left::1::"+rand.getMessageHash()+"::SOLO");
                break;
            case PAN_RIGHT:
                data.put("custom_id","MJ::JOB::pan_right::1::"+rand.getMessageHash()+"::SOLO");
                break;
            case PAN_UP:
                data.put("custom_id","MJ::JOB::pan_up::1::"+rand.getMessageHash()+"::SOLO");
                break;
            case PAN_DOWN:
                data.put("custom_id","MJ::JOB::pan_down::1::"+rand.getMessageHash()+"::SOLO");
                break;
            case OUTPAINT_2:
                data.put("custom_id","MJ::Outpaint::50::1::"+rand.getMessageHash()+"::SOLO");
                break;
            case OUTPAINT_1_5:
                data.put("custom_id","MJ::Outpaint::75::1::"+rand.getMessageHash()+"::SOLO");
                break;
            case LOW_VARIATION:
                data.put("custom_id","MJ::JOB::low_variation::1::"+rand.getMessageHash()+"::SOLO");
                break;
            case HIGH_VARIATION:
                data.put("custom_id","MJ::JOB::high_variation::1::"+rand.getMessageHash()+"::SOLO");
                break;
            case RESET:
                data.put("custom_id","MJ::JOB::reroll::0::"+rand.getMessageHash()+"::SOLO");
                break;
            case UPSCALE:
                String rule = rand.getRule();
                if(StringUtils.isEmpty(rule)){
                    rand.setRule("upscale:"+index);
                }else{
                    rand.setRule(rule+","+index);
                }
                imageJobMapper.updateById(rand);
                data.put("custom_id","MJ::JOB::upsample::"+index+"::"+rand.getMessageHash());
                break;
            case VARIATION:
                data.put("custom_id","MJ::JOB::variation::"+index+"::"+rand.getMessageHash());
                break;
        }
        redisTemplate.opsForHash().put(RedisKeyPrefixConstant.IMAGE_TASK+father.getUserId(),father.getHash()+"",JSONUtil.toJsonStr(imageJobPO));
        redisTemplate.opsForValue().set(RedisKeyPrefixConstant.TASK_USER_ID+father.getHash(),father.getUserId()+"");
        HttpRequest post = HttpUtil.createPost(MidJourneyUrlConstant.GET_IMAGES);
        data.put("component_type",2);
        Map<String, Object> other = new HashMap<>();
        other.put("message_flags",0);
        other.put("message_id",rand.getMessageId());
        Map<String, Object> body = midjourneyParamsConfig.getBody(3, data, other);
        Map<String, String> header = midjourneyParamsConfig.getHeader();
        execute(post, body, header,father.getUserId(),father.getId());
    }

    @Override
    public void variation(MidjourneyUpsampleReqDTO dto) {
        MidjourneyOtherReqDTO midjourneyOtherReqDTO = new MidjourneyOtherReqDTO();
        midjourneyOtherReqDTO.setType(ImageJobType.VARIATION);
        midjourneyOtherReqDTO.setTaskId(dto.getTaskId());
        this.other(midjourneyOtherReqDTO,dto.getIndex());
    }


    @Override
    public void reset(MidjourneyResetReqDTO dto) {
        MidjourneyOtherReqDTO midjourneyOtherReqDTO = new MidjourneyOtherReqDTO();
        midjourneyOtherReqDTO.setType(ImageJobType.RESET);
        midjourneyOtherReqDTO.setTaskId(dto.getTaskId());
        this.other(midjourneyOtherReqDTO,null);
    }

    private void checkTask(Long taskId){
        LambdaQueryWrapper<ImageJobPO> query = new LambdaQueryWrapper<>();
        query.eq(ImageJobPO::getJobFkID,taskId)
                .eq(ImageJobPO::getStatus, Status.VALID)
                .in(ImageJobPO::getJobStatus,ImageJobStatus.GENERATING,ImageJobStatus.CREATED,ImageJobStatus.CREATING);
        List<ImageJobPO> list = imageJobMapper.selectList(query);
        if(list.size()>0){
            throw new BusinessException(ResultCode.HAVE_TASK_RUN);
        }
    }

    private ImageJobPO getFatherTask(ImageJobPO father){
        if(father==null||father.getMessageId()==null||father.getMessageHash()==null){
            throw new BusinessException(ResultCode.NOT_FOND_TASK);
        }
        if(father.getJobFkID()!=null){
            ImageJobPO fatherTemp = imageJobMapper.selectById(father.getJobFkID());
            if(fatherTemp!=null){
                return fatherTemp;
            }
        }
        return father;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        Map<String, String> header = midjourneyParamsConfig.getHeader();
        //TODO 1.先得到预上传任务
        String uploadURL=MidJourneyUrlConstant.UPLOAD_ATTACHMENT_URL.replace("CHANNEL_ID",midjourneyParamsConfig.getChannelId());
        HttpRequest upload = HttpUtil.createPost(uploadURL);
        upload.headerMap(header,true);
        List<Map<String, Object>> maps = new ArrayList<>();
        Map<String, Object> bodyData = new HashMap<>();
        Map<String, List<Map<String, Object>>> body = new HashMap<>();
        bodyData.put("filename",fileName);
        bodyData.put("file_size",fileSize);
        bodyData.put("id","0");
        maps.add(bodyData);
        body.put("files",maps);
        String bodyStr = JSONUtil.toJsonStr(body);
        upload.body(bodyStr);
        HttpResponse execute = upload.execute();
        if(execute.getStatus()!=200){
            log.info("Upload failed body:"+execute.body());
            log.info("Upload failed status:"+execute.getStatus());
            return null;
        }
        String uploadResp = execute.body();
        DiscordUploadRespDTO discordUploadRespDTO = JSONUtil.toBean(uploadResp, DiscordUploadRespDTO.class);
        String upload_url = discordUploadRespDTO.getAttachments().get(0).getUpload_url();
        String upload_filename = discordUploadRespDTO.getAttachments().get(0).getUpload_filename();

        //TODO 2.执行预上传任务
        HttpRequest preUpload = HttpUtil.createRequest(Method.PUT,upload_url);
        Map<String, String> preHeader = new HashMap<>();
        preHeader.put("Content-Type","image/png");
        preUpload.body(bytes);
        preUpload.headerMap(preHeader,true);
        HttpResponse preUploadResp = preUpload.execute();
        if(preUploadResp.getStatus()!=200){
            log.info("preUpload Failed body:"+preUploadResp.body());
            log.info("preUpload Failed status:"+preUploadResp.getStatus());
            return null;
        }
        //TODO 3.发送上传消息
        String sendMsgURL=MidJourneyUrlConstant.SEND_MESSAGE_URL.replace("CHANNEL_ID",midjourneyParamsConfig.getChannelId());
        HttpRequest sendMsg = HttpUtil.createPost(sendMsgURL);
        HashMap<String, Object> sendBody = new HashMap<>();
        sendBody.put("content","");
        sendBody.put("channel_id",midjourneyParamsConfig.getChannelId());
        sendBody.put("type",0);
        sendBody.put("sticker_ids",new ArrayList<>());
        List<Map<String, String>> attachments = new ArrayList<>();
        Map<String, String> attachment = new HashMap<>();
        log.info("upload_filename:"+upload_filename);
        attachment.put("id","0");
        attachment.put("filename",upload_filename.split("/")[upload_filename.split("/").length-1]);
        attachment.put("uploaded_filename",upload_filename);
        attachments.add(attachment);
        sendBody.put("attachments",attachments);
        String sendBodyStr = JSONUtil.toJsonStr(sendBody);
        sendMsg.headerMap(header,true);
        sendMsg.body(sendBodyStr);
        log.info("body:"+sendBodyStr);
        HttpResponse sendMsgResp = sendMsg.execute();
        if(sendMsgResp.getStatus()!=200){
            log.info("sendMsgResp Failed body:"+sendMsgResp.body());
            log.info("sendMsgResp Failed status:"+sendMsgResp.getStatus());
            return null;
        }
        DiscordSendMsgRespDTO discordSendMsgRespDTO = JSONUtil.toBean(sendMsgResp.body(), DiscordSendMsgRespDTO.class);
        return discordSendMsgRespDTO.getAttachments().get(0).getUrl();
    }

    private void execute(HttpRequest method, Map<String, Object> body, Map<String, String> header, Long userId, Long taskId){
        String bodyStr = JSONUtil.toJsonStr(body);
        method.body(bodyStr);
        method.headerMap(header,true);
        HttpResponse response = method.execute();
        log.info("###response status:"+response.getStatus());
        if(response.getStatus()!=204){
            String respBody = response.body();
            log.info("###Response: " + respBody);
            redisTemplate.delete(RedisKeyPrefixConstant.TASK_USER_ID+taskId);
            redisTemplate.opsForHash().delete(RedisKeyPrefixConstant.IMAGE_TASK+userId,taskId+"");
            throw new BusinessException(ResultCode.GENERATE_FAIL);
        }
    }
}
