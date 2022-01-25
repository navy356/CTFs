package org.shatranj.diwana.shatranjserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShatranjServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShatranjServerApplication.class, args);
	}

}
