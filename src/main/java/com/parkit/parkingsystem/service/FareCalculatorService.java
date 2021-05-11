package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Date;

/**
 *  Class to contain the fare's calculator
 */
public class FareCalculatorService {
    //  Method to calcul the fare with discount
      public static double DISCOUNT_IN_PERCENTAGE = 5;
    /**
     * Method to calculate the fare with duration staying to park.
     * @param ticket
     * @return duration
     */
    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        // TODO: Some tests are failing here. Need to check if this logic is correct

        double duration = duration(ticket.getInTime(), ticket.getOutTime());

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {

                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);

                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");

        }
        //Recurrent user
        if (ticket.isRecurrentUser()) {
            ticket.setPrice(ticket.getPrice() - (ticket.getPrice() * DISCOUNT_IN_PERCENTAGE / 100));
        }

    }

    public static double duration(Date start, Date end) {
        /**
         *  Method to calcul the fare parking free and 1hour passed in the parking
         */
        if((end.getTime() - start.getTime())<=( 30 * 60 * 1000)){
            return 0.0;
        }else {
            return (end.getTime() - start.getTime()) / (double) (60 * 60 * 1000);
        }
    }
}

