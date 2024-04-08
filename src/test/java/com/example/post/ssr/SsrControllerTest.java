package com.example.post.ssr;

import java.util.List;

import com.example.post.Post;
import com.example.post.PostService;
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

@WebMvcTest(SsrController.class)
@Import(ReactRenderer.class)
class SsrControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	WebClient webClient;

	@MockBean
	PostService postService;

	@Test
	void index() throws Exception {
		given(this.postService.getPosts()).willReturn(List.of(new Post(2, "SSR", "Server Side Rendering!", 1000),
				new Post(1, "Hello", "Hello World!", 1000)));
		HtmlPage page = this.webClient.getPage("/");
		HtmlDivision root = page.getHtmlElementById("root");
		assertThat(root).isNotNull();
		DomNodeList<DomNode> titles = root.querySelectorAll("h3");
		assertThat(titles).hasSize(2);
		assertThat(titles.get(0).getTextContent()).isEqualTo("SSR");
		assertThat(titles.get(1).getTextContent()).isEqualTo("Hello");
	}

	@Test
	void post() throws Exception {
		given(this.postService.getPost(1)).willReturn(new Post(1, "Hello", "Hello World!", 1000));
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

}