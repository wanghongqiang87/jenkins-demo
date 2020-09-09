package com.neo.controller;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.neo.pojo.UserLogin;
import com.neo.service.UserService;
import com.neo.util.JWTUtil;
import com.neo.util.ResponseCodeEnum;
import com.neo.util.ResponseResult;
import com.neo.vo.LoginRequest;
import com.neo.vo.LoginResponse;

@RestController
public class LoginController {
	private static final String secretKey = "fddghjsgdfvbfdgfhsgdfehg";
	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private UserService userService;

	/**
	 * 登录
	 */
	@PostMapping("/login")
	public ResponseResult login(@RequestBody @Validated LoginRequest request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseResult.error(ResponseCodeEnum.PARAMETER_ILLEGAL.getCode(),
					ResponseCodeEnum.PARAMETER_ILLEGAL.getMessage());
		}

		String username = request.getUsername();
		String password = request.getPassword();
		// 假设查询到用户ID是1001
		// String userId = "1001";

		UserLogin userLogin = userService.findByid(username, password);
		if (userLogin != null) {
			// 生成Token
			String token = JWTUtil.generateToken(userLogin.getUserId(), secretKey);
			// 生成刷新Token
			String refreshToken = UUID.randomUUID().toString().replace("-", "");
			List<?> menus = userService.findAllMenu(userLogin.getRoleId());

			// 放入缓存
//			ValueOperations opsForValue = redisTemplate.opsForValue();
			HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
			// hashOperations.put(refreshToken, "token", token);
			// hashOperations.put(refreshToken, "user", username);
			// stringRedisTemplate.expire(refreshToken,
			// JWTUtil.TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

			/**
			 * 如果可以允许用户退出后token如果在有效期内仍然可以使用的话，那么就不需要存Redis
			 * 因为，token要跟用户做关联的话，就必须得每次都带一个用户标识，
			 * 那么校验token实际上就变成了校验token和用户标识的关联关系是否正确，且token是否有效
			 */

			// String key = MD5Encoder.encode(userId.getBytes());
			hashOperations.put(userLogin.getUserId(), "token", token);
			hashOperations.put(userLogin.getUserId(), "refreshToken", refreshToken);
			hashOperations.put(userLogin.getUserId(), "menu", JSON.toJSONString(menus));
			redisTemplate.expire(userLogin.getUserId(), JWTUtil.TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

			LoginResponse loginResponse = new LoginResponse();

			loginResponse.setToken(token);
			loginResponse.setRefreshToken(refreshToken);
			loginResponse.setUserName(username);
			loginResponse.setUserid(userLogin.getUserId());
			loginResponse.setMenus(menus);
			return ResponseResult.success(loginResponse);
		}

		return ResponseResult.error(ResponseCodeEnum.LOGIN_ERROR.getCode(), ResponseCodeEnum.LOGIN_ERROR.getMessage());
	}

	/**
	 * 退出
	 */
	@GetMapping("/logout")
	public ResponseResult<?> logout(@RequestParam("userId") String userId) {
		HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
		String key = userId;
		hashOperations.delete(key,"token");
		hashOperations.delete(key,"refreshToken");
		hashOperations.delete(key,"menu");
		return ResponseResult.success();
	}

	/**
	 * 刷新Token
	 */
	// @PostMapping("/refreshToken")
	// public ResponseResult refreshToken(@RequestBody @Validated RefreshRequest
	// request, BindingResult bindingResult) {
	// String userId = request.getUserId();
	// String refreshToken = request.getRefreshToken();
	// HashOperations<String, String, String> hashOperations =
	// stringRedisTemplate.opsForHash();
	// String key = userId;
	// String originalRefreshToken = hashOperations.get(key, "refreshToken");
	// if (StringUtils.isBlank(originalRefreshToken) ||
	// !originalRefreshToken.equals(refreshToken)) {
	// return
	// ResponseResult.error(ResponseCodeEnum.REFRESH_TOKEN_INVALID.getCode(),
	// ResponseCodeEnum.REFRESH_TOKEN_INVALID.getMessage());
	// }
	//
	// // 生成新token
	// String newToken = JWTUtil.generateToken(userId, secretKey);
	// hashOperations.put(key, "token", newToken);
	// stringRedisTemplate.expire(userId, JWTUtil.TOKEN_EXPIRE_TIME,
	// TimeUnit.MILLISECONDS);
	//
	// return ResponseResult.success(newToken);
	// }
	// }
}