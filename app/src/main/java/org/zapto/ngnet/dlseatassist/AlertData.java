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

    private List<selectSeatParams> alertList;



    public Integer addAlert(selectSeatParams sSP) {

        Iterator it = this.alertList.iterator();
        while(it.hasNext()) {
            selectSeatParams otherSSP = (selectSeatParams) it.next();
            if (sSP.flifo.equals(otherSSP.flifo)) {
                this.alertList.remove(otherSSP);
                this.alertList.add(sSP);
                return -1*this.alertList.indexOf(sSP);
            }
        }

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
            selectSeatParams sSP = (selectSeatParams) it.next();
            Integer alertIdx = this.alertList.indexOf(sSP);
            alertList += alertIdx.toString() + ": ";
            alertList += sSP.flifo.carrier + "" + sSP.flifo.flightNumber.toString();
            alertList += " " + sSP.flifo.flightOrig + ":" + sSP.flifo.flightDest;
            alertList += " " + sSP.flifo.longFlightDate;
            alertList += "\n";
        }

        return alertList;
    }

    public boolean testSeat (selectSeatParams sSP, seatDataClass seatData, boolean showUnavailable) {

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
        selectSeatParams sSP = this.alertList.get(alertIdx);
        DLSeatInterface DLSI = new DLSeatInterface(sSP.flifo);
        return testAlert(sSP,DLSI);
    }

    public boolean testAlert(selectSeatParams sSP, DLSeatInterface DLSI) {
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
        this.alertList=new LinkedList<selectSeatParams>();
    }

}
