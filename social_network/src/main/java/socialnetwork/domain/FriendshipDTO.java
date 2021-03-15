package socialnetwork.domain;

import java.time.LocalDateTime;

public class FriendshipDTO extends Entity<Long>{
    private String firstName;
    private String lastName;

    LocalDateTime date;

    public FriendshipDTO(String firstName, String lastName, LocalDateTime date) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}