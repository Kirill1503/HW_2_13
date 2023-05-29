package com.example.hw_2_13.service;

import com.example.hw_2_13.exception.EmployeeAlreadyAddedException;
import com.example.hw_2_13.exception.EmployeeNotFoundException;
import com.example.hw_2_13.exception.EmployeeStorageIsFullException;
import com.example.hw_2_13.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private static final int LIMIT = 10;

    private final List<Employee> employees = new ArrayList<>();

    private final ValidatorService validatorService;

    public EmployeeService(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }



    public Employee addEmployee(String name, String surname, int department, double salary) {
        Employee employee = new Employee(validatorService.validateName(name),
                validatorService.validateSurname(surname),
                department,
                salary);
        if (employees.contains(employee)) {
            throw new EmployeeAlreadyAddedException();
        }
        if (employees.size() < LIMIT) {
            employees.add(employee);
            return employee;
        } else {
            throw new EmployeeStorageIsFullException();
        }
    }

    public Employee findEmployee(String name, String surname, int department, double salary) {
        Employee employee = new Employee(validatorService.validateName(name),
                validatorService.validateSurname(surname),
                department,
                salary);
        if (!employees.contains(employee)) {
            throw new EmployeeNotFoundException();
        }
        return employee;
    }


    public Employee removeEmployee(String name, String surname, int department, double salary) {
        Employee employee = new Employee(validatorService.validateName(name),
                validatorService.validateSurname(surname),
                department,
                salary);
        if (!employees.contains(employee)) {
            throw new EmployeeNotFoundException();
        }
        employees.remove(employee);
        return employee;
    }

    public List<Employee> getAll() {
        return new ArrayList<>(employees);
    }
}
