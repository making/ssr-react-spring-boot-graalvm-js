package com.example.post.ssr;

import java.util.List;

import com.example.post.Post;
import com.example.post.PostService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SsrController.class)
@Import(ReactRenderer.class)
class SsrControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	PostService postService;

	@Test
	void index() throws Exception {
		given(this.postService.getPosts()).willReturn(List.of(new Post(2, "SSR", "Server Side Rendering!", 1000),
				new Post(1, "Hello", "Hello World!", 1000)));
		MvcResult result = this.mvc.perform(get("/")).andExpect(status().isOk()).andReturn();
		String html = result.getResponse().getContentAsString();
		assertThat(html).contains(
				"""
						<div id="root"><div><h2>Posts</h2><h3><a href="/posts/2">SSR</a></h3><h3><a href="/posts/1">Hello</a></h3></div></div>
						""");
		assertThat(html).contains(
				"""
						<script id="__INIT_DATA__" type="application/json">{"preLoadedPosts":[{"id":2,"title":"SSR","body":"Server Side Rendering!","userId":1000},{"id":1,"title":"Hello","body":"Hello World!","userId":1000}]}</script>
						""");
	}

	@Test
	void post() throws Exception {
		given(this.postService.getPost(1)).willReturn(new Post(1, "Hello", "Hello World!", 1000));
		MvcResult result = this.mvc.perform(get("/posts/1")).andExpect(status().isOk()).andReturn();
		String html = result.getResponse().getContentAsString();
		assertThat(html).contains(
				"""
						<div id="root"><h3><a href="/posts/1">Hello</a></h3><p>Hello World!</p><hr/><a href="/">« Go to Posts</a></div>
						""");
		assertThat(html).contains(
				"""
						<script id="__INIT_DATA__" type="application/json">{"preLoadedPost":{"id":1,"title":"Hello","body":"Hello World!","userId":1000}}</script>
						""");
	}

}