package org.zapto.ngnet.dlseatassist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by adev on 4/3/15.
 */
public class AlertData implements Serializable {

    private List<seatSelectParams> alertList;

    private class seatSelectParams implements Serializable {
        public boolean isWindow;
        public boolean isAisle;
        public boolean isBulkhead;
        public boolean notPrem;
        public boolean notEC;
        public boolean notBulkhead;
        public boolean notNormal;
        public boolean ECOnly;
        public String isClass;
        public FlightInfo flifo;

        public List<String> isSeat;

        public seatSelectParams(List<String> isSeatList) {
            this.isSeat = new ArrayList<String>(isSeatList);
        }

    }

    public Integer addAlert(boolean isWindow, boolean isAisle,
                         boolean isBulkhead, boolean notPrem, boolean notEC, boolean notBulkhead,
                         boolean notNormal, boolean ECOnly, String isClass,
                         FlightInfo flifo, List<String> isSeat) {

        seatSelectParams sSP = new seatSelectParams(isSeat);

        sSP.isWindow=isWindow;
        sSP.isAisle=isAisle;
        sSP.isBulkhead=isBulkhead;
        sSP.notPrem=notPrem;
        sSP.notEC=notEC;
        sSP.notBulkhead=notBulkhead;
        sSP.notNormal=notNormal;
        sSP.ECOnly=ECOnly;
        sSP.flifo=flifo;
        sSP.isClass=isClass;

        this.alertList.add(sSP);
        return this.alertList.indexOf(sSP);

    }

    public void removeAlert(Integer alertIdx) {
        this.alertList.remove(alertIdx.intValue());

    }

    public void removeAllAlerts() {
        this.alertList.removeAll(this.alertList);
    }

    public String alertListToString() {

        if(this.alertList.size() == 0)
            return "No Active Alerts";

        String alertList = "";
        Iterator it = this.alertList.iterator();
        while (it.hasNext()) {
            seatSelectParams sSP = (seatSelectParams) it.next();
            Integer alertIdx = this.alertList.indexOf(sSP);
            alertList += alertIdx.toString() + ": ";
            alertList += sSP.flifo.carrier + "" + sSP.flifo.flightNumber.toString();
            alertList += " " + sSP.flifo.flightOrig + ":" + sSP.flifo.flightDest;
            alertList += " " + sSP.flifo.longFlightDate;
            alertList += "\n";
        }

        return alertList;
    }

    public boolean testSeat (seatSelectParams sSP, seatDataClass seatData, boolean showUnavailable) {

        if (!seatData.cabinCode.equals(sSP.isClass))
            return false;

        if (showUnavailable & !seatData.available)
            return false;

        if ((seatData.noStowage | seatData.bulkhead) & sSP.notBulkhead)
            return false;

        if (seatData.preferred & sSP.notPrem)
            return false;

        if (seatData.economyComfortFlag & sSP.notEC)
            return false;

        if ((seatData.preferred | seatData.economyComfortFlag) & sSP.notNormal)
            return false;

        if (!seatData.economyComfortFlag & sSP.ECOnly)
            return false;

        Iterator sNameIt = sSP.isSeat.iterator();
        while (sNameIt.hasNext()) {
            String testName = (String) sNameIt.next();
            if (testName.equals(seatData.seatName))
                return true;

        }

        if (seatData.window & sSP.isWindow)
                return true;

        if (seatData.aisle & sSP.isAisle)
            return true;

        if ((seatData.noStowage | seatData.bulkhead) & sSP.isBulkhead)
            return true;




        return false;
    }

    public boolean testAlert(Integer alertIdx) {
        seatSelectParams sSP = this.alertList.get(alertIdx);
        DLSeatInterface DLSI = new DLSeatInterface(sSP.flifo);
        return testAlert(alertIdx,DLSI);
    }

    public boolean testAlert(Integer alertIdx, DLSeatInterface DLSI) {
        seatSelectParams sSP = this.alertList.get(alertIdx);
        if (sSP==null)
            return false;
        Iterator it = DLSI.seatDataMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String seatName = (String) pair.getKey();
            seatDataClass seatData = (seatDataClass) pair.getValue();

            if (testSeat(sSP,seatData,false))
                return true;

        }

        return false;
    }

    public AlertData() {
        this.alertList=new LinkedList<seatSelectParams>();
    }

}
