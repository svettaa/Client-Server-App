package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.DB.Goods;
import com.lukichova.olenyn.app.DB.GoodsDao;
import com.lukichova.olenyn.app.DB.Group;
import com.lukichova.olenyn.app.DB.GroupDao;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchNameException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class TestDB {

    private GoodsDao goodsDao = new GoodsDao();
    private GroupDao groupDao = new GroupDao();

    @Before
    public void before() throws Exception {
        goodsDao.deleteAll();
        groupDao.deleteAll();
    }

    @Test
    public void testGroupAndGoods() throws Exception, noItemWithSuchNameException {
        final int id = 1;
        //test groups
        Group group1 = new Group(id, "beverages", "so many drinks");
        Assert.assertTrue(groupDao.create(group1));

        Group group2 = new Group(id+1, "dairy products", "so many dairy products");
        Assert.assertTrue(groupDao.create(group2));

        Group group3 = new Group("unnecessary", "unnecessary");
        Assert.assertTrue(groupDao.create(group3));

        Group get = groupDao.getById(id);
        Assert.assertEquals(group1, get);

        group1 = new Group(id, "bread", "a lot of bread");
        Assert.assertTrue(groupDao.update(group1));

        /*Group get2 = groupDao.getByName("dairyproducts");
        Assert.assertEquals(group2, get2);*/

        Assert.assertTrue(groupDao.delete(id+2));

        groupDao.readAll();

        //test goods
        BigDecimal priceFor1 = new BigDecimal(20.0);
        Goods goods1 = new Goods(1, "cola", priceFor1, 100, "Coca Cola Ukraine", "best cola", 1);
        Assert.assertTrue(goodsDao.create(goods1));

        BigDecimal priceFor2 = new BigDecimal(25.0);
        Goods goods2 = new Goods(id+1, "fanta", priceFor1, 50, "Coca Cola Ukraine", "best fanta", 1);
        Assert.assertTrue(goodsDao.create(goods2));

        BigDecimal priceFor3 = new BigDecimal(20.0);
        Goods goods3 = new Goods(id+2, "milk", priceFor1, 35, "Yagotinske", "best milk", 2);
        Assert.assertTrue(goodsDao.create(goods3));

        BigDecimal priceFor4 = new BigDecimal(20.0);
        Goods goods4 = new Goods("kefir", priceFor1, 200, "Slovianochka", "best kefir", 2);
        Assert.assertTrue(goodsDao.create(goods4));

        Goods getGoods1 = goodsDao.getById(id);
        Assert.assertEquals(goods1, getGoods1);

        BigDecimal priceForUpdated = new BigDecimal(20.0);
        Goods goodsUpdated = new Goods(id, "COCA COLA", priceForUpdated, 99, "Coca Cola Ukraine", "best coca cola", 1);
        Assert.assertTrue(goodsDao.update(goodsUpdated));

        goodsDao.readAll();

        Assert.assertTrue(goodsDao.delete(id+1));

        goodsDao.readAll();
    }


}
