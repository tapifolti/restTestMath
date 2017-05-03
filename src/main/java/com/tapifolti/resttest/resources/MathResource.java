package com.tapifolti.resttest.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tapifolti on 5/3/2017.
 */

@Path("/math")
public class MathResource {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("powerof/{index}")
    public Response powerOf(@PathParam("index") int index, @QueryParam("number") Double number,
                             @QueryParam("numbers") List<Double> numbers) {
        Double[] result = null;
        if (number != null) {
            result = new Double[1];
            result[0] = Math.pow(number, index);
        } else if (numbers != null) {
            result = numbers.stream().map(x -> Math.pow(x, index)).toArray(Double[]::new);
        }
        return Response.ok().entity(result).build();
    }
}
