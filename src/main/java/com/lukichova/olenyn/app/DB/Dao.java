package com.lukichova.olenyn.app.DB;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<Goods> read(long id);

    List<Goods> readAll();

    void create(String[] params);

    void update(T t, String[] params);

    void delete(T t);

    List<Goods> listByCriteria(String[] params);
}