package com.lukichova.olenyn.app.DB;

import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<Goods> read(long id);

    List<Goods> readAll() throws wrongDataBaseConnection;

    void create(String[] params);

    void update(T t, String[] params);

    void delete(T t);

    void listByCriteria(T t);

}