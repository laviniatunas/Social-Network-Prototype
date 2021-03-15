package socialnetwork.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


public class Friendship extends Entity<Tuple<Long,Long>> {

    LocalDateTime date;

    /**
     * sets date with the date of constructor call
     */
    public Friendship() {
        date=LocalDateTime.now();
    }

    /**
     *
     * @param data - sets date with the given date
     */
    public Friendship(String data) {
        date=LocalDateTime.parse(data);
    }


    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     *
     * @return all information about a friendship
     */
    @Override
    public String toString() {
        return "Friendship{" +
                "date=" + date +
                ", id=" + super.toString()+
                '}';
    }

}
