package com.zhaojf.springdocdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class SpringDocDemoApplication {

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(SpringDocDemoApplication.class);
		// 可使用 ‘cat app.pid | xargs kill’ 命令优雅停机
		application.addListeners(new ApplicationPidFileWriter("app.pid"));
		application.run();
	}

}
