package com.lukichova.olenyn.app.service;

import com.lukichova.olenyn.app.DB.*;
import com.lukichova.olenyn.app.Exceptions.*;

import java.math.BigDecimal;
import java.util.List;

public class GoodsService {

    private final GoodsDao goodsDao = new GoodsDao();

    public Goods listByCriteria(Integer id) throws noItemWithSuchIdException, wrongDataBaseConnection {
        return goodsDao.getById(id);
    }

    public Goods listByCriteria(String name) throws noItemWithSuchNameException, wrongDataBaseConnection {
        return goodsDao.getByName(name);
    }

    public List<Goods> searchByName(String name) throws noItemWithSuchIdException, wrongDataBaseConnection, noItemWithSuchNameException {
        return goodsDao.searchByName(name);
    }
    public List<Goods> searchGoodsByGroup(String name) throws noItemWithSuchIdException, wrongDataBaseConnection, noItemWithSuchNameException {
        return goodsDao.searchGoodsByGroup(name);
    }
    public List<Goods> getByGroupId(Integer group_id) throws noItemWithSuchIdException, wrongDataBaseConnection {
        return goodsDao.getByGroupId(group_id);
    }
 public Integer gettotalPrice() throws wrongDataBaseConnection {
        return goodsDao.totalPrice();
 }
    public Integer getGroupTotalPrice(int id) throws wrongDataBaseConnection {

        return goodsDao.totalGroupPrice(id);
    }
    public List<Goods> getAll() throws wrongDataBaseConnection {
        return goodsDao.readAll();
    }

    public boolean create(Goods goods) throws wrongNotUniqueValue, wrongDataBaseConnection, WrongJsonInputData {
        if (goods.getName() == null || goods.getProducer() == null ||
                goods.getLeft_amount() < 0 || goods.getLeft_amount() == null || goods.getPrice() == null ||
                (goods.getPrice().compareTo(new BigDecimal("0.00")) < 0) || goods.getGroup_id() == null ||
                goods.getGroup_id() < 1){
            throw new WrongJsonInputData();
        }
        return goodsDao.create(goods);
    }

    public boolean update(Goods goods) throws wrongDataBaseConnection, WrongJsonInputData, noItemWithSuchIdException, wrongNotUniqueValue {
//        if (goods.getId() == null || goods.getName() == null || goods.getProducer() == null ||
//                goods.getLeft_amount() < 0 || goods.getLeft_amount() == null || goods.getPrice() == null ||
//                (goods.getPrice().compareTo(new BigDecimal("0.00")) < 0) || goods.getGroup_id() == null ||
//                goods.getGroup_id() < 1){
//            throw new WrongJsonInputData();
//        }


        return goodsDao.update(goods);
    }
    public boolean writeOffAmount(Goods goods) throws wrongDataBaseConnection, WrongJsonInputData, noItemWithSuchIdException, wrongNotUniqueValue {
//        if (goods.getId() == null || goods.getName() == null || goods.getProducer() == null ||
//                goods.getLeft_amount() < 0 || goods.getLeft_amount() == null || goods.getPrice() == null ||
//                (goods.getPrice().compareTo(new BigDecimal("0.00")) < 0) || goods.getGroup_id() == null ||
//                goods.getGroup_id() < 1){
//            throw new WrongJsonInputData();
//        }


        return goodsDao.writeOffAmount(goods);
    }
    public boolean addAmount(Goods goods) throws wrongDataBaseConnection, WrongJsonInputData, noItemWithSuchIdException, wrongNotUniqueValue {
//        if (goods.getId() == null || goods.getName() == null || goods.getProducer() == null ||
//                goods.getLeft_amount() < 0 || goods.getLeft_amount() == null || goods.getPrice() == null ||
//                (goods.getPrice().compareTo(new BigDecimal("0.00")) < 0) || goods.getGroup_id() == null ||
//                goods.getGroup_id() < 1){
//            throw new WrongJsonInputData();
//        }


        return goodsDao.addAmount(goods);
    }
    public boolean delete(int id) throws wrongDataBaseConnection, noItemWithSuchIdException {
        return goodsDao.delete(id);
    }

    public void deleteByGroupId(int group_id) throws noItemWithSuchIdException, wrongDataBaseConnection {
        goodsDao.deleteAllByGroupId(group_id);
    }

    public void deleteAll() throws wrongDataBaseConnection {
        goodsDao.deleteAll();
    }

}
