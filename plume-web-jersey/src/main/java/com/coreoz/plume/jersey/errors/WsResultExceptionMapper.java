package com.coreoz.plume.jersey.errors;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

@Provider
public class WsResultExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger logger = LoggerFactory.getLogger(WsResultExceptionMapper.class);

	@Override
	public Response toResponse(Throwable e) {
		if (e instanceof WsException) {
			WsException wsException = (WsException) e;
			return Response
				.status(Status.BAD_REQUEST)
				.entity(new ErrorResponse(wsException.getError(), wsException.getStatusArguments()))
				.type(MediaType.APPLICATION_JSON_TYPE)
				.build();
		}
		if(e instanceof WebApplicationException) {
			return ((WebApplicationException) e).getResponse();
		}
		if(e instanceof JsonRequestParseException) {
			return Response
				.status(Status.BAD_REQUEST)
				.entity(new ErrorResponse(WsError.REQUEST_INVALID, ImmutableList.of("JSON object supplied in request is invalid")))
				.type(MediaType.APPLICATION_JSON_TYPE)
				.build();
		}

		logger.error("Erreur inconnue sur le WS", e);

		return Response
			.status(Status.INTERNAL_SERVER_ERROR)
			.entity(new ErrorResponse(WsError.INTERNAL_ERROR, ImmutableList.of()))
			.type(MediaType.APPLICATION_JSON_TYPE)
			.build();
	}

}
