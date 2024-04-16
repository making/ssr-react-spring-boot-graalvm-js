package com.example.post.ssr;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.example.post.Post;
import com.example.post.PostClient;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SsrController.class, properties = "logging.level.com.example=trace")
@Import(ReactRenderer.class)
class SsrControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	WebClient webClient;

	@MockBean
	PostClient postClient;

	@Test
	void index() throws Exception {
		given(this.postClient.getPosts()).willReturn(List.of(new Post(2, "SSR", "Server Side Rendering!", 1000),
				new Post(1, "Hello", "Hello World!", 1000)));
		HtmlPage page = this.webClient.getPage("/");
		HtmlDivision root = page.getHtmlElementById("root");
		assertThat(root).isNotNull();
		DomNodeList<DomNode> titles = root.querySelectorAll("li");
		assertThat(titles).hasSize(2);
		assertThat(titles.get(0).getTextContent()).isEqualTo("SSR");
		assertThat(titles.get(1).getTextContent()).isEqualTo("Hello");
	}

	@Test
	void post() throws Exception {
		given(this.postClient.getPost(1)).willReturn(new Post(1, "Hello", "Hello World!", 1000));
		HtmlPage page = this.webClient.getPage("/posts/1");
		HtmlDivision root = page.getHtmlElementById("root");
		assertThat(root).isNotNull();
		DomNode title = root.querySelector("h3");
		assertThat(title).isNotNull();
		assertThat(title.getTextContent()).isEqualTo("Hello");
		DomNode body = root.querySelector("p");
		assertThat(body).isNotNull();
		assertThat(body.getTextContent()).isEqualTo("Hello World!");
		HtmlScript initData = page.getHtmlElementById("__INIT_DATA__");
		assertThat(initData).isNotNull();
		assertThat(initData.getTextContent()).isEqualTo("""
				{"preLoadedPost":{"id":1,"title":"Hello","body":"Hello World!","userId":1000}}
				""".trim());
	}

	@Test
	void concurrentAccess() throws Exception {
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