package com.quick.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.member.common.config.exception.BusinessException;
import com.quick.member.common.enums.BalanceType;
import com.quick.member.common.enums.ResultCode;
import com.quick.member.common.enums.Status;
import com.quick.member.common.enums.UseAccountEvent;
import com.quick.member.dao.CardResourceMapper;
import com.quick.member.dao.UseAccountMapper;
import com.quick.member.dao.UseLogMapper;
import com.quick.member.domain.po.CardResourcePO;
import com.quick.member.domain.po.ProductPO;
import com.quick.member.domain.po.UseAccountPO;
import com.quick.member.domain.po.UseLogPO;
import com.quick.member.service.CardResourceService;
import com.quick.member.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 卡资源表 服务实现类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Service
public class CardResourceServiceImpl extends ServiceImpl<CardResourceMapper, CardResourcePO> implements CardResourceService {

    @Autowired
    private UseAccountMapper useAccountMapper;

    @Autowired
    private UseLogMapper useLogMapper;

    @Autowired
    private ProductService productService;

    @Transactional
    @Override
    public void cardResourceConvert(Long userId, String password) {
        LambdaQueryWrapper<CardResourcePO> cardQuery = new LambdaQueryWrapper<>();
        cardQuery.eq(CardResourcePO::getCardPassword,password).eq(CardResourcePO::getStatus, Status.VALID);
        List<CardResourcePO> cards = this.list(cardQuery);
        if(cards==null||cards.size()==0){
            throw new BusinessException(ResultCode.CARD_SOURCE_NOT_EXISTS);
        }
        if(cards.size()>1){
            throw new BusinessException(ResultCode.CARD_SOURCE_ERROR);
        }
        LambdaQueryWrapper<ProductPO> productQuery = new LambdaQueryWrapper<>();
        //TODO 卡密查询产品
        List<ProductPO> products = productService.list(productQuery);
        if(products==null||products.size()==0){
            throw new BusinessException(ResultCode.CARD_SOURCE_ERROR);
        }
        ProductPO product = products.get(0);
        switch (product.getType()){
            case COUNT_CARD:
                //更新次数账户
                QueryWrapper<UseAccountPO> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id",userId).eq("status", Status.VALID);
                UseAccountPO useAccount = useAccountMapper.selectOne(queryWrapper);
                useAccount.setBalanceCount(useAccount.getBalanceCount()+product.getUseCount());
                useAccountMapper.updateById(useAccount);
                //生成记录
                UseLogPO useLog = new UseLogPO();
                useLog.setType(BalanceType.BRANCH_IN)
                        .setEvent(UseAccountEvent.CARMI)
                        .setUseAccountId(useAccount.getId())
                        .setCount(product.getUseCount());
                useLogMapper.insert(useLog);
                break;
            case MEMBER_DAY:
                break;
            case MEMBER_WEEK:
                break;
            case MEMBER_MONTH:
                break;
            case MEMBER_PERMANENT:
                break;
            case MEMBER_YEAR:
                break;
            case MEMBER_QUARTER:
                break;

        }

        //兑换完毕，将卡密设置无效
        cards.get(0).setStatus(Status.INVALID);
        this.updateById(cards.get(0));

    }
}
