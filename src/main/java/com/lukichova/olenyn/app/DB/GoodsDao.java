package com.lukichova.olenyn.app.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.lukichova.olenyn.app.resoures.Resoures.GOODS_TABLE;

public class GoodsDao implements Dao<Goods>
{


    @Override
    public Optional<Goods> read(long id) {
        return Optional.empty();
    }

    @Override
    public List<Goods> readAll() {
        String sql = "SELECT * FROM " + GOODS_TABLE;
        System.out.println("readAll() invoked");
        List<Goods> list = new ArrayList<Goods>();
        try { PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Goods g = new Goods();
                g.setName(rs.getString("name_of_product"));
                g.setPrice(rs.getDouble("price"));

                list.add(g);
            }
        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return list;


    }

    @Override
    public void create(String[] params) {
        String sqlQuery = "INSERT INTO " + GOODS_TABLE +  " (name_of_product, price) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, params[0]);
            preparedStatement.setDouble(2, Double.parseDouble(params[1]));

            preparedStatement.executeUpdate();

            System.out.println("Inserted " + params[0]+ " " + params[1]);
            System.out.println();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void update(Goods g, String[] params) {
        String sqlQuery = "UPDATE " + GOODS_TABLE + " SET price = ? WHERE name_of_product = ?";

        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sqlQuery);

            preparedStatement.setDouble(1, Double.parseDouble(params[1]));
            preparedStatement.setString(2, g.getName());

            preparedStatement.executeUpdate();

            System.out.println("Updated " +g.getName()  + " " + g.getPrice());
            System.out.println();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    @Override
    public void delete(Goods goods) {
        String sql = "DELETE FROM " + GOODS_TABLE + " WHERE name_of_product = ?";



        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sql);

            preparedStatement.setString(1, goods.getName());

            preparedStatement.executeUpdate();

            System.out.println("Deleted " + goods.getName());
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Goods> listByCriteria(String[] params) {


        String sqlQuery = "SELECT * FROM " + GOODS_TABLE +  " WHERE " +params[0]+ " = ?";
        List<Goods> list = new ArrayList<Goods>();
        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, params[1]);

            ResultSet rs= preparedStatement.executeQuery();

            while (rs.next()) {
                Goods g = new Goods();
                g.setName(rs.getString("name_of_product"));
                g.setPrice(rs.getDouble("price"));

                list.add(g);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return list;
    }
}