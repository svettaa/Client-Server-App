package com.lukichova.olenyn.app.DB;

import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;

import java.util.List;

public interface Dao<T> {

    T getById(int id) throws wrongDataBaseConnection;

    T getByName(String name) throws wrongDataBaseConnection;

    List<T> readAll() throws wrongDataBaseConnection;

    boolean create(T t) throws wrongDataBaseConnection;

    boolean update(T t) throws wrongDataBaseConnection;

    boolean delete(int id) throws wrongDataBaseConnection;

    void deleteAll() throws wrongDataBaseConnection;
}