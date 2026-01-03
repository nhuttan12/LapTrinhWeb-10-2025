package com.example.Service.Role;

import com.example.DAO.RoleDAO;
import com.example.Model.Role;

import java.sql.SQLException;

public class RoleService {
    private final RoleDAO roleDAO;

    public RoleService() {
        roleDAO = new RoleDAO();
    }

    public Role getRoleByRoleName(String roleName) {
        try {
            return roleDAO.getRoleByRoleName(roleName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
