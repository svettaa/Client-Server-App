package com.lukichova.olenyn.app.DB;

import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.lukichova.olenyn.app.resoures.Resoures.GOODS_TABLE;
import static com.lukichova.olenyn.app.resoures.Resoures.GROUPS_TABLE;

public class GroupDao implements Dao<Group>{
    @Override
    public Group getById(int id) throws wrongDataBaseConnection {
        String sqlQuery = "SELECT * FROM " + GROUPS_TABLE +  " WHERE " + id + " = ?";
        System.out.println("getById() invoked");
        List<Goods> list = new ArrayList<Goods>();
        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(0, id);
            ResultSet rs= preparedStatement.executeQuery();

            if (rs.next()) {
                return createGroup(rs);
            } else {
                return null; //custom exception no such id
            }
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
    }

    private Group createGroup(ResultSet rs) throws SQLException {
        Group g = new Group();
        g.setName(rs.getString("name"));
        g.setDescription(rs.getString("description"));
        return g;
    }

    @Override
    public Group getByName(String name) throws wrongDataBaseConnection {
        String sqlQuery = "SELECT * FROM " + GROUPS_TABLE +  " WHERE " +name+ " = ?";
        System.out.println("getByName() invoked");
        List<Goods> list = new ArrayList<Goods>();
        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, name);
            ResultSet rs= preparedStatement.executeQuery();

            if (rs.next()) {
                return createGroup(rs);
            } else {
                return null; //custom exception no such name
            }
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
    }

    @Override
    public List<Group> readAll() throws wrongDataBaseConnection {
        String sql = "SELECT * FROM " + GROUPS_TABLE;
        System.out.println("readAll() invoked");
        List<Group> list = new ArrayList<Group>();
        try { PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(createGroup(rs));
            }
        }catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
        return list;
    }

    @Override
    public void create(Group group) throws wrongDataBaseConnection {
        String sqlQuery = "INSERT INTO " + GOODS_TABLE +
                " (name, price, description) " +
                " VALUES (?, ?, ?)";
        System.out.println("create() invoked");
        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, group.getName());
            preparedStatement.setString(4, group.getDescription());


            preparedStatement.executeUpdate();

            System.out.println("Inserted " + group.getName() + " " + group.getDescription()) ;
            System.out.println();
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
    }

    @Override
    public void update(Group group) throws wrongDataBaseConnection {
        String sqlQuery = "UPDATE " + GROUPS_TABLE + " " +
                "SET description = ? WHERE name = ?";
        System.out.println("update() invoked");
        try {
            PreparedStatement preparedStatement = DataBase.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, group.getDescription());
            preparedStatement.setString(2, group.getName());

            preparedStatement.executeUpdate();

            System.out.println("Updated " + group.getName() + " " + group.getDescription()) ;
            System.out.println();
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
    }

    @Override
    public void delete(int id) throws wrongDataBaseConnection {
        String sql = "DELETE FROM " + GROUPS_TABLE + " WHERE id = ?";
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
    }
}
