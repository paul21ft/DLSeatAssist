package org.zapto.ngnet.dlseatassist;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by adev on 4/5/15.
 */
public class selectSeatParams {
    public boolean isWindow;
    public boolean isAisle;
    public boolean isBulkhead;
    public boolean notPrem;
    public boolean notEC;
    public boolean notBulkhead;
    public boolean notNormal;
    public boolean ECOnly;
    public boolean anySeat;
    public String isClass;
    public FlightInfo flifo;

    public List<String> isSeat;

    public selectSeatParams(List<String> isSeatList) {
        this.isSeat = new ArrayList<String>(isSeatList);
    }

    public String toJSONString() {
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("isWindow", isWindow);
            jobj.put("isWindow", isWindow);
            jobj.put("isAisle", isAisle);
            jobj.put("isBulkhead", isBulkhead);
            jobj.put("notPrem", notPrem);
            jobj.put("notEC", notEC);
            jobj.put("notBulkhead", notBulkhead);
            jobj.put("notNormal", notNormal);
            jobj.put("ECOnly", ECOnly);
            jobj.put("anySeat", anySeat);
            jobj.put("isClass",isClass);
            jobj.put("flifo",flifo.toJSONString());

            JSONArray jarr = new JSONArray();

            Iterator it = isSeat.iterator();
            while(it.hasNext()) {
                String sStr = (String) it.next();
                jarr.put(sStr);
            }
            jobj.put("isSeat",jarr.toString());

            return jobj.toString();
        } catch(Exception e) {
            assert Boolean.TRUE;
            return "";
        }

    }

    public selectSeatParams(String jstr) {
        this.isSeat = new ArrayList<String>();
        loadJSONString(jstr);
    }

    public void loadJSONString(String jstr) {
        try {
            JSONObject jobj = new JSONObject(jstr);
            isWindow = jobj.getBoolean("isWindow");
            isAisle = jobj.getBoolean("isAisle");
            isBulkhead = jobj.getBoolean("isBulkhead");
            notPrem = jobj.getBoolean("notPrem");
            notEC = jobj.getBoolean("notEC");
            notBulkhead = jobj.getBoolean("notBulkhead");
            notNormal = jobj.getBoolean("notNormal");
            ECOnly = jobj.getBoolean("ECOnly");
            anySeat = jobj.getBoolean("anySeat");
            isClass = jobj.getString("isClass");

            flifo = new FlightInfo(jobj.getString("flifo"));

            JSONArray jarr = new JSONArray(jobj.getString("isSeat"));

            for (int i=0;i<jarr.length();i++) {
                isSeat.add(jarr.getString(i));
            }


        } catch (Exception e) {
            assert Boolean.TRUE;
            return;
        }



    }

}
