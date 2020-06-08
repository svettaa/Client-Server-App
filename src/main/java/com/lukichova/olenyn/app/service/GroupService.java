package com.lukichova.olenyn.app.service;

import com.lukichova.olenyn.app.DB.Dao;
import com.lukichova.olenyn.app.DB.Group;
import com.lukichova.olenyn.app.DB.GroupDao;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchIdException;
import com.lukichova.olenyn.app.Exceptions.noItemWithSuchNameException;
import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;
import com.lukichova.olenyn.app.Exceptions.wrongNotUniqueValue;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GroupService {


    private final Dao<Group> dao;

    public GroupService(){
        dao = new GroupDao();
    }

    public Group getGroup(Integer id) throws noItemWithSuchIdException, wrongDataBaseConnection {
        return dao.getById(id);
    }

    public Group getGroup(String name) throws noItemWithSuchNameException, wrongDataBaseConnection {
        return dao.getByName(name);
    }

    public List<Group> getAll() throws wrongDataBaseConnection {
        return dao.readAll();
    }

    public boolean create(Group group) throws wrongNotUniqueValue, wrongDataBaseConnection {
        return dao.create(group);
    }

    public boolean update(Group group) throws wrongNotUniqueValue, wrongDataBaseConnection {
        return dao.update(group);
    }

    public boolean delete(int id) throws wrongDataBaseConnection {
        return dao.delete(id);
    }

    public void deleteAll() throws wrongDataBaseConnection {
        dao.deleteAll();
    }
}
