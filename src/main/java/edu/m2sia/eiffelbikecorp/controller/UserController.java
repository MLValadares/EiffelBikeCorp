package edu.m2sia.eiffelbikecorp.controller;

import edu.m2sia.eiffelbikecorp.service.UserService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    private static final UserService userService = new UserService();

//    @GET
//    @Path("/hello")
//    @Produces(MediaType.TEXT_PLAIN)
//    public String ServiceHelloWorld(){
//        return "Hello World!";
//    }

    @POST
    @Path("/login")
    public Response login(Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        String token = userService.login(username, password);
        if (token != null) {
            return Response.ok(Map.of("token", token)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
    }

    @POST
    @Path("/register")
    public Response register(Map<String, String> user) {
        String name = user.get("name");
        String username = user.get("username");
        String password = user.get("password");
        boolean isAdded = userService.addUser(name, username, password);
        if (isAdded) {
            return Response.ok("User registered successfully").build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity("Username already exists").build();
        }
    }

    @POST
    @Path("/notify")
    public Response notifyUser(Map<String, Object> notificationData) {
        int userId = (int) notificationData.get("userId");
        String message = (String) notificationData.get("message");
        userService.notifyUser(userId, message);
        return Response.ok("User notified successfully").build();
    }
}
