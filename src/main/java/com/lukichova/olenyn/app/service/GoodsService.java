package com.lukichova.olenyn.app.service;

import com.lukichova.olenyn.app.DB.*;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchIdException;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchNameException;
import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;
import com.lukichova.olenyn.app.Exceptions.wrongNotUniqueValue;

import java.util.List;

public class GoodsService {

    private final Dao<Goods> dao;

    public GoodsService(GoodsDao goodsDao){
        dao = new GoodsDao();
    }

    public Goods getGoods(Integer id) throws noItemWithSuchIdException, wrongDataBaseConnection {
        return dao.getById(id);
    }

    public Goods getGoods(String name) throws noItemWithSuchNameException, wrongDataBaseConnection {
        return dao.getByName(name);
    }

    public List<Goods> getAll() throws wrongDataBaseConnection {
        return dao.readAll();
    }

    public boolean create(Goods goods) throws wrongNotUniqueValue, wrongDataBaseConnection {
        return dao.create(goods);
    }

    public boolean update(Goods goods) throws wrongNotUniqueValue, wrongDataBaseConnection {
        return dao.update(goods);
    }

    public boolean delete(int id) throws wrongDataBaseConnection {
        return dao.delete(id);
    }

    public void deleteAll() throws wrongDataBaseConnection {
        dao.deleteAll();
    }

}
