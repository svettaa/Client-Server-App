package com.lukichova.olenyn.app.DB;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> read(long id);

    List<T> readAll();

    void create(String[] params);

    void update(T t, String[] params);

    void delete(T t);

    void listByCriteria(T t);

}