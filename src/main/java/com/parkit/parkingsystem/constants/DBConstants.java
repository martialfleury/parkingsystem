package com.parkit.parkingsystem.constants;

/**
 * Class contains the differents queries.
 * This allows the exchange between the application and the different tables.
 */
public class DBConstants {
    /**
     * here,the query is used to search for available parking spots
     */
    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
    /**
     * here,the query is used to update parking spot availability
     */
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";
    /**
     * here,the query is used to save tickets into database
     */
    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
    /**
     * here,the query is used to update ticket with price and outTime
     */
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
    /**
     * here,the query is used to retrieve a ticket from database.
     */
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";
    /**
     * check incoming vehicle registration number.
     * and check the vehicles that are using the parking recurrent
     */
    public static final String PARKING_RECURRENCE = "select count(ID) from ticket where VEHICLE_REG_NUMBER=?";
    /**
     * here , the query is used for to find out the available parking spot
     */
    public static final String GET_PARKING_SPOT = "select PARKING_NUMBER, TYPE, AVAILABLE from parking where PARKING_NUMBER = ?";




}

