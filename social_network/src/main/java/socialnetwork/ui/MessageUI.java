/*
package socialnetwork.ui;

import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.Service;
import socialnetwork.service.ServiceException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MessageUI {
    private final Service srv;

    */
/**
     * creates an entity
     * @param srv
     *//*

    public MessageUI(Service srv) {
        this.srv = srv;
    }

    */
/**
     *adds a message or reports any error that occurred
     *//*

    protected void ui_sendmessage() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Who are you?: ");
        String idS1 = myObj.nextLine();
        try{
            Message message= new Message(null,null,null, LocalDateTime.now());
            Long sender = Long.parseLong(idS1);
            User u = new User("","");
            u.setId(sender);
            message.setFrom(u);
            System.out.println("Whom do you want to send a message?(0 to stop)");
            String idS = myObj.nextLine();
            List<User> to = new ArrayList<>();
            while (!idS.equals("0")) {
                User user = new User("","");
                user.setId(Long.parseLong(idS));
                to.add(user);
                System.out.println("Whom do you want to send a message?(0 to stop)");
                idS = myObj.nextLine();
            }
            message.setTo(to);
            System.out.println("What is the message you want to send?");
            message.setMessage(myObj.nextLine());
            message.setId(srv.getMaxIdMessage() + 1);
            srv.addMessage(message);
            System.out.println("Message sent successfully!");
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
     * adds a reply or reports any error that occurred
     *//*


    protected void ui_addreply() {
        Scanner myInput = new Scanner(System.in);
        System.out.println("Insert your ID: ");
        String id = myInput.nextLine();

        try{
            Long idd = Long.parseLong(id);
            User user = new User("", "");
            user.setId(idd);
            List<Message> messages = srv.getAllMessagesForUser(user);
            if(messages.size() == 0)
                System.out.println("The user does not have messages.");
            else {

                System.out.println("Insert the ID message: ");
                String idM = myInput.nextLine();
                Long iddM = Long.parseLong(idM);

                System.out.println("Insert the Reply message: ");
                String replyMessage = myInput.nextLine();

                List<User> to = new ArrayList<>();

                //reply.to = message.from + message.to - reply.from
                for (Message msg : messages)
                    if (msg.getId() == iddM) {
                        to.add(msg.getFrom());
                        to.addAll(msg.getTo());
                    }

                //reply.from
                to.removeIf(x -> x.getId() == user.getId());

                Message message = new Message(user, to, replyMessage, LocalDateTime.now());
                message.setId(srv.getMaxIdMessage()+1);

                Message reply = new Message(null, null, null, null);

                //set the ID for the reply with the message ID
                reply.setId(iddM);

                srv.addReply(message, reply);

                System.out.println("The reply message was added with success.");
            }
        }
        catch (NumberFormatException e) {
            System.out.println(e);
        }
        catch (ValidationException e ) {
            System.out.println(e);
        }
        catch(ServiceException e){
            System.out.println(e);
        }
    }
        */
/*
        Scanner myObj = new Scanner(System.in);
        System.out.println("Who are you?: ");
        String idS1 = myObj.nextLine();
        try{
            Message message = new Message(null,null,null,LocalDateTime.now());
            message.setId(srv.getMaxIdMessage()+1);
            Long sender = Long.parseLong(idS1);
            User u = new User("","");
            u.setId(sender);
            User user = srv.findOne(u);
            List<Long> messages = new ArrayList<>();
            for (Message m : srv.getAllMessages()){
                if(m.getFrom().getId() == sender)
                    messages.add(m.getId());
                else for(User utilizator : m.getTo())
                    if(utilizator.getId() == sender)
                        messages.add(m.getId());
            }
            if(messages.size()>0) {
                System.out.println("Select the id of the message you want to reply to: " + messages);
                String newid = myObj.nextLine();
                while (messages.indexOf(Long.parseLong(newid)) == -1) {
                    System.out.println("You can't reply to that message! Select the id of the message you want to reply to: " + messages);
                    newid = myObj.nextLine();
                }
                Message original = srv.findMessage(Long.parseLong(newid));
                message.setReplyMessage(original);
                if(original.getFrom().getId() != sender ) {
                    message.setFrom(user);
                    List<User> to = original.getTo();
                    to.add(original.getFrom());
                    to.removeIf(x->x.getId() == sender);
                    message.setTo(to);
                }
                else {
                    message.setTo(original.getTo());
                    message.setFrom(original.getFrom());
                }
                System.out.println("What is the reply message?");
                message.setMessage(myObj.nextLine());
                srv.addReply(message, original);
                System.out.println("Reply sent successfully!");
            }
            else System.out.println("You don't have any messages to reply to!");
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
    }*//*


    */
/**
     * shows conversation between two users or reports any error that occurred
     *//*

    protected void ui_conversation() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Give id1: ");
        String idS1 = myObj.nextLine();
        System.out.println("Give id2: ");
        String idS2 = myObj.nextLine();
        try {
            Long id = Long.parseLong(idS1);
            Long id1 = Long.parseLong(idS2);
            User u1 = new User("","");
            u1.setId(id);
            User u2= new User("","");
            u2.setId(id1);
            List<Message> messages = srv.getConversation(u1,u2);
            if (messages.size() == 0)
                System.out.println("There is no conversation between these two users!");
            else for (Message m : messages ) {
                System.out.println(m);
                System.out.println("\n");
            }
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
