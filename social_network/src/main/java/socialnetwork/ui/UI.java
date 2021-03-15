/*

package socialnetwork.ui;

import socialnetwork.domain.*;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.Service;
import socialnetwork.service.ServiceException;

import java.time.LocalDateTime;
import java.util.*;

public class UI {

    private final Service srv;
    private final MessageUI mUi;
    private final FriendshipUI fUi;


    */
/**
     * creates a UI entity
     * @param srv
     * @param mUi
     * @param fUi
     *//*

    public UI(Service srv, MessageUI mUi, FriendshipUI fUi) {
        this.srv = srv;
        this.mUi = mUi;
        this.fUi = fUi;
    }

    */
/**
     * adds a user or reports any error that may occur
     *//*

    private void ui_add(){
        Scanner myObj = new Scanner(System.in);

        System.out.println("Give first name: ");
        String firstN = myObj.nextLine();
        System.out.println("Give last name: ");
        String lastN = myObj.nextLine();
        User u = new User(firstN,lastN);
        u.setId(srv.getNextId());
        try{
            User result = srv.addUser(u);
            if(result == null)
                System.out.println("The user was saved!");
        }
        catch(ValidationException e){
            System.out.println(e);
        }
    }

    */
/**
     * removes a user or reports any error that may occur
     *//*

    private void ui_remove(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("Give id: ");
        String idS = myObj.nextLine();
        try {
            Long id = Long.parseLong(idS);
            User u = new User("", "");
            u.setId(id);
            srv.removeUser(u);
            System.out.println("The user was deleted!");
        } catch (ServiceException e) {
                System.out.println(e);
        }
        catch(NumberFormatException e){
            System.out.println(e);
        }

    }

    */
/**
     * function that lists available options
     *//*

    public void meniu(){
        srv.addFriendsFile();
        Scanner myObj = new Scanner(System.in);
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Add a user");
            System.out.println("2. Remove a user");
            System.out.println("3. Show all users");
            System.out.println("4. Show friends of a user");
            System.out.println("5. Add a friendship");
            System.out.println("6. Remove a friendship");
            System.out.println("7. Number of communities");
            System.out.println("8. Show all friendships");
            System.out.println("9. Show most social community");
            System.out.println("10. Show friendships of a user created in a certain month");
            System.out.println("11. Send a friendship request");
            System.out.println("12. Answer friendship requests");
            System.out.println("13. Send a message");
            System.out.println("14. Reply to a message");
            System.out.println("15. Get conversation of two users");
            System.out.println("0. Exit");
            System.out.println("Insert option: ");
            int option = myObj.nextInt();
            switch (option) {
                case 0:
                    System.out.println("Goodbye!\n");
                    System.exit(0);
                    break;
                case 1:
                    ui_add();
                    break;
                case 2:
                    ui_remove();
                    break;
                case 3:
                    srv.getAll().forEach(System.out::println);
                    break;
                case 4:
                    fUi.ui_userfriends();
                    break;
                case 5:
                    fUi.ui_addfriendship();
                    break;
                case 6:
                    fUi.ui_removefriendship();
                    break;
                case 7:
                    fUi.ui_communities();
                    break;
                case 8:
                    fUi.ui_showfriendships();
                    break;
                case 9:
                    fUi.ui_getsocial();
                    break;
                case 10:
                    fUi.ui_friendsmonth();
                    break;
                case 11:
                    fUi.ui_sendrequest();
                    break;
                case 12:
                    fUi.ui_changestatus();
                    break;
                case 13:
                    mUi.ui_sendmessage();
                    break;
                case 14:
                    mUi.ui_addreply();
                    break;
                case 15:
                    mUi.ui_conversation();
                    break;
                default:
                    System.out.println("This option does not exist!");
                    break;
            }
        }
    }

}

*/
