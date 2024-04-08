package com.example.post;

import java.util.List;
import java.util.Set;

import am.ik.spring.http.client.RetryableClientHttpRequestInterceptor;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.web.client.RestClient;

@Service
public class PostService {

	private final RestClient restClient;

	public PostService(PostApiProps props, RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder.baseUrl(props.url())
			.requestInterceptor(new RetryableClientHttpRequestInterceptor(
					new FixedBackOff(1_000, 2), opts -> opts.sensitiveHeaders(Set.of("nel", "report-to",
							"reporting-endpoints") /* noisy headers */)))
			.build();
	}

	public List<Post> getPosts() {
		return this.restClient.get().uri("posts").retrieve().body(new ParameterizedTypeReference<>() {
		});
	}

	public Post getPost(int id) {
		return this.restClient.get().uri("posts/{id}", id).retrieve().body(Post.class);
	}

}
