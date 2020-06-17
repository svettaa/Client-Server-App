package com.lukichova.olenyn.app.service;

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


    public final GroupDao groupDao = new GroupDao();

    public Group listByCriteria(Integer id) throws noItemWithSuchIdException, wrongDataBaseConnection {
        return groupDao.getById(id);
    }

    public Group listByCriteria(String name) throws noItemWithSuchNameException, wrongDataBaseConnection {
        return groupDao.getByName(name);
    }

    public List<Group> getAll() throws wrongDataBaseConnection {
        return groupDao.readAll();
    }

    public boolean create(Group group) throws wrongNotUniqueValue, wrongDataBaseConnection {
        return groupDao.create(group);
    }

    public boolean update(Group group) throws wrongNotUniqueValue, wrongDataBaseConnection {
        return groupDao.update(group);
    }

    public boolean delete(int id) throws wrongDataBaseConnection {
        return groupDao.delete(id);
    }

    public void deleteAll() throws wrongDataBaseConnection {
        groupDao.deleteAll();
    }
}
