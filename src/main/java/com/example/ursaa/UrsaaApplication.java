package com.example.ursaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class UrsaaApplication {

	public static void main(String[] args) {
//		BlockHound.install();
		SpringApplication.run(UrsaaApplication.class, args);
	}

}
