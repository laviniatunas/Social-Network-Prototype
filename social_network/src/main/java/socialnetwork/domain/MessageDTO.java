package socialnetwork.domain;

import java.time.LocalDateTime;

public class MessageDTO extends Entity<Long>{
    //private Long idMessage;
    private String to;
    private LocalDateTime date;


    public MessageDTO(String to) {
        this.to = to;
    }


    public MessageDTO(String to, LocalDateTime date) {
        //this.idMessage = idMessage;
        this.to = to;
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

   /* public Long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
    }*/

    /*@Override
    public String toString() {
        return "MessageDTO{" +
                "idMessage=" + idMessage +
                ", to='" + to + '\'' +
                ", date=" + date +
                '}';
    }*/

    @Override
    public String toString() {
        return "MessageDTO{" +
                "to='" + to + '\'' +
                ", date=" + date +
                '}';
    }
}
