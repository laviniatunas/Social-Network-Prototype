package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private List<User> friends=new ArrayList<User>();

    /**
     *
     * @param firstName
     * @param lastName
     * creates a user with the given firstName and lastName
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     *
     * @return firstName of user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     * sets firstName of user to given firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return lastName of a user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     * sets the lastName of a user to the given lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return list of friends of a user
     */
    public List<User> getFriends() {
        return friends;
    }

    /**
     *
     * @return information of a user as a string
     */
    @Override
    public String toString() {
        return "Utilizator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=" + friends.size() +
                '}';
    }

    /**
     *
     * @param o
     * @return true, if the two users are identical
     *          false, otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    /**
     *
     * @return hashCode of a user
     */
    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }
}