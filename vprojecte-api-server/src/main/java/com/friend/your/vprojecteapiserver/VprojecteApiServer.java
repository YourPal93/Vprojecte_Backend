package com.friend.your.vprojecteapiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class VprojecteApiServer {

	public static void main(String[] args) {
		SpringApplication.run(VprojecteApiServer.class, args);
	}

}
