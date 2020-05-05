/*
 * Main screen controller
 */
package jasonloutensockinventorysystem;

import static jasonloutensockinventorysystem.Inventory.alertBox;
import static jasonloutensockinventorysystem.Inventory.getAllParts;
import static jasonloutensockinventorysystem.Inventory.getProducts;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Jason
 */
public class MainScreenController implements Initializable {
    
    //declare Parts section objects on main screen
    @FXML TextField searchPartTextField;
    @FXML TextField searchProductTextField;
    @FXML Button searchPartButton;
    @FXML Button addPartButton;
    @FXML Button modifyPartButton;
    @FXML Button deletePartButton;
    //declare Products section objects on main screen
    @FXML Button searchProductButton;
    @FXML Button addProductButton;
    @FXML Button modifyProductButton;
    @FXML Button deleteProductButton;
    //declare exit button at bottom right of main screen
    @FXML Button exitMainButton;
    //file location variables for opening new stage
    @FXML String fileLocationFXML = "AddPartScreen.fxml";
    //declare cancel button
    @FXML javafx.scene.control.Button cancelButton;
    //configure Add Part table view
    @FXML TableView<Part> addPartTableView;
    @FXML TableColumn<Part, Integer> addPartPartIDColumn;  
    @FXML TableColumn<Part, String> addPartNameColumn;   
    @FXML TableColumn<Part, Integer> addPartInStockColumn;   
    @FXML TableColumn<Part, Double> addPartPriceColumn;   
    //configure products table view
    @FXML TableView<Product> productTableView;
    @FXML TableColumn<Product, Integer> productIDColumn; 
    @FXML TableColumn<Product, String> productNameColumn;   
    @FXML TableColumn<Product, Integer> productInStockColumn;   
    @FXML TableColumn<Product, Double> productPriceColumn; 

    
    //method to close window
    @FXML
    private void cancelButtonActionEvent(){
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
    }

    //general method for opening a new scene based on button pressed
    @FXML
    private void newSceneButton(ActionEvent event) throws IOException {   
    try 
    {
        //get correct FXML document based on button pushed
        if (event.getSource() == addPartButton)
            fileLocationFXML = "AddPartScreen.fxml";
        if (event.getSource() == modifyPartButton)
        {
            fileLocationFXML = "ModifyPartScreen.fxml";
            modifyPart();
        }
        if (event.getSource() == addProductButton)
            fileLocationFXML = "AddProductScreen.fxml";
        if (event.getSource() == modifyProductButton)
        {
            fileLocationFXML = "ModifyProductScreen.fxml";
            modifyProduct();
        }
            
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fileLocationFXML));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));  
        stage.show();
    } 
    catch(Exception e) 
    {
        if(event.getSource() == modifyPartButton)
        {
            Inventory.alertBox("Invalid selection.","A part must be selected to modify it.");
        }
        if(event.getSource() == modifyProductButton)
        {
            Inventory.alertBox("Invalid selection.","A product must be selected to modify it.");
        }
    }
}
    
    //method to delete part from inventory
    @FXML
    public void deletePartButton()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Are you sure you want to remove this part from the inventory?");
        alert.setContentText("Click \"OK\" to delete.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            Inventory invObj = new Inventory();
            Part selectedPart = addPartTableView.getSelectionModel().getSelectedItem();
            if (!invObj.deletePart(selectedPart))
                alertBox("Deletion Failure", "The selected part was not removed.");
        }
    }

    //method to delete product from inventory
    @FXML
    public void deleteProductButton()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Are you sure you want to remove this product from the inventory?");
        alert.setContentText("Click \"OK\" to delete.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            Inventory invObj = new Inventory();
            int selectedProductID = productTableView.getSelectionModel().getSelectedItem().getProductID();
            if (!invObj.removeProduct(selectedProductID))
                alertBox("Deletion Failure", "The selected product was not removed.");
        }

    }
    
    //method to search parts inventory and filter the table
    @FXML
    public void searchPartsField()
    {
        ObservableList<Part> partFoundAsOL = FXCollections.observableArrayList();
        //if the search field is empty, reload all parts to the table
        if (searchPartTextField.getText().isEmpty())
        {
            getPartTableData();
        }
        //if part is found, it is returned and loaded to table
        else
        {
            if (Inventory.searchForPart(searchPartTextField.getText()) != null)
            {
                partFoundAsOL.add(Inventory.searchForPart(searchPartTextField.getText()));
                addPartTableView.setItems(partFoundAsOL);
            }
        }
    }//end of searchPartsField

    
    //method to search products inventory and filter the table
    @FXML
    public void searchProductsField()
    {
        ObservableList<Product> productFoundAsOL = FXCollections.observableArrayList();
        //if the search field is empty, reload all products to the table
        if (searchProductTextField.getText().isEmpty())
        {
            getProductTableData();
        }
        //if products is found, it is returned and loaded to table
        else
        {
            if (Inventory.searchForProduct(searchProductTextField.getText()) != null)
            {
                productFoundAsOL.add(Inventory.searchForProduct(searchProductTextField.getText()));
                productTableView.setItems(productFoundAsOL);
            }
        }
    }//end of searchProductsField
    
    
    
    @FXML //method to pass selected part object to modify part screen
    public void modifyPart()
    {
        Part selectedPart = addPartTableView.getSelectionModel().getSelectedItem();
        ModifyPartScreenController.getSelectedPart(selectedPart);
    }
    
    @FXML //method to pass selected product object to modify product screen
    public void modifyProduct()
    {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        ModifyProductScreenController.getSelectedProduct(selectedProduct);
    }

    
    //method to pre-load some parts and products to inventory (called in initialize method) -- Dummy Data
    public void initializeInventory()
    {
        Inventory addPartToInventory = new Inventory();
        //create first INHOUSE object
        Inhouse newEntry = new Inhouse();
        newEntry.setMachineID(312);
        newEntry.setPartID(Inventory.getUniquePartID());
        newEntry.setName("Nightvision Goggles");
        newEntry.setPrice(999.99);
        newEntry.setInStock(15);
        newEntry.setMin(10);
        newEntry.setMax(20);
        addPartToInventory.addPart(newEntry);
        //create second INHOUSE object
        Inhouse newEntry2 = new Inhouse();
        newEntry2.setMachineID(418);
        newEntry2.setPartID(Inventory.getUniquePartID());
        newEntry2.setName("Jeep Tires");
        newEntry2.setPrice(7.00);
        newEntry2.setInStock(3);
        newEntry2.setMin(0);
        newEntry2.setMax(1000);
        addPartToInventory.addPart(newEntry2);
        //create first OUTSOURCED object
        Outsourced newEntryO = new Outsourced();
        newEntryO.setCompanyName("Jurassic Park, LLC");
        newEntryO.setPartID(Inventory.getUniquePartID());
        newEntryO.setName("Outhouse Roof");
        newEntryO.setPrice(300.00);
        newEntryO.setInStock(0);
        newEntryO.setMin(0);
        newEntryO.setMax(20);
        addPartToInventory.addPart(newEntryO);
        //create second OUTSOURCED object
        Outsourced newEntryO2 = new Outsourced();
        newEntryO2.setCompanyName("Jurassic World, LLC");
        newEntryO2.setPartID(Inventory.getUniquePartID());
        newEntryO2.setName("Flare");
        newEntryO2.setPrice(10.00);
        newEntryO2.setInStock(1);
        newEntryO2.setMin(1);
        newEntryO2.setMax(20);
        addPartToInventory.addPart(newEntryO2);
    }//end of initializeInventory();

    //method to get part inventory
    public void getPartTableData()
    { 
        addPartTableView.setItems(getAllParts());
    }
    
    //method to get product inventory
    public void getProductTableData()
    { 
        productTableView.setItems(getProducts());
    }   
    
    @FXML public void confirmExit()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("You are about to exit the Inventory Management System.");
        alert.setContentText("Click \"OK\" to exit.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            cancelButtonActionEvent();
    }
 
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        //grab parts to load
        addPartPartIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDGetProperty().asObject());
        addPartNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameGetProperty());
        addPartInStockColumn.setCellValueFactory(cellData -> cellData.getValue().partInStockGetProperty().asObject());
        addPartPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceGetProperty().asObject());
        //grab products to load
        productIDColumn.setCellValueFactory(cellData -> cellData.getValue().productIDGetProperty().asObject());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameGetProperty());
        productInStockColumn.setCellValueFactory(cellData -> cellData.getValue().productInStockGetProperty().asObject());
        productPriceColumn.setCellValueFactory(cellData -> cellData.getValue().productPriceGetProperty().asObject());
        //pre-load some parts to work with (sends parts to inventory)
        initializeInventory();
        //load part data from inventory
        getPartTableData();
        //load product data from inventory
        getProductTableData();
    }//end of initialize    
    

    
}
