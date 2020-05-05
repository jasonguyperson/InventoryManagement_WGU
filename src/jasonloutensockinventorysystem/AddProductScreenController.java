/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jasonloutensockinventorysystem;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jason
 */
public class AddProductScreenController implements Initializable {

    //add part table view
    @FXML TableView<Part> addPartProductTableView;
    @FXML TableColumn<Part, Integer> addPartIDColumn;  
    @FXML TableColumn<Part, String> addPartNameColumn;   
    @FXML TableColumn<Part, Integer> addPartInStockColumn;   
    @FXML TableColumn<Part, Double> addPartPriceColumn; 
    //remove part table view
    @FXML TableView<Part> removePartProductTableView;
    @FXML TableColumn<Part, Integer> removePartIDColumn;  
    @FXML TableColumn<Part, String> removePartNameColumn;   
    @FXML TableColumn<Part, Integer> removePartInStockColumn;   
    @FXML TableColumn<Part, Double> removePartPriceColumn; 
    //product entry fields
    @FXML TextField addProductIDTextField;
    @FXML TextField addProductNameTextField;
    @FXML TextField addProductInStockTextField;
    @FXML TextField addProductPriceTextField;
    @FXML TextField addProductMaxTextField;
    @FXML TextField addProductMinTextField;
    //search field
    @FXML TextField searchPartsTextField;
    //buttons
    @FXML Button cancelButton;
    @FXML Button searchPartsButton;
    @FXML Button addPartSaveButton;
    @FXML Button partDeleteButton;
    @FXML Button saveProductButton;
    //OL to track current parts in product
    private ObservableList<Part> addedParts = FXCollections.observableArrayList();
    
    
    
    
    //- methods:
    @FXML
    public void saveNewProduct()
    {
        if (!Inventory.isThisADouble(addProductPriceTextField.getText()) || !Inventory.isThisANumber(addProductInStockTextField.getText()) || !Inventory.isThisANumber(addProductMinTextField.getText()) || !Inventory.isThisANumber(addProductMaxTextField.getText()))
            Inventory.alertBox("Part entry invalid.", "Numeric values required.");
        else
        {
            if (addProductNameTextField.getText().isEmpty() || 
                addProductPriceTextField.getText().isEmpty() || 
                addProductInStockTextField.getText().isEmpty() ||
                addProductMinTextField.getText().isEmpty() ||
                addProductMaxTextField.getText().isEmpty())
            {
                Inventory.alertBox("Product entry is invalid.", "All fields must be complete.");
            }
            else
            {
                //get text field data
                int productID = Inventory.getUniqueProductID();
                String name = addProductNameTextField.getText();
                double price = Double.parseDouble(addProductPriceTextField.getText());
                int inStock = Integer.parseInt(addProductInStockTextField.getText());
                int min = Integer.parseInt(addProductMinTextField.getText());
                int max = Integer.parseInt(addProductMaxTextField.getText());
                if (price < 0)
                    Inventory.alertBox("Product entry invalid.", "Price must be a positive value.");
                else
                {
                    if (Product.validateNewProduct(name, price, inStock, min, max))
                    {
                        if (addedParts.isEmpty())
                        {
                            Inventory.alertBox("No product selected.", "Please select a product to add.");
                        }
                        else
                        {
                            //pass product fields to products
                            Product newProduct = new Product();
                            newProduct.setProductID(productID);
                            newProduct.setName(name);
                            newProduct.setPrice(price);
                            newProduct.setInStock(inStock);
                            newProduct.setMax(max);
                            newProduct.setMin(min);     
                            //add to inventory
                            Inventory invObj = new Inventory();
                            invObj.addProduct(newProduct);

                            //get addedParts individually and pass as type part to associatedParts in products
                            for (int i =0; i<addedParts.size(); i++)
                            {
                                newProduct.addAssociatedParts(addedParts.get(i));
                            }
                            Inventory.alertBox("Success!", "Product added to inventory.");
                            //close window
                            cancelButtonActionEvent();
                        }
                    }
                }
            }
        }
    }//end of saveNewProduct
    

    //method to add existing inventory parts to new product OL
    
    public void addPartToNewProductOL()
    {
        Part selectedPart = addPartProductTableView.getSelectionModel().getSelectedItem();
        if (selectedPart != null)
            addedParts.add(selectedPart);
    }
    
    //method to remove part from new product OL
    @FXML
    public void removePartFromProductOL()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Part Removal");
        alert.setHeaderText("Are you sure you want to remove part?");
        alert.setContentText("Click \"OK\" to cancel.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            Part selectedPart = removePartProductTableView.getSelectionModel().getSelectedItem();
            if (selectedPart != null)
                addedParts.remove(selectedPart);
        }
    }    
    
    @FXML
    public void searchAddPartsTableField()
    {
        ObservableList<Part> partFoundAsOL = FXCollections.observableArrayList();
        //if the search field is empty, reload all parts to the table
        if (searchPartsTextField.getText().isEmpty())
        {
            getAddPartTableData();
        }
        //if part is found, it is returned and loaded to table
        else
        {
            if (Inventory.searchForPart(searchPartsTextField.getText()) != null)
            {
                partFoundAsOL.add(Inventory.searchForPart(searchPartsTextField.getText()));
                addPartProductTableView.setItems(partFoundAsOL);
            }
        }
    }//end of searchPartsField
    

    //close window
    @FXML
    public void cancelButtonActionEvent()
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void cancelProduct()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Product will not be saved.");
        alert.setContentText("Click \"OK\" to cancel.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            cancelButtonActionEvent();
    }
    
    //get observable list for add part table
    public void getAddPartTableData() 
    {
        addPartProductTableView.setItems(Inventory.getAllParts());
    }
    
    //get observable list for remove part table
    public void getRemovePartTableData() 
    {
        removePartProductTableView.setItems(addedParts);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        //add parts table
        addPartIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDGetProperty().asObject());
        addPartNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameGetProperty());
        addPartInStockColumn.setCellValueFactory(cellData -> cellData.getValue().partInStockGetProperty().asObject());
        addPartPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceGetProperty().asObject());
        //remove parts table
        removePartIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDGetProperty().asObject());
        removePartNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameGetProperty());
        removePartInStockColumn.setCellValueFactory(cellData -> cellData.getValue().partInStockGetProperty().asObject());
        removePartPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceGetProperty().asObject());
        //load part data from inventory
        getAddPartTableData();
        //load parts that have been added to new product
        getRemovePartTableData();

    }//end of initialize    
    
    
}//end of initializable
