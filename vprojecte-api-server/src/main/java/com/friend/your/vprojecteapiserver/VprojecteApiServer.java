package com.friend.your.vprojecteapiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


//@SpringBootApplication
//@EnableEurekaClient
//@ComponentScan(basePackages = {"ru.javabegin.micro.planner"})
//@EnableJpaRepositories(basePackages = {"ru.javabegin.micro.planner.todo"})
//@EnableFeignClients
//@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
public class VprojecteApiServer {

	public static void main(String[] args) {
		SpringApplication.run(VprojecteApiServer.class, args);
	}

}
