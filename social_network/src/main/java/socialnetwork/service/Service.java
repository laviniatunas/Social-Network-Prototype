package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.UserEvents;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Service implements Observable<UserEvents> {
    private Repository<Long, User> repoUsers;
    private Repository<Tuple<Long, Long>, Friendship> repoFriendships;
    private Repository<Tuple<Long, Long>, FriendshipRequest> repoRequests;


    /**
     * @param repoUsers
     * @param repoFriendships
     * @param repoRequests
     * creates a service entity
     */
    public Service(Repository<Long, User> repoUsers, Repository<Tuple<Long, Long>, Friendship> repoFriendships, Repository<Tuple<Long, Long>, FriendshipRequest> repoRequests) {
        this.repoUsers = repoUsers;
        this.repoFriendships = repoFriendships;
        this.repoRequests = repoRequests;
    }

    /**
     * adds a User
     *
     * @param user
     * @return null if user was saved successfully,
     * User with given id that existed, otherwise
     */
    public User addUser(User user) {
        User task = repoUsers.save(user);
        return task;
    }

    /**
     * removes a User based on id, all his Friendships and all his friendship requests
     *
     * @param user
     * @return User if the User with the given id exists
     * @throws ServiceException if the User with the given id does not exist
     */
    public User removeUser(User user) {
        User u = repoUsers.findOne(user.getId());
        if (u == null)
            throw new ServiceException("No user was deleted, id does not exist!");
        for (User friend : u.getFriends()) {
            friend.getFriends().remove(u);
            if (u.getId() < friend.getId())
                repoFriendships.delete(new Tuple<>(u.getId(), friend.getId()));
            else
                repoFriendships.delete(new Tuple<>(friend.getId(), u.getId()));
        }
        List<FriendshipRequest> requests = new ArrayList<>();
        for (FriendshipRequest request : getAllRequests())
            requests.add(request);
        for (FriendshipRequest req : requests) {
            if (req.getId().getRight() == user.getId())
                repoRequests.delete(new Tuple<>(req.getId().getLeft(), user.getId()));
            if (req.getId().getLeft() == user.getId())
                repoRequests.delete(new Tuple<>(user.getId(), req.getId().getRight()));
        }
        User task = repoUsers.delete(user.getId());
        return task;
    }

    /**
     * @return all the requests pending
     */
    public Iterable<FriendshipRequest> getAllRequests() {
        return repoRequests.findAll();
    }

    /**
     * @return all the Users
     */
    public Iterable<User> getAll() {
        return repoUsers.findAll();
    }

    /**
     * gets User with given id
     *
     * @param u
     * @return User, if a User with given id exists or null otherwise
     */
    public User findOne(User u) {
        return repoUsers.findOne(u.getId());
    }

    /**
     * gets the number of Users
     *
     * @return number of Users
     */
    private int getSize() {
        Iterable<User> all = getAll();
        int lungime = 0;
        for (User user : all) {
            lungime++;
        }
        return lungime;
    }

    /**
     * finds the biggest id of an existing User
     *
     * @return biggest id of an existing User
     */
    private Long getMaxId() {
        if (getSize() != 0) {
            Long maxId = (long) 0;
            Iterable<User> all = getAll();
            for (User user : all) {
                if (user.getId() > maxId)
                    maxId = user.getId();
            }
            return maxId;
        } else return (long) 0;
    }

    /**
     * increases biggest id existing
     *
     * @return biggest id existing increased
     */
    public Long getNextId() {
        Long nextId = getMaxId();
        nextId += 1;
        return nextId;
    }

    /**
     * uses depth first search to visit all friends of a user
     *
     * @param list
     * @param u
     * @param visited
     */
    protected void dfs(ArrayList<User> list, User u, boolean visited[]) {
        if (u.getFriends().size() > 0) {
            for (User friend : u.getFriends()) {
                int index = list.indexOf(friend);
                if (!visited[index]) {
                    visited[index] = true;
                    dfs(list, friend, visited);
                }
            }
        }
    }

    /**
     * @return number of communities
     */
    public int count_communities() {
        int nr = 0;
        int l = 0;
        ArrayList<User> list = new ArrayList<>();
        for (User u : getAll()) {
            list.add(l, u);
            l++;
        }
        boolean[] visited = new boolean[list.size() + 1];
        for (int i = 0; i < list.size(); i++) {
            if (!visited[i]) {
                visited[i] = true;
                dfs(list, list.get(i), visited);
                nr++;
            }
        }
        return nr;
    }

    /**
     * uses depth first search to visit all friends of a user and keep track of the visited ids
     *
     * @param list
     * @param u
     * @param visited
     * @param rezultat
     * @return a string containing ids of users in a community
     */
    protected String dfs1(ArrayList<User> list, User u, boolean visited[], String rezultat) {
        if (u.getFriends().size() > 0) {
            for (User friend : u.getFriends()) {
                int index = list.indexOf(friend);
                if (!visited[index]) {
                    visited[index] = true;
                    rezultat += Long.toString(friend.getId());
                    rezultat += " ";
                    rezultat += dfs1(list, friend, visited, rezultat);
                }
            }
        }
        return rezultat;
    }

    /**
     * @return a string containing all ids from the most social community
     */
    public String mostsocial() {
        String rezultat = "";
        int l = 0;
        ArrayList<User> list = new ArrayList<>();
        for (User u : getAll()) {
            list.add(l, u);
            l++;
        }
        boolean[] visited = new boolean[list.size() + 1];
        for (int i = 0; i < list.size(); i++) {
            String rezultatP = "";
            if (!visited[i]) {
                visited[i] = true;
                rezultatP += Long.toString(list.get(i).getId());
                rezultatP += " ";
                rezultatP += dfs1(list, list.get(i), visited, rezultatP);
            }
            if (rezultatP.length() > rezultat.length())
                rezultat = rezultatP;
        }

        String[] parts = rezultat.split(" ");
        rezultat = "";
        for (int i = 0; i < parts.length - 1; i++) {

            for (int j = i + 1; j < parts.length; j++) {
                if (parts[i].equals(parts[j]))
                    parts[j] = "";
            }
            if (!parts[i].equals("")) {
                rezultat += parts[i];
                rezultat += " ";
            }
        }
        return rezultat;
    }

    /**
     *
     * @param friendship
     * @return friendship by id
     */
    public Friendship getfriendship(Friendship friendship) {
        return repoFriendships.findOne(friendship.getId());
    }

    /**
     *
     * @param u1
     * @param u2
     * @return date when two users became friends
     */
    public LocalDateTime getfriendshipdate(User u1, User u2) {
        Friendship friendship = new Friendship();
        Tuple<Long, Long> f;
        if (u1.getId() < u2.getId())
            f = new Tuple<>(u1.getId(), u2.getId());
        else
            f = new Tuple<>(u2.getId(), u1.getId());
        friendship.setId(f);
        return getfriendship(friendship).getDate();
    }

    /**
     * gets friendships of a user and returns them as strings containing friends first name, last name and
     * date of friendship
     * @param user
     * @param month
     * @param check
     * @return List of Strings containing all friends first name, last name and date of friendship,
     *                              if check is false
     *         List of Strings containing all friends first name, last name and date of friendship made
     *         in a the month "month" if check is true
     */
    public List<String> userfriendships(User user, int month, boolean check) {
        List<String> result = new ArrayList<>();
        List<User> friends = user.getFriends();
        if (check == false) {
            result = friends.stream()
                    .map(x -> x.getLastName() + " | " + x.getFirstName() + " | " + getfriendshipdate(x, user).toString())
                    .collect(Collectors.toList());
        } else {
            result = friends.stream()
                    .filter(x -> getfriendshipdate(user, x).getMonthValue() == month)
                    .map(x -> x.getLastName() + " | " + x.getFirstName() + " | " + getfriendshipdate(x, user).toString())
                    .collect(Collectors.toList());
        }
        return result;
    }

    private List<Observer<UserEvents>> observers=new ArrayList<>();

    /**
     * adds an observer
     * @param e
     */
    @Override
    public void addObserver(Observer<UserEvents> e) {
        observers.add(e);
    }

    /**
     * removes an observer
     * @param e
     */
    @Override
    public void removeObserver(Observer<UserEvents> e) {
        observers.remove(e);
    }

    /**
     * notifies all observers
     * @param t
     */
    @Override
    public void notifyObservers(UserEvents t) {
        observers.stream().forEach(x->x.update(t));
    }
}
