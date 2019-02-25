package com.naer.pdfreader;

import java.math.BigDecimal;

public class SearchMapService {
    public static  final int  DEFAULT_DIV_SCALE = 10;
    public static Boolean check(Place company, Double lat, Double lag,
                                Integer r) {
        double R = 6371;// 地球半径
        double distance = 0.0;
        double dLat = Double.valueOf(new BigDecimal(String.valueOf((company
                .getLat() - lat)))
                .multiply(new BigDecimal(String.valueOf(Math.PI)))
                .divide(new BigDecimal(String.valueOf(180)), DEFAULT_DIV_SCALE,
                        BigDecimal.ROUND_HALF_EVEN).toString());
        double dLon = Double.valueOf(new BigDecimal(String.valueOf((company
                .getLag() - lag)))
                .multiply(new BigDecimal(String.valueOf(Math.PI)))
                .divide(new BigDecimal(String.valueOf(180)), DEFAULT_DIV_SCALE,
                        BigDecimal.ROUND_HALF_EVEN).toString());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(company.getLat() * Math.PI / 180)
                * Math.cos(lat * Math.PI / 180) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R * 1000;
        System.out.println(distance);
        if (distance > Double.valueOf(String.valueOf(r))) {
            return false;
        }
        return true;
    }
}
