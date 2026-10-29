package com.store.digital.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsyncClientsConfig {

	@Bean(destroyMethod = "close")
	ExecutorService downstreamServiceExecutor() {
		return Executors.newVirtualThreadPerTaskExecutor();
	}
}
