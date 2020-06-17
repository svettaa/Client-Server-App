package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.DB.Goods;
import com.lukichova.olenyn.app.DB.GoodsDao;
import com.lukichova.olenyn.app.DB.Group;
import com.lukichova.olenyn.app.DB.GroupDao;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchIdException;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchNameException;
import com.lukichova.olenyn.app.service.GoodsService;
import com.lukichova.olenyn.app.service.GroupService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class TestDB {

    GroupService groupService = new GroupService();
    GoodsService goodsService = new GoodsService();

    @Before
    public void before() throws Exception {
        goodsService.deleteAll();
        groupService.deleteAll();
    }

    @Test
    public void testGroupAndGoods() throws Exception, noItemWithSuchNameException {

        Group[] groups = new Group[]{
                new Group(1, "drinks", "a lot of drinks"),
                new Group(2, "dairy products", "so many dairy products"),
                new Group(18, "unnecessary", "unnecessary")

        };
        groupService.create(groups[0]);
        groupService.create(groups[1]);
        groupService.create(groups[2]);

        List<Group> allGroups = groupService.getAll();
        Assert.assertArrayEquals(groups, allGroups.toArray());


        Assert.assertEquals(groups[0], groupService.listByCriteria("drinks"));
        Assert.assertEquals(groups[1], groupService.listByCriteria(2));


        Goods[] goods = new Goods[]{
                new Goods(1, "fanta", new BigDecimal("25"), 50, "Coca Cola Ukraine", "best fanta", 1),
                new Goods(2, "milk", new BigDecimal("20"), 35, "Yagotinske", "best milk", 2),
                new Goods(26, "kefir", new BigDecimal("10"), 200, "Slovianochka", "best kefir", 2)
        };

        goodsService.create(goods[0]);
        goodsService.create(goods[1]);
        goodsService.create(goods[2]);

        List<Goods> allGoods = goodsService.getAll();
        Assert.assertArrayEquals(goods, allGoods.toArray());

        Assert.assertEquals(goods[1], goodsService.listByCriteria("milk"));
        Assert.assertEquals(goods[2], goodsService.listByCriteria(26));

        // Update group
        Group newGroup = new Group(18, "bread", "a lot of bread");
        Assert.assertTrue(groupService.update(newGroup));
        Assert.assertEquals(newGroup, groupService.listByCriteria(18));

        Assert.assertTrue(groupService.delete(18));
        try {
            groupService.listByCriteria(18);
            Assert.assertTrue(false);
        } catch (noItemWithSuchIdException e) {
            Assert.assertTrue(true);
        }

        try {
            groupService.listByCriteria("jbenjbeng");
            Assert.assertTrue(false);
        } catch (noItemWithSuchNameException e) {
            Assert.assertTrue(true);
        }

        // Update goods

        Goods newGoods = new Goods(1, "COCA COLA", new BigDecimal("15"), 99, "Coca Cola Ukraine", "best coca cola", 1);
        Assert.assertTrue(goodsService.update(newGoods));
        Assert.assertEquals(newGoods, goodsService.listByCriteria(1));

        Assert.assertTrue(goodsService.delete(1));
        try {
            goodsService.listByCriteria(1);
            Assert.assertTrue(false);
        } catch (noItemWithSuchIdException e) {
            Assert.assertTrue(true);
        }

        try {
            goodsService.listByCriteria("jbenjbeng");
            Assert.assertTrue(false);
        } catch (noItemWithSuchNameException e) {
            Assert.assertTrue(true);
        }


    }


}
