package com.ler.two.dbaop.service;

import com.ler.two.dbaop.domain.UserAccount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lww
 * @since 2019-08-14
 */
public interface UserAccountService extends IService<UserAccount> {

	void addUser(UserAccount user);
}
