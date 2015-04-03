package org.zapto.ngnet.dlseatassist;

import java.io.Serializable;

@SuppressWarnings("serial")

/**
 * Created by adev on 4/3/15.
 */
public class FlightInfo implements Serializable {
    public Integer flightNumber;
    public String flightDate;
    public String longFlightDate;
    public String carrier;
    public String flightOrig;
    public String flightDest;

    public FlightInfo(Integer tflightNumber, String tflightDate, String tlongFlightDate, String tflightOrig, String tflightDest, String tcarrier) {
        this.flightNumber=tflightNumber;
        this.flightDate=tflightDate;
        this.longFlightDate=tlongFlightDate;
        this.flightOrig=tflightOrig;
        this.flightDest=tflightDest;
        this.carrier=tcarrier;
    }
}
