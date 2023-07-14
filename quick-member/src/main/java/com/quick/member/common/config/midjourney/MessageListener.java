package com.quick.member.common.config.midjourney;

import cn.hutool.json.JSONUtil;
import com.quick.member.common.constant.RedisKeyPrefixConstant;
import com.quick.member.common.enums.ImageJobStatus;
import com.quick.member.common.enums.ImageJobType;
import com.quick.member.dao.ImageJobMapper;
import com.quick.member.domain.po.ImageJobPO;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class MessageListener extends ListenerAdapter {

    @Autowired
    private ImageJobMapper imageJobMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.getAuthor().getId().equals("936929561302675456")) return;
        Message message = event.getMessage();
        String messageId = message.getId();
        String content = message.getContentRaw();
        log.info("############# message content: " + content);
        List<Message.Attachment> attachments = message.getAttachments();
        if(attachments.size()>0) {
            String taskId = getTaskId(content);
            if(taskId==null){
                return;
            }
            //查询redis并且删除redis
            String userId = redisTemplate.opsForValue().get(RedisKeyPrefixConstant.TASK_USER_ID + taskId);
            String taskStr = (String)redisTemplate.opsForHash().get(RedisKeyPrefixConstant.IMAGE_TASK + userId, taskId);
            ImageJobPO imageJobPO = JSONUtil.toBean(taskStr, ImageJobPO.class);
            Message.Attachment attachment = attachments.get(0);
            String url = attachment.getUrl();
            String[] mass = attachment.getFileName().split("_");
            String messageHash = null;
            if(mass.length>0){
                messageHash = mass[mass.length-1].split("\\.")[0];
            }
            ImageJobType jobType = imageJobPO.getJobType();
            String[] resUrl = url.split(".com/");
            imageJobPO.setJobStatus(ImageJobStatus.GENERATED)
                    .setImgPath(resUrl[1])
                    .setJobSchedule("100")
                    .setMessageId(messageId)
                    .setMessageHash(messageHash);
            imageJobMapper.updateById(imageJobPO);
            //更新完毕删除缓存
            redisTemplate.delete(RedisKeyPrefixConstant.TASK_USER_ID + taskId);
            redisTemplate.opsForHash().delete(RedisKeyPrefixConstant.IMAGE_TASK + userId, taskId);

        }
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        if(!event.getAuthor().getId().equals("936929561302675456")) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        List<Message.Attachment> attachments = message.getAttachments();
        if(attachments.size()>0) {
            String taskId = getTaskId(content);
            if(taskId==null){
                return;
            }
            Message.Attachment attachment = attachments.get(0);
            String url = attachment.getUrl();
            String[] resUrl = url.split(".com/");
            //查询redis并且更新redis
            String userId = redisTemplate.opsForValue().get(RedisKeyPrefixConstant.TASK_USER_ID + taskId);
            String taskStr = (String)redisTemplate.opsForHash().get(RedisKeyPrefixConstant.IMAGE_TASK + userId, taskId);
            ImageJobPO imageJobPO = JSONUtil.toBean(taskStr, ImageJobPO.class);
            if(imageJobPO==null||imageJobPO.getId()==null){
                return;
            }
            ImageJobType jobType = imageJobPO.getJobType();
            String schedule = getSchedule(content);
            switch (jobType) {
                case TEXT_GENERATE:
                    if(schedule==null){
                        return;
                    }
                    imageJobPO.setJobSchedule(schedule)
                            .setImgPath(resUrl[1]);
                    redisTemplate.opsForHash().put(RedisKeyPrefixConstant.IMAGE_TASK + userId,taskId,JSONUtil.toJsonStr(imageJobPO));
                    break;
                case UPSCALE:
                    imageJobPO.setJobSchedule("99")
                            .setImgPath(resUrl[1]);
                    redisTemplate.opsForHash().put(RedisKeyPrefixConstant.IMAGE_TASK + userId,taskId,JSONUtil.toJsonStr(imageJobPO));
            }
        }
    }

    private String getTaskId(String input){
        // 正则表达式匹配数字，要求前面是"[Ignore this "，后面是"]"
        String pattern = "(?<=\\(\\[Ignore this )\\d+(?=\\]\\))";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);

        // 查找并输出匹配到的数字
        if (matcher.find()) {
            return matcher.group();
        }
        return null;


    }

    private String getSchedule(String input){
        String pattern1 = "(?<=\\> \\()\\d+(?=%\\))";
        Pattern regex1 = Pattern.compile(pattern1);
        Matcher matcher1 = regex1.matcher(input);

        // 查找并输出匹配到的数字
        if (matcher1.find()) {
            return matcher1.group();
        }
        return null;
    }
}
