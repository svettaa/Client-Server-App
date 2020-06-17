package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.DB.DataBase;
import com.lukichova.olenyn.app.DB.Goods;
import com.lukichova.olenyn.app.DB.GoodsDao;
import com.lukichova.olenyn.app.DB.GroupDao;
import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;
import com.lukichova.olenyn.app.Exceptions.wrongNotUniqueValue;
import com.lukichova.olenyn.app.service.GoodsService;
import com.lukichova.olenyn.app.service.GroupService;
import org.junit.Test;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.lukichova.olenyn.app.resoures.Resoures.GROUP_TABLE;

public class TestDBExceptions {

    GoodsService goodsService = new GoodsService();

    @Test(expected = wrongDataBaseConnection.class)
    public void testWrongConnection() throws Exception {

        try {
            Connection connection = DriverManager.getConnection(DataBase.url);
            String sql = " SELECT " + GROUP_TABLE;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
    }

    @Test(expected = wrongNotUniqueValue.class)
    public void testWrongNotUniqueValue() throws Exception {
        Goods gg = new Goods(1, "rice", new BigDecimal("33"), 333, "Rice",
                "Description", 2);
        Goods gg1 = new Goods(1, "rice", new BigDecimal("33"), 333, "Rice",
                "Description", 2);
        GoodsDao gd = new GoodsDao();
        goodsService.create(gg);
        goodsService.create(gg1);
    }
}


