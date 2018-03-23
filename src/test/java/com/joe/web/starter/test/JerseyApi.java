package com.joe.web.starter.test;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author joe
 * @version 2018.03.19 16:33
 */
@Path("jersey")
public class JerseyApi {
    @RolesAllowed("user")
    @GET
    @Path("hello")
    public String hello() {
        return "hello";
    }
}
