package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.controller.UserController;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.RequestValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.FriendshipFile;
import socialnetwork.repository.file.MessageFile;
import socialnetwork.repository.file.RequestFile;
import socialnetwork.repository.file.UserFile;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MessageService;
import socialnetwork.service.Service;

import java.io.IOException;


public class MainApp extends Application {

    Repository<Long, User> userFileRepository;
    Repository<Tuple<Long,Long>, Friendship> FriendshipRepository;
    Repository<Tuple<Long,Long>, FriendshipRequest> RequestRepository;
    Repository<Long, Message> messageRepository;
    Service srv;
    FriendshipService friendshipService;
    MessageService messageService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        //System.out.println("ok");
        String fileName="data/users.csv";
        String fileName1="data/friendships.csv";
        String fileName2="data/requests.csv";
        String fileName3="data/messages.csv";

        userFileRepository =
                new UserFile(fileName, new UserValidator());

        FriendshipRepository =
                new FriendshipFile(fileName1, new FriendshipValidator());

        RequestRepository =
                new RequestFile(fileName2, new RequestValidator());

        messageRepository =
                new MessageFile(fileName3, new MessageValidator());

        srv = new Service(userFileRepository, FriendshipRepository, RequestRepository);
        friendshipService = new FriendshipService(userFileRepository,FriendshipRepository,RequestRepository);
        messageService = new MessageService(userFileRepository,messageRepository);

        initView(primaryStage);
        primaryStage.setWidth(800);

        primaryStage.show();

    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("/views/users.fxml"));
        AnchorPane messageTaskLayout = messageLoader.load();
        primaryStage.setScene(new Scene(messageTaskLayout));
        primaryStage.setTitle("Users");

        UserController messageTaskController = messageLoader.getController();
        messageTaskController.setUserService(srv, friendshipService,messageService);

    }
}
