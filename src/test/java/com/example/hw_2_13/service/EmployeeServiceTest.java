package com.example.hw_2_13.service;

import com.example.hw_2_13.exception.*;
import com.example.hw_2_13.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class EmployeeServiceTest {

    private final EmployeeService employeeService = new EmployeeService(new ValidatorService());

    @BeforeEach
    public void beforeEach() {
        employeeService.addEmployee("Иван", "Иванов", 1, 10000.00);
        employeeService.addEmployee("Николай", "Николаев", 2, 20000.00);
        employeeService.addEmployee("Роман", "Романов", 3, 30000.00);
    }

    @AfterEach
    public void afterEach() {
        employeeService.getAll()
                .forEach(employee -> employeeService.removeEmployee(employee.getName(), employee.getSurname(),
                        employee.getDepartment(), employee.getSalary()));
    }

    @Test
    public void addTest() {
        int beforeCount = employeeService.getAll().size();
        Employee expected = new Employee("Ivan", "Ivanov", 1, 123456.00);

        assertThat(employeeService.addEmployee("Ivan", "Ivanov", 1, 123456.00))
                .isEqualTo(expected)
                .isIn(employeeService.getAll());

        assertThat(employeeService.getAll()).hasSize(beforeCount + 1);
        assertThat(employeeService.findEmployee("Ivan", "Ivanov", 1, 123456.00)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("testWithIncorrectNameParams")
    public void testWithIncorrectName(String incorrectName) {
        assertThatExceptionOfType(IncorrectNameException.class)
                .isThrownBy(() -> employeeService.addEmployee(incorrectName, "Ivanov", 1, 123456.00));

    }

    @ParameterizedTest
    @MethodSource("testWithIncorrectSurnameParams")
    public void testWithIncorrectSurname(String incorrectSurname) {
        assertThatExceptionOfType(IncorrectSurnameException.class)
                .isThrownBy(() -> employeeService.addEmployee("Ivan", incorrectSurname, 1, 123456.00));

    }

    @Test
    public void testWhenAlreadyAdded() {
        assertThatExceptionOfType(EmployeeAlreadyAddedException.class)
                .isThrownBy(() -> employeeService.addEmployee("Иван", "Иванов", 1, 10000.00));
    }

    @Test
    public void testWhenStorageIsFull() {
        Stream.iterate(1, i -> i + 1)
                .limit(7)
                .map(number -> new Employee(
                        "Иван" + ((char) ('a' + number)),
                        "Иванов" + ((char) ('a' + number)),
                        number,
                        10000.00 + number))
                .forEach(employee -> employeeService.addEmployee(
                        employee.getName(),
                        employee.getSurname(),
                        employee.getDepartment(),
                        employee.getSalary()));


        assertThatExceptionOfType(EmployeeStorageIsFullException.class)
                .isThrownBy(() -> employeeService.addEmployee("Kirill", "Kirillov", 1, 10000.00));
    }

    @Test
    public void removeTest() {
        int beforeCount = employeeService.getAll().size();
        Employee expected = new Employee("Иван", "Иванов", 1, 10000.00);

        assertThat(employeeService.removeEmployee("Иван", "Иванов", 1, 10000.00))
                .isEqualTo(expected)
                .isNotIn(employeeService.getAll());

        assertThat(employeeService.getAll()).hasSize(beforeCount - 1);

        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.findEmployee("Иван", "Иванов", 1, 10000.00));
    }

    @Test
    public void removeNegativeTest() {
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.removeEmployee("Lev", "Tolstoy", 2, 99999.00));
    }

    @Test
    public void findTest() {
        int beforeCount = employeeService.getAll().size();
        Employee expected = new Employee("Иван", "Иванов", 1, 10000.00);

        assertThat(employeeService.findEmployee("Иван", "Иванов", 1, 10000.00))
                .isEqualTo(expected)
                .isIn(employeeService.getAll());

        assertThat(employeeService.getAll()).hasSize(beforeCount);
    }

    @Test
    public void findNegativeTest() {
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.findEmployee("Lev", "Tolstoy", 2, 99999.00));
    }

    public static Stream<Arguments> testWithIncorrectNameParams() {
        return Stream.of(
                Arguments.of("Ivan2"),
                Arguments.of("Ivan!"),
                Arguments.of("Ivan$")
        );
    }

    public static Stream<Arguments> testWithIncorrectSurnameParams() {
        return Stream.of(
                Arguments.of("Ivanov2"),
                Arguments.of("Ivanov!"),
                Arguments.of("Ivanov$")
        );
    }
}