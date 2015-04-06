package org.zapto.ngnet.dlseatassist;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Created by adev on 4/2/15.
 */
public class DLSeatInterface {

    public Boolean successful;

    private FlightInfo flifo;

    /*
    private int flightNumber;
    private String flightDate;
    private String longFlightDate;
    private String carrier;
    private String flightOrig;
    private String flightDest;
    */
    //private String eqCode;
    private HttpClient httpclient;
    private CookieStore cookieStore;
    private HttpContext localContext;
    private String seatDataStr;
    private String jsesid="NONE";
    public TreeMap<Integer,String> seatLayoutMap;

    public HashMap<String,seatDataClass> seatDataMap;



    private String buildSeatUrl() {
        String turl;
        turl="/ism/SeatMapDisplay.action?cmd=LSM&commandContext=na?1?1&flightSegment=1?1?";

        turl += this.flifo.flightNumber + "?Y?";
        turl += this.flifo.flightOrig + "?";
        turl += this.flifo.flightDate + "?";
        turl += "12:01pm?";
        turl += this.flifo.flightDest + "?";
        turl += this.flifo.flightDate + "?";
        turl += "12:02pm?0?PREFERRED_PARTY";

        return turl;

    }

    private String buildSeatRequestData() {
        String tstr="callCount=1\n";
        tstr += "page=/seat/RetrieveLSMAction\n";
        tstr += "httpSessionId="+this.jsesid+"\nscriptSessionId=0\n";
        tstr += "c0-scriptName=SeatMapGUIDWR\n";
        tstr += "c0-methodName=retrieveLogicalSeatMapDWR\n";
        tstr += "c0-id=0\n";
        tstr += "c0-e2=boolean:false\n";
        tstr += "c0-e3=string:" + this.flifo.carrier + "\n";
        tstr += "c0-e4=boolean:false\nc0-e5=boolean:false\nc0-e6=boolean:false\nc0-e7=boolean:false\nc0-e8=boolean:false\n";

        tstr += "c0-e9=string:" + this.flifo.flightDest + "\n";

        tstr += "c0-e10=string:" + this.flifo.longFlightDate + "\n";
        //Time seems unimportant
        tstr += "c0-e11=string:08%2030%20A\n";

        tstr += "c0-e12=boolean:false\nc0-e13=string:\nc0-e14=string:Y\nc0-e15=string:\nc0-e16=boolean:false\nc0-e17=string:\nc0-e18=string:\nc0-e19=boolean:false\nc0-e20=boolean:false\nc0-e21=string:\n";

        tstr += "c0-e22=string:" + this.flifo.flightOrig + "\n";
        tstr += "c0-e23=string:" + this.flifo.longFlightDate + "\n";
        //Time seems unimportant
        tstr += "c0-e24=string:07%2000%20A\n";

        tstr += "c0-e25=boolean:false\nc0-e26=boolean:false\nc0-e27=boolean:false\nc0-e28=string:\nc0-e29=string:\nc0-e30=string:\nc0-e31=string:\n";

        tstr += "c0-e32=string:" + this.flifo.flightNumber + "\n";

        tstr += "c0-e33=string:\nc0-e34=string:\nc0-e35=boolean:false\nc0-e36=string:\nc0-e37=string:\nc0-e38=boolean:false\nc0-e39=string:\nc0-e40=string:\nc0-e41=string:\nc0-e42=string:\nc0-e43=string:\nc0-e44=string:\nc0-e45=string:\nc0-e46=string:\nc0-e47=string:\nc0-e48=boolean:false\nc0-e49=boolean:false\nc0-e50=string:\nc0-e51=boolean:false\nc0-e1=Object_Object:{ETicketPresent:reference:c0-e2, airlineCode:reference:c0-e3, airportGateCheckInProgress:reference:c0-e4, allFlightsDeparted:reference:c0-e5, allowSeatForOACodeshare:reference:c0-e6, allowSeatSelForOa:reference:c0-e7, allowSeatSelectionForKL:reference:c0-e8, arrivalCity:reference:c0-e9, arrivalDate:reference:c0-e10, arrivalTime:reference:c0-e11, basicEconomyDisplayFlag:reference:c0-e12, category:reference:c0-e13, classOfServiceCode:reference:c0-e14, classOfServiceDescription:reference:c0-e15, cleanedFlag:reference:c0-e16, codeshareAirlineName:reference:c0-e17, currentActionCode:reference:c0-e18, decontentedFlight:reference:c0-e19, departedFlight:reference:c0-e20, departureArrivalCityName:reference:c0-e21, departureCity:reference:c0-e22, departureDate:reference:c0-e23, departureTime:reference:c0-e24, displayStaticSeatMap:reference:c0-e25, dlConnectionCarrier:reference:c0-e26, economyCabinOnly:reference:c0-e27, equipmentCode:reference:c0-e28, equipmentType:reference:c0-e29, equipmentUrl:reference:c0-e30, flightInfoIndex:reference:c0-e31, flightNumber:reference:c0-e32, flightTime:reference:c0-e33, flightType:reference:c0-e34, groundHandled:reference:c0-e35, groundHandledBy:reference:c0-e36, imageUrl:reference:c0-e37, iropProtectedBlock:reference:c0-e38, iropTripType:reference:c0-e39, iropType:reference:c0-e40, itineraryDestination:reference:c0-e41, itineraryOrigin:reference:c0-e42, legId:reference:c0-e43, marketingAirlineCode:reference:c0-e44, operatingAirline:reference:c0-e45, operatingFlightNumber:reference:c0-e46, segmentNumber:reference:c0-e47, standbyFlag:reference:c0-e48, transconFlight:reference:c0-e49, type:reference:c0-e50, withinCheckInWindow:reference:c0-e51}\nc0-param0=Array:[reference:c0-e1]\nc0-param1=string:1\nc0-param2=string:USD\nc0-param3=boolean:true\nc0-param4=string:SHOPPING\nbatchId=0";

        return tstr;
    }

    private void prepCookie() {

        try {
            HttpGet httpget = new HttpGet("http://www.delta.com");
            HttpResponse response = this.httpclient.execute(httpget,this.localContext);
            String seatUrl = this.buildSeatUrl();
            httpget=new HttpGet("http://www.delta.com" + seatUrl);
            response = this.httpclient.execute(httpget,this.localContext);

            List<Cookie> cookies = this.cookieStore.getCookies();


            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID"))
                    this.jsesid = cookie.getValue();
            }
            int i=0;
            i=i+1;

        } catch(Exception e) {
            //Handle error here
            int i=0;
            i=i+1;
            Log.d(Constants.TAG, "DLSI.prepCookie()", e);
        }


    }

    private void parseSeatData() {
        String sdata=this.seatDataStr;
        String[] splitData = sdata.split(";");
        HashMap<String,String> rawVarData = new HashMap<String,String>();
        HashSet<String> varNames = new HashSet<String>();

        //Load rawVarData
        for(int i=0;i<splitData.length;i++) {
            String tstr = splitData[i];
            String[] strSplit = tstr.split("=");
            if (strSplit.length == 1)
                continue;
            String varName = strSplit[0];
            String varData = strSplit[1];
            String varValue = varData.replaceAll("\"", "");
            rawVarData.put(varName, varValue);
            String[] varPrefix = varName.split(Pattern.quote("."));
            if (varPrefix.length > 1 && varPrefix[0].length() >= 2)
                varNames.add(varPrefix[0]);
        }

        //Create map from variable ID to seat names
        for(String varStr : varNames) {

            if (!rawVarData.containsKey(varStr + ".isseat"))
                continue;

            String isSeat = rawVarData.get(varStr + ".isseat");
            if (!isSeat.isEmpty() && isSeat.equals("true")) {
                String seatName = rawVarData.get(varStr + ".id");
                seatDataClass sdc = new seatDataClass(seatName,varStr);
                this.seatDataMap.put(seatName,sdc);
            }

        }

        //Determine Configuration
        this.seatLayoutMap=new TreeMap<>();
        for(String varStr : varNames) {

            /*

            if (rawVarData.containsKey(varStr + ".equipmentCode")) {
                String eqCode = rawVarData.get(varStr + ".equipmentCode");
                this.eqCode=eqCode;
            }
            */
            if (!rawVarData.containsKey(varStr + ".seatConfiguration"))
                continue;

            String seatConfig = rawVarData.get(varStr + ".seatConfiguration");
            if (seatConfig.equals("null"))
                continue;

            String seatRowVar = rawVarData.get(varStr+".seatRows");
            String cabinCode = rawVarData.get(varStr+".cabinCode");


            for (int i=0;i<100;i++) {
                if (rawVarData.containsKey(seatRowVar+"["+i+"]")) {
                    String rowVar = rawVarData.get(seatRowVar + "[" + i + "]");
                    String rowNumStr = rawVarData.get(rowVar + ".rowNumber");
                    String seatListVar = rawVarData.get(rowVar+".seatColumns");
                    if (!rawVarData.containsKey(seatListVar+"[0]"))
                        continue;
                    Integer rowNumber = Integer.parseInt(rowNumStr);
                    for (int k=0;k<20;k++) {
                        if(rawVarData.containsKey(seatListVar+"["+k+"]")) {
                            String colVar = rawVarData.get(seatListVar+"["+k+"]");
                            String isSeat = rawVarData.get(colVar+".isseat");
                            if (isSeat.equals("false"))
                                continue;
                            String seatName = rawVarData.get(colVar+".id");
                            seatDataClass sdc = this.seatDataMap.get(seatName);
                            sdc.cabinCode=cabinCode;

                            String columnNumber = rawVarData.get(colVar + ".columnNumber");
                            sdc.columnIdx=Integer.parseInt(columnNumber);
                            if (sdc.columnIdx == 0)
                                sdc.window=true;
                            if ((sdc.columnIdx+1) == seatConfig.length())
                                sdc.window=true;
                            if (seatConfig.length() > (sdc.columnIdx + 1) && seatConfig.charAt(sdc.columnIdx+1) == '|')
                                sdc.aisle=true;
                            if (sdc.columnIdx > 0 && (sdc.columnIdx-1 >= seatConfig.length() || seatConfig.charAt(sdc.columnIdx-1) == '|'))
                                sdc.aisle=true;
                            sdc.rowNumber = rowNumber;
                            this.seatLayoutMap.put(rowNumber,seatConfig);
                        }
                    }
                }
            }

        }


        //Load seat classes with data
        Iterator it = this.seatDataMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String seatName = (String)pair.getKey();
            seatDataClass sdc = (seatDataClass)pair.getValue();
            String varid = sdc.varMain + ".";

            String varSeat = rawVarData.get(varid + "seat");
            sdc.varSeat=varSeat;
            varSeat += ".";


            String bulkheadVal = rawVarData.get(varid + "bulkhead");
            if (bulkheadVal.equals("true"))
                sdc.bulkhead=true;
            else
                sdc.bulkhead=false;




            String availableVal = rawVarData.get(varSeat + "available");
            if (availableVal.equals("true"))
                sdc.available=true;
            else
                sdc.available=false;

            String blockedVal = rawVarData.get(varSeat + "blocked");
            if (blockedVal.equals("true"))
                sdc.blocked=true;
            else
                sdc.blocked=false;


            String choiceVal = rawVarData.get(varSeat + "choice");
            if (choiceVal.equals("true"))
                sdc.choice=true;
            else
                sdc.choice=false;

            String preferredVal = rawVarData.get(varSeat + "preferred");
            if (preferredVal.equals("true"))
                sdc.preferred=true;
            else
                sdc.preferred=false;

            String preferredAisleVal = rawVarData.get(varSeat + "preferredAisle");
            if (preferredAisleVal.equals("true"))
                sdc.preferredAisle=true;
            else
                sdc.preferredAisle=false;

            String economyComfortFlagVal = rawVarData.get(varSeat + "economyComfortFlag");
            if (economyComfortFlagVal.equals("true"))
                sdc.economyComfortFlag=true;
            else
                sdc.economyComfortFlag=false;

            String exitVal = rawVarData.get(varSeat + "exit");
            if (exitVal.equals("true"))
                sdc.exit=true;
            else
                sdc.exit=false;

            String noStowageVal = rawVarData.get(varSeat + "noStowage");
            if (noStowageVal.equals("true"))
                sdc.noStowage=true;
            else
                sdc.noStowage=false;





        }

        it = this.seatDataMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String seatName = (String) pair.getKey();
            seatDataClass sdc = (seatDataClass) pair.getValue();



            seatDataClass seatBefore = this.seatDataMap.get(sdc.rowNumber.toString() + (char)( (int)sdc.colNumber.charAt(0) - 1));
            seatDataClass seatAfter = this.seatDataMap.get(sdc.rowNumber.toString() + (char)( (int)sdc.colNumber.charAt(0) + 1));

            if (sdc.window && sdc.columnIdx == 0)
                sdc.leftBar=true;
            else if (sdc.window)
                sdc.rightBar=true;

            if (sdc.window && sdc.aisle) {
                sdc.leftBar = true;
                sdc.rightBar = true;
            }

            if (sdc.aisle && seatBefore != null && seatBefore.aisle)
                sdc.leftBar=true;
            if (sdc.aisle && seatAfter != null && seatAfter.aisle)
                sdc.rightBar=true;

            if (sdc.aisle && seatAfter == null)
                sdc.rightBar=true;
            else if (sdc.aisle && seatBefore == null)
                sdc.leftBar=true;


        }


    }

    private void loadSeatData() {
        String seatDataStr="";
        try {
            String dataStr = this.buildSeatRequestData();
            HttpPost httppost = new HttpPost("http://www.delta.com/seat/dwr/call/plaincall/SeatMapGUIDWR.retrieveLogicalSeatMapDWR.dwr");
            httppost.setEntity(new StringEntity(dataStr));
            HttpResponse resp= this.httpclient.execute(httppost);
            seatDataStr= EntityUtils.toString(resp.getEntity());
            seatDataStr=seatDataStr.replace("\n","").replace("\r","");
            this.seatDataStr=seatDataStr;
            //this.parseSeatData();

        }catch(Exception e) {
            Log.d(Constants.TAG,"DLSI.loadSeatData()",e);
        }





    }

    public String createDisplayString() {

        String retstr = "";

        //Build Header

        retstr += this.flifo.carrier + this.flifo.flightNumber + "  ";
        retstr += this.flifo.flightOrig + " to " + this.flifo.flightDest + "\n";
        retstr += this.flifo.longFlightDate + "\n\n";
        retstr += "O-Open X-Occupied B-Blocked E-EC+ P-Preferred\n   Row (Class)\n\n";

        if (!this.successful)
            return retstr + "\n\nSearch Failed!";





        Iterator it = this.seatLayoutMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Integer rowNumber = (Integer) pair.getKey();
            String layout = (String) pair.getValue();
            String bulkHead = "|";
            String newRow = "|";
            String cabinCode = "Y";
            Boolean hasBulk = false;

            for (int i=0;i<layout.length();i++) {
                String newBulkStr = " ";
                String seatName = rowNumber.toString() + layout.charAt(i);
                if (this.seatDataMap.containsKey(seatName)) {
                    seatDataClass sdc = this.seatDataMap.get(seatName);
                    sdc.assignDisplayString();
                    newRow += sdc.dispStr;
                    if (sdc.noStowage) {
                        newBulkStr = "_";
                        hasBulk = true;
                    }
                    cabinCode=sdc.cabinCode;

                } else if (layout.charAt(i)=='|') {
                    newRow += '|';
                } else {
                    newRow += " ";
                }
                bulkHead += newBulkStr;
            }
            bulkHead += "|\n";
            newRow += "| " + rowNumber.toString() + "("+cabinCode+")\n";

            if (hasBulk) {
                retstr += bulkHead + newRow;
            } else {
                retstr += newRow;
            }
        }


        retstr += "\n\n Window Seat List \n\n";

        it = this.seatLayoutMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Integer rowNumber = (Integer) pair.getKey();
            String layout = (String) pair.getValue();
            String bulkHead = "|";
            String newRow = "|";
            String cabinCode = "Y";
            Boolean hasBulk = false;

            for (int i=0;i<layout.length();i++) {
                String newBulkStr = " ";
                String seatName = rowNumber.toString() + layout.charAt(i);
                if (this.seatDataMap.containsKey(seatName)) {
                    seatDataClass sdc = this.seatDataMap.get(seatName);
                    sdc.assignDisplayString();
                    if (sdc.window)
                        newRow += sdc.dispStr;
                    else
                        newRow += '-';
                    if (sdc.noStowage) {
                        newBulkStr = "_";
                        hasBulk = true;
                    }
                    cabinCode=sdc.cabinCode;

                } else if (layout.charAt(i)=='|') {
                    newRow += '|';
                } else {
                    newRow += " ";
                }
                bulkHead += newBulkStr;
            }
            bulkHead += "|\n";
            newRow += "| " + rowNumber.toString() + "("+cabinCode+")\n";

            if (hasBulk) {
                retstr += bulkHead + newRow;
            } else {
                retstr += newRow;
            }
        }

        retstr += "\n\n Aisle Seat List \n\n";

        it = this.seatLayoutMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Integer rowNumber = (Integer) pair.getKey();
            String layout = (String) pair.getValue();
            String bulkHead = "|";
            String newRow = "|";
            String cabinCode = "Y";
            Boolean hasBulk = false;

            for (int i=0;i<layout.length();i++) {
                String newBulkStr = " ";
                String seatName = rowNumber.toString() + layout.charAt(i);
                if (this.seatDataMap.containsKey(seatName)) {
                    seatDataClass sdc = this.seatDataMap.get(seatName);
                    sdc.assignDisplayString();
                    if (sdc.aisle)
                        newRow += sdc.dispStr;
                    else
                        newRow += '-';
                    if (sdc.noStowage) {
                        newBulkStr = "_";
                        hasBulk = true;
                    }
                    cabinCode=sdc.cabinCode;

                } else if (layout.charAt(i)=='|') {
                    newRow += '|';
                } else {
                    newRow += " ";
                }
                bulkHead += newBulkStr;
            }
            bulkHead += "|\n";
            newRow += "| " + rowNumber.toString() + "("+cabinCode+")\n";

            if (hasBulk) {
                retstr += bulkHead + newRow;
            } else {
                retstr += newRow;
            }
        }

        retstr += "\n\n Bulkhead Seat List \n\n";

        it = this.seatLayoutMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Integer rowNumber = (Integer) pair.getKey();
            String layout = (String) pair.getValue();
            String bulkHead = "|";
            String newRow = "|";
            String cabinCode = "Y";
            Boolean hasBulk = false;

            for (int i=0;i<layout.length();i++) {
                String newBulkStr = " ";
                String seatName = rowNumber.toString() + layout.charAt(i);
                if (this.seatDataMap.containsKey(seatName)) {
                    seatDataClass sdc = this.seatDataMap.get(seatName);
                    sdc.assignDisplayString();
                    if (sdc.bulkhead)
                        newRow += sdc.dispStr;
                    else
                        newRow += '-';
                    if (sdc.noStowage) {
                        newBulkStr = "_";
                        hasBulk = true;
                    }
                    cabinCode=sdc.cabinCode;

                } else if (layout.charAt(i)=='|') {
                    newRow += '|';
                } else {
                    newRow += " ";
                }
                bulkHead += newBulkStr;
            }
            bulkHead += "|\n";
            newRow += "| " + rowNumber.toString() + "("+cabinCode+")\n";

            if (hasBulk) {
                retstr += bulkHead + newRow;
            } else {
                retstr += newRow;
            }
        }

        retstr += "\n\n No Stowage Seat List \n\n";

        it = this.seatLayoutMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Integer rowNumber = (Integer) pair.getKey();
            String layout = (String) pair.getValue();
            String bulkHead = "|";
            String newRow = "|";
            String cabinCode = "Y";
            Boolean hasBulk = false;

            for (int i=0;i<layout.length();i++) {
                String newBulkStr = " ";
                String seatName = rowNumber.toString() + layout.charAt(i);
                if (this.seatDataMap.containsKey(seatName)) {
                    seatDataClass sdc = this.seatDataMap.get(seatName);
                    sdc.assignDisplayString();
                    if (sdc.noStowage)
                        newRow += sdc.dispStr;
                    else
                        newRow += '-';
                    if (sdc.noStowage) {
                        newBulkStr = "_";
                        hasBulk = true;
                    }
                    cabinCode=sdc.cabinCode;

                } else if (layout.charAt(i)=='|') {
                    newRow += '|';
                } else {
                    newRow += " ";
                }
                bulkHead += newBulkStr;
            }
            bulkHead += "|\n";
            newRow += "| " + rowNumber.toString() + "("+cabinCode+")\n";

            if (hasBulk) {
                retstr += bulkHead + newRow;
            } else {
                retstr += newRow;
            }
        }

        /*
        TreeMap<Integer,TreeSet<String>> rowSet = new TreeMap<>();


        Iterator it = this.seatDataMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String seatName = (String) pair.getKey();
            seatDataClass sdc = (seatDataClass) pair.getValue();
            sdc.assignDisplayString();
            TreeSet<String> sList;
            if (rowSet.containsKey(sdc.rowNumber))
                sList = rowSet.get(sdc.rowNumber);
            else {
                sList = new TreeSet<String>();
                rowSet.put(sdc.rowNumber,sList);
            }
            sList.add(sdc.seatName);
        }


        it = rowSet.entrySet().iterator();


        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Integer rowNum = (Integer) pair.getKey();
            TreeSet<String> sList = (TreeSet<String>) pair.getValue();
            String rowStr = rowNum.toString();
            rowStr=String.format("%-3s: ",rowStr);
            Iterator it2 = sList.iterator();
            while(it2.hasNext()) {
                seatDataClass sdc = this.seatDataMap.get((String)it2.next());
                String dispStr = sdc.dispStr;
                if (sdc.leftBar)
                    dispStr = "|" + dispStr;
                if (sdc.rightBar)
                    dispStr += "|";
                rowStr += dispStr + " ";
            }
            retstr += rowStr + "\n";
        }

        */


        return retstr;

    }

    public DLSeatInterface(FlightInfo tflifo) {
        this.cookieStore = new BasicCookieStore();
        this.localContext = new BasicHttpContext();
        this.localContext.setAttribute(ClientContext.COOKIE_STORE,this.cookieStore);
        this.httpclient = new DefaultHttpClient();

        this.seatDataMap=new HashMap<>();

        /*
        this.flightNumber=tflightNumber;
        this.flightDate=tflightDate;
        this.longFlightDate=tlongFlightDate;
        this.flightOrig=tflightOrig;
        this.flightDest=tflightDest;
        this.carrier=tcarrier;
        */
        this.flifo=tflifo;

        this.successful=false;

        try {
            long timeBefore, timeAfter;
            timeBefore = System.currentTimeMillis();
            this.prepCookie();
            timeAfter = System.currentTimeMillis();
            Log.d(Constants.TAG,"Prep Cookie Time: " + (timeAfter-timeBefore));
            timeBefore=timeAfter;
            this.loadSeatData();
            timeAfter = System.currentTimeMillis();
            Log.d(Constants.TAG,"Load Seat Data Time: " + (timeAfter-timeBefore));
            timeBefore=timeAfter;
            this.parseSeatData();
            timeAfter = System.currentTimeMillis();
            Log.d(Constants.TAG,"Parse Seat Data Time: " + (timeAfter-timeBefore));
            if(this.seatDataMap.size() > 0)
                this.successful=true;
        } catch (Exception e) {
            Log.d(Constants.TAG,"Search Failed: " ,e);
            this.successful=false;
        }

    }
}
