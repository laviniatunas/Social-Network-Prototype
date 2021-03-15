package socialnetwork.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.MessageService;
import socialnetwork.service.ServiceException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SendMessageController {


    @FXML
    private TextField textFieldTo;
    @FXML
    private TextField textFieldMessage;

    private MessageService service;
    Stage dialogStage;
    User user;

    @FXML
    private void initialize() { }

    public void setService(MessageService service, Stage stage, User u) {
        this.service = service;
        this.dialogStage=stage;
        this.user=u;
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
     * sends a message to an already existing conversation or to a new chat
     * @param actionEvent
     */
    public void handleSend(ActionEvent actionEvent) {
        String to = textFieldTo.getText();
        String message = textFieldMessage.getText();
        if(!(to.equals("") || message.equals(""))) {
            List<User> toList = new ArrayList<>();
            for (String s : to.split(" ")) {
                User u = new User("", "");
                u.setId(Long.parseLong(s));
                toList.add(u);
            }
            Message m = new Message(user, toList, message, LocalDateTime.now());
            m.setId(service.getMaxIdMessage()+1);
            try {
                if (service.addMessage(m) == null)
                    showMessage(Alert.AlertType.CONFIRMATION,"All ok","Message sent!");
            }catch( ServiceException | ValidationException e){
                showMessage(Alert.AlertType.ERROR,"Error",e.toString());
            }
        }
        else showMessage(Alert.AlertType.ERROR,"Error","Data is incorrect!");
        textFieldMessage.clear();
        textFieldTo.clear();
    }

}
