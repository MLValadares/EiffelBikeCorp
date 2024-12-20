package edu.m2sia.eiffelbikecorp.controller;

import edu.m2sia.eiffelbikecorp.model.*;
import edu.m2sia.eiffelbikecorp.service.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Treats the api paths

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/bikeRental")
public class BikeRentalController {
    private static final UserService userService = new UserService();
    private static final BikeRentalService bikeService = new BikeRentalService(userService);
    private static final CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
    private static final BankService bankService = new BankService(currencyExchangeService);
    private static final GustaveBikeService gustaveBikeService = new GustaveBikeService(bikeService, bankService);

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
        User user = userService.getUserByToken(token);
//        System.out.println("waitingList: " + waitingList);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }
        BikeRentalService.RentBikeResult result = bikeService.rentBike(id, user.getId(), waitingList);
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
        if (!user.isEmployee()) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only employees can add bikes").build();
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
        if (!user.isEmployee()) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only employees can remove bikes").build();
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

    @GET
    @Path("/gustaveBike/availableBikes")
    public List<Bike> getAvailableBikesForSale() {
        return gustaveBikeService.getAvailableBikesForSale();
    }

    @POST
    @Path("/gustaveBike/basket/{bikeId}")
    public Response addToBasket(@PathParam("bikeId") int bikeId, @HeaderParam("Authorization") String token) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }
        boolean success = gustaveBikeService.addToBasket(bikeId, user);
        if (success) {
            return Response.ok("Bike added to basket").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bike not added to basket").build();
    }

    @GET
    @Path("/gustaveBike/basket")
    public Response getBasket(@HeaderParam("Authorization") String token, @QueryParam("currency") String currency) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }
        Basket basket = user.getBasket();
        double totalPrice = basket.getTotalPrice();
        Map<String, Object> response = new HashMap<>();
        response.put("bikes", basket.getBikes());
        response.put("totalPrice", totalPrice);
        if (currency != null && !currency.isEmpty()) {
            double convertedPrice = currencyExchangeService.convert(totalPrice, currency);
            response.put("totalPriceIn" + currency, convertedPrice);
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/gustaveBike/basket/buy")
    public Response buyBasket(@HeaderParam("Authorization") String token, @QueryParam("currency") String currency) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }
        boolean success = gustaveBikeService.buyBasket(user, currency);
        if (success) {
            return Response.ok("Basket purchased successfully").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Basket purchase failed").build();
    }


}
