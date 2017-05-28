package com.tapifolti.resttest.resources;

import com.tapifolti.resttest.MathRestTestApplication;
import com.tapifolti.resttest.MathRestTestConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.json.simple.JSONArray;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Client;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by tapifolti on 5/28/2017.
 */

public class MathResourcePowerOfTest {

    String prefixURL = "http://mathtestlinux.azurewebsites.net/math/";
    String powerofPostfixURL = "powerof/%d";

    @Test
    public void runPowerOfTest1() {
        int index = 2;
        Client client = new JerseyClientBuilder().build();
        JSONArray json = client
                .target(String.format(prefixURL+powerofPostfixURL, index))
                .queryParam("number", "12")
                .request()
                .get(JSONArray.class);
        assertThat(json.size()).isEqualTo(1);
        assertThat((double)json.get(0)).isEqualTo(144);
    }

    @Test
    public void runPowerOfTest2() {
        int index = 3;
        Client client = new JerseyClientBuilder().build();
        JSONArray json = client
                .target(String.format(prefixURL+powerofPostfixURL, index))
                .queryParam("numbers", "11", "8", "13")
                .request()
                .get(JSONArray.class);
        assertThat(json.size()).isEqualTo(3);
        assertThat((double)json.get(0)).isEqualTo(1331);
        assertThat((double)json.get(1)).isEqualTo(512);
        assertThat((double)json.get(2)).isEqualTo(2197);
    }
}
