package com.example.post.ssr;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.io.IOAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.NativeDetector;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class ReactRenderer implements AutoCloseable {

	private final String template;

	private final ObjectMapper objectMapper;

	private final Source globalSource;

	private final Source mainServerSource;

	private final Engine engine;

	private static final ConcurrentMap<String, File> fileCache = new ConcurrentHashMap<>();

	private static final Logger log = LoggerFactory.getLogger(ReactRenderer.class);

	public ReactRenderer(ObjectMapper objectMapper) throws IOException {
		this.objectMapper = objectMapper;
		this.globalSource = Source.create("js", """
				globalThis.Buffer = require('buffer').Buffer;
				globalThis.URL = require('whatwg-url-without-unicode').URL;
				globalThis.process = {
					env: {
						NODE_ENV: 'production'
					}
				};
				globalThis.document = {};
				global = globalThis;
				""");
		Path mainServer = Paths.get(getRoot("server").getAbsolutePath(), "main-server.js");
		this.mainServerSource = Source.newBuilder("js", mainServer.toFile())
			.mimeType("application/javascript+module")
			.build();
		// engine that ensures code caching
		this.engine = Engine.newBuilder("js").build();
		this.template = Files.readString(Paths.get(getRoot("META-INF/resources").getAbsolutePath(), "index.html"));
	}

	public String render(String url, Map<String, Object> input) {
		try (Context context = Context.newBuilder("js")
			// set the engine to a context to ensure optimized code is cached
			.engine(this.engine)
			.allowIO(IOAccess.ALL)
			.allowExperimentalOptions(true)
			.option("js.esm-eval-returns-exports", "true")
			.option("js.commonjs-require", "true")
			.option("js.commonjs-require-cwd", getRoot("polyfill").getAbsolutePath())
			.option("js.commonjs-core-modules-replacements",
					"stream:stream-browserify,util:fastestsmallesttextencoderdecoder,buffer:buffer/")
			.build()) {
			context.eval(this.globalSource);
			Value exports = context.eval(this.mainServerSource);
			Value render = exports.getMember("render");
			try {
				String s = this.objectMapper.writeValueAsString(input);
				Value executed = render.execute(url, s);
				Value head = executed.getMember("head");
				Value html = executed.getMember("html");
				return this.template
					.replace("<!--app-head-->", Objects.requireNonNullElse(head == null ? null : head.asString(), ""))
					.replace("<!--app-html-->", Objects.requireNonNullElse(html == null ? null : html.asString(), ""))
					.replace("<!--app-init-data-->", """
							<script id="__INIT_DATA__" type="application/json">%s</script>
							""".formatted(s));
			}
			catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}

	static File getRoot(String root) {
		return fileCache.computeIfAbsent(root, key -> {
			log.trace("computing getRoot({})", key);
			try {
				if (NativeDetector.inNativeImage()) {
					// in native image
					return copyResources(root).toFile();
				}
				ClassPathResource resource = new ClassPathResource(root);
				if (resource.getURL().toString().startsWith("jar:")) {
					// in jar file
					return new FileSystemResource("./target/classes/" + root).getFile();
				}
				else {
					return resource.getFile();
				}
			}
			catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}

	private static Path copyResources(String root) {
		try {
			Path baseDir = Files.createTempDirectory("copied-");
			baseDir.toFile().deleteOnExit();
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			for (Resource resource : resolver.getResources("classpath:/%s/**".formatted(root))) {
				String path = resource.getURI().toString().replace("resource:", "");
				Path destination = Paths.get(baseDir.toFile().getAbsolutePath(), path);
				Path parentDir = destination.getParent();
				if (parentDir != null) {
					if (!Files.exists(parentDir)) {
						log.trace("Create parent directory {}", parentDir);
						Files.createDirectories(parentDir);
					}
				}
				if (resource.isReadable()) {
					log.trace("Copy file {} to {}", resource, destination);
					try (InputStream input = resource.getInputStream();
							OutputStream output = Files.newOutputStream(destination)) {
						StreamUtils.copy(input, output);
					}
				}
				else {
					log.trace("Create directory {}", destination);
					Files.createDirectories(destination);
				}
			}
			return Paths.get(baseDir.toFile().getAbsolutePath(), root);
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@PostConstruct
	void warmUp() {
		log.info("Started warming up.");
		this.render("/", Map.of());
		log.info("Finished warming up.");
	}

	@Override
	public void close() {
		this.engine.close();
	}

}
