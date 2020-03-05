package net.pennix.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.AbstractClientHttpResponse;

public class JavaNetHttpClientHttpHttpResponse extends AbstractClientHttpResponse {

	private HttpResponse<InputStream> response;

	public JavaNetHttpClientHttpHttpResponse(HttpResponse<InputStream> response) {
		super();
		this.response = response;
	}

	@Override
	public int getRawStatusCode() throws IOException {
		return response.statusCode();
	}

	@Override
	public String getStatusText() throws IOException {
		return HttpStatus.valueOf(getRawStatusCode()).getReasonPhrase();
	}

	@Override
	public void close() {
		try {
			response.body().close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public InputStream getBody() throws IOException {
		return response.body();
	}

	@Override
	public HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		response.headers().map().forEach(headers::addAll);
		return HttpHeaders.readOnlyHttpHeaders(headers);
	}
}
