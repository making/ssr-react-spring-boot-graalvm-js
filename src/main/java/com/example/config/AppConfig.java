package com.example.config;

import java.util.Set;

import am.ik.spring.http.client.RetryableClientHttpRequestInterceptor;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.backoff.FixedBackOff;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

	@Bean
	public RestClientCustomizer restClientCustomizer(
			LogbookClientHttpRequestInterceptor logbookClientHttpRequestInterceptor) {
		return builder -> builder.requestInterceptor(logbookClientHttpRequestInterceptor)
			.requestInterceptor(new RetryableClientHttpRequestInterceptor(new FixedBackOff(1_000, 2),
					opts -> opts.sensitiveHeaders(Set.of("nel", "report-to",
							"reporting-endpoints") /* noisy headers */)));
	}

}