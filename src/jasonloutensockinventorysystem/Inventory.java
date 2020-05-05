package jasonloutensockinventorysystem;
/**
 * @author Jason
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class Inventory 
{

  private static int uniquePartID = 0;
  private static int uniqueProductID = 0;
  private static ObservableList<Product> products = FXCollections.observableArrayList();
  private static ObservableList<Part> allParts = FXCollections.observableArrayList();

  // **** CONSTRUCTOR ****
    public Inventory() 
    {
    
    }
  
    
    public static void addPart(Part newEntry) 
    {
        allParts.add(newEntry);
    }
    
    public static boolean deletePart(Part selectedPart)
    {
        boolean partInInventory = false;
        
        if (allParts.contains(selectedPart))
        {
            allParts.remove(selectedPart);
            partInInventory = true;
        }
        
        return partInInventory;
    } 
    
    public static void updatePart(int partIndex, Part updatedPart) //must also pass part to update
    {
        allParts.set(partIndex, updatedPart);
    } 
    
    public static void addProduct(Product newEntry)  
    {
        products.add(newEntry);
    }
    
    public static boolean removeProduct(int selectedProductID)
    {
        boolean productInInventory = false;
        Product thisProduct = null;
        
        for (Product p : products)
        {
            if (p.getProductID() == selectedProductID)
                thisProduct = p;
        }
        if (thisProduct != null)
        {
            products.remove(thisProduct);
            productInInventory = true;
            return productInInventory;
        }
        else
        {
            return productInInventory;
        }
    }
    
    public static void updateProduct(int productID, Product updatedProduct)  
    {
        products.set(lookupProductIndex(productID), updatedProduct);
    }

    //method to return Part object based on Part ID given
    public static Part lookupPart(int searchPartID)  
    {
        return allParts.get(lookupPartIndex(searchPartID));  
    }

    //method to return index of part given partID
    public static int lookupPartIndex(int searchPartID)
    {
        int index = -1;
        for (int i=0; i<allParts.size(); i++)
            {
                if (searchPartID == allParts.get(i).getPartID())
                {
                    index = i;
                }
            }
        return index;
    }
   
    //method to return uniquePartID
    public static int getUniquePartID()
    {
        uniquePartID++;
        return uniquePartID;
    }
 
    //method to return uniqueProductID
    public static int getUniqueProductID()
    {
        uniqueProductID++;
        return uniqueProductID;
    }
    
    public static ObservableList<Part> getAllParts()
    {
        return allParts;
    }
    
    public static ObservableList<Product> getProducts()
    {
        return products;
    }

    public static Product lookupProduct(int productID)
    {
        for (int i = 0; i<products.size(); i++)
        {
            if (productID == products.get(i).getProductID());
            {
                return products.get(i);
            }
        }
        return null;
    } 
    
    //method to return index of part given productID
    public static int lookupProductIndex(int searchProductID)
    {
        int index = -1;
        for (int i=0; i<products.size(); i++)
            {
                if (searchProductID == products.get(i).getProductID())
                {
                    index = i;
                }
            }
        return index;
    }

    
    //method to search for partID (int) or name (string)
    public static Part searchForPart(String search)
    {
        boolean isPartFound = false;
        
        //if search term is a number, search as partID and return Part object
        if (isThisANumber(search))
        {
            int partID = Integer.parseInt(search);
            for (int i=0; i<allParts.size(); i++)
            {
                if (partID == allParts.get(i).getPartID())
                {
                    isPartFound = true;
                    return allParts.get(i);
                }
            }
        }
        if (!isThisANumber(search))
        {
            for (int i=0; i<allParts.size(); i++)
            {
                if (search.equals(allParts.get(i).getName()))
                {
                    isPartFound = true;
                    return allParts.get(i);
                }
            }
        }
        if (isPartFound == false)
        {
            alertBox("Part not found.", "The part you searched for is not found. \nSearch by part ID or part name.");
            return null;
        }
        else
        {
            return null;
        }
    }//end of searchForPart
    
    
    
    //method to search for productID (int) or name (string)
    public static Product searchForProduct(String search)
    {
        boolean isProductFound = false;
        
        //if search term is a number, search as productID and return Product object
        if (isThisANumber(search))
        {
            int productID = Integer.parseInt(search);
            for (int i=0; i<products.size(); i++)
            {
                if (productID == products.get(i).getProductID())
                {
                    isProductFound = true;
                    return products.get(i);
                }
            }
        }
        if (!isThisANumber(search))
        {
            for (int i=0; i<products.size(); i++)
            {
                if (search.equals(products.get(i).getName()))
                {
                    isProductFound = true;
                    return products.get(i);
                }
            }
        }
        if (isProductFound == false)
        {
            alertBox("Product not found.", "The product you searched for is not found. \nSearch by product ID or product name.");
            return null;
        }
        else
        {
            return null;
        }
    }//end of searchForProduct
    
    
    
    //find out if entry is of type int
    public static boolean isThisANumber(String searchTerm)
    {
        try
        {
            Integer.parseInt(searchTerm);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    //find out if entry is of type double
    public static boolean isThisADouble(String searchTerm)
    {
        try
        {
            Double.parseDouble(searchTerm);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    //method to display alert box
    public static void alertBox(String alertReason, String alertDescription)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Alert");
        alert.setHeaderText(alertReason);
        alert.setContentText(alertDescription);
        alert.showAndWait();    
    }
    
    //method to cancel action
    public static void confirmAlert(String alertReason, String alertDescription)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Alert");
        alert.setHeaderText(alertReason);
        alert.setContentText(alertDescription);
        alert.showAndWait();    
    }
    
    //method to validate data when saving new part
    public static boolean validateNewPart(String name, double price, int inStock, int min, int max)
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
            alert.setHeaderText("Part entry is invalid.");
            alert.setContentText(errorMessage);
            alert.showAndWait(); 
        }
        return isValid;   
    }

}//end of class
