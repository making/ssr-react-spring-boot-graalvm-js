package com.example.post;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

	private final PostClient postClient;

	private static final CacheControl swrCacheControl = CacheControl.maxAge(Duration.ofHours(1))
		.staleWhileRevalidate(Duration.ofMinutes(10));

	public PostController(PostClient postClient) {
		this.postClient = postClient;
	}

	@GetMapping(path = "/api/posts")
	public ResponseEntity<List<Post>> getPosts(@RequestHeader(name = HttpHeaders.IF_NONE_MATCH) Optional<String> etag) {
		if (etag.isPresent()) {
			ResponseEntity<Void> head = this.postClient.headPosts(etag.get());
			if (head.getStatusCode().isSameCodeAs(HttpStatus.NOT_MODIFIED)) {
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
			}
		}
		ResponseEntity<List<Post>> response = this.postClient.getPosts();
		return ResponseEntity.ok()
			.cacheControl(swrCacheControl)
			.headers(headers -> headers.setETag(response.getHeaders().getETag()))
			.body(response.getBody());
	}

	@GetMapping(path = "/api/posts/{id}")
	public ResponseEntity<Post> getPost(@PathVariable int id,
			@RequestHeader(name = HttpHeaders.IF_NONE_MATCH) Optional<String> etag) {
		if (etag.isPresent()) {
			ResponseEntity<Void> head = this.postClient.headPost(id, etag.get());
			if (head.getStatusCode().isSameCodeAs(HttpStatus.NOT_MODIFIED)) {
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
			}
		}
		ResponseEntity<Post> response = this.postClient.getPost(id);
		return ResponseEntity.ok()
			.cacheControl(swrCacheControl)
			.headers(headers -> headers.setETag(response.getHeaders().getETag()))
			.body(response.getBody());
	}

}
