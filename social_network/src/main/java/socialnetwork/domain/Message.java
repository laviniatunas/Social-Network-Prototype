package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    private Long id;
    private User from;
    private List<User> to;
    private String message;
    private LocalDateTime data;
    private Message replyMessage = null;

    public Message(User from, List<User> to, String message, LocalDateTime data) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
    }

    public Message getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(Message replyMessage) {
        this.replyMessage = replyMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String s = "";
        for ( User u : to)
            s+=u.getId().toString()+" ";
        String result="";
        result+= "From: " + from.getId().toString() +
                "\nTo: " + s +
                "\nDate: "+ data.toString() +
                "\n" + message;
        /*if(getReplyMessage() == null)
            result+= "Message id: " + id +
                "\nFrom: " + from.getId().toString() +
                "\nTo: " + s +
                "\nDate: "+ data.toString() +
                "\n" + message;
        else result+= "Message id: " + id +
                "\nFrom: " + from.getId().toString() +
                "\nTo: " + s +
                "\nDate: "+ data.toString() +
                "\n" + message +
                "\nReply to message with id: " + replyMessage.getId();*/
        return result;
    }
}
