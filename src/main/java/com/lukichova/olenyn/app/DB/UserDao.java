package com.lukichova.olenyn.app.DB;

import com.lukichova.olenyn.app.Exceptions.noItemWithSuchIdException;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchNameException;
import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;
import com.lukichova.olenyn.app.Exceptions.wrongNotUniqueValue;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.lukichova.olenyn.app.resoures.Resoures.GOODS_TABLE;
import static com.lukichova.olenyn.app.resoures.Resoures.USERS_TABLE;

public class UserDao {

    public User getByLogin(String login) throws wrongDataBaseConnection, noItemWithSuchIdException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "SELECT * FROM " + USERS_TABLE + " WHERE login = ?";
            System.out.println("getByLogin() invoked");

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, login);
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return createUser(rs);
            } else {
                return null;

            }
        } catch (SQLException sqlException) {

            sqlException.printStackTrace();
            throw new wrongDataBaseConnection();
        } finally {
            //close(connection, preparedStatement, rs);
        }
    }
    private User createUser(ResultSet rs) throws SQLException {
        User g = new User();
        g.setId(rs.getInt("id"));
        g.setLogin(rs.getString("login"));
        g.setPassword(rs.getString("password"));
        g.setRole(rs.getString("role"));
        return g;
    }


    public User getById(Integer id) throws wrongDataBaseConnection, noItemWithSuchNameException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sqlQuery = "SELECT * FROM " + USERS_TABLE + " WHERE id = ?";
            System.out.println("getByRoleId() invoked");

            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();


            if (rs.next()) {
                return createUser(rs);
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

    public List<User> readAll() throws wrongDataBaseConnection {
        List<User> list = new ArrayList<User>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sql = "SELECT * FROM " + USERS_TABLE;
            System.out.println("readAll() invoked");

            preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(createUser(rs));
                System.out.println(createUser(rs));
            }
            rs.close();
            return list;
        } catch (SQLException sqlException) {
            throw new wrongDataBaseConnection();
        } finally {
            close(connection, preparedStatement);
        }
    }

    public boolean create(User user) throws wrongDataBaseConnection, wrongNotUniqueValue {
        System.out.println("create() invoked");

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DataBase.url);

            if (user.getId() == null) {
                String sqlQuery = "INSERT INTO " + USERS_TABLE +
                        " (login, password, role) " +
                        " VALUES (?, ?,?)";

                preparedStatement = connection.prepareStatement(sqlQuery);

                preparedStatement.setString(1, user.getLogin());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getRole());

                preparedStatement.executeUpdate();
            }
            System.out.println("Inserted " + user.getId()+" "+user.getLogin() + " " + user.getRole());
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

    public boolean update(User user) throws wrongDataBaseConnection, wrongNotUniqueValue {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DataBase.url);

            String sqlQuery = "UPDATE " + USERS_TABLE + " " +
                    "SET password = ?, role = ? , login = ? WHERE id = ?";
            System.out.println("update() invoked");

            preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getRole());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setInt(4, user.getId());

            preparedStatement.executeUpdate();

            System.out.println("Updated " + user.getLogin() + " " + user.getRole());
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

    public boolean delete(int id) throws wrongDataBaseConnection {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(DataBase.url);
            String sql = "DELETE FROM " + USERS_TABLE + " WHERE id = ?";
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
            String sql = "DELETE FROM " + USERS_TABLE;
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
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
