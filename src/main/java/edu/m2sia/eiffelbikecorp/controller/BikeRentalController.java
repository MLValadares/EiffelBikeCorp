package edu.m2sia.eiffelbikecorp.controller;

import edu.m2sia.eiffelbikecorp.model.*;
import edu.m2sia.eiffelbikecorp.service.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

// Treats the api paths

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/bikeRental")
public class BikeRentalController {
    private static final BikeRentalService bikeService = new BikeRentalService();
    private static final UserService userService = new UserService();

//    http://localhost:8080/EiffelBikeCorp_war_exploded/api/bikeRental/hello
//    @GET
//    @Path("/hello")
//    @Produces(MediaType.TEXT_PLAIN)
//    public String ServiceHelloWorld(){
//        return "Hello World!";
//    }

//    http://localhost:8080/EiffelBikeCorp_war_exploded/api/bikeRental
    @GET
    public List<Bike> getAllBikes() {
        return bikeService.getAllBikes();
    }

//    http://localhost:8080/EiffelBikeCorp_war_exploded/api/bikeRental/rent/1
    @POST
    @Path("/rent/{id}")
    public Response rentBike(@PathParam("id") int id, @HeaderParam("Authorization") String token) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }
        Bike rentedBike = bikeService.rentBike(id, user.getUsername());
        if (rentedBike != null) {
            user.rentBike(id);
            return Response.ok(rentedBike).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bike not available").build();
    }

    @POST
    @Path("/return/{id}")
    public Response returnBike(@PathParam("id") int id, @HeaderParam("Authorization") String token) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }
        Bike returnedBike = bikeService.returnBike(id, user.getUsername() );
        if (returnedBike != null) {
            user.returnBike(id);
            return Response.ok(returnedBike).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Invalid return request").build();
    }
}
