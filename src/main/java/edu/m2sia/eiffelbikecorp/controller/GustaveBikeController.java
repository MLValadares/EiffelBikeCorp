package edu.m2sia.eiffelbikecorp.controller;

import edu.m2sia.eiffelbikecorp.model.Bike;
import edu.m2sia.eiffelbikecorp.service.GustaveBikeService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/gustaveBike")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GustaveBikeController {
    private final GustaveBikeService gustaveBikeService;

    @Inject
    public GustaveBikeController(GustaveBikeService gustaveBikeService) {
        this.gustaveBikeService = gustaveBikeService;
    }

    @GET
    @Path("/availableBikes")
    public List<Bike> getAvailableBikesForSale() {
        return gustaveBikeService.getAvailableBikesForSale();
    }

    @GET
    @Path("/price/{bikeId}")
    public Response getBikePriceInCurrency(@PathParam("bikeId") int bikeId, @QueryParam("currency") String currency) {
        double price = gustaveBikeService.getBikePriceInCurrency(bikeId, currency);
        return Response.ok(price).build();
    }

    @POST
    @Path("/purchase/{bikeId}")
    public Response purchaseBike(@PathParam("bikeId") int bikeId, @HeaderParam("Authorization") String token, @QueryParam("currency") String currency) {
        boolean success = gustaveBikeService.purchaseBike(bikeId, token, currency);
        if (success) {
            return Response.ok("Bike purchased successfully").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Purchase failed").build();
    }
}
