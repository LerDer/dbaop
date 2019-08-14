package com.ler.two.dbaop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ler.two.dbaop.config.aspect.MultiTransactional;
import com.ler.two.dbaop.dao.demo1.UserAccountDao;
import com.ler.two.dbaop.dao.demo2.UserOrderDao;
import com.ler.two.dbaop.domain.UserAccount;
import com.ler.two.dbaop.domain.UserOrder;
import com.ler.two.dbaop.service.UserAccountService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2019-08-14
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountDao, UserAccount> implements UserAccountService {

	@Resource
	private UserAccountDao userAccountDao;

	@Resource
	private UserOrderDao userOrderDao;

	@Override
	@MultiTransactional({"transactionManagerDemo1", "transactionManagerDemo2"})
	public void addUser(UserAccount user) {
		int insert = userAccountDao.insert(user);
		System.out.println("insert = " + insert);

		int i = 1 / 0;

		Long id = user.getId();
		UserOrder order = new UserOrder();
		order.setUserId(id);
		order.setOrderName("测试订单");
		int insert1 = userOrderDao.insert(order);
		System.out.println("insert1 = " + insert1);
	}
}

