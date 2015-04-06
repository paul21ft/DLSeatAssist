package org.zapto.ngnet.dlseatassist;


import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Created by adev on 4/3/15.
 */

public class AlertData  {

    private List<selectSeatParams> alertList;

    public String toJSONString() {

        JSONArray jarr = new JSONArray();

        Iterator it=alertList.iterator();
        while(it.hasNext()) {
            selectSeatParams sSP = (selectSeatParams) it.next();
            jarr.put(sSP.toJSONString());
        }

        return jarr.toString();

    }

    public AlertData(String jstr) {
        this.alertList=new LinkedList<selectSeatParams>();

        try {
            JSONArray jarr = new JSONArray(jstr);

            for(int i=0;i<jarr.length();i++) {
                selectSeatParams sSP = new selectSeatParams(jarr.getString(i));
                alertList.add(sSP);
            }

        }catch (Exception e) {
            assert Boolean.TRUE;
            return;
        }

    }



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

        if (seatData.economyComfortFlag & sSP.ECOnly)
            return true;

        if (sSP.anySeat)
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


    public static AlertData loadSavedAlerts(Context context) {
        AlertData flightAlertData;
        try {
            SharedPreferences prefs = context.getSharedPreferences(Constants.MY_PREFS_NAME, context.MODE_PRIVATE);
            //SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            String objectString = prefs.getString(Constants.PREFS_ALERT_STORAGE,"");
            if (objectString.length() == 0) {
                flightAlertData = new AlertData();
            } else {
                flightAlertData = new AlertData(objectString);
            }

        } catch (Exception e) {
            flightAlertData=new AlertData();
            flightAlertData.saveAlerts(context);
            assert Boolean.TRUE;
        }

        return flightAlertData;
    }

    public boolean saveAlerts(Context context) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(Constants.MY_PREFS_NAME, context.MODE_PRIVATE).edit();
            editor.putString(Constants.PREFS_ALERT_STORAGE, this.toJSONString());
            editor.apply();
            return true;
        } catch (Exception e) {
            assert Boolean.TRUE;
            return false;
        }
    }

    public Integer getNumAlerts() {
        return this.alertList.size();
    }

}
