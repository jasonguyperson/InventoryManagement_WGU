package jasonloutensockinventorysystem;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class ModifyProductScreenController implements Initializable {

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
    @FXML TextField modifyProductIDTextField;
    @FXML TextField modifyProductNameTextField;
    @FXML TextField modifyProductInStockTextField;
    @FXML TextField modifyProductPriceTextField;
    @FXML TextField modifyProductMaxTextField;
    @FXML TextField modifyProductMinTextField;
    //search field
    @FXML TextField searchPartsTextField;
    //buttons
    @FXML Button cancelButton;
    @FXML Button searchPartsButton;
    @FXML Button addPartSaveButton;
    @FXML Button partDeleteButton;
    @FXML Button saveProductButton;
    //OL to track current parts in product
    private final ObservableList<Part> addedParts = FXCollections.observableArrayList();
    private ObservableList<Part> oldAssociatedParts = FXCollections.observableArrayList();
    private static Product selectedProductToModify;
    
    
    
    //- methods:
    //method to get selected product from main screen (used by main screen to pass product when modify button is clicked)
    public static void getSelectedProduct(Product selectedProduct)
    {
        selectedProductToModify = selectedProduct;
    }
    
    //method to ADD part to lower table (added parts)
    @FXML
    public void addPartToModifyProductOL()
    {
        Part selectedPart = addPartProductTableView.getSelectionModel().getSelectedItem();
        addedParts.add(selectedPart);
    }
    
    //method to REMOVE part from lower table (added parts)
    @FXML
    public void removePartModifyProductOL()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Part Removal");
        alert.setHeaderText("Are you sure you want to remove part?");
        alert.setContentText("Click \"OK\" to cancel.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            Part selectedPart = removePartProductTableView.getSelectionModel().getSelectedItem();
                addedParts.remove(selectedPart);
        }
    }    
    
    @FXML
    public void modifyProduct()
    {
        populateFieldsWithSelectedProduct();
    }
    
    @FXML //method to save modified part
    public void modifyProductSaveButton()
    {
        if (!Inventory.isThisADouble(modifyProductPriceTextField.getText()) || !Inventory.isThisANumber(modifyProductInStockTextField.getText()) || !Inventory.isThisANumber(modifyProductMinTextField.getText()) || !Inventory.isThisANumber(modifyProductMaxTextField.getText()))
            Inventory.alertBox("Part entry invalid.", "Numeric values required.");
        else
        {
            if (modifyProductNameTextField.getText().isEmpty() || 
                modifyProductPriceTextField.getText().isEmpty() || 
                modifyProductInStockTextField.getText().isEmpty() ||
                modifyProductMinTextField.getText().isEmpty() ||
                modifyProductMaxTextField.getText().isEmpty())
            {
                Inventory.alertBox("Product entry is invalid.", "All fields must be complete.");
            }
            else
            {
                //get text field data
                int productID = Integer.parseInt(modifyProductIDTextField.getText());
                String name = modifyProductNameTextField.getText();
                double price = Double.parseDouble(modifyProductPriceTextField.getText());
                int inStock = Integer.parseInt(modifyProductInStockTextField.getText());
                int min = Integer.parseInt(modifyProductMinTextField.getText());
                int max = Integer.parseInt(modifyProductMaxTextField.getText());
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
                            //modify existing product
                            Inventory.updateProduct(selectedProductToModify.getProductID(), newProduct);
                            //get addedParts individually and pass as type part to associatedParts in products
                            for (int i =0; i<addedParts.size(); i++)
                                {
                                    newProduct.addAssociatedParts(addedParts.get(i));
                                }
                            Inventory.alertBox("Success!", "Product modification successful.");
                            //close window
                            cancelButtonActionEvent();
                        }
                    }
                }
            }
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
    
    //method to load fields with part data
    public void populateFieldsWithSelectedProduct()
    {
        //load simple part properties into text fields
        modifyProductIDTextField.setText(Integer.toString(selectedProductToModify.getProductID()));
        modifyProductNameTextField.setText(selectedProductToModify.getName());
        modifyProductInStockTextField.setText(Integer.toString(selectedProductToModify.getInStock()));
        modifyProductPriceTextField.setText(Double.toString(selectedProductToModify.getPrice()));
        modifyProductMaxTextField.setText(Integer.toString(selectedProductToModify.getMax()));
        modifyProductMinTextField.setText(Integer.toString(selectedProductToModify.getMin()));
    }//end of populateFieldsWithSelectedPart()
    
    
    public void getOldAssociatedParts()
    {
        //load old associated parts
        oldAssociatedParts = selectedProductToModify.getAssociatedParts();
        //add each part to addedParts (which is displayed and saved)
        for (int i = 0; i<oldAssociatedParts.size(); i++)
        {
            addedParts.add(oldAssociatedParts.get(i));
        }
    }
    
    //method to close window
    @FXML
    private void cancelButtonActionEvent()
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void cancelModifyProduct()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Modifications will not be saved.");
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
    
    //get observable list for current parts table
    public void getRemovePartTableData() 
    {
       getOldAssociatedParts();
       removePartProductTableView.setItems(addedParts);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        //load product info fields
        populateFieldsWithSelectedProduct();
        //load part data from inventory
        getAddPartTableData();
        //load selected product from main screen
        getRemovePartTableData();
    }    
    
}//end of ModifyProductScreenController
