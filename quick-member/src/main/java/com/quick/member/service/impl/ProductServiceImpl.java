package com.quick.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.member.dao.ProductMapper;
import com.quick.member.domain.po.ProductPO;
import com.quick.member.service.ProductService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品表 服务实现类
 * </p>
 *
 * @author kougw
 * @since 2023-06-16
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductPO> implements ProductService {

}
