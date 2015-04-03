package org.zapto.ngnet.dlseatassist;

/**
 * Created by adev on 4/3/15.
 */
public class seatDataClass {
    public boolean available;
    public boolean blocked;
    public boolean aisle;
    public boolean window;
    public boolean bulkhead;
    public boolean noStowage;
    public boolean choice;
    public boolean preferred;
    public boolean preferredAisle;
    public boolean economyComfortFlag;
    public boolean exit;
    public String varMain;
    public String varSeat;
    public String seatName;
    public String dispStr;
    public Integer rowNumber;
    public String colNumber;
    public Integer columnIdx;
    public String cabinCode;

    public boolean leftBar;
    public boolean rightBar;

    public seatDataClass(String tseatName,String tvarid) {
        this.varMain=tvarid;
        this.seatName=tseatName;
        this.dispStr = "?";
        String rowStr = tseatName.substring(0,tseatName.length() - 1);
        this.rowNumber=Integer.parseInt(rowStr);
        this.colNumber = tseatName.substring(tseatName.length() - 1, tseatName.length());
        this.leftBar=false;
        this.rightBar=false;
    }

    public void assignDisplayString() {
        String openStr = "O";
        String ecStr = "E";
        String prefStr = "P";
        String unavailStr = "X";
        String blockedStr = "B";

        this.dispStr=openStr;


        if (this.blocked) {
            this.dispStr = blockedStr;
            return;
        }

        if (!this.available) {
            this.dispStr = unavailStr;
            return;
        }

        if (this.economyComfortFlag) {
            this.dispStr = ecStr;
            return;
        }

        if (this.preferred) {
            this.dispStr=prefStr;
            return;
        }

    }
}
