package com.coreoz.plume.jersey.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.cfg.Annotations;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * A Jackson JSON provider that throws a {@link JsonRequestParseException}
 * during the parsing.
 * It is useful to return proper 400 errors when JSON request input is not valid
 * @see JacksonJaxbJsonProvider
 */
@Slf4j
public class WsJacksonJsonProvider extends JacksonJaxbJsonProvider {
	public WsJacksonJsonProvider() {
		super();
	}

	public WsJacksonJsonProvider(Annotations... annotationsToUse) {
		super(annotationsToUse);
	}

	public WsJacksonJsonProvider(ObjectMapper mapper, Annotations[] annotationsToUse) {
		super(mapper, annotationsToUse);
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException {
		try {
			return super.readFrom(type, genericType, annotations, mediaType, httpHeaders, entityStream);
		} catch (IOException e) {
			// do not log the full stack trace since it points only to Jersey internals and not application code
			logger.warn("Could not parse JSON request: {}", e.getMessage());
			throw new JsonRequestParseException();
		}
	}
}
