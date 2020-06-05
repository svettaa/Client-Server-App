package com.lukichova.olenyn.app.DB;

import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    T getById(int id) throws wrongDataBaseConnection;

    T getByName(String name) throws wrongDataBaseConnection;

    List<T> readAll() throws wrongDataBaseConnection;

    void create(T t) throws wrongDataBaseConnection;

    void update(T t) throws wrongDataBaseConnection;

    void delete(int id) throws wrongDataBaseConnection;
}