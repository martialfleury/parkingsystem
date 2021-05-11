package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import net.sf.saxon.style.DataElement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @version 3.0
 * @see Fare
 * @see ParkingSpotDAO
 * @see TicketDAO
 * @see DataBaseTestConfig
 * @see DataBasePrepareService
 * @see ParkingSpot
 * @see Ticket
 * @see ParkingService
 * @see InputReaderUtil
 */
@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static FareCalculatorService fareCalculatorService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        dataBasePrepareService = new DataBasePrepareService();
        fareCalculatorService = new FareCalculatorService();
    }


    @AfterAll
    private static void tearDown(){

    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService.clearDataBaseEntries();
    }

    /**
     * Integration test incoming car
     * @throws Exception
     */
    @Test
    public void testParkingACar() throws Exception {

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        String vehicleNumber = "";
        try {
            vehicleNumber = inputReaderUtil.readVehicleRegistrationNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ParkingSpot spot = parkingService.getNextParkingNumberIfAvailable();

        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability

        assertEquals(vehicleNumber, ticketDAO.getTicket(vehicleNumber).getVehicleRegNumber());
        assertFalse(parkingSpotDAO.getParkingSpot(spot.getId()).isAvailable());
    }

    /**
     * Integration test exiting car
     * @throws Exception
     */
    @Test
    public void testParkingLotExit() throws Exception {
        testParkingACar();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        parkingService.processExitingVehicle();
        String vehicleNumber = "";
        try {
            vehicleNumber = inputReaderUtil.readVehicleRegistrationNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO: check that the fare generated and out time are populated correctly in the database
        Ticket ticket = ticketDAO.getTicket(vehicleNumber);
        fareCalculatorService.calculateFare(ticket);

        assertNotNull(ticketDAO.getTicket(vehicleNumber).getOutTime());
        assertEquals(ticket.getPrice(), ticketDAO.getTicket(vehicleNumber).getPrice());
    }

    /**
     * Integration test with discount in application
     * @throws Exception
     */
    @Test
    public void checkDiscountApplication() throws Exception {
        testParkingLotExit();
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        String vehicleNumber = "";
        try {
            vehicleNumber = inputReaderUtil.readVehicleRegistrationNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Ticket ticket = ticketDAO.getTicket(vehicleNumber);
        long inTime =  ticket.getInTime().getTime() ;
        long outTime = inTime + (  60 * 60 * 1000);
        Date outDate = new Date();
        outDate.setTime(outTime);

        parkingService.processExitingVehicleWithSpecificTime(outDate);
        assertEquals(Fare.CAR_RATE_PER_HOUR - (Fare.CAR_RATE_PER_HOUR * FareCalculatorService.DISCOUNT_IN_PERCENTAGE / 100), ticketDAO.getTicket(vehicleNumber).getPrice());
    }
}
