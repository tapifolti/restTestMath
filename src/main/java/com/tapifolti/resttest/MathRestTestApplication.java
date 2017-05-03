package com.tapifolti.resttest;

import com.tapifolti.resttest.resources.MathResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MathRestTestApplication extends Application<MathRestTestConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MathRestTestApplication().run(args);
    }

    @Override
    public String getName() {
        return "MathRestTest";
    }

    @Override
    public void initialize(final Bootstrap<MathRestTestConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final MathRestTestConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
        environment.jersey().register(new MathResource());
    }

}
