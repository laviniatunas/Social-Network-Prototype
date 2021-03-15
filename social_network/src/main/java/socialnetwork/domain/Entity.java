package socialnetwork.domain;

import socialnetwork.domain.validators.ValidationException;

import java.io.Serializable;

public class Entity<ID> implements Serializable {

    private static final long serialVersionUID = 7331115341259248461L;
    private ID id;

    /**
     *@return id of an entity
     **/
    public ID getId() {
        return id;
    }

    /**
     * @param id
     * sets id of an entity
     **/
    public void setId(ID id) {
        this.id = id;
    }

    /**
     *
     * @return entity as string
     */
    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}