package net.pennix.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.AbstractClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

public class JavaNetHttpClientHttpRequest extends AbstractClientHttpRequest {

	private HttpClient client;

	private URI uri;

	private String methodValue;

	private int pipeSize;

	private PipedInputStream in;

	public JavaNetHttpClientHttpRequest(HttpClient client, URI uri, String methodValue) {
		this(client, uri, methodValue, 0);
	}

	public JavaNetHttpClientHttpRequest(HttpClient client, URI uri, String methodValue, int pipeSize) {
		super();
		this.client = client;
		this.uri = uri;
		this.methodValue = methodValue;
		this.pipeSize = pipeSize;
	}

	@Override
	protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
		this.in = pipeSize > 0 ? new PipedInputStream(pipeSize) : new PipedInputStream();
		return new PipedOutputStream(in);
	}

	@Override
	protected ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException {
		BodyPublisher body = in == null ? BodyPublishers.noBody() : BodyPublishers.ofInputStream(() -> in);
		Builder builder = HttpRequest.newBuilder(uri).method(getMethodValue(), body);
		headers.forEach((headerName, headerValues) -> {
			if (HttpHeaders.COOKIE.equalsIgnoreCase(headerName)) { // RFC 6265
				String headerValue = StringUtils.collectionToDelimitedString(headerValues, "; ");
				builder.header(headerName, headerValue);
			} else {
				for (String headerValue : headerValues) {
					String actualHeaderValue = headerValue != null ? headerValue : "";
					builder.header(headerName, actualHeaderValue);
				}
			}
		});
		try {
			HttpResponse<InputStream> response = client.send(builder.build(), BodyHandlers.ofInputStream());
			return new JavaNetHttpClientHttpHttpResponse(response);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getMethodValue() {
		return methodValue;
	}

	@Override
	public URI getURI() {
		return uri;
	}
}
