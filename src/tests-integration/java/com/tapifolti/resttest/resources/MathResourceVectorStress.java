package com.tapifolti.resttest.resources;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.common.util.concurrent.AtomicDoubleArray;
import org.apache.commons.lang3.ArrayUtils;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.json.simple.JSONArray;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by tapifolti on 5/28/2017.
 */
public class MathResourceVectorStress {
    private static final String prefixURL = "http://mathtestlinux.azurewebsites.net/math/";
    private static final String addvURL = "addv";
    private static final String mulvURL = "mulv";
    private static final String expvURL = "expv";
    private static final Random rand = new Random(System.currentTimeMillis());
    private static final AtomicDouble durationStat = new AtomicDouble();
    private static final MinMaxAtomic minMaxAtomic = new MinMaxAtomic();

    private static class MinMaxAtomic {
        private double min = Long.MAX_VALUE;
        private double max = 0;

        public synchronized double getMin() {
            return min;
        }
        public synchronized double getMax() {
            return max;
        }
        public synchronized void setMin(double min) {
            if (min < this.min) {
                this.min = min;
            }
        }
        public synchronized void setMax(double max) {
            if (max > this.max) {
                this.max = max;
            }
        }
    }

    private static final int LOOP = 7000;
    @Test
    public void runVectorStressTest() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        for (int i=0; i<LOOP; i++) {
            if (i%500 == 0) {
                System.out.println("Loop: " + i);
                System.gc();
            }
            int count = rand.nextInt(10)+1;
            double[] v1 = new double[count];
            double[] v2 = new double[count];
            for (int j=0; j<count; j++) {
                v1[j] = Double.parseDouble(String.format("%.2f", rand.nextDouble()*count));
                v2[j] = Double.parseDouble(String.format("%.2f", rand.nextDouble()*count));
            }
            AddvTask addvTask = new AddvTask(v1, v2);
            executor.execute(addvTask);
            MulvTask mulvTask = new MulvTask(v1, v2);
            executor.execute(mulvTask);
            ExpvTask expvTask = new ExpvTask(v1);
            executor.execute(expvTask);
        }
        try {
            while (executor.getCompletedTaskCount() < 3*LOOP) {
                Thread.sleep(10000);
            }
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch(Exception e) {
            e.printStackTrace();
        }
        double averageDuration = durationStat.get()/3/LOOP;
        System.out.println("averageDuration: " + String.format("%.2f", averageDuration) + " msec");
        System.out.println("minDuration: " + minMaxAtomic.getMin() + " msec");
        System.out.println("maxDuration: " + minMaxAtomic.getMax() + " msec");
    }

    private static void updateStat(long duration) {
        durationStat.getAndAdd(duration);
        minMaxAtomic.setMax(duration);
        minMaxAtomic.setMin(duration);
    }

    private static class AddvTask implements Runnable {
        private double[] v1;
        private double[] v2;
        public AddvTask(double[] v1, double[] v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        @Override
        public void run()
        {
            try {
                WebTarget addvTarget = new JerseyClientBuilder().build().target(prefixURL+addvURL);
                long start = System.currentTimeMillis();
                JSONArray json = addvTarget
                        .queryParam("v1", ArrayUtils.toObject(v1))
                        .queryParam("v2", ArrayUtils.toObject(v2))
                        .request()
                        .get(JSONArray.class);
                long finish = System.currentTimeMillis();
                updateStat(finish-start);
                assertThat(json.size()).isEqualTo(v1.length);
//                System.out.println("Succ addv! " + (finish- start)/1000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static class MulvTask implements Runnable {
        private double[] v1;
        private double[] v2;
        public MulvTask(double[] v1, double[] v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        @Override
        public void run()
        {
            try {
                WebTarget mulvTarget = new JerseyClientBuilder().build().target(prefixURL + mulvURL);
                long start = System.currentTimeMillis();
                JSONArray json = mulvTarget
                        .queryParam("v1", ArrayUtils.toObject(v1))
                        .queryParam("v2", ArrayUtils.toObject(v2))
                        .request()
                        .get(JSONArray.class);
                long finish = System.currentTimeMillis();
                updateStat(finish - start);
                assertThat(json.size()).isEqualTo(v1.length);
//                System.out.println("Succ mulv! " + (finish- start)/1000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static class ExpvTask implements Runnable {
        private double[] v1;
        public ExpvTask(double[] v1) {
            this.v1 = v1;
        }

        @Override
        public void run()
        {
            try {
                WebTarget expvTarget = new JerseyClientBuilder().build().target(prefixURL+expvURL);
                long start = System.currentTimeMillis();
                JSONArray json = expvTarget
                        .queryParam("v1", ArrayUtils.toObject(v1))
                        .request()
                        .get(JSONArray.class);
                long finish = System.currentTimeMillis();
                updateStat(finish-start);
                assertThat(json.size()).isEqualTo(v1.length);
//                System.out.println("Succ expv! " + (finish- start)/1000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
