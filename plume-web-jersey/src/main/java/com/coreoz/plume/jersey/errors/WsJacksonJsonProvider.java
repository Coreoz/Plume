package com.coreoz.plume.jersey.errors;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.cfg.Annotations;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * A Jackson JSON provider that throws a {@link JsonRequestParseException}
 * during the parsing.
 * @see JacksonJaxbJsonProvider
 */
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
		} catch (Exception e) {
			throw new JsonRequestParseException();
		}
	}

}
