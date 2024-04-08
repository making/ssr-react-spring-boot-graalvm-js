package com.example.post.ssr;

import java.util.List;
import java.util.Map;

import com.example.post.Post;
import com.example.post.PostService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SsrController {

	private final ReactRenderer reactRenderer;

	private final PostService postService;

	public SsrController(ReactRenderer reactRenderer, PostService postService) {
		this.reactRenderer = reactRenderer;
		this.postService = postService;
	}

	@GetMapping(path = { "/", "/posts" })
	public String index() {
		List<Post> posts = this.postService.getPosts();
		return this.reactRenderer.render("/", Map.of("preLoadedPosts", posts));
	}

	@GetMapping(path = { "/posts/{id}" })
	public String post(@PathVariable int id) {
		Post post = this.postService.getPost(id);
		return this.reactRenderer.render("/posts/%d".formatted(id), Map.of("preLoadedPost", post));
	}

}
