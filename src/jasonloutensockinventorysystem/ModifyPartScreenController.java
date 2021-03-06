/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jasonloutensockinventorysystem;

import static jasonloutensockinventorysystem.Inventory.getAllParts;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jason
 */

public class ModifyPartScreenController implements Initializable {

    @FXML RadioButton inHouse;
    @FXML RadioButton outsourced;
    @FXML ToggleGroup inHouseOrOutsourced;
    @FXML TextField partIDTextField;
    @FXML TextField nameTextField;
    @FXML TextField invTextField;
    @FXML TextField priceTextField;
    @FXML TextField maxTextField;
    @FXML TextField minTextField;
    @FXML Label machineIDLabel;
    @FXML TextField machineIDTextField;  
    @FXML Button cancelButton;
    @FXML Button modifyPartSaveButton;
    @FXML Label errorLabel;
    private static Part selectedPartToModify;
    
    

    
    //method to select inhouse or outsourced - will be used to route to inhouse or outsourced
    public void radioButtonChanged()
        {
            if (this.inHouseOrOutsourced.getSelectedToggle().equals(this.inHouse))
                {
                    machineIDLabel.setText("Machine ID");
                    machineIDTextField.setText("");
                    machineIDTextField.setPromptText("Machine ID");
                    
                }
            if (this.inHouseOrOutsourced.getSelectedToggle().equals(this.outsourced))
                {
                    machineIDLabel.setText("Company Name");
                    machineIDTextField.setText("");
                    machineIDTextField.setPromptText("Company Name");
                }
            
        }//end of radioButtonChanged()
    
    //method to get selected part from main screen (used by main screen to pass part when modify button is clicked)
    public static void getSelectedPart(Part selectedPart)
    {
        selectedPartToModify = selectedPart;
    }
    
    
    //method to load fields with part data
    public void populateFieldsWithSelectedPart()
    {
        //load simple part properties into text fields
        partIDTextField.setText(Integer.toString(selectedPartToModify.getPartID()));
        nameTextField.setText(selectedPartToModify.getName());
        invTextField.setText(Integer.toString(selectedPartToModify.getInStock()));
        priceTextField.setText(Double.toString(selectedPartToModify.getPrice()));
        maxTextField.setText(Integer.toString(selectedPartToModify.getMax()));
        minTextField.setText(Integer.toString(selectedPartToModify.getMin()));

        //get parts inventory to find machineID or companyName
        int index = Inventory.lookupPartIndex(selectedPartToModify.getPartID());
        if (selectedPartToModify instanceof Inhouse)
        {
            machineIDTextField.setText(Integer.toString(((Inhouse) getAllParts().get(index)).getMachineID()));
            inHouse.setSelected(true);
        }
        else
        {
            machineIDTextField.setText(((Outsourced) getAllParts().get(index)).getCompanyName());
            outsourced.setSelected(true);
        }
    }//end of populateFieldsWithSelectedPart()
    
    
    //method to modify parts
    @FXML
    private void modifyPartSaveButton()
    {
        if (!Inventory.isThisADouble(priceTextField.getText()) || !Inventory.isThisANumber(invTextField.getText()) || !Inventory.isThisANumber(minTextField.getText()) || !Inventory.isThisANumber(maxTextField.getText()))
            Inventory.alertBox("Part entry invalid.", "Numeric values required.");
        else
        {
        String name = nameTextField.getText();
        int partID = Integer.parseInt(partIDTextField.getText());
        double price = Double.parseDouble(priceTextField.getText());
        int inStock = Integer.parseInt(invTextField.getText());
        int min = Integer.parseInt(minTextField.getText());
        int max = Integer.parseInt(maxTextField.getText());
        if (price < 0)
            Inventory.alertBox("Part entry invalid.", "Price must be a positive value.");

        else
        {
            if (Inventory.validateNewPart(name, price, inStock, min, max))
            {    
                if (this.inHouseOrOutsourced.getSelectedToggle().equals(this.inHouse))
                    {
                        if (!Inventory.isThisANumber(machineIDTextField.getText()))
                            Inventory.alertBox("Part entry invalid.", "Machine ID must be an integer.");
                        else
                        {
                            //declare machineID for inhouse object
			    int partIndex = Inventory.lookupPartIndex(Integer.parseInt(partIDTextField.getText()));
                            int machineID = Integer.parseInt(machineIDTextField.getText());
                            //create INHOUSE object
                            Inhouse newEntry = new Inhouse();
                            newEntry.setMachineID(machineID);
                            newEntry.setPartID(partID);
                            newEntry.setName(name);
                            newEntry.setPrice(price);
                            newEntry.setInStock(inStock);
                            newEntry.setMin(min);
                            newEntry.setMax(max);

                            //pass inhouse object to inventory
                            Inventory addPartToInventory = new Inventory();
                	    addPartToInventory.updatePart(partIndex, newEntry);
                            Inventory.alertBox("Success!", "Part added to inventory.");
                            Stage stage = (Stage) cancelButton.getScene().getWindow();
                            stage.close();
                        }
                    }
                if (this.inHouseOrOutsourced.getSelectedToggle().equals(this.outsourced))
                    {
                        //declare companyName (as machineID) for outsourced object
			int partIndex = Inventory.lookupPartIndex(Integer.parseInt(partIDTextField.getText()));
                        String machineID = machineIDTextField.getText();
                        //create OUTSOURCED object
                        Outsourced newEntry = new Outsourced();
                        newEntry.setCompanyName(machineID);
                        newEntry.setPartID(partID);
                        newEntry.setName(name);
                        newEntry.setPrice(price);
                        newEntry.setInStock(inStock);
                        newEntry.setMin(min);
                        newEntry.setMax(max);
                        //pass outsourced object to inventory
                        Inventory addPartToInventory = new Inventory();
               	    	addPartToInventory.updatePart(partIndex, newEntry);
                        Inventory.alertBox("Success!", "Part added to inventory.");
                        Stage stage = (Stage) cancelButton.getScene().getWindow();
                        stage.close();
                    }
                }
            }
        }
    }//end of modifyPartSaveButton
  
    
    
    //method to close window
    @FXML
    private void cancelButtonActionEvent()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Modifications will not be saved.");
        alert.setContentText("Click \"OK\" to cancel.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        //this controls the toggling of the radio buttons ("ToggleGroup")
        inHouseOrOutsourced = new ToggleGroup();
        this.inHouse.setToggleGroup(inHouseOrOutsourced);
        this.outsourced.setToggleGroup(inHouseOrOutsourced);

        //populates part attributes in text fields
        populateFieldsWithSelectedPart();
    }    
    

    
}//end of initializable
