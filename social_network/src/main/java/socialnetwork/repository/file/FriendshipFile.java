package socialnetwork.repository.file;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;

import java.util.List;

public class FriendshipFile extends AbstractFileRepository<Tuple<Long,Long>, Friendship> {

    public FriendshipFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship friendship = new Friendship(attributes.get(0));
        friendship.setId(new Tuple<>(Long.parseLong(attributes.get(1)),Long.parseLong(attributes.get(2))));
        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getDate().toString() + ";" + entity.getId().getLeft().toString()
                + ";" + entity.getId().getRight().toString();
    }
}
