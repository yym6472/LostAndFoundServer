package com.yymstaygold.lostandfound.server.util.distance;

/**
 * Created by yanyu on 2018/4/16.
 * From https://blog.csdn.net/u011001084/article/details/52980834
 */
public class DistanceUtil {
    private static final double EARTH_RADIUS = 6371.0;

    public static double getDistance(double lon1, double lat1, double lon2, double lat2) {

        lat1 = convertDegreesToRadians(lat1);
        lon1 = convertDegreesToRadians(lon1);
        lat2 = convertDegreesToRadians(lat2);
        lon2 = convertDegreesToRadians(lon2);

        double vLon = Math.abs(lon1 - lon2);
        double vLat = Math.abs(lat1 - lat2);
        
        double h = haverSin(vLat) + Math.cos(lat1) * Math.cos(lat2) * haverSin(vLon);
        double distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(h));
        return distance * 1000.0;
    }

    private static double haverSin(double theta) {
        double v = Math.sin(theta / 2);
        return v * v;
    }

    private static double convertDegreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    private static double convertRadiansToDegrees(double radian) {
        return radian * 180.0 / Math.PI;
    }

    public static void main(String[] args) {
        System.out.println(DistanceUtil.getDistance(
                116.362687,39.964423, 116.362939,39.964434));
    }
}
