package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martial Fleury
 * Launch of the Park'it Application.
 */
public class App {
    private static final Logger logger = LogManager.getLogger("App");

    /**
     * This method main launch Park'it Application.
     * @param args
     */
    public static void main(String args[]){
        logger.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
