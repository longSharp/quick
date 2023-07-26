package com.quick.member.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.common.constant.Constant;
import com.quick.member.common.constant.Query;
import com.quick.member.domain.dto.req.BasePageDTO;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: agent
 * @Package: com.star.boss.agent.domain.dao
 * @ClassName: IntegralAccountDao
 * @Author: zhulei
 * @Description:
 * @Version: 1.0
 */
public interface PageBaseMapper<T> extends BaseMapper<T> {

    /**
     * 如果分页条件为空，则不按分页查询,返回数据列表
     * @param basePageDTO
     * @param wrap
     * @return
     */
    default List<T> selectPageOrList(BasePageDTO basePageDTO, QueryWrapper<T> wrap) {
        //查询分页参数，若不传参则查询所有数据
        if (!StringUtils.isEmpty(basePageDTO.getCurPage())){
            Query<T> query = new Query<>();
            Map<String, Long> param = new HashMap<>();
            param.put(Constant.PAGE, basePageDTO.getCurPage());
            param.put(Constant.LIMIT, basePageDTO.getPageSize());


            IPage<T> page = query.getPage(param, basePageDTO.getOrder(), basePageDTO.getIsAsc());
            IPage<T> resPage = selectPage(page, wrap);
            return resPage.getRecords();
        }
        //如果没有指定排序字段则不排序
        if (!StringUtils.isEmpty(basePageDTO.getOrder())){
            wrap.orderBy(true, basePageDTO.getIsAsc() == null, basePageDTO.getOrder());
        }
        return selectList(wrap);
    }

    /**
     * 如果分页条件为空，则不按分页查询,返回分页信息，包括数据总数，总分页数，数据列表等
     * @param basePageDTO
     * @param wrap
     * @return
     */
    default IPage<T> selectPage(BasePageDTO basePageDTO, QueryWrapper<T> wrap){
        Query<T> query = new Query<>();
        Map<String, Long> param = new HashMap<>();
        param.put(Constant.PAGE,basePageDTO.getCurPage());
        param.put(Constant.LIMIT,basePageDTO.getPageSize());
        IPage<T> page = query.getPage(param, basePageDTO.getOrder(), basePageDTO.getIsAsc());
        IPage<T> resPage = selectPage(page, wrap);
        return resPage;
    }

}
