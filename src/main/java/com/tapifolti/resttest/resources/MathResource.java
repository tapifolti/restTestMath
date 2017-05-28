package com.tapifolti.resttest.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.tapifolti.resttest.api.DoubleVector;
import com.tapifolti.resttest.api.DoubleVectorHelper;
import com.tapifolti.resttest.api.WebTest;
import io.dropwizard.jersey.params.IntParam;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
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

    static {
        try {
            System.loadLibrary("WebTest"); // Load native library at runtime
            // WebTest.dll (Windows) or WebTest.so (Unixes)
            // -Djava.library.path=/path/to/libs DOES NOT WORK on Linux
            // instead use export LD_LIBRARY_PATH = $LD_LIBRARY_PATH:/mylocation
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("powerof/{index}")
    @Timed
    public Response powerOf(@PathParam("index") IntParam index, @QueryParam("number") Double number,
                            @QueryParam("numbers") List<Double> numbers) {
        Double[] result = null;
        if (number != null) {
            result = new Double[1];
            result[0] = Math.pow(number, index.get());
        } else if (numbers != null) {
            result = numbers.stream().map(x -> Math.pow(x, index.get())).toArray(Double[]::new);
        }
        return Response.ok().entity(result).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("addv")
    @Timed
    public Response addV(@QueryParam("v1") @NotNull @NotEmpty List<Double> v1,
                            @QueryParam("v2") @NotNull @NotEmpty List<Double> v2) {
        if (v1==null || v2==null || v1.size() == 0 || v1.size() != v2.size()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Empty argument or not matching length").build();
        }
        DoubleVector dv1 = DoubleVectorHelper.createDoubleVector(v1);
        DoubleVector dv2 = DoubleVectorHelper.createDoubleVector(v2);
        DoubleVector vRes = new DoubleVector(v1.size());

        WebTest.addV(dv1, dv2, vRes);
        double[] result = DoubleVectorHelper.getDoubles(vRes);
        dv1.delete();
        dv2.delete();
        vRes.delete();
        return Response.ok().entity(result).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("mulv")
    @Timed
    public Response mulV(@QueryParam("v1") @NotNull @NotEmpty List<Double> v1,
                         @QueryParam("v2") @NotNull @NotEmpty List<Double> v2) {
        if (v1==null || v2==null || v1.size() == 0 || v1.size() != v2.size()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Empty argument or not matching length").build();
        }
        DoubleVector dv1 = DoubleVectorHelper.createDoubleVector(v1);
        DoubleVector dv2 = DoubleVectorHelper.createDoubleVector(v2);
        DoubleVector vRes = new DoubleVector(v1.size());

        WebTest.mulV(dv1, dv2, vRes);
        double[] result = DoubleVectorHelper.getDoubles(vRes);
        dv1.delete();
        dv2.delete();
        vRes.delete();
        return Response.ok().entity(result).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("expv")
    @Timed
    public Response expV(@QueryParam("v1") @NotNull @NotEmpty List<Double> v1) {
        if (v1==null || v1.size() == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Empty argument").build();
        }
        DoubleVector dv1 = DoubleVectorHelper.createDoubleVector(v1);
        DoubleVector vRes = new DoubleVector(v1.size());

        WebTest.expV(dv1, vRes);
        double[] result = DoubleVectorHelper.getDoubles(vRes);
        dv1.delete();
        vRes.delete();
        return Response.ok().entity(result).build();
    }
}
