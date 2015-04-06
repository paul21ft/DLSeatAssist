package org.zapto.ngnet.dlseatassist;

import org.json.JSONObject;

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

    public boolean equals(FlightInfo other) {

        if (!this.flightNumber.equals(other.flightNumber))
            return false;
        if (!this.flightDate.equals(other.flightDate))
            return false;
        if (!this.longFlightDate.equals(other.longFlightDate))
            return false;
        if (!this.carrier.equals(other.carrier))
            return false;
        if (!this.flightOrig.equals(other.flightOrig))
            return false;
        if (!this.flightDest.equals(other.flightDest))
            return false;

        return true;
    }

    public String toJSONString() {
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("flightNumber", flightNumber);
            jobj.put("flightDate", flightDate);
            jobj.put("longFlightDate", longFlightDate);
            jobj.put("carrier", carrier);
            jobj.put("flightOrig", flightOrig);
            jobj.put("flightDest", flightDest);
            return jobj.toString();
        } catch (Exception e) {
            assert Boolean.TRUE;
            return "";
        }
    }

    public FlightInfo(String jstr) {
        loadJSONString(jstr);
    }

    public void loadJSONString(String jstr) {
        try {
            JSONObject jobj = new JSONObject(jstr);
            flightNumber = jobj.getInt("flightNumber");
            flightDate = jobj.getString("flightDate");
            longFlightDate = jobj.getString("longFlightDate");
            carrier = jobj.getString("carrier");
            flightOrig = jobj.getString("flightOrig");
            flightDest = jobj.getString("flightDest");
        } catch (Exception e) {
            assert Boolean.TRUE;
            //nothing
        }

    }
}
