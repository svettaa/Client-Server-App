package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.DB.Goods;
import com.lukichova.olenyn.app.DB.GoodsDao;
import com.lukichova.olenyn.app.DB.Group;
import com.lukichova.olenyn.app.DB.GroupDao;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchIdException;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchNameException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

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

        Group[] groups = new Group[]{
                new Group(1, "drinks", "a lot of drinks"),
                new Group(2, "dairy products", "so many dairy products"),
                new Group(18, "unnecessary", "unnecessary")

        };
        groupDao.create(groups[0]);
        groupDao.create(groups[1]);
        groupDao.create(groups[2]);

        List<Group> allGroups = groupDao.readAll();
        Assert.assertArrayEquals(groups, allGroups.toArray());


        Assert.assertEquals(groups[0], groupDao.getByName("drinks"));
        Assert.assertEquals(groups[1], groupDao.getById(2));


        Goods[] goods = new Goods[]{
                new Goods(1, "fanta", new BigDecimal("25"), 50, "Coca Cola Ukraine", "best fanta", 1),
                new Goods(2, "milk", new BigDecimal("20"), 35, "Yagotinske", "best milk", 2),
                new Goods(26, "kefir", new BigDecimal("10"), 200, "Slovianochka", "best kefir", 2)
        };

        goodsDao.create(goods[0]);
        goodsDao.create(goods[1]);
        goodsDao.create(goods[2]);

        List<Goods> allGoods = goodsDao.readAll();
        Assert.assertArrayEquals(goods, allGoods.toArray());

        Assert.assertEquals(goods[1], goodsDao.getByName("milk"));
        Assert.assertEquals(goods[2], goodsDao.getById(26));

        // Update group

        Group newGroup = new Group(18, "bread", "a lot of bread");
        Assert.assertTrue(groupDao.update(newGroup));
        Assert.assertEquals(newGroup, groupDao.getById(18));

        Assert.assertTrue(groupDao.delete(18));
        try {
            groupDao.getById(18);
            Assert.assertTrue(false);
        } catch (noItemWithSuchIdException e) {
            Assert.assertTrue(true);
        }

        try {
            groupDao.getByName("jbenjbeng");
            Assert.assertTrue(false);
        } catch (noItemWithSuchNameException e) {
            Assert.assertTrue(true);
        }

        // Update goods

        Goods newGoods = new Goods(1, "COCA COLA", new BigDecimal("15"), 99, "Coca Cola Ukraine", "best coca cola", 1);
        Assert.assertTrue(goodsDao.update(newGoods));
        Assert.assertEquals(newGoods, goodsDao.getById(1));

        Assert.assertTrue(goodsDao.delete(1));
        try {
            goodsDao.getById(1);
            Assert.assertTrue(false);
        } catch (noItemWithSuchIdException e) {
            Assert.assertTrue(true);
        }

        try {
            goodsDao.getByName("jbenjbeng");
            Assert.assertTrue(false);
        } catch (noItemWithSuchNameException e) {
            Assert.assertTrue(true);
        }


    }


}
