package com.example.post;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping(path = "/api/posts")
	public List<Post> getPosts() {
		return this.postService.getPosts();
	}

	@GetMapping(path = "/api/posts/{id}")
	public Post getPost(@PathVariable int id) {
		return this.postService.getPost(id);
	}

}
