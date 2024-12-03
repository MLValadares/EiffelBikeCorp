package edu.m2sia.eiffelbikecorp.controller;

import edu.m2sia.eiffelbikecorp.model.*;
import edu.m2sia.eiffelbikecorp.service.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

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
        Bike rentedBike = bikeService.rentBike(id, user.getId());
        if (rentedBike != null) {
            user.rentBike(id);
            return Response.ok(rentedBike).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bike not available").build();
    }

    @POST
    @Path("/return/{id}")
    public Response returnBike(
            @PathParam("id") int id,
            @HeaderParam("Authorization") String token,
            @QueryParam("conditionRating") int conditionRating,
            @QueryParam("conditionNotes") String conditionNotes) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        if (conditionRating < 1 || conditionRating > 5) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Condition rating must be between 1 and 5.").build();
        }

        Bike returnedBike = bikeService.returnBike(id, user.getId(), conditionRating, conditionNotes); // Pass user ID
//        System.out.println("Returned bike: " + returnedBike);
        if (returnedBike != null) {
            return Response.ok(returnedBike).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Invalid return request").build();
    }


    //methodo para criar uma bicicleta
    @PUT
    @Path("/add")
    public Response addBike(@HeaderParam("Authorization") String token, Map<String, String> bikeData) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        String model = bikeData.get("model");
        if (model == null || model.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bike model is required").build();
        }

        Bike newBike = bikeService.addBike(model, user.getUsername());
        return Response.ok(newBike).build();
    }

    @DELETE
    @Path("/remove/{bikeId}")
    public Response removeBike(@HeaderParam("Authorization") String token, @PathParam("bikeId") int bikeId) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        boolean removed = bikeService.removeBike(bikeId, user.getUsername());
        if (!removed) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Bike cannot be removed. Ensure you own the bike and it is not currently rented.")
                    .build();
        }

        return Response.ok("Bike successfully removed").build();
    }

//    @GET
//    @Path("/waitingList/{bikeId}")
//    public Response getWaitingList(@PathParam("bikeId") int bikeId) {
//        Queue<String> queue = bikeService.getWaitingList(bikeId);
//        return Response.ok(queue).build();
//    }

}
