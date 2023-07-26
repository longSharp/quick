package com.quick.gpt.domain.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Document("user_problem_log")
@Data
@Accessors(chain = true)
public class UserProblemLogPO {
    /**
     * id
     */
    @Id
    private String id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 类型：文字/图片
     */
    private Integer type;
    /**
     * 问题内容
     */
    private String question;
    /**
     * 回复内容
     */
    private String answer;
    /**
     * 图片路径
     */
    private String picturePath;
    /**
     * 回答时间(yyyy-MM-dd HH:mm:ss)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime askTime;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 对话id
     */
    private Long dialogueId;
    /**
     * 上限tokens
     */
    private Integer tokens;
    /**
     * 是否是主题对话
     */
    private Boolean topic;
}
