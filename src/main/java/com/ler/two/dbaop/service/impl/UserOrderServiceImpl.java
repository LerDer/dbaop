package com.ler.two.dbaop.service.impl;

import com.ler.two.dbaop.domain.UserOrder;
import com.ler.two.dbaop.dao.demo2.UserOrderDao;
import com.ler.two.dbaop.service.UserOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户订单表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2019-08-14
 */
@Service
public class UserOrderServiceImpl extends ServiceImpl<UserOrderDao, UserOrder> implements UserOrderService {

}
