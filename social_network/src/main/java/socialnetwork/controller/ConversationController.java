package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.Message;
import socialnetwork.domain.MessageDTO;
import socialnetwork.domain.User;
import socialnetwork.service.MessageService;
import socialnetwork.service.ServiceException;
import socialnetwork.utils.events.MessageEvents;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConversationController implements Observer<MessageEvents> {

    MessageService srv;
    Stage stage;
    User user;
    MessageDTO dto;
    ObservableList<MessageDTO> model = FXCollections.observableArrayList();

    @FXML
    TableView<MessageDTO> tableView;
    @FXML
    TableColumn<MessageDTO,String> tableColumnTo;
    @FXML
    TextField textFieldMessage;

    public void setService(MessageService serviceFriends, Stage stage, User u, MessageDTO dto) {
        this.stage = stage;
        this.user = u;
        this.dto = dto;
        srv = serviceFriends;
        srv.addObserver(this);
        initModel();
    }

    /**
     * puts data into table
     */
    private void initModel() {
        String ids = dto.getTo();
        ids+=user.getId().toString();
        model.setAll(srv.getConversationDTO(ids));
    }

    /**
     * initializes the columns
     */
    @FXML
    public void initialize() {
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<MessageDTO, String>("to"));
/*
        tableColumnTo.setCellFactory(column -> {
            return new TableCell<MessageDTO, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty); //This is mandatory

                    if (item == null || empty) { //If the cell is empty
                        setText(null);
                        setStyle("");
                    } else { //If the cell is not empty

                        setText(item); //Put the String data in the cell

                        //We get here all the info of the Person of this row
                        MessageDTO auxPerson = getTableView().getItems().get(getIndex());

                        // Style all persons wich name is "Edgard"
                        if (auxPerson.getTo().contains("From: "+user.getId().toString())) {
                            setTextFill(Color.BLACK); //The text in red
                            setStyle("-fx-background-color: #ABF7FB"); //The background of the cell in yellow
                        } else {
                            //Here I see if the row of this cell is selected or not

                                setTextFill(Color.BLACK);
                        }
                    }
                }
            };
        });*/


        tableView.setItems(model);
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
     * updates table if any event occurs
     * @param messageEvents
     */
    @Override
    public void update(MessageEvents messageEvents) {
        initModel();
    }

    /**
     * adds a new message or reply to the conversation
     * @param actionEvent
     */
    public void handleSendMessage(ActionEvent actionEvent) {
        MessageDTO messageDTO = tableView.getSelectionModel().getSelectedItem();
        String message = textFieldMessage.getText();
        if(message.equals(""))
            showMessage(Alert.AlertType.ERROR,"Error","Message can't be null");
        else{
            List<User> toList = new ArrayList<>();
            String ids = dto.getTo();
            for (String id: ids.split(" ")) {
                User current = new User("","");
                current.setId(Long.parseLong(id));
                toList.add(current);
            }
            try {
                Message m = new Message(user, toList, message, LocalDateTime.now());
                m.setId(srv.getMaxIdMessage()+1);
                if (messageDTO == null) {
                    srv.addMessage(m);
                    showMessage(Alert.AlertType.CONFIRMATION, "All ok", "Message sent!");
                } else {
                    srv.addReply(m, srv.findMessage(messageDTO.getId()));
                    showMessage(Alert.AlertType.CONFIRMATION, "All ok", "Message sent!");
                }
            }
            catch(ServiceException exception) {
                showMessage(Alert.AlertType.ERROR, "", exception.toString());
            }
        }
        textFieldMessage.clear();
    }
}
