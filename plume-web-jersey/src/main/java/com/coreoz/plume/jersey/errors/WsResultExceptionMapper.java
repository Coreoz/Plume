package com.coreoz.plume.jersey.errors;

import lombok.extern.slf4j.Slf4j;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Slf4j
@Provider
public class WsResultExceptionMapper implements ExceptionMapper<Throwable> {
	@Override
	public Response toResponse(Throwable e) {
		if (e instanceof WsException wsException) {
            return Response
				.status(Status.BAD_REQUEST)
				.entity(new ErrorResponse(wsException.getError(), wsException.getStatusArguments()))
				.type(MediaType.APPLICATION_JSON_TYPE)
				.build();
		}
		if(e instanceof WebApplicationException webApplicationException) {
			return webApplicationException.getResponse();
		}
		if(e instanceof JsonRequestParseException) {
			return Response
				.status(Status.BAD_REQUEST)
				.entity(new ErrorResponse(WsError.REQUEST_INVALID, List.of("JSON object supplied in request is invalid")))
				.type(MediaType.APPLICATION_JSON_TYPE)
				.build();
		}

		logger.error("Erreur inconnue sur le WS", e);

		return Response
			.status(Status.INTERNAL_SERVER_ERROR)
			.entity(new ErrorResponse(WsError.INTERNAL_ERROR, List.of()))
			.type(MediaType.APPLICATION_JSON_TYPE)
			.build();
	}
}
