package utils;

import entities.GeoPoint;

public interface InDistanceCalculator {
    double km(GeoPoint point1, GeoPoint point2);
}
