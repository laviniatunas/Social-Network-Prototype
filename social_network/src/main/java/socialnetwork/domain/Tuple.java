package socialnetwork.domain;

import java.util.Objects;


/**
 * Define a Tuple o generic type entities
 * @param <E1> - tuple first entity type
 * @param <E2> - tuple second entity type
 */
public class Tuple<E1, E2> {
    private E1 e1;
    private E2 e2;

    /**
     *
     * @param e1
     * @param e2
     * creates a tuple with the given entities
     */
    public Tuple(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     *
     * @return the left entity of the tuple
     */
    public E1 getLeft() {
        return e1;
    }

    /**
     *
     * @param e1
     * sets the left entity to the given entity
     */
    public void setLeft(E1 e1) {
        this.e1 = e1;
    }

    /**
     *
     * @return the rioght entity of the tuple
     */
    public E2 getRight() {
        return e2;
    }

    /**
     *
     * @param e2
     * sets the right entity to the given entity
     */
    public void setRight(E2 e2) {
        this.e2 = e2;
    }

    /**
     *
     * @return the entities of the tuple as a string
     */
    @Override
    public String toString() {
        return "" + e1 + "," + e2;

    }

    /**
     *
     * @param obj
     * @return true, if two tuples are the same
     *          false, otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return this.e1.equals(((Tuple) obj).e1) && this.e2.equals(((Tuple) obj).e2);
    }

    /**
     *
     * @return hashCode of a tuple
     */
    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }
}