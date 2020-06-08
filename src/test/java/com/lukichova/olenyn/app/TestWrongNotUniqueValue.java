package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.DB.Goods;
import com.lukichova.olenyn.app.DB.GoodsDao;
import com.lukichova.olenyn.app.Exceptions.wrongNotUniqueValue;
import org.junit.Test;

import java.math.BigDecimal;

public class TestWrongNotUniqueValue {

    @Test(expected = wrongNotUniqueValue.class)
    public void testWrongConnection() throws Exception {
        int id=0;
        //BigDecimal d

        Goods gg = new Goods(1,"son", new BigDecimal("33"),333,"kdkd",
                "sjdncjs",2);
        Goods gg1 = new Goods(1,"son", new BigDecimal("33"),333,"kdkd",
                "sjdncjs",2);
        GoodsDao gd = new GoodsDao();
        gd.create(gg);
        gd.create(gg1);


    }
}
