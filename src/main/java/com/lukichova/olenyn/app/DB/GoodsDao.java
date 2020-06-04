package com.lukichova.olenyn.app.DB;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import com.lukichova.olenyn.app.DB.Goods;
import static com.lukichova.olenyn.app.resoures.Resoures.GOODS_TABLE;

public class GoodsDao<Goods> implements Dao<Goods> {

    @Override
    public Optional<Goods> read(long id) {
        return Optional.empty();
    }

    @Override
    public List<Goods> readAll() {

        return null;
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
        com.lukichova.olenyn.app.DB.Goods ss=new com.lukichova.olenyn.app.DB.Goods("kdm",3);
        ss.getName();
        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sqlQuery);

            preparedStatement.setDouble(1, Double.parseDouble(params[0]));
          // preparedStatement.setString(2, g.getName());

            preparedStatement.executeUpdate();

          //  System.out.println("Updated " +  + " " + title);
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

          //  preparedStatement.setString(1, goods.getName());

            preparedStatement.executeUpdate();

            System.out.println("Deleted " + goods);
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void listByCriteria(Goods goods) {

    }
}
