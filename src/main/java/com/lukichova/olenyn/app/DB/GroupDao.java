package com.lukichova.olenyn.app.DB;

import com.lukichova.olenyn.app.Exceptions.noItemWithSuchIdException;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchNameException;
import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;
import com.lukichova.olenyn.app.Exceptions.wrongNotUniqueValue;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.lukichova.olenyn.app.resoures.Resoures.GROUP_TABLE;

public class GroupDao implements Dao<Group> {


    @Override
    public Group getById(int id) throws wrongDataBaseConnection, noItemWithSuchIdException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "SELECT * FROM " + GROUP_TABLE + " WHERE " + id + " = ?";
            System.out.println("getById() invoked");
            List<Goods> list = new ArrayList<Goods>();

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return createGroup(rs);
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

    private Group createGroup(ResultSet rs) throws SQLException {
        Group g = new Group();
        g.setId(rs.getInt("id"));
        g.setName(rs.getString("name"));
        g.setDescription(rs.getString("description"));
        return g;
    }

    @Override
    public Group getByName(String name) throws wrongDataBaseConnection, noItemWithSuchNameException {
        try {
            Connection connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "SELECT * FROM " + GROUP_TABLE + " WHERE " + name + " = ?";
            System.out.println("getByName() invoked");

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();

            close(connection);
            if (rs.next()) {
                return createGroup(rs);
            } else {
                throw new noItemWithSuchNameException();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new wrongDataBaseConnection();
        }
    }

    @Override
    public List<Group> readAll() throws wrongDataBaseConnection {
        List<Group> list = new ArrayList<Group>();
        try {
            Connection connection = DriverManager.getConnection(DataBase.url);
            String sql = "SELECT * FROM " + GROUP_TABLE;
            System.out.println("readAll() invoked");

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(createGroup(rs));
                System.out.println(createGroup(rs));
            }
            close(connection);
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
        return list;
    }

    @Override
    public boolean create(Group group) throws wrongDataBaseConnection, wrongNotUniqueValue {
        System.out.println("create() invoked");
        try {
            Connection connection = DriverManager.getConnection(DataBase.url);

            if (group.getId() == null) {
                String sqlQuery = "INSERT INTO " + GROUP_TABLE +
                        " (name, description) " +
                        " VALUES (?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

                preparedStatement.setString(1, group.getName());
                preparedStatement.setString(2, group.getDescription());

                preparedStatement.executeUpdate();
            } else {
                String sqlQuery = "INSERT INTO " + GROUP_TABLE +
                        " (id, name, description) " +
                        " VALUES (?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

                preparedStatement.setInt(1, group.getId());
                preparedStatement.setString(2, group.getName());
                preparedStatement.setString(3, group.getDescription());

                preparedStatement.executeUpdate();
            }
            System.out.println("Inserted " + group.getName() + " " + group.getDescription());
            close(connection);
            return true;
        } catch (SQLiteException e){

            throw new wrongNotUniqueValue();

        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new wrongDataBaseConnection();
        }
    }

    @Override
    public boolean update(Group group) throws wrongDataBaseConnection,wrongNotUniqueValue {
        try {
            Connection connection = DriverManager.getConnection(DataBase.url);

            String sqlQuery = "UPDATE " + GROUP_TABLE + " " +
                    "SET name = ?, description = ? WHERE id = ?";
            System.out.println("update() invoked");

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, group.getName());
            preparedStatement.setString(2, group.getDescription());
            preparedStatement.setInt(3, group.getId());

            preparedStatement.executeUpdate();

            System.out.println("Updated " + group.getName() + " " + group.getDescription());
            close(connection);
        } catch (SQLiteException e){

            throw new wrongNotUniqueValue();

        }catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        }
        return true;
    }

    @Override
    public boolean delete(int id) throws wrongDataBaseConnection {
        try {
            Connection connection = DriverManager.getConnection(DataBase.url);
            String sql = "DELETE FROM " + GROUP_TABLE + " WHERE id = ?";
            System.out.println("delete() invoked");

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            System.out.println("Deleted " + id);
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
            String sql = "DELETE FROM " + GROUP_TABLE;
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
