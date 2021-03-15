package socialnetwork.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socialnetwork.domain.FriendshipRequest;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.ServiceException;

public class SendRequestController {

    public TextField textFieldMessage;
    @FXML
    private TextField textFieldTo;

    private FriendshipService service;
    Stage dialogStage;
    private User user;

    @FXML
    private void initialize() { }

    public void setService(FriendshipService service,  Stage stage, User user) {
        this.service = service;
        this.dialogStage = stage;
        this.user = user;
    }

    /**
     * creates a message of a certain type with a certain title and text and shows it
     * @param type
     * @param title
     * @param text
     */
    public void showMessage(Alert.AlertType type, String title, String text){
        Alert a = new Alert(type);
        a.initOwner(null);
        a.setTitle(title);
        a.setContentText(text);
        double height = a.getHeight();
        a.setHeight(height*2);
        a.setWidth(height*1.2);
        a.showAndWait();
    }

    /**
     * adds a friendship request
     * @param actionEvent
     */
    public void handleSend(ActionEvent actionEvent) {
        String to=textFieldTo.getText();
        try{
            Long idto = Long.parseLong(to);

            FriendshipRequest request = new FriendshipRequest();
            Tuple<Long,Long> f = new Tuple<>(user.getId(), idto);
            request.setId(f);

            service.addRequest(request);
            showMessage(Alert.AlertType.CONFIRMATION,"Request sent!","Request is now pending!");

            textFieldTo.clear();


        }catch(NumberFormatException exception){
            showMessage(Alert.AlertType.ERROR,"Error",exception.toString());
        }catch (ServiceException exception){
            showMessage(Alert.AlertType.ERROR,"Error",exception.toString());
        }
    }
}
