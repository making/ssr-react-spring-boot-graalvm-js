package com.example.post;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

	private final PostClient postClient;

	public PostController(PostClient postClient) {
		this.postClient = postClient;
	}

	@GetMapping(path = "/api/posts")
	public List<Post> getPosts() {
		return this.postClient.getPosts();
	}

	@GetMapping(path = "/api/posts/{id}")
	public Post getPost(@PathVariable int id) {
		return this.postClient.getPost(id);
	}

}
