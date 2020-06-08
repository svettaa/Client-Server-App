package com.lukichova.olenyn.app;

import com.lukichova.olenyn.app.DB.DataBase;
import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;
import org.junit.Test;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.lukichova.olenyn.app.resoures.Resoures.GROUP_TABLE;

public class TestDBExceptions {

        @Test(expected = wrongDataBaseConnection.class)
        public void testWrongConnection() throws Exception {

            try {
                Connection connection = DriverManager.getConnection(DataBase.url);
                String sql = "  " + GROUP_TABLE;
                System.out.println("deleteAll() invoked");
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();

            } catch (SQLException sqlException) {
                throw new wrongDataBaseConnection();
            }

        }
    }


