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
import socialnetwork.domain.Friendship;
import socialnetwork.domain.FriendshipDTO;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.ServiceException;
import socialnetwork.utils.events.FriendshipEvents;
import socialnetwork.utils.observer.Observer;

import java.io.IOException;
import java.time.LocalDateTime;

public class FriendshipController implements Observer<FriendshipEvents> {
    FriendshipService srvFriends;
    Stage stage;
    User user;
    ObservableList<FriendshipDTO> model = FXCollections.observableArrayList();

    @FXML
    TableView<FriendshipDTO> tableView;
    @FXML
    TableColumn<FriendshipDTO,Long> tableColumnId;
    @FXML
    TableColumn<FriendshipDTO,String> tableColumnFirstName;
    @FXML
    TableColumn<FriendshipDTO,String> tableColumnLastName;
    @FXML
    TableColumn<FriendshipDTO,LocalDateTime> tableColumnDate;

    public void setFriendsService(FriendshipService serviceFriends,Stage stage,User u) {
        this.stage = stage;
        this.user = u;
        srvFriends = serviceFriends;
        srvFriends.addObserver(this);
        initModel();
    }

    /**
     * puts data into table
     */
    private void initModel() {
        model.setAll(srvFriends.friendsOfUser(user));
    }

    /**
     * initializes the columns
     */
    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, Long>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, String>("lastName"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<FriendshipDTO, LocalDateTime>("date"));
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
     * @param friendshipEvents
     */
    @Override
    public void update(FriendshipEvents friendshipEvents) {
        initModel();
    }

    /**
     * deletes a friendship
     * @param actionEvent
     */
    public void DeleteFriendship(ActionEvent actionEvent) {
        FriendshipDTO dto = tableView.getSelectionModel().getSelectedItem();
        if( dto == null){
            showMessage(Alert.AlertType.ERROR,"Error","Nothing is selected!");
        }
        else {
            Friendship friendship = new Friendship();

            Tuple<Long, Long> id;
            if (dto.getId() < user.getId() )
                id = new Tuple<>(dto.getId(), user.getId());
            else
                id = new Tuple<>(user.getId(), dto.getId());

            friendship.setId(id);

            try{
                srvFriends.removeFriendship(friendship);
                showMessage(Alert.AlertType.CONFIRMATION,"Successful deletion","Friendship was deleted!");
            }catch (ServiceException exception){
                showMessage(Alert.AlertType.ERROR,"Error!", exception.toString());
            }

        }
    }

    /**
     * opens a new window with possibility of sending friendship requests
     * @param actionEvent
     */
    public void SendRequest(ActionEvent actionEvent) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/sendrequests.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Send requests");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            SendRequestController requestController = loader.getController();
            requestController.setService(srvFriends, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}