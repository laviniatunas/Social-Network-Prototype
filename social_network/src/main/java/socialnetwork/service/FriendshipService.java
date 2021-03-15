package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.FriendshipEvents;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FriendshipService  implements Observable<FriendshipEvents> {

    private Repository<Long, User> repoUsers;
    private Repository<Tuple<Long, Long>, Friendship> repoFriendships;
    private Repository<Tuple<Long, Long>, FriendshipRequest> repoRequests;

    public FriendshipService(Repository<Long, User> repoUsers, Repository<Tuple<Long, Long>, Friendship> repoFriendships, Repository<Tuple<Long, Long>, FriendshipRequest> repoRequests) {
        this.repoUsers = repoUsers;
        this.repoFriendships = repoFriendships;
        this.repoRequests = repoRequests;
        for (Friendship f : getFriendships()) {
            Long id1 = f.getId().getLeft();
            Long id2 = f.getId().getRight();

            User u1 = repoUsers.findOne(id1);
            User u2 = repoUsers.findOne(id2);

            u1.getFriends().add(u2);
            u2.getFriends().add(u1);
        }
    }

    /**
     * @param u1
     * @param u2
     * @return date when friendship between two users started
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
     *
     * @return all existing friendships
     */
    public Iterable<Friendship> getFriendships() {
        return repoFriendships.findAll();
    }

    /**
     * adds a Friendship
     *
     * @param friendship
     * @return null if friendship was saved successfully,
     * Friendship that existed, otherwise
     * @throws ServiceException if any of the given ids from friendship does not exist
     */
    public Friendship addFriendship(Friendship friendship) {
        User user1 = repoUsers.findOne(friendship.getId().getLeft());
        User user2 = repoUsers.findOne(friendship.getId().getRight());
        if (user1 == null || user2 == null)
            throw new ServiceException("At least one id does not exist!");

        if (repoFriendships.save(friendship) != null)
            return friendship;

        user1.getFriends().add(user2);
        user2.getFriends().add(user1);
        notifyObservers(new FriendshipEvents());

        return null;
    }

    /**
     * removes a Friendship
     *
     * @param friendship
     * @return Friendship if the friendship was successfully deleted
     * @throws ServiceException if given friendship does not exist
     */
    public Friendship removeFriendship(Friendship friendship) {
        Friendship friendship1 = repoFriendships.findOne(friendship.getId());
        if (friendship1 == null)
            throw new ServiceException("This friendship does not exist!");

        User user1 = repoUsers.findOne(friendship.getId().getLeft());
        User user2 = repoUsers.findOne(friendship.getId().getRight());

        user1.getFriends().remove(user2);
        user2.getFriends().remove(user1);

        repoFriendships.delete(friendship.getId());
        notifyObservers(new FriendshipEvents());
        return friendship1;
    }

    /**
     *
     * @param friendship
     * @return friendship with an id
     */
    public Friendship getfriendship(Friendship friendship) {
        return repoFriendships.findOne(friendship.getId());
    }

    /**
     * loads all Friendships from file and put each User to the other User's friends list
     */
    public void addFriendsFile() {
        Iterable<Friendship> allFriendships = getFriendships();
        for (Friendship f : allFriendships) {
            Long id1 = f.getId().getLeft();
            Long id2 = f.getId().getRight();
            User u1 = repoUsers.findOne(id1);
            User u2 = repoUsers.findOne(id2);
            u1.getFriends().add(u2);
            u2.getFriends().add(u1);
        }
    }

    /**
     *
     * @param user
     * @return dto list of friends of a user
     */
    public List<FriendshipDTO> friendsOfUser(User user)
    {
        List<FriendshipDTO> list ;

        list = repoUsers.findOne(user.getId()).getFriends()
                .stream()
                .map(x->{
                    if (user.getId() < x.getId()) {
                        FriendshipDTO friendshipDTO = new FriendshipDTO(x.getFirstName(), x.getLastName(), repoFriendships.findOne(new Tuple<>(user.getId(), x.getId())).getDate());
                        friendshipDTO.setId(x.getId());
                        return friendshipDTO;
                    }
                    else {
                        FriendshipDTO friendshipDTO = new FriendshipDTO(x.getFirstName(), x.getLastName(), repoFriendships.findOne(new Tuple<>(x.getId(), user.getId())).getDate());
                        friendshipDTO.setId(x.getId());
                        return friendshipDTO;
                    }
                })
                .collect(Collectors.toList());
        return list;
    }

    /**
     *
     * @param user
     * @return dto list of pending requests of a user
     */
    public List<FriendshipDTO> requestsOfUser(User user) {

        List<FriendshipDTO> list = new ArrayList<>();

        for (FriendshipRequest request : getFriendshipRequest(user)){
            Long idsender = request.getId().getLeft();
            User sender = repoUsers.findOne(idsender);
            FriendshipDTO friendshipDTO = new FriendshipDTO(sender.getFirstName(), sender.getLastName(), repoRequests.findOne(new Tuple<>(sender.getId(), user.getId())).getDate());
            friendshipDTO.setId(idsender);
            list.add(friendshipDTO);
        }

        return list;
    }

    /**
     *
     * @param user
     * @return dto list of pending requests sent by a user
     */
    public List<FriendshipDTO> sentRequestsOfUser(User user) {

        List<FriendshipDTO> list = new ArrayList<>();

        for (FriendshipRequest request : getAllRequests() ){
            if(request.getId().getLeft() == user.getId()) {
                Long idreceiver = request.getId().getRight();
                User receiver = repoUsers.findOne(idreceiver);
                FriendshipDTO friendshipDTO = new FriendshipDTO(receiver.getFirstName(), receiver.getLastName(), repoRequests.findOne(new Tuple<>(user.getId(), receiver.getId())).getDate());
                friendshipDTO.setId(idreceiver);
                list.add(friendshipDTO);
            }
        }

        return list;
    }

    /**
     * adds a request
     * @param request
     * @return null if all was ok
     * @throws ServiceException if anything is wrong
     */
    public FriendshipRequest addRequest(FriendshipRequest request) {
        if (repoRequests.findOne(request.getId()) != null)
            throw new ServiceException("Request already sent!");
        User user1 = repoUsers.findOne(request.getId().getLeft());
        User user2 = repoUsers.findOne(request.getId().getRight());
        if (user1 == null || user2 == null)
            throw new ServiceException("At least one id does not exist!");
        Friendship friendship = new Friendship();
        Tuple<Long, Long> f;
        if (user1.getId() < user2.getId())
            f = new Tuple<>(user1.getId(), user2.getId());
        else
            f = new Tuple<>(user2.getId(), user1.getId());
        friendship.setId(f);
        Friendship fr = getfriendship(friendship);
        if (fr == null) {
            if (repoRequests.findOne(new Tuple<>(user2.getId(), user1.getId())) == null) {
                FriendshipRequest result = repoRequests.save(request);
                notifyObservers(new FriendshipEvents());
                return result;
            }
            else
                throw new ServiceException("You have a pending friendship request from this user!");
        } else
            throw new ServiceException("You are already friend with this user!");
    }

    /**
     * changes the status of an existing pending request
     * @param request
     * @param status
     * @return FriendshipRequest in any of the status case
     *      if status is "approved", the friendship is added and the request is deleted
     *      if status is "rejected", the request is deleted
     *      if status is "ignored", the request will still be pending
     */
    public FriendshipRequest changeRequestStatus(FriendshipRequest request, Status status) {
        FriendshipRequest req = repoRequests.findOne(request.getId());
        if (req == null)
            throw new ServiceException("This friendship request doesn't exists!");
        if (status == Status.APPROVED) {
            repoRequests.delete(request.getId());
            Friendship friendship = new Friendship();
            Tuple<Long, Long> f;
            Long id = req.getId().getLeft();
            Long id1 = req.getId().getRight();
            if (id < id1)
                f = new Tuple<>(id, id1);
            else
                f = new Tuple<>(id1, id);
            friendship.setId(f);
            if (addFriendship(friendship) != null)
                return request;
        }
        if (status == Status.REJECTED)
            repoRequests.delete(req.getId());

        notifyObservers(new FriendshipEvents());
        return null;
    }

    /**
     * @return all the requests pending
     */
    public Iterable<FriendshipRequest> getAllRequests() {
        return repoRequests.findAll();
    }

    /**
     * finds all the requests of a user
     * @param user
     * @return a list of requests waiting to be answered by the user
     */
    public List<FriendshipRequest> getFriendshipRequest(User user) {
        List<FriendshipRequest> requests = new ArrayList<>();
        for (FriendshipRequest req : getAllRequests())
            requests.add(req);
        return requests.stream()
                .filter(x -> x.getId().getRight().equals(user.getId()))
                .collect(Collectors.toList());
    }

    private List<Observer<FriendshipEvents>> observers=new ArrayList<>();

    /**
     * adds an observer
     * @param e
     */
    @Override
    public void addObserver(Observer<FriendshipEvents> e) {
        observers.add(e);
    }

    /**
     * removes an observer
     * @param e
     */
    @Override
    public void removeObserver(Observer<FriendshipEvents> e) {
        observers.remove(e);
    }

    /**
     * notifies observers that a change occurred
     * @param t
     */
    @Override
    public void notifyObservers(FriendshipEvents t) {
        observers.stream().forEach(x->x.update(t));
    }

    /**
     * deletes a friendship request
     * @param request
     * @return null
     */
    public FriendshipRequest deleteRequest(FriendshipRequest request) {
        repoRequests.delete(request.getId());
        notifyObservers(new FriendshipEvents());
        return null;
    }
}
