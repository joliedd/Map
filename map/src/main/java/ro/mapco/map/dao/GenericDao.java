package ro.mapco.map.dao;

/**
 * Created by juliedd on 6/1/2016.
 */

import java.util.List;

public interface GenericDao<E, K> {

    void add(E entity);

    void update(E entity);

    void remove(E entity);

    E find(K key);

    List<E> list();

}
