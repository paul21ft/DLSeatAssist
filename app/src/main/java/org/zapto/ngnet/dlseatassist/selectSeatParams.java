package org.zapto.ngnet.dlseatassist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adev on 4/5/15.
 */
public class selectSeatParams implements Serializable {
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

    public selectSeatParams(List<String> isSeatList) {
        this.isSeat = new ArrayList<String>(isSeatList);
    }

}
