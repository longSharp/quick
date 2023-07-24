package com.quick.member.domain.dto.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * @ProjectName: agent
 * @Package: com.star.boss.agent.app.dto
 * @ClassName: QueryPageDTO
 * @Author: Longcm
 * @Description: 分页查询
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BasePageDTO {
    /**
     * 每页数据量
     */
    private Long pageSize;

    /**
     * 当前页
     */
    private Long curPage;

    /**
     * 排序字段
     */
    private String order;

    /**
     * 排序方式:true 升序，false 降序
     */
    private Boolean isAsc;
}
