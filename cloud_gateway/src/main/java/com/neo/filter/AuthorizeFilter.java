package com.neo.filter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSON;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

	private String secretKey = "fddghjsgdfvbfdgfhsgdfehg";

	@Autowired
	private RedisTemplate redisTemplate;

	// @Autowired
	// private StringRedisTemplate stringRedisTemplate;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest serverHttpRequest = exchange.getRequest();
		ServerHttpResponse serverHttpResponse = exchange.getResponse();
		String uri = serverHttpRequest.getURI().getPath();

		// 检查白名单（配置）及 静态资源放行
		// if (uri.indexOf("/SPRING-CLOUD-AUTH/login") >= 0) {
		if (uri.indexOf("/login") >= 0) {
			return chain.filter(exchange);
		}
		// TODO 统一设置静态资源访问时 必须包含的字符 如 static/resources
		// login logout register 等放行。
		// if (checkUri(uri)) {
		// return chain.filter(exchange);
		// }
		//
		String token = serverHttpRequest.getHeaders().getFirst("token");
		if (StringUtils.isBlank(token)) {
			serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
			return getVoidMono(serverHttpResponse, ResponseCodeEnum.TOKEN_MISSION);
		}

		// todo 检查Redis中是否有此Token

		String userId = JWTUtil.getUserInfo(token);
		Object object = redisTemplate.opsForHash().get(userId, "token");
		if (!token.equals((String) object)) {
			serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
			return getVoidMono(serverHttpResponse, ResponseCodeEnum.TOKEN_INVALID);
		}

		String userIdHeader = serverHttpRequest.getHeaders().getFirst("userId");
		if (!userId.equals(userIdHeader)) {
			serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
			return getVoidMono(serverHttpResponse, ResponseCodeEnum.USREID_ERROR);
		}

		String menus = (String) redisTemplate.opsForHash().get(userId, "menu");
		// Object json = JSON.toJSON(menus);
		/*
		 * if (!menus.contains(uri)) {
		 * serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED); return
		 * getVoidMono(serverHttpResponse, ResponseCodeEnum.FORBIDDEN); }
		 */

		try {
			JWTUtil.verifyToken(token, secretKey);
		} catch (TokenAuthenticationException ex) {
			return getVoidMono(serverHttpResponse, ResponseCodeEnum.TOKEN_INVALID);
		} catch (Exception ex) {
			return getVoidMono(serverHttpResponse, ResponseCodeEnum.UNKNOWN_ERROR);
		}

		ServerHttpRequest mutableReq = serverHttpRequest.mutate().header("userId", userId).build();
		ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();

		return chain.filter(mutableExchange);
	}

	private Mono<Void> getVoidMono(ServerHttpResponse serverHttpResponse, ResponseCodeEnum responseCodeEnum) {
		serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
		ResponseResult responseResult = ResponseResult.error(responseCodeEnum.getCode(), responseCodeEnum.getMessage());
		DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JSON.toJSONString(responseResult).getBytes());
		return serverHttpResponse.writeWith(Flux.just(dataBuffer));
	}

	@Override
	public int getOrder() {
		return -100;
	}
}