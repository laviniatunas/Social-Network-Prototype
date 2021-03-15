package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.MessageDTO;
import socialnetwork.domain.User;
import socialnetwork.service.MessageService;
import socialnetwork.utils.events.MessageEvents;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;

public class GroupChatController implements Observer<MessageEvents> {

    MessageService srv;
    Stage stage;
    User user;
    ObservableList<MessageDTO> model = FXCollections.observableArrayList();

    @FXML
    TableView<MessageDTO> tableView;
    @FXML
    TableColumn<MessageDTO,String> tableColumnTo;

    public void setService(MessageService serviceFriends, Stage stage, User u) {
        this.stage = stage;
        this.user = u;
        srv = serviceFriends;
        srv.addObserver(this);
        initModel();
    }

    /**
     * puts data into table
     */
    private void initModel() {
        model.setAll(srv.getAllGroups(user));
    }

    /**
     * initializes the columns
     */
    @FXML
    public void initialize() {
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<MessageDTO, String>("to"));
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
     * opens a window for sending a new message
     * @param actionEvent
     */
    public void SendNewMessages(ActionEvent actionEvent) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/sendmessages.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Send requests");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            SendMessageController messageController = loader.getController();
            messageController.setService(srv, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * opens a new window with all the messages of a chat
     * @param mouseEvent
     */
    public void OpenChat(MouseEvent mouseEvent) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/conversation.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            MessageDTO dto = tableView.getSelectionModel().getSelectedItem();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            if(dto.getTo().length()>2)
                dialogStage.setTitle("User " + user.getId().toString() + " in conversation with Users " + dto.getTo() );
            else dialogStage.setTitle("User " + user.getId().toString() + " in conversation with User " + dto.getTo() );
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            ConversationController convController = loader.getController();
            convController.setService(srv, dialogStage, user, dto);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
