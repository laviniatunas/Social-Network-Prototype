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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.User;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MessageService;
import socialnetwork.service.Service;
import socialnetwork.utils.events.UserEvents;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserEvents> {

    Service srv;
    FriendshipService friendshipService;
    MessageService messageService;
    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User,Long> tableColumnId;
    @FXML
    TableColumn<User,String> tableColumnFName;
    @FXML
    TableColumn<User,String> tableColumnLName;

    public void setUserService(Service service, FriendshipService friendshipService, MessageService messageService) {
        srv = service;
        srv.addObserver(this);
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        initModel();
       /* tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );*/
    }

    /**
     * initializes the columns
     */
    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        tableColumnFName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableView.setItems(model);
    }

    /**
     * puts data into table
     */
    private void initModel() {
        Iterable<User> users = srv.getAll();
        List<User> usersList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(usersList);
    }

    /**
     * updates table if any event occurs
     * @param userEvents
     */
    @Override
    public void update(UserEvents userEvents) {
        initModel();
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
     * shows friends of selected user
     * @param actionEvent
     */
    public void ShowFriends(ActionEvent actionEvent) {
        User user = tableView.getSelectionModel().getSelectedItem();
        if (user == null)
            showMessage(Alert.AlertType.ERROR,"Error","Nothing is selected!");
        else {
            showFriendshipsUser(user);
        }
    }

    /**
     * loads a new window that will show the friends of the given user
     * @param user
     */
    private void showFriendshipsUser(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/friendships.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Friends of User "+ user.getId().toString());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            FriendshipController friendshipController = loader.getController();
            friendshipController.setFriendsService(friendshipService, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * shows pending friendship requests
     * @param actionEvent
     */
    public void AnswerRequests(ActionEvent actionEvent) {
        User user = tableView.getSelectionModel().getSelectedItem();
        if (user == null)
            showMessage(Alert.AlertType.ERROR,"Error","Nothing is selected!");
        else {
            showRequestsUser(user);
        }
    }

    /**
     * loads a new window that will show the pending friendship requests of the given user
     * @param user
     */
    private void showRequestsUser(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/answerrequests.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Pending requests of User "+ user.getId().toString());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            AnswerRequestController requestController = loader.getController();
            requestController.setService(friendshipService, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * shows all pending request sent by the selected user
     * @param actionEvent
     */
    public void CancelRequest(ActionEvent actionEvent) {
        User user = tableView.getSelectionModel().getSelectedItem();
        if (user == null)
            showMessage(Alert.AlertType.ERROR,"Error","Nothing is selected!");
        else {
            showSentRequestsUser(user);
        }
    }

    /**
     * opens a window that shows all pending request sent by the selected user
     * @param user
     */
    private void showSentRequestsUser(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/sentrequests.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Sent requests of User "+ user.getId().toString());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            SentRequestController requestController = loader.getController();
            requestController.setService(friendshipService, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * will show chats of a user
     * @param actionEvent
     */
    public void SeeChats(ActionEvent actionEvent) {
        User user = tableView.getSelectionModel().getSelectedItem();
        if (user == null)
            showMessage(Alert.AlertType.ERROR,"Error","Nothing is selected!");
        else {
            showGroups(user);
        }
    }

    /**
     * opens a new window that shows the chats of the selected user
     * @param user
     */
    private void showGroups(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/groupchats.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Chats of User "+ user.getId().toString());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            GroupChatController requestController = loader.getController();
            requestController.setService(messageService, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
