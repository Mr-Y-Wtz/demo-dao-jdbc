package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department dep) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO department "
                            + "(Name) "
                            + "VALUES "
                            + "(?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, dep.getName());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    dep.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department dep) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "UPDATE department "
                            + "SET Name = ? "
                            + "WHERE Id = ? ",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, dep.getName());
            st.setInt(2, dep.getId());

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    dep.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else{
                throw new DbException("Unexpected error! No rows affected!");
            }
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteByID(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");

            st.setInt(1, id);
            int rows = st.executeUpdate();

            if(rows == 0){
                System.out.println("No rows affected");
            }
            else{
                System.out.println("Delete completed");
            }
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * from department "
                            + "WHERE Id = ?");

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                return dep;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(
                    "SELECT * FROM department "
                        + "ORDER BY Name");
            rs = st.executeQuery();
            List<Department> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()){

                Department dep = map.get(rs.getInt("Id"));

                if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("Id"), dep);
                }
                Department department = instantiateDepartment(rs);
                list.add(department);
            }
            return list;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));
        return dep;
    }
}
