package com.friend.your.vprojecte;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class Vprojecte {

	public static void main(String[] args) {

		new SpringApplicationBuilder().sources(Vprojecte.class).run(args);
	}


}
