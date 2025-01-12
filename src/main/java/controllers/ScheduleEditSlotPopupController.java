package controllers;

import classes.Price;
import classes.Slot;
import dataBaseSQL.PriceSQL;
import dataBaseSQL.SlotPricingSQL;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class ScheduleEditSlotPopupController extends CinemaManagementController {
    private ScheduleTabController scheduleTabController;

    @FXML
    private VBox vbox;

    @FXML
    private Label seatOccupiedSeatAvailable;

    @FXML
    private Label labelForRoomAndTime;

    private Stage stage;

    private Slot slotUsed;

    private List<Price> pricing;

    private int newSeatOccupied;

    public void setScheduleEditSlotPopup(Slot slot, String minHour, String maxHour, Parent root, ScheduleTabController scheduleTabController) {
        this.scheduleTabController = scheduleTabController;
        slotUsed = slot;
        labelForRoomAndTime.setText("Créneau pour le film '" + slot.getMovie().getName() + "' de " + minHour + maxHour + " dans la salle '" +
                slot.getRoom().getName() + "'");
        labelForRoomAndTime.setWrapText(true);

        pricing = PriceSQL.GetPricing();
        HashMap<Integer, Integer> slotPricing = SlotPricingSQL.GetSlotPricing(slot.getId());
        int seatOccupied = 0;
        for (Price price: pricing) {
            Label label = new Label(price.getName() + " - Prix: " + price.getCost() + "€");
            HBox hBox = new HBox();
            hBox.setId("hboxPricingId"+price.getId());
            Spinner<Integer> spinner = new Spinner<>();
            int initialValue = 0;
            if (!slotPricing.isEmpty()){
                initialValue = slotPricing.get(price.getId());
            }
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,  slot.getRoom().getCapacity(), initialValue);

            spinner.setValueFactory(valueFactory);
            spinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
            spinner.setPrefWidth(75);

            spinner.setOnMouseClicked(mouseEvent -> {
                newSeatOccupied = 0;
                for (Price price2: pricing) {
                    Spinner<Integer> spinner2 = null;
                    for (Node node: vbox.getChildren()) {
                        if (node.getId() != null && node.getId().equals("hboxPricingId"+price2.getId())){
                            for(Node nodeIn:((HBox)node).getChildren()){
                                if(nodeIn instanceof Spinner<?>){
                                    spinner2 = (Spinner<Integer>) nodeIn;
                                    break;
                                }
                            }
                        }
                    }
                    if (spinner2 == null){
                        showDefaultErrorAlert("",null);
                        return;
                    } else {
                        newSeatOccupied += spinner2.getValue();
                    }
                }
                if (newSeatOccupied > slot.getRoom().getCapacity()){
                    seatOccupiedSeatAvailable.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
                    seatOccupiedSeatAvailable.setText(newSeatOccupied + "/" + slot.getRoom().getCapacity() + " Dépassement du nombre de place disponible");
                } else {
                    seatOccupiedSeatAvailable.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
                    seatOccupiedSeatAvailable.setText(newSeatOccupied + "/" + slot.getRoom().getCapacity());
                }
            });

            hBox.getChildren().addAll(spinner, new Label(" "), new Label("places occupées"));
            hBox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(label, hBox, new Separator());
            seatOccupied += initialValue;
            newSeatOccupied = seatOccupied;
        }
        seatOccupiedSeatAvailable.setText(seatOccupied + "/" + slot.getRoom().getCapacity());
        seatOccupiedSeatAvailable.setStyle("-fx-font-weight: bold");
        double[] screenSize = CinemaManagementController.setScreenSize(0.3, 0.3);
        stage = new Stage();
        stage.setScene(new Scene(root, screenSize[1], screenSize[0]));
        stage.setTitle("Modifier le créneau");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void saveSlotSchedule() {
        SQLException exception = null;
        if (newSeatOccupied > slotUsed.getRoom().getCapacity()){
            showAlert("Erreur","Le nombre de places occupées est supérieur au nombre de place disponible", Alert.AlertType.ERROR);
            return;
        }
        for (Price price: pricing) {
            Spinner<Integer> spinner = null;
            for (Node node: vbox.getChildren()) {
                if (node.getId() != null && node.getId().equals("hboxPricingId"+price.getId())){
                    for(Node nodeIn:((HBox)node).getChildren()){
                        if(nodeIn instanceof Spinner<?>){
                            spinner = (Spinner<Integer>) nodeIn;
                            break;
                        }
                    }
                }
            }
            if (spinner == null){
                showDefaultErrorAlert("",null);
                break;
            } else {
                if (SlotPricingSQL.GetSlotPricing(slotUsed.getId(), price.getId())){
                    exception = SlotPricingSQL.UpdateSlotPricing(slotUsed.getId(), price.getId(), spinner.getValue());
                } else {
                    exception = SlotPricingSQL.AddSlotPricing(slotUsed.getId(), price.getId(), spinner.getValue());
                }
                if (exception != null){
                    break;
                }
            }
        }
        if (exception != null) {
            showDefaultErrorAlert(" lors de la mise a jour du créneau", exception);
        } else {
            showAlert("Créneau modifié", "Le créneau a été modifié avec succès!",
                    Alert.AlertType.INFORMATION);
            stage.close();
            scheduleTabController.scheduleGridPane.setGridLinesVisible(false);
            scheduleTabController.scheduleGridPane.getChildren().clear();
            scheduleTabController.scheduleGridPane.setGridLinesVisible(true);
            scheduleTabController.refreshSchedule();
        }
    }
}
