package socialnetwork.domain;

import java.time.LocalDateTime;

public class FriendshipRequest extends Entity<Tuple<Long,Long>>{
    private Status status;
    private LocalDateTime date;

    public FriendshipRequest(String status) {
        this.status = Status.valueOf(status);
        this.date = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public FriendshipRequest(String status, String data) {
        this.status = Status.valueOf(status);
        this.date = LocalDateTime.parse(data);
    }

    public FriendshipRequest() {
        this.status = Status.PENDING;
        this.date = LocalDateTime.now();

    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }


}
