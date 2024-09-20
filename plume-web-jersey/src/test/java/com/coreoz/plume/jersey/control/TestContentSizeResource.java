package com.coreoz.plume.jersey.control;

import com.coreoz.plume.jersey.security.control.ContentSizeLimit;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/test")
public class TestContentSizeResource {

    static final int CUSTOM_MAX_SIZE = 10;

    @POST
    @Path("/upload-default")
    public Response uploadDefaultLimit(byte[] data) {
        return Response.ok("Upload successful").build();
    }

    @GET
    @Path("/upload-default")
    public Response getDefaultLimit(byte[] data) {
        return Response.ok("get successful").build();
    }

    @POST
    @Path("/upload-custom")
    @ContentSizeLimit(CUSTOM_MAX_SIZE)
    public Response uploadCustomLimit(byte[] data) {
        return Response.ok("Upload successful").build();
    }

    @GET
    @Path("/upload-custom")
    @ContentSizeLimit(CUSTOM_MAX_SIZE)
    public Response getCustomLimit(byte[] data) {
        return Response.ok("get successful").build();
    }

}
