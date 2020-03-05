package net.pennix.http.client;

import static java.net.http.HttpClient.Redirect.NEVER;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;

public class JavaNetHttpClientHttpRequestFactory implements ClientHttpRequestFactory {

	private HttpClient client = HttpClient.newBuilder().followRedirects(NEVER).build();

	@Override
	public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
		return new JavaNetHttpClientHttpRequest(client, uri, httpMethod.name(), 4096);
	}
}
