package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import socialnetwork.domain.FriendshipDTO;
import socialnetwork.domain.FriendshipRequest;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.service.FriendshipService;
import socialnetwork.utils.events.FriendshipEvents;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;

public class SentRequestController implements Observer<FriendshipEvents> {

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
    TableColumn<FriendshipDTO, LocalDateTime> tableColumnDate;

    public void setService(FriendshipService serviceFriends,Stage stage,User u) {
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
        model.setAll(srvFriends.sentRequestsOfUser(user));
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
     * cancels a sent request
     * @param actionEvent
     */
    public void CancelRequest(ActionEvent actionEvent) {
        FriendshipDTO dto = tableView.getSelectionModel().getSelectedItem();
        if( dto == null){
            showMessage(Alert.AlertType.ERROR,"Error","Nothing is selected!");
        }
        else{
            FriendshipRequest request = new FriendshipRequest();
            Tuple<Long, Long> f = new Tuple<>(user.getId(), dto.getId());
            request.setId(f);
            srvFriends.deleteRequest(request);
            showMessage(Alert.AlertType.CONFIRMATION,"Cancelled!","Request cancelled!");
        }
    }
}
