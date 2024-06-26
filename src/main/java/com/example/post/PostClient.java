package com.example.post;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PostClient {

	private final RestClient restClient;

	public PostClient(PostApiProps props, RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder.baseUrl(props.url()).build();
	}

	public ResponseEntity<List<Post>> getPosts() {
		return this.restClient.get().uri("posts").retrieve().toEntity(new ParameterizedTypeReference<>() {
		});
	}

	public ResponseEntity<Void> headPosts(String etag) {
		return this.restClient.head()
			.uri("posts")
			.headers(headers -> headers.setIfNoneMatch(etag))
			.retrieve()
			.toBodilessEntity();
	}

	public ResponseEntity<Post> getPost(int id) {
		return this.restClient.get().uri("posts/{id}", id).retrieve().toEntity(Post.class);
	}

	public ResponseEntity<Void> headPost(int id, String etag) {
		return this.restClient.head()
			.uri("posts/{id}", id)
			.headers(headers -> headers.setIfNoneMatch(etag))
			.retrieve()
			.toBodilessEntity();
	}

}
