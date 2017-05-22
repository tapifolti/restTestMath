package com.tapifolti.resttest.api;

import java.util.List;

/**
 * Created by tapifolti on 5/8/2017.
 */
public class DoubleVectorHelper {

    public static DoubleVector createDoubleVector(List<Double> v1) {
        if (v1 == null || v1.size()== 0) {
            return null;
        }
        DoubleVector dv1 = new DoubleVector(v1.size());
        for (int i=0; i<v1.size(); i++) {
            dv1.set(i, v1.get(i));
        }
        return dv1;
    }


    public static DoubleVector createDoubleVector(Double[] v1) {
        if (v1 == null || v1.length == 0) {
            return null;
        }
        DoubleVector dv1 = new DoubleVector(v1.length);
        for (int i=0; i<v1.length; i++) {
            dv1.set(i, v1[i]);
        }
        return dv1;
    }

    public static DoubleVector createDoubleVector(double[] v1) {
        if (v1 == null || v1.length == 0) {
            return null;
        }
        DoubleVector dv1 = new DoubleVector(v1.length);
        for (int i=0; i<v1.length; i++) {
            dv1.set(i, v1[i]);
        }
        return dv1;
    }

    public static double[] getDoubles(DoubleVector dv) {
        if(dv == null || dv.size() == 0) {
            return null;
        }
        double[] ret = new double[(int)dv.size()];
        for (int i=0; i<dv.size(); i++) {
            ret[i] = dv.get(i);
        }
        return ret;
    }

}
