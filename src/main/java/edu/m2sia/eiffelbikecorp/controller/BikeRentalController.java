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
//    private static final UserService userService = new UserService();
    private static final BikeRentalService bikeService = new BikeRentalService();

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
    public Response rentBike(@PathParam("id") int id,
                             @HeaderParam("Authorization") String token,
                             @QueryParam("waitingList") @DefaultValue("false") boolean waitingList) {
            Integer userId = bikeService.getUserIdByToken(token);
            if (userId == null) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
            }
        BikeRentalService.RentBikeResult result = bikeService.rentBike(id, userId, waitingList);
        return switch (result) {
            case SUCCESS -> Response.ok("Bike rented successfully").build();
            case NOT_AVAILABLE -> Response.status(Response.Status.BAD_REQUEST).entity("Bike is not available").build();
            case ADDED_TO_WAITING_LIST -> Response.ok("Bike added to waiting list").build();
        };
    }

    @POST
    @Path("/return/{id}")
    public Response returnBike(
            @PathParam("id") int id,
            @HeaderParam("Authorization") String token,
            @QueryParam("conditionRating") int conditionRating,
            @QueryParam("conditionNotes") String conditionNotes) {
        Integer userId = bikeService.getUserIdByToken(token);
        if (userId == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        if (conditionRating < 1 || conditionRating > 5) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Condition rating must be between 1 and 5.").build();
        }

        Bike returnedBike = bikeService.returnBike(id, userId, conditionRating, conditionNotes); // Pass user ID
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
        Integer userId = bikeService.getUserIdByToken(token);
        if (userId == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        String model = bikeData.get("model");
        if (model == null || model.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bike model is required").build();
        }

        Bike newBike = bikeService.addBike(model, userId);
        return Response.ok(newBike).build();
    }

    @DELETE
    @Path("/remove/{bikeId}")
    public Response removeBike(@HeaderParam("Authorization") String token, @PathParam("bikeId") int bikeId) {
        Integer userId = bikeService.getUserIdByToken(token);
        if (userId == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        boolean removed = bikeService.removeBike(bikeId);
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
