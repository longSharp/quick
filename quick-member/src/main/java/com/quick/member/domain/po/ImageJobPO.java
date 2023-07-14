package com.quick.member.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.quick.member.common.enums.ImageJobStatus;
import com.quick.member.common.enums.ImageJobType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@TableName("image_job")
@Accessors(chain = true)
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class ImageJobPO extends AbstractPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("`user_id`")
    private Long userId;

    @TableField("`job_fk_id`")
    private Long jobFkID;

    @TableField("`prompt`")
    private String prompt;

    @TableField("`img_path`")
    private String imgPath;

    @TableField("`tags`")
    private String tags;

    @TableField("`job_status`")
    private ImageJobStatus jobStatus;

    @TableField("`job_schedule`")
    private String jobSchedule;

    @TableField("`message_id`")
    private String messageId;

    @TableField("`message_hash`")
    private String messageHash;

    @TableField("`job_type`")
    private ImageJobType jobType;
}
