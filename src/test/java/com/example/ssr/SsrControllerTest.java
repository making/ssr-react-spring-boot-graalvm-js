package com.example.ssr;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.example.post.Post;
import com.example.post.PostClient;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.ulfs.assertj.jsoup.Assertions.assertThatDocument;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SsrController.class, properties = "logging.level.com.example=trace")
@Import(ReactRenderer.class)
class SsrControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	PostClient postClient;

	@Test
	void index() throws Exception {
		given(this.postClient.getPosts()).willReturn(ResponseEntity.ok(List
			.of(new Post(2, "SSR", "Server Side Rendering!", 1000), new Post(1, "Hello", "Hello World!", 1000))));
		String body = this.mvc.perform(get("/"))
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();
		assertThatDocument(body) //
			.elementHasText("#root ul > li:nth-child(1)", "SSR") //
			.elementHasText("#root ul > li:nth-child(2)", "Hello") //
			.elementHasHtml("#__INIT_DATA__",
					"""
							{"preLoadedPosts":[{"id":2,"title":"SSR","body":"Server Side Rendering!","userId":1000},{"id":1,"title":"Hello","body":"Hello World!","userId":1000}]}
							"""
						.trim());
	}

	@Test
	void post() throws Exception {
		given(this.postClient.getPost(1)).willReturn(ResponseEntity.ok(new Post(1, "Hello", "Hello World!", 1000)));
		String body = this.mvc.perform(get("/posts/1"))
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();
		assertThatDocument(body) //
			.elementHasText("#root > h3", "Hello") //
			.elementHasText("#root > p", "Hello World!") //
			.elementHasHtml("#__INIT_DATA__", """
					{"preLoadedPost":{"id":1,"title":"Hello","body":"Hello World!","userId":1000}}
					""".trim());
	}

	@Test
	void concurrentAccess() throws Exception {
		given(this.postClient.getPosts()).willReturn(ResponseEntity.ok(List
			.of(new Post(2, "SSR", "Server Side Rendering!", 1000), new Post(1, "Hello", "Hello World!", 1000))));
		int n = 32;
		CountDownLatch latch;
		try (ExecutorService executorService = Executors.newFixedThreadPool(n)) {
			latch = new CountDownLatch(n);
			for (int i = 0; i < n; i++) {
				executorService.submit(() -> {
					try {
						this.mvc.perform(get("/")).andExpect(status().isOk());
					}
					catch (Exception e) {
						throw new RuntimeException(e);
					}
					latch.countDown();
				});
			}
		}
		boolean awaited = latch.await(30, TimeUnit.SECONDS);
		assertThat(awaited).isTrue();
	}

}