package com.tapifolti.resttest.resources;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.json.simple.JSONArray;
import org.junit.Test;

import javax.ws.rs.client.Client;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by tapifolti on 5/28/2017.
 */
public class MathResourceAddVTest {
    String prefixURL = "http://mathtestlinux.azurewebsites.net/math/";
    String addvURL = "addv";

    @Test
    public void runAddVTest1() {
        Client client = new JerseyClientBuilder().build();
        JSONArray json = client
                .target(prefixURL+ addvURL)
                .queryParam("v1", "11", "8", "13")
                .queryParam("v2", "22", "25", "20")
                .request()
                .get(JSONArray.class);
        assertThat(json.size()).isEqualTo(3);
        assertThat((double)json.get(0)).isEqualTo(33);
        assertThat((double)json.get(1)).isEqualTo(33);
        assertThat((double)json.get(2)).isEqualTo(33);
    }


}
