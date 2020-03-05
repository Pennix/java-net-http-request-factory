package net.pennix.http.client;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

class AppTest {

	private RestTemplate rest;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		rest = new RestTemplate(asList(new StringHttpMessageConverter(UTF_8)));
		rest.setRequestFactory(new JavaNetHttpClientHttpRequestFactory());
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		get();
	}

	private String get() {
		String string = rest.getForObject("https://www.baidu.com/", String.class);
		System.out.println(string);
		return string;
	}
}
