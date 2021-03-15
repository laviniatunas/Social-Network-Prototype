package socialnetwork.domain.validators;

import socialnetwork.domain.FriendshipRequest;

public class RequestValidator  implements  Validator<FriendshipRequest> {
    @Override
    public void validate(FriendshipRequest entity) throws ValidationException {
        String erori="";

        if(entity.getId().getLeft() == null)
            erori += "First user does not exist!\n";
        if(entity.getId().getRight() == null)
            erori += "Second user does not exist!\n";
        if(entity.getId().getLeft() == entity.getId().getRight())
            erori += "The id can't be the same!\n";
        if(erori.length() > 0)
            throw new ValidationException(erori);
    }
}
