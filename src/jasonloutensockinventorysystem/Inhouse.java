package jasonloutensockinventorysystem;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


/**
 *
 * @author Jason
 */


public class Inhouse extends Part {

    private final IntegerProperty machineID;

    //*** CONSTRUCTOR ***
    public Inhouse() 
    {
        super();
        machineID = new SimpleIntegerProperty();
    }


    
    public void setMachineID(int machineID)
    {
        this.machineID.set(machineID);
    }

    public int getMachineID()
    {
        return this.machineID.get();
    }
    
}//end of class
