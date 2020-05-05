package jasonloutensockinventorysystem;
/**
 *
 * @author Jason
 */
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public abstract class Part {

    private final IntegerProperty partID;
    private final SimpleStringProperty name;  
    private final DoubleProperty price;
    private final IntegerProperty inStock;
    private final IntegerProperty min;
    private final IntegerProperty max;
    
    
   // *****  CONSTRUCTOR *****
    public Part() 
    {
        partID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        inStock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }
    
    
 
  public void setName(String name)
  {
      this.name.set(name);
  }
  
  public String getName()
  {
      return this.name.get();
  }
  
  public void setPrice(double price)
  {
      this.price.set(price);
  }
  
  public double getPrice()
  {
      return this.price.get();
  }
  
  public void setInStock(int inStock)
  {
      this.inStock.set(inStock);
  }
          
  public int getInStock()
  {
      return this.inStock.get();
  }
  
  public void setMin(int min)
  {
      this.min.set(min);
  }
  
  public int getMin()
  {
      return this.min.get();
  }
  
  public void setMax(int max)
  {
      this.max.set(max);
  }
          
  public int getMax()
  {
      return this.max.get();
  }
  
  public void setPartID(int partID) 
  {
      
      this.partID.set(partID);
  }
          
  public int getPartID()
  {
      return this.partID.get();
  }

  
  //added get property methods for OL - must be on parts! (not listed on UML)
  public StringProperty partNameGetProperty()
  {
      return name;
  }
  
  public DoubleProperty partPriceGetProperty()
  {
      return price;
  }
  
  public IntegerProperty partIDGetProperty()
  {
      return partID;
  }
    
  public IntegerProperty partInStockGetProperty()
  {
      return inStock;
  }

}//end of class
