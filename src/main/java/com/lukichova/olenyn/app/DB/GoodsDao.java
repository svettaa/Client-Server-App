package com.lukichova.olenyn.app.DB;

import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.lukichova.olenyn.app.resoures.Resoures.GOODS_TABLE;

public class GoodsDao implements Dao<Goods>
{

    @Override
    public Goods getById(int id) throws wrongDataBaseConnection {
        String sqlQuery = "SELECT * FROM " + GOODS_TABLE +  " WHERE " +id+ " = ?";
        System.out.println("getById() invoked");
        List<Goods> list = new ArrayList<Goods>();
        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(0, id);
            ResultSet rs= preparedStatement.executeQuery();

            if (rs.next()) {
                return createGoods(rs);
            } else {
                return null; //custom exception no such id
            }
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
    }

    private Goods createGoods(ResultSet rs) throws SQLException {
        Goods g = new Goods();
        g.setName(rs.getString("name"));
        g.setPrice(rs.getBigDecimal("price"));
        g.setLeft_amount(rs.getInt("left_amount"));
        g.setProducer(rs.getString("producer"));
        g.setDescription(rs.getString("description"));
        g.setGroup_id(rs.getInt("group_id"));

        return g;
    }

    @Override
    public Goods getByName(String name) throws wrongDataBaseConnection {
        String sqlQuery = "SELECT * FROM " + GOODS_TABLE +  " WHERE " +name+ " = ?";
        System.out.println("getByName() invoked");
        List<Goods> list = new ArrayList<Goods>();
        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, name);
            ResultSet rs= preparedStatement.executeQuery();

            if (rs.next()) {
                return createGoods(rs);
            } else {
                return null; //custom exception no such name
            }
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
    }

    @Override
    public List<Goods> readAll() throws wrongDataBaseConnection {
        String sql = "SELECT * FROM " + GOODS_TABLE;
        System.out.println("readAll() invoked");
        List<Goods> list = new ArrayList<Goods>();
        try { PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(createGoods(rs));
            }
        }catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
        return list;
    }

    @Override
    public boolean create(Goods goods) throws wrongDataBaseConnection {
        String sqlQuery = "INSERT INTO " + GOODS_TABLE +
                " (name, price, left_amount, producer, description, group_id) " +
                " VALUES (?, ?, ?, ?, ?, ?)";
        System.out.println("create() invoked");
        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, goods.getName());
            preparedStatement.setBigDecimal(2, goods.getPrice());
            preparedStatement.setInt(3, goods.getLeft_amount());
            preparedStatement.setString(4, goods.getProducer());
            preparedStatement.setString(4, goods.getDescription());
            preparedStatement.setInt(4, goods.getGroup_id());

            preparedStatement.executeUpdate();

            System.out.println("Inserted " + goods.getName() + " " + goods.getPrice() + " "
                    + goods.getLeft_amount() + " " + goods.getProducer()+ " " + goods.getDescription()) ;
            System.out.println();
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
        return false;
    }

    @Override
    public boolean update(Goods goods) throws wrongDataBaseConnection {
        String sqlQuery = "UPDATE " + GOODS_TABLE + " " +
                "SET price = ?, " +
                "left_amount = ?," +
                "producer = ?," +
                "description = ?," +
                "group_id = ? WHERE name = ?";
        System.out.println("update() invoked");
        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sqlQuery);
            preparedStatement.setBigDecimal(1, goods.getPrice());
            preparedStatement.setInt(2, goods.getLeft_amount());
            preparedStatement.setString(3, goods.getProducer());
            preparedStatement.setString(4, goods.getDescription());
            preparedStatement.setInt(5, goods.getGroup_id());
            preparedStatement.setString(6, goods.getName());

            preparedStatement.executeUpdate();

            System.out.println("Updated " + goods.getName() + " " + goods.getPrice() + " "
                    + goods.getLeft_amount() + " " + goods.getProducer()+ " " + goods.getDescription()) ;
            System.out.println();
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
        return false;
    }

    @Override
    public boolean delete(int id) throws wrongDataBaseConnection {
        String sql = "DELETE FROM " + GOODS_TABLE + " WHERE id = ?";
        System.out.println("delete() invoked");
        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            System.out.println("Deleted " + id);
            System.out.println();
        } catch (SQLException e) {
            throw new wrongDataBaseConnection();
        }
        return false;
    }

    @Override
    public void deleteAll() throws wrongDataBaseConnection {
        String sql = "DELETE FROM " + GOODS_TABLE;
        System.out.println("deleteAll() invoked");
        try { PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sql);
            preparedStatement.executeQuery();
        }catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
    }
}