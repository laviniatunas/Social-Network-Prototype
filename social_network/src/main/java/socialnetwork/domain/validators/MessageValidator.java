package socialnetwork.domain.validators;

import socialnetwork.domain.Message;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MessageValidator implements Validator<Message> {

    @Override
    public void validate(Message entity) throws ValidationException {
        String erori="";
        if (entity.getMessage().equals(""))
            erori+="The message can't be empty!\n";
        if (entity.getTo().size() == 0)
            erori+="The message needs a recipient!\n";
        if(entity.getFrom() == null)
            erori+="A sender is needed!\n";

        List<Long> ids = entity.getTo().stream()
                            .map(x->x.getId())
                            .collect(Collectors.toList());

        Set<Long> set = new HashSet<>(ids);
        if(entity.getTo().size() != set.size())
            erori+="Receivers can't be duplicated!";


        if (erori.length() > 0)
            throw new ValidationException(erori);
    }
}
