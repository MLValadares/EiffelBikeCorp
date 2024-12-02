package edu.m2sia.eiffelbikecorp.rental;

import edu.m2sia.model.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

// Treats the api paths

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/bikeRental")
public class BikeRentalService {

    private BikeRentalManager manager = new BikeRentalManager();

//    http://localhost:8080/EiffelBikeCorp_war_exploded/api/bikeRental/hello
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String ServiceHelloWorld(){
        return "Hello World!";
    }

    @GET
    public List<Bike> getAllBikes() {
        return manager.getAllBikes();
    }

    @POST
    @Path("/rent/{id}")
    public Response rentBike(@PathParam("id") int id) {
        Bike rentedBike = manager.rentBike(id);
        if (rentedBike != null) {
            return Response.ok(rentedBike).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bike not available").build();
    }

    @POST
    @Path("/return/{id}")
    public Response returnBike(@PathParam("id") int id) {
        Bike returnedBike = manager.returnBike(id);
        if (returnedBike != null) {
            return Response.ok(returnedBike).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Bike was not rented").build();
    }
}
