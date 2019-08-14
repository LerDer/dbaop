package com.ler.two.dbaop.controller;

import com.ler.two.dbaop.domain.UserAccount;
import com.ler.two.dbaop.service.UserAccountService;
import com.ler.two.dbaop.util.HttpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author lww
 * @since 2019-08-14
 */
@Api(value = "/user", description = "用户相关")
@RestController
@RequestMapping("/user")
public class UserAccountController {

	@Resource
	private UserAccountService userAccountService;

	@ApiOperation("测试事物")
	@PostMapping("/add")
	public HttpResult add() {
		UserAccount user = new UserAccount();
		user.setName("测试一下");
		userAccountService.addUser(user);
		return HttpResult.success();
	}


}
