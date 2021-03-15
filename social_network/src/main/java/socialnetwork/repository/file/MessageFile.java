package socialnetwork.repository.file;

import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageFile extends AbstractFileRepository<Long, Message> {

    public MessageFile(String fileName, Validator<Message> validator) {
        super(fileName, validator);
    }

    @Override
    public Message extractEntity(List<String> attributes) {
        User from = new User("", "");
        from.setId(Long.parseLong(attributes.get(1)));
        List<User> to = new ArrayList<>();
        for (String user : attributes.get(2).split(",")){
            User userTo = new User("","");
            userTo.setId(Long.parseLong(user));
            to.add(userTo);
        }
        Message message = new Message(from,to,attributes.get(3), LocalDateTime.parse(attributes.get(4)));
        message.setId(Long.parseLong(attributes.get(0)));
        if(attributes.size() == 6){
            Message reply = new Message(null,null,null,null);
            reply.setId(Long.parseLong(attributes.get(5)));
            message.setReplyMessage(reply);
        }
        return message;
    }

    @Override
    protected String createEntityAsString(Message entity) {
        String to = "";
        for (User user : entity.getTo())
            to += user.getId().toString() + ",";
        to = to.substring(0,to.length() - 1);
        if(entity.getReplyMessage() == null)
            return entity.getId().toString() + ";" + entity.getFrom().getId().toString() + ";" +
                to + ";" + entity.getMessage() + ";" + entity.getData().toString();
        else
            return entity.getId().toString() + ";" + entity.getFrom().getId().toString() + ";" +
                    to + ";" + entity.getMessage() + ";" + entity.getData().toString() + ";"
                    + entity.getReplyMessage().getId().toString();
    }
}
