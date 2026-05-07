package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Program2 {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== Test 1: seller findById ====");
        Department department = departmentDao.findById(3);

        System.out.println(department);


        System.out.println("\n=== TEST 2: department findAll ===");
        List<Department> list = departmentDao.findAll();

        for (Department obj : list) {
            System.out.println(obj);
        }


        System.out.println("\n=== TEST 4: department insert ===");

        Department newDepartment = new Department(null, "Musika");
        departmentDao.insert(newDepartment);
        System.out.println("Inserted! New id = " + newDepartment.getId());

        System.out.println("\n=== TEST 5: department update ===");
        department = departmentDao.findById(6);
        department.setName("Churrasco");
        departmentDao.update(department);
        System.out.println("Update completed");



        System.out.println("\n=== TEST 6: department DELETE ===");
        System.out.println("\n How many rows would you like to delete? ");
        int n = sc.nextInt();
        for(int i = 1; i <= n; i++){
            System.out.print("Enter id for delete test: ");
            int id = sc.nextInt();
            departmentDao.deleteByID(id);
        }

        sc.close();
    }
}