package jasonloutensockinventorysystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Jason
 */
public class Outsourced extends Part {

    private final StringProperty companyName;

    //*** CONSTRUCTOR ***
    public Outsourced() 
    {
        super();
        companyName = new SimpleStringProperty();
    }


    
    public void setCompanyName(String companyName)
    {
        this.companyName.set(companyName);
    }

    public String getCompanyName()
    {
        return this.companyName.get();
    }
    
}//end of class
