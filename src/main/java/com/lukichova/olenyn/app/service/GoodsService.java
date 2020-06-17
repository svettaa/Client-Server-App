package com.lukichova.olenyn.app.service;

import com.lukichova.olenyn.app.DB.*;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchIdException;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchNameException;
import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;
import com.lukichova.olenyn.app.Exceptions.wrongNotUniqueValue;

import java.util.List;

public class GoodsService {

    private final GoodsDao goodsDao = new GoodsDao();

    public Goods listByCriteria(Integer id) throws noItemWithSuchIdException, wrongDataBaseConnection {
        return goodsDao.getById(id);
    }

    public Goods listByCriteria(String name) throws noItemWithSuchNameException, wrongDataBaseConnection {
        return goodsDao.getByName(name);
    }

    public List<Goods> getByGroupId(Integer group_id) throws noItemWithSuchIdException, wrongDataBaseConnection {
        return goodsDao.getByGroupId(group_id);
    }

    public List<Goods> getAll() throws wrongDataBaseConnection {
        return goodsDao.readAll();
    }

    public boolean create(Goods goods) throws wrongNotUniqueValue, wrongDataBaseConnection {
        return goodsDao.create(goods);
    }

    public boolean update(Goods goods) throws wrongNotUniqueValue, wrongDataBaseConnection {
        return goodsDao.update(goods);
    }

    public boolean delete(int id) throws wrongDataBaseConnection {
        return goodsDao.delete(id);
    }

    public void deleteByGroupId(int group_id) throws noItemWithSuchIdException, wrongDataBaseConnection {
        goodsDao.deleteAllByGroupId(group_id);
    }

    public void deleteAll() throws wrongDataBaseConnection {
        goodsDao.deleteAll();
    }

}
