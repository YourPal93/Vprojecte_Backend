package com.friend.your.vprojecte.vprojecteconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class VprojecteConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VprojecteConfigServerApplication.class, args);
	}

}
