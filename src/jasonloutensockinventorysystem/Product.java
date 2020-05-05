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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Modality;


public class Product {
  
  private final IntegerProperty productID;
  private final StringProperty name;  
  private final DoubleProperty price;
  private final IntegerProperty inStock;
  private final IntegerProperty min;
  private final IntegerProperty max;
  private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
  
    //  *****  CONSTRUCTOR  *****
    public Product() 
    {
        productID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        inStock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();  
    }
  
  //setter and getter methods
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
  
  public void setProductID(int productID)  
  {
      this.productID.set(productID);
  }
  
  public int getProductID()
  {
      return this.productID.get();
  }
  
  //dealing with assoicated parts
  
  public void addAssociatedParts(Part associatedPart) 
  {
      associatedParts.add(associatedPart);
      this.associatedParts = associatedParts;
  }

  public ObservableList<Part> getAssociatedParts()
  {
      return associatedParts;
  }
  
  public boolean removeAssociatedPart(int partID)
  {
      
      for (int i = 0; i<associatedParts.size(); i++)
      {
          if (associatedParts.get(i).getPartID() == partID)
          {
              associatedParts.remove(associatedParts.get(i));
              return true;
          }
      }
      return false;
  }

  public Part lookupAssociatedPart(int partID) 
  {
      for (int i = 0; i<associatedParts.size(); i++)
      {
          if (associatedParts.get(i).getPartID() == partID)
          {
              return associatedParts.get(i);
          }
      }
      return null;
  }
 
    public static boolean validateNewProduct(String name, double price, int inStock, int min, int max)
    {
        boolean isValid = false;
        String errorMessage = "";
       
        //validate entry
        if (price < 0.0) 
        {
            errorMessage = errorMessage+"Price must be a positive value.\n";
        }
        if (min >= max || min < 0)
        {
            errorMessage = errorMessage+"Minimum value exceeds maximum value.\n";
        }
        if (inStock > max || inStock < min)
        {
            errorMessage = errorMessage+"Inventory count must be within range given.\n";
        }
        //valid entry
        else
        {
            isValid = true;
        }
        //invalid entry
        if (!isValid)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Alert");
            alert.setHeaderText("Product entry is invalid.");
            alert.setContentText(errorMessage);
            alert.showAndWait(); 
 
        }
        return isValid;   
    }
  

    
    
  //get variable for ObservableList
  public StringProperty productNameGetProperty()
  {
      return name;
  }
  
  public DoubleProperty productPriceGetProperty()
  {
      return price;
  }
  
  public IntegerProperty productIDGetProperty()
  {
      return productID;
  }
    
  public IntegerProperty productInStockGetProperty()
  {
      return inStock;
  }

  
}//end of class
