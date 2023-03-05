package com.friend.your.vprojecte.vprojectecloudserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class VprojecteCloudServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VprojecteCloudServerApplication.class, args);
	}

}
