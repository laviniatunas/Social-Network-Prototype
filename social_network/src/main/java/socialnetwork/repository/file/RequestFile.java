package socialnetwork.repository.file;

import socialnetwork.domain.FriendshipRequest;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.util.List;

public class RequestFile  extends AbstractFileRepository<Tuple<Long,Long>, FriendshipRequest>{
    public RequestFile(String fileName, Validator<FriendshipRequest> validator) {
        super(fileName, validator);
    }

    @Override
    public FriendshipRequest extractEntity(List<String> attributes) {
        FriendshipRequest request = new FriendshipRequest(attributes.get(2),attributes.get(3));
        request.setId(new Tuple<>(Long.parseLong(attributes.get(0)),Long.parseLong(attributes.get(1))));

        return request;
    }

    @Override
    protected String createEntityAsString(FriendshipRequest entity) {
        return  entity.getId().getLeft().toString() + ";" + entity.getId().getRight().toString() + ";" + entity.getStatus() + ";" + entity.getDate().toString() ;
    }
}