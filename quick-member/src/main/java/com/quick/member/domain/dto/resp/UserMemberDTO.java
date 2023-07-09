package com.quick.member.domain.dto.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
public class UserMemberDTO {
    /**
     * 会员类型
     */
    private String type;
    /**
     * 开始时间
     */
    private LocalDateTime startDate;
    /**
     * 结束时间
     */
    private LocalDateTime endDate;
    /**
     * 今日剩余次数
     */
    private Long toDayCount;
}
