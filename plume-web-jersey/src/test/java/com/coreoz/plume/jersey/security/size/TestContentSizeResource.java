package com.coreoz.plume.jersey.security.size;

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
    public Response getDefaultLimit() {
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
    public Response getCustomLimit() {
        return Response.ok("get successful").build();
    }

}
