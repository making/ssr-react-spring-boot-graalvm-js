package com.example.ssr;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.example.post.Post;
import com.example.post.PostClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SsrController {

	private final ReactRenderer reactRenderer;

	private final PostClient postClient;

	public SsrController(ReactRenderer reactRenderer, PostClient postClient) {
		this.reactRenderer = reactRenderer;
		this.postClient = postClient;
	}

	@GetMapping(path = { "/", "/posts" })
	public String index() {
		List<Post> posts = this.postClient.getPosts().getBody();
		return this.reactRenderer.render("/", Map.of("preLoadedPosts", Objects.requireNonNull(posts)));
	}

	@GetMapping(path = { "/posts/{id}" })
	public String post(@PathVariable int id) {
		Post post = this.postClient.getPost(id).getBody();
		return this.reactRenderer.render("/posts/%d".formatted(id),
				Map.of("preLoadedPost", Objects.requireNonNull(post)));
	}

}
