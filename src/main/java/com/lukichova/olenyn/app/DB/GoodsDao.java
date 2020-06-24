package com.lukichova.olenyn.app.DB;

import com.lukichova.olenyn.app.Exceptions.noItemWithSuchIdException;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchNameException;
import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;
import com.lukichova.olenyn.app.Exceptions.wrongNotUniqueValue;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.lukichova.olenyn.app.resoures.Resoures.GOODS_TABLE;
import static com.lukichova.olenyn.app.resoures.Resoures.GROUP_TABLE;

public class GoodsDao {
    public Goods getById(int id) throws wrongDataBaseConnection, noItemWithSuchIdException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "SELECT * FROM " + GOODS_TABLE + " WHERE id = ?";
            System.out.println("getById() invoked");

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();



            if (rs.next()) {

                return createGoods(rs);
            } else {

                throw new noItemWithSuchIdException();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();

            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement, rs);
        }
    }
    public int getLeftAmountById(int id) throws wrongDataBaseConnection, noItemWithSuchIdException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "SELECT * FROM " + GOODS_TABLE + " WHERE id = ?";
            System.out.println("getLeftAmountById() invoked");

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();



            if (rs.next()) {
                 Goods goods =createGoods(rs);
                return goods.getLeft_amount();
            } else {
                throw new noItemWithSuchIdException();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();

            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement, rs);
        }
    }
    public List<Goods> searchByName(String name) throws wrongDataBaseConnection, noItemWithSuchIdException {
        List<Goods> list = new ArrayList<Goods>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sql = "SELECT * FROM " + GOODS_TABLE+
                    " WHERE name LIKE ? " ;
            System.out.println("searchByName() invoked");


            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, '%' + name + '%');
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                list.add(createGoods(rs));
                System.out.println(createGoods(rs));
            }

            rs.close();
            return list;
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement);
        }
    }
    //загальна вартість товару на складі (кількість * на ціну),


    public Integer totalPrice() throws wrongDataBaseConnection {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "SELECT SUM(price*left_amount) FROM " + GOODS_TABLE ;
            System.out.println("totalPrice() invoked");

            preparedStatement = connection.prepareStatement(sqlQuery);
         //   preparedStatement.setString(1, name);
            rs = preparedStatement.executeQuery();
            int amount = rs.getInt(1);
           return amount;
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement, rs);
        }
    }
    public Integer totalGroupPrice(int id) throws wrongDataBaseConnection {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {

            connection = DriverManager.getConnection(DataBase.url);

            String sqlQuery = "SELECT SUM(price*left_amount) FROM " + GOODS_TABLE +" WHERE group_id = ?";


            System.out.println("totalGroupPrice() invoked");
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();
            int amount = rs.getInt(1);
            return amount;
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement, rs);
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
    public List<Goods> searchGoodsByGroup(String name) throws wrongDataBaseConnection, noItemWithSuchIdException {
        List<Goods> list = new ArrayList<Goods>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sql = "SELECT * FROM " + GOODS_TABLE+
                                " WHERE group_id IN" +
              " (SELECT id FROM " +GROUP_TABLE+" WHERE name LIKE ? )" ;
            System.out.println("readAll() invoked");


            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, '%' + name + '%');
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(createGoods(rs));
                System.out.println(createGoods(rs));
            }
            rs.close();
            return list;
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement);
        }
    }


    public List<Goods> getByGroupId(int group_id) throws wrongDataBaseConnection, noItemWithSuchIdException {
        List<Goods> list = new ArrayList<Goods>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "SELECT * FROM " + GOODS_TABLE + " WHERE group_id = ?";
            System.out.println("getByGroupId() invoked");

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, group_id);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                list.add(createGoods(rs));
            } else {
                throw new noItemWithSuchIdException();
            }
            rs.close();
            return list;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement, rs);
        }
    }

    public Goods getByName(String name) throws wrongDataBaseConnection, noItemWithSuchNameException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "SELECT * FROM " + GOODS_TABLE + " WHERE name = ?";
            System.out.println("getByName() invoked");
            List<Goods> list = new ArrayList<Goods>();

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, name);
             rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return createGoods(rs);
            } else {
                throw new noItemWithSuchNameException();
            }
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement, rs);
        }
    }

    public List<Goods> readAll() throws wrongDataBaseConnection {
        List<Goods> list = new ArrayList<Goods>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sql = "SELECT * FROM " + GOODS_TABLE;
            System.out.println("readAll() invoked");


            preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(createGoods(rs));
                System.out.println(createGoods(rs));
            }
            rs.close();
            return list;
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement);
        }
    }

    public boolean create(Goods goods) throws wrongDataBaseConnection, wrongNotUniqueValue {
        System.out.println("create() invoked");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);

            if (goods.getId() == null) {
                String sqlQuery = "INSERT INTO " + GOODS_TABLE +
                        " (name, price, left_amount, producer, description, group_id) " +
                        " VALUES (?, ?, ?, ?, ?, ?)";
                System.out.println("create() invoked");
                preparedStatement = connection.prepareStatement(sqlQuery);

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
                preparedStatement = connection.prepareStatement(sqlQuery);

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
            return true;
        } catch (SQLiteException e) {
            throw new wrongNotUniqueValue();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement);
        }
    }

    public boolean update(Goods goods) throws wrongDataBaseConnection, noItemWithSuchIdException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Goods g = new Goods();
        g=getById(goods.getId());

        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "UPDATE " + GOODS_TABLE + " " +
                    "SET name = ?, " +
                    "price = ?, " +

                    "producer = ?," +
                    "description = ?," +
                    "group_id = ? WHERE id = ?";
            System.out.println("update() invoked");

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, goods.getName());
            preparedStatement.setBigDecimal(2, goods.getPrice());

            preparedStatement.setString(3, goods.getProducer());
            preparedStatement.setString(4, goods.getDescription());
            preparedStatement.setInt(5, goods.getGroup_id());
            preparedStatement.setInt(6, goods.getId());


            preparedStatement.executeUpdate();

            System.out.println("Updated " + goods.getName() + " " + goods.getPrice() + " "
                    + goods.getLeft_amount() + " " + goods.getProducer() + " " + goods.getDescription());
            System.out.println();
            return true;
        } catch (SQLiteException e) {

            throw new noItemWithSuchIdException();

        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement);
        }
    }
    public boolean writeOffAmount(Goods goods) throws wrongDataBaseConnection, noItemWithSuchIdException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Goods g = new Goods();
        g=getById(goods.getId());
        int amount =  getLeftAmountById(goods.getId())- goods.getLeft_amount();
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "UPDATE " + GOODS_TABLE + " " +
                    "SET left_amount = "+ amount+" WHERE id = ?";
            System.out.println("update() invoked");

            preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, goods.getId());

            preparedStatement.executeUpdate();

            System.out.println("Updated " + goods.getName() + " " + goods.getPrice() + " "
                    + goods.getLeft_amount() + " " + goods.getProducer() + " " + goods.getDescription());
            System.out.println();
            return true;
        } catch (SQLiteException e) {

            throw new noItemWithSuchIdException();

        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement);
        }
    }
    public boolean addAmount(Goods goods) throws wrongDataBaseConnection, noItemWithSuchIdException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Goods g = new Goods();
        g=getById(goods.getId());
        int amount =  getLeftAmountById(goods.getId())+ goods.getLeft_amount();
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "UPDATE " + GOODS_TABLE + " " +
                    "SET left_amount = "+ amount+" WHERE id = ?";
            System.out.println("update() invoked");

            preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, goods.getId());

            preparedStatement.executeUpdate();

            System.out.println("Updated " + goods.getName() + " " + goods.getPrice() + " "
                    + goods.getLeft_amount() + " " + goods.getProducer() + " " + goods.getDescription());
            System.out.println();
            return true;
        } catch (SQLiteException e) {

            throw new noItemWithSuchIdException();

        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement);
        }
    }

    public boolean delete(int id) throws wrongDataBaseConnection, noItemWithSuchIdException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        Goods test = getById(id);
        test.getName();



        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sql = "DELETE FROM " + GOODS_TABLE + " WHERE id = ?";
            System.out.println("delete() invoked");

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            System.out.println("Deleted " + id);
            return true;
        } catch (SQLException e) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement);
        }
    }

    public void deleteAllByGroupId(int group_id) throws wrongDataBaseConnection, noItemWithSuchIdException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sql = "DELETE FROM " + GOODS_TABLE + " WHERE group_id = ?";
            System.out.println("deleteAllByGroupId() invoked");

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, group_id);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement, rs);
        }
    }

    public void deleteAll() throws wrongDataBaseConnection {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sql = "DELETE FROM " + GOODS_TABLE;
            System.out.println("deleteAll() invoked");

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement);
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

    public void close(Connection connection, PreparedStatement preparedStatement) {
        try {
            connection.close();
            preparedStatement.close();

            System.out.println("Connection closed");
            System.out.println();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            connection.close();
            preparedStatement.close();
            resultSet.close();

            System.out.println("Connection closed");
            System.out.println();
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
    }
}