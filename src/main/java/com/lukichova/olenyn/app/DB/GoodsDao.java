package com.lukichova.olenyn.app.DB;

import com.lukichova.olenyn.app.Exceptions.noItemWithSuchIdException;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchNameException;
import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.lukichova.olenyn.app.resoures.Resoures.GOODS_TABLE;

public class GoodsDao implements Dao<Goods> {

    @Override
    public Goods getById(int id) throws wrongDataBaseConnection, noItemWithSuchIdException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "SELECT * FROM " + GOODS_TABLE + " WHERE " + id + " = ?";
            System.out.println("getById() invoked");

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return createGoods(rs);
            } else {
                throw new noItemWithSuchIdException();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new wrongDataBaseConnection();
        } finally {
            close(connection);
        }
    }

    private Goods createGoods(ResultSet rs) throws SQLException {
        Goods g = new Goods();
        g.setId(rs.getInt("id"));
        g.setName(rs.getString("name"));
        g.setPrice(rs.getBigDecimal("price"));
        g.setLeft_amount(rs.getInt("left_amount"));
        g.setProducer(rs.getString("producer"));
        g.setDescription(rs.getString("description"));
        g.setGroup_id(rs.getInt("group_id"));

        return g;
    }

    @Override
    public Goods getByName(String name) throws wrongDataBaseConnection, noItemWithSuchNameException {
        try {
            Connection connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "SELECT * FROM " + GOODS_TABLE + " WHERE " + name + " = ?";
            System.out.println("getByName() invoked");
            List<Goods> list = new ArrayList<Goods>();

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            close(connection);
            if (rs.next()) {
                return createGoods(rs);
            } else {
                throw new noItemWithSuchNameException();
            }

        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
    }

    @Override
    public List<Goods> readAll() throws wrongDataBaseConnection {
        List<Goods> list = new ArrayList<Goods>();
        try {
            Connection connection = DriverManager.getConnection(DataBase.url);
            String sql = "SELECT * FROM " + GOODS_TABLE;
            System.out.println("readAll() invoked");


            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(createGoods(rs));
            }
            close(connection);
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
        return list;
    }

    @Override
    public boolean create(Goods goods) throws wrongDataBaseConnection {
        System.out.println("create() invoked");
        try {
            Connection connection = DriverManager.getConnection(DataBase.url);

            if (goods.getId() == null) {
                String sqlQuery = "INSERT INTO " + GOODS_TABLE +
                        " (name, price, left_amount, producer, description, group_id) " +
                        " VALUES (?, ?, ?, ?, ?, ?)";
                System.out.println("create() invoked");
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

                preparedStatement.setString(1, goods.getName());
                preparedStatement.setBigDecimal(2, goods.getPrice());
                preparedStatement.setInt(3, goods.getLeft_amount());
                preparedStatement.setString(4, goods.getProducer());
                preparedStatement.setString(5, goods.getDescription());
                preparedStatement.setInt(6, goods.getGroup_id());

                preparedStatement.executeUpdate();
            } else {
                String sqlQuery = "INSERT INTO " + GOODS_TABLE +
                        " (id, name, price, left_amount, producer, description, group_id) " +
                        " VALUES (?, ?, ?, ?, ?, ?, ?)";
                System.out.println("create() invoked");
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

                preparedStatement.setInt(1, goods.getId());
                preparedStatement.setString(2, goods.getName());
                preparedStatement.setBigDecimal(3, goods.getPrice());
                preparedStatement.setInt(4, goods.getLeft_amount());
                preparedStatement.setString(5, goods.getProducer());
                preparedStatement.setString(6, goods.getDescription());
                preparedStatement.setInt(7, goods.getGroup_id());

                preparedStatement.executeUpdate();
            }


            System.out.println("Inserted " + goods.getName() + " " + goods.getPrice() + " "
                    + goods.getLeft_amount() + " " + goods.getProducer() + " " + goods.getDescription());
            System.out.println();
            close(connection);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new wrongDataBaseConnection();
        }
        return true;
    }

    @Override
    public boolean update(Goods goods) throws wrongDataBaseConnection {
        try {
            Connection connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "UPDATE " + GOODS_TABLE + " " +
                    "SET name = ?, " +
                    "price = ?, " +
                    "left_amount = ?," +
                    "producer = ?," +
                    "description = ?," +
                    "group_id = ? WHERE id = ?";
            System.out.println("update() invoked");

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, goods.getName());
            preparedStatement.setBigDecimal(2, goods.getPrice());
            preparedStatement.setInt(3, goods.getLeft_amount());
            preparedStatement.setString(4, goods.getProducer());
            preparedStatement.setString(5, goods.getDescription());
            preparedStatement.setInt(6, goods.getGroup_id());
            preparedStatement.setInt(7, goods.getId());


            preparedStatement.executeUpdate();

            System.out.println("Updated " + goods.getName() + " " + goods.getPrice() + " "
                    + goods.getLeft_amount() + " " + goods.getProducer() + " " + goods.getDescription());
            System.out.println();
            close(connection);
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
        return true;
    }

    @Override
    public boolean delete(int id) throws wrongDataBaseConnection {
        try {
            Connection connection = DriverManager.getConnection(DataBase.url);
            String sql = "DELETE FROM " + GOODS_TABLE + " WHERE id = ?";
            System.out.println("delete() invoked");

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            System.out.println("Deleted " + id);
            System.out.println();
            close(connection);
        } catch (SQLException e) {
            throw new wrongDataBaseConnection();
        }
        return true;
    }

    @Override
    public void deleteAll() throws wrongDataBaseConnection {
        try {
            Connection connection = DriverManager.getConnection(DataBase.url);
            String sql = "DELETE FROM " + GOODS_TABLE;
            System.out.println("deleteAll() invoked");

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            close(connection);
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
    }

    public void close(Connection connection) {
        try {
            connection.close();

            System.out.println("Connection closed");
            System.out.println();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}