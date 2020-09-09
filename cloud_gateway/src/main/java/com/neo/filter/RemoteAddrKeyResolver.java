package com.neo.filter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class RemoteAddrKeyResolver implements KeyResolver {

	@Override
	public Mono<String> resolve(ServerWebExchange exchange) {
		/*System.out.println("11111111"+ exchange.getRequest()); // org.springframework.http.server.reactive.ReactorServerHttpRequest@56a26187
		System.out.println("22222"+ exchange.getRequest().getRemoteAddress() );// peer1/127.0.0.1:54154
		System.out.println("33333"+ exchange.getRequest().getRemoteAddress().getAddress() );// peer1/127.0.0.1
		System.out.println("4444"+ exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());//127.0.0.1
		System.out.println("555"+ exchange.getRequest().getRemoteAddress().getHostName() );//peer1*/
		
		return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
		
//		return Mono.just(exchange.getRequest().getRemoteAddress().getHostName());

	}
}
