package socialnetwork.service;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Message;
import socialnetwork.domain.MessageDTO;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.MessageEvents;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.util.*;
import java.util.stream.Collectors;

public class MessageService implements Observable<MessageEvents> {
    private Repository<Long, User> repoUsers;
    private Repository<Long, Message> repoMessage;

    public MessageService(Repository<Long, User> repoUsers, Repository<Long, Message> repoMessage) {
        this.repoUsers = repoUsers;
        this.repoMessage = repoMessage;
    }

    /**
     * @return all the messages
     */
    public Iterable<Message> getAllMessages() {
        return repoMessage.findAll();
    }

    /**
     * @return the biggest existing id of a message
     */
    public Long getMaxIdMessage() {
        Long maxId = 0L;
        Iterable<Message> all = getAllMessages();
        for (Message msg : all) {
            if (msg.getId() > maxId)
                maxId = msg.getId();
        }
        return maxId;
    }

    /**
     * adds a message
     * @param message
     * @return null if message was saved successfully
     * @throws ServiceException if any of the users, sender or receiver doesn't exist
     */
    public Message addMessage(Message message) {
        User from = repoUsers.findOne(message.getFrom().getId());
        if (from == null)
            throw new ServiceException("Sender user does not exist!");

        List<User> to = new ArrayList<>();

        for (User userto : message.getTo()) {
            User toAdd = repoUsers.findOne(userto.getId());
            if (toAdd == null)
                throw new ServiceException("At least one id does not exist!");
            to.add(toAdd);
        }
        List<User> sortedList = to.stream().sorted(Comparator.comparing(Entity::getId)).collect(Collectors.toList());
        message.setFrom(from);
        message.setTo(sortedList);
        Message addResult = repoMessage.save(message);
        if( addResult == null )
            notifyObservers(new MessageEvents());
        return addResult;
    }

    /**
     * adds a reply
     * @param reply
     * @param original
     * @return
     */
    public Message addReply(Message reply, Message original) {
        Message m = repoMessage.findOne(original.getId());
        if(m == null)
            throw new ServiceException("Original message you want to reply to doesn't exist!");
        reply.setReplyMessage(m);
        return addMessage(reply);
    }

    /**
     *
     * @param u1
     * @param u2
     * @return list of messages between two users
     */
    public List<Message> getConversation(User u1, User u2){

        if (repoUsers.findOne(u1.getId()) == null || repoUsers.findOne(u2.getId()) == null)
            throw new ServiceException("At least one user doesn't exist!");

        List<Message> messages = new ArrayList<>();
        for ( Message m : repoMessage.findAll() ) {
            if(m.getFrom().getId() == u1.getId() && m.getTo().size() == 1 && m.getTo().get(0).getId() == u2.getId())
                messages.add(m);
            if(m.getFrom().getId() == u2.getId() && m.getTo().size() == 1 && m.getTo().get(0).getId() == u1.getId())
                messages.add(m);
        }


        return messages.stream()
                .sorted(Comparator.comparing(Message::getData))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param id
     * @return message with given id
     */
    public Message findMessage(Long id){
        return repoMessage.findOne(id);
    }

    /**
     *
     * @param user
     * @return list of messages the were sent by or sent to a given user
     */
    public List<Message> getAllMessagesForUser(User user){
        List<Message> msg = new ArrayList<>();

        for(Message m : getAllMessages()){
            if(m.getFrom().getId() == user.getId())
                msg.add(m);

            for(User m1 : m.getTo()){
                if(m1.getId() == user.getId())
                    msg.add(m);
            }
        }
        return msg;
    }

    /**
     *
     * @param user
     * @return dto list with all group chats of a user
     */
    public List<MessageDTO> getAllGroups(User user){
        List<MessageDTO> msg = new ArrayList<>();
        List<String> rez= new ArrayList<>();
        boolean sem;
        for(Message m : getAllMessages()){
           if(m.getFrom().getId() == user.getId()){
               List<Long> ids = new ArrayList<>();
               for (User u : m.getTo())
                   ids.add(u.getId());
               ids = ids.stream().sorted().collect(Collectors.toList());
               String s="";
               for (Long idc : ids)
                   s=s+idc.toString()+ " ";
               rez.add(s);
           }
           sem=false;
           for (User u: m.getTo())
               if(u.getId() == user.getId()){
                   sem=true;
                   break;
               }
           if (sem){
               List<Long> ids = new ArrayList<>();
               for (User u : m.getTo())
                   if(u.getId()!=user.getId())
                       ids.add(u.getId());
               ids.add(m.getFrom().getId());
               ids = ids.stream().sorted().collect(Collectors.toList());
               String s="";
               for (Long idc : ids)
                   s=s+idc.toString()+ " ";
               rez.add(s);
           }
        }
        Set<String> hSet = new HashSet<String>();
        for (String x : rez)
            hSet.add(x);
        for (String string : hSet)
            msg.add(new MessageDTO(string));
        return msg;
    }

    /**
     *
     * @param s
     * @return dto list with messages from a certain group, s contains all ids from the group
     */
    public  List<MessageDTO> getConversationDTO(String s){
        List<MessageDTO> result = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (String string : s.split(" "))
            ids.add(Long.parseLong(string));

        if(ids.size() == 2 && ids.get(0) == ids.get(1)) {
            for (Message m : getAllMessages()){

                if(m.getFrom().getId() == ids.get(1) && m.getTo().get(0).getId() == ids.get(1)) {
                    MessageDTO dto = new MessageDTO(m.toString(), m.getData());
                    dto.setId(m.getId());
                    result.add(dto);
                }
            }
        }

        else for (Long currentId : ids){
            List<Long> newIdList = new ArrayList<>();
            for(Long id : ids)
                if(id != currentId)
                    newIdList.add(id);

            List<Long> sortedList = newIdList.stream().sorted().collect(Collectors.toList());

            for (Message m : getAllMessages()){
                List<Long> messageList = new ArrayList<>();
                for(User u : m.getTo())
                    messageList.add(u.getId());

                if(m.getFrom().getId() == currentId && messageList.equals(sortedList)) {
                    MessageDTO dto = new MessageDTO(m.toString(), m.getData());
                    dto.setId(m.getId());
                    result.add(dto);
                }
            }
        }
        return result.stream().sorted(Comparator.comparing(MessageDTO::getDate)).collect(Collectors.toList());
    }

    private List<Observer<MessageEvents>> observers = new ArrayList<>();

    /**
     * adds an observer
     * @param e
     */
    @Override
    public void addObserver(Observer<MessageEvents> e) {
        observers.add(e);
    }

    /**
     * removes an observer
     * @param e
     */
    @Override
    public void removeObserver(Observer<MessageEvents> e) {
        observers.remove(e);
    }

    /**
     * notifies observers that a change occurred
     * @param t
     */
    @Override
    public void notifyObservers(MessageEvents t) {
        observers.stream().forEach(x->x.update(t));
    }

}
