package com.neo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.neo.filter.RemoteAddrKeyResolver;

@SpringBootApplication
@EnableDiscoveryClient
public class GateWayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GateWayApplication.class, args);
	}

	@Autowired
	private Environment env;
	 
	@Bean(name="remoteAddrKeyResolver")
	public  RemoteAddrKeyResolver  getRemoteAddrKeyResolver(){
		return new RemoteAddrKeyResolver();
	}
	
 
}
