/*
package socialnetwork.ui;

import socialnetwork.domain.*;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.Service;
import socialnetwork.service.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FriendshipUI {
    private final Service srv;

    */
/**
     * creates an entity
     * @param srv
     *//*

    public FriendshipUI(Service srv) {
        this.srv = srv;
    }

    */
/**
     *adds a friendship or reports any error that occurred
     *//*

    public void ui_addfriendship(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("Give id1: ");
        String idS1 = myObj.nextLine();
        System.out.println("Give id2: ");
        String idS2 = myObj.nextLine();
        try{
            Long id = Long.parseLong(idS1);
            Long id1 = Long.parseLong(idS2);
            Friendship friendship = new Friendship();
            Tuple<Long,Long> f;
            if(id < id1)
                f = new Tuple<>(id,id1);
            else
                f = new Tuple<>(id1,id);

            friendship.setId(f);

            Friendship result = srv.addFriendship(friendship);
            if (result == null)
                System.out.println("Friendship was added!");
            else
                System.out.println("Friendship already exists!");
        }
        catch(ValidationException e){
            System.out.println(e);
        }
        catch(ServiceException e){
            System.out.println(e);
        }
        catch (NumberFormatException e){
            System.out.println(e);
        }
    }

    */
/**
     * removes a friendships or reports any error that occurred
     *//*

    public void ui_removefriendship() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Give id1: ");
        String idS1 = myObj.nextLine();
        System.out.println("Give id2: ");
        String idS2 = myObj.nextLine();
        try {
            Long id = Long.parseLong(idS1);
            Long id1 = Long.parseLong(idS2);
            Friendship friendship = new Friendship();
            Tuple<Long, Long> f;
            if (id < id1)
                f = new Tuple<>(id, id1);
            else
                f = new Tuple<>(id1, id);
            friendship.setId(f);
            srv.removeFriendship(friendship);
            System.out.println("Friendship was deleted!");
        }
        catch(ServiceException e){
            System.out.println(e);
        }
    }

    */
/**
     * shows number of communities
     *//*

    public void ui_communities(){
        System.out.println("Number of communities is: "+ srv.count_communities());
    }

    */
/**
     * shows all friendships
     *//*

    protected void ui_showfriendships() {
        for (Friendship f : srv.getFriendships()){
            System.out.println(f);
        }
    }

    */
/**
     * return the list of ids that is in the most social community
     *//*

    protected void ui_getsocial() {
        System.out.println("Most social community is:"+srv.mostsocial());
    }

    */
/**
     * shows all friendships of a user or reports any error that occurred
     *//*

    protected void ui_userfriends(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("Give id: ");
        String idS = myObj.nextLine();
        try {
            Long id = Long.valueOf(idS);
            User w = new User("", "");
            w.setId(id);
            User u1 = srv.findOne(w);
            if (u1 == null) {
                System.out.println("The id is invalid!");
                return;
            }
            List<String> friends = srv.userfriendships(u1,0,false);
            if(friends.size() == 0)
                System.out.println("The user has no friends!");
            else {
                System.out.println("Last name | First name | Date of friendship");
                friends.forEach(System.out::println);
            }
        }catch (NumberFormatException e){
            System.out.println(e);
        }
    }

    */
/**
     *shows friendships of a user in a certain month or reports any error that occurred
     *//*

    protected void ui_friendsmonth(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("Give id: ");
        String idS = myObj.nextLine();
        System.out.println("Give month [1-12] : ");
        String idM = myObj.nextLine();
        try {
            Long id = Long.valueOf(idS);
            int month = Integer.parseInt(idM);
            User w = new User("", "");
            w.setId(id);
            User u1 = srv.findOne(w);
            if (u1 == null) {
                System.out.println("The id is invalid!");
                return;
            }
            List<String> friends = srv.userfriendships(u1,month,true);
            if(friends.size() == 0)
                System.out.println("The user has no friendships that started in this month!");
            else {
                System.out.println("Last name | First name | Date of friendship");
                friends.forEach(System.out::println);
            }
        }catch (NumberFormatException e){
            System.out.println(e);
        }
    }

    */
/**
     * sends a friendship request or reports any error that occurred
     *//*

    public void ui_sendrequest(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("From who: ");
        String idS1 = myObj.nextLine();
        System.out.println("To whom: ");
        String idS2 = myObj.nextLine();
        try{
            Long id = Long.parseLong(idS1);
            Long id1 = Long.parseLong(idS2);
            FriendshipRequest request = new FriendshipRequest();
            Tuple<Long,Long> f = new Tuple<>(id,id1);
            request.setId(f);

            srv.addRequest(request);
            System.out.println("Friendship request is now pending!");
        }
        catch(ValidationException e){
            System.out.println(e);
        }
        catch(ServiceException e){
            System.out.println(e);
        }
        catch (NumberFormatException e){
            System.out.println(e);
        }
    }

    */
/**
     * changes status of a request or reports any error that occurred
     *//*

    public void ui_changestatus(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("Who are you?: ");
        String idS1 = myObj.nextLine();
        try{
            Long id = Long.parseLong(idS1);
            User intermediar = new User("","");
            intermediar.setId(id);
            User user = srv.findOne(intermediar);
            if(user == null){
                System.out.println("This user doesn't exist!");
                return;
            }
            List<FriendshipRequest> requests = srv.getFriendshipRequest(user);
            List<Long> ids = new ArrayList<>();
            for(FriendshipRequest req : requests )
                ids.add(req.getId().getLeft());

            if(ids.size()>0) {
                System.out.println("You have friendship request(s) from user(s): "+ids);
                String option;
                System.out.println("Do you want to answer a friendship request?[y/n]");
                option = myObj.nextLine();

                while (option.equals("y")) {
                    System.out.println("Select id whose request you want to answer from the list: "+ids);
                    String fromwho = myObj.nextLine();
                    Long idfromwho = Long.parseLong(fromwho);
                    if (ids.indexOf(idfromwho) == -1) {
                        System.out.println("You have no friendship request from this user!");
                    }
                    else {
                        intermediar.setId(idfromwho);
                        User from = srv.findOne(intermediar);
                        FriendshipRequest request = new FriendshipRequest();
                        Tuple<Long, Long> f = new Tuple<>(from.getId(), user.getId());
                        request.setId(f);
                        System.out.println("Do you want to accept(a), reject(r) or ignore(i)?");
                        String newstatus = myObj.nextLine();
                        if (newstatus.equals("a")) {
                            srv.changeRequestStatus(request, Status.APPROVED);
                            ids.remove(idfromwho);
                        }
                        else if (newstatus.equals("r")) {
                            srv.changeRequestStatus(request, Status.REJECTED);
                            ids.remove(idfromwho);
                        }
                        else if (newstatus.equals("i"))
                            System.out.println("Status won't change!");
                        else
                            System.out.println("Invalid option for friendship request!");

                        if(ids.size()>0) {
                            System.out.println("Do you want to answer a friendship request?[y/n]");
                            option = myObj.nextLine();
                        }
                        else{
                            System.out.println("You no longer have friendship requests!");
                            return;
                        }
                    }
                }
            }
            else {
                System.out.println("You have no friendship requests!");
                return;
            }
        }
        catch(ValidationException e){
            System.out.println(e);
        }
        catch(ServiceException e){
            System.out.println(e);
        }
        catch (NumberFormatException e){
            System.out.println(e);
        }
    }
}
*/
