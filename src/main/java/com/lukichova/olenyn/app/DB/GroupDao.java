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

import static com.lukichova.olenyn.app.resoures.Resoures.GROUP_TABLE;

public class GroupDao {

    public Group getById(int id) throws wrongDataBaseConnection, noItemWithSuchIdException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "SELECT * FROM " + GROUP_TABLE + " WHERE id = ?";
            System.out.println("getById() invoked");
            List<Goods> list = new ArrayList<Goods>();

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return createGroup(rs);
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


    private Group createGroup(ResultSet rs) throws SQLException {
        Group g = new Group();
        g.setId(rs.getInt("id"));
        g.setName(rs.getString("name"));
        g.setDescription(rs.getString("description"));
        return g;
    }

    public Group getByName(String name) throws wrongDataBaseConnection, noItemWithSuchNameException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "SELECT * FROM " + GROUP_TABLE + " WHERE name = ?";
            System.out.println("getByName() invoked");

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, name);
            rs = preparedStatement.executeQuery();


            if (rs.next()) {
                return createGroup(rs);
            } else {
                throw new noItemWithSuchNameException();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement, rs);
        }

    }
    public List<Group> searchByName(String name) throws wrongDataBaseConnection, noItemWithSuchIdException {
        List<Group> all =  readAll();
        List<Group> list = new ArrayList<Group>();
        Object dd[] = all.stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase())).toArray();
        ArrayList<Group> list1 = new ArrayList(Arrays.asList(dd));
        list=list1;

        return list;
    }
    public List<Group> readAll() throws wrongDataBaseConnection {
        List<Group> list = new ArrayList<Group>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sql = "SELECT * FROM " + GROUP_TABLE;
            System.out.println("readAll() invoked");

            preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(createGroup(rs));
                System.out.println(createGroup(rs));
            }
            rs.close();
            return list;
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement);
        }
    }

    public boolean create(Group group) throws wrongDataBaseConnection, wrongNotUniqueValue {
        System.out.println("create() invoked");

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DataBase.url);

            if (group.getId() == null) {
                String sqlQuery = "INSERT INTO " + GROUP_TABLE +
                        " (name, description) " +
                        " VALUES (?, ?)";

                preparedStatement = connection.prepareStatement(sqlQuery);

                preparedStatement.setString(1, group.getName());
                preparedStatement.setString(2, group.getDescription());

                preparedStatement.executeUpdate();
            } else {
                String sqlQuery = "INSERT INTO " + GROUP_TABLE +
                        " (id, name, description) " +
                        " VALUES (?, ?, ?)";

                preparedStatement = connection.prepareStatement(sqlQuery);

                preparedStatement.setInt(1, group.getId());
                preparedStatement.setString(2, group.getName());
                preparedStatement.setString(3, group.getDescription());

                preparedStatement.executeUpdate();
            }
            System.out.println("Inserted " + group.getName() + " " + group.getDescription());
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

    public boolean update(Group group) throws wrongDataBaseConnection, wrongNotUniqueValue {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DataBase.url);

            String sqlQuery = "UPDATE " + GROUP_TABLE + " " +
                    "SET name = ?, description = ? WHERE id = ?";
            System.out.println("update() invoked");

            preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, group.getName());
            preparedStatement.setString(2, group.getDescription());
            preparedStatement.setInt(3, group.getId());

            preparedStatement.executeUpdate();

            System.out.println("Updated " + group.getName() + " " + group.getDescription());
            return true;
        } catch (SQLiteException e) {
            e.printStackTrace();
            throw new wrongNotUniqueValue();

        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement);
        }
    }

    public boolean delete(int id) throws wrongDataBaseConnection, noItemWithSuchIdException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Group testtt = getById(id);
       testtt.getName();
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sql = "DELETE FROM " + GROUP_TABLE + " WHERE id = ?";
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

    public void deleteAll() throws wrongDataBaseConnection {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sql = "DELETE FROM " + GROUP_TABLE;
            System.out.println("deleteAll() invoked");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            close(connection);
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
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
