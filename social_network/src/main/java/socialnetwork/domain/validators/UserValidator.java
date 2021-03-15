package socialnetwork.domain.validators;

import socialnetwork.domain.User;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User entity) throws ValidationException {
        String erori = "";
        if(entity.getFirstName().equals(""))
            erori = erori + "First name can't be null!\n";
        if(entity.getFirstName().matches(".*\\d.*"))
            erori = erori + "First name can't contain numbers!\n";
        if(entity.getLastName().equals(""))
            erori = erori + "Last name can't be null!\n";
        if(entity.getLastName().matches(".*\\d.*"))
            erori = erori + "Last name can't contain numbers!\n";
        if(erori.length() > 0)
            throw new ValidationException(erori);
    }
}
