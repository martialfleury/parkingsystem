package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 *  Class to contain parking spot model and his getter/setter
 */

public class ParkingSpot {
    private int number;
    private ParkingType parkingType;
    private boolean isAvailable;

    /**
     *
     * @param number
     * @param parkingType
     * @param isAvailable
     */
    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    /**
     * @return number
     */
    public int getId() {
        return number;
    }

    /**
     * @param number
     */
    public void setId(int number) {
        this.number = number;
    }

    /**
     * @return parkingType
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

    /**
     * @param parkingType
     */
    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    /**
     * @return isAvailable
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * @param available
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     *
     * @param o
     * @return number
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    /**
     *
     * @return number
     */
    @Override
    public int hashCode() {
        return number;
    }
}
