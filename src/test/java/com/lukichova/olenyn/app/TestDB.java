package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.DB.GoodsDao;
import com.lukichova.olenyn.app.DB.Group;
import com.lukichova.olenyn.app.DB.GroupDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestDB {

    private GoodsDao goodsDao;
    private GroupDao groupDao;

    @Before
    public void before() throws Exception {
        goodsDao.deleteAll();
        groupDao.deleteAll();
    }

    @Test
    public void testCategoryCRUD() throws Exception {
        final int id = 1;

        Group group = new Group(id, "drinks", "so many drinks");
        Assert.assertTrue(groupDao.create(group));

        Group get = groupDao.getById(id);
        Assert.assertEquals(group, get);

        group = new Group(id, "bread", "a lot of bread");
        Assert.assertTrue(groupDao.update(group));

        Group get2 = groupDao.getById(id);
        Assert.assertEquals(group, get2);

        Assert.assertTrue(groupDao.delete(id));

        //groupDao.readAll();
    }


}
