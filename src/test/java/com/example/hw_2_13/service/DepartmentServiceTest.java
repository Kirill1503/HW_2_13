package com.example.hw_2_13.service;

import com.example.hw_2_13.exception.EmployeeNotFoundException;
import com.example.hw_2_13.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {
    @Mock
    private EmployeeService employeeService;
    @InjectMocks
    private DepartmentService departmentService;

    public static Stream<Arguments> findEmployeeWithMaxSalaryFromDepartmentTestParams() {
        return Stream.of(
                Arguments.of(1, new Employee("Semen", "Semyonov", 1, 22000.00)),
                Arguments.of(2, new Employee("Roman", "Romanov", 2, 42000.00)),
                Arguments.of(3, new Employee("Petr", "Yan", 3, 52000.00))
        );
    }

    public static Stream<Arguments> findEmployeeWithMinSalaryFromDepartmentTestParams() {
        return Stream.of(
                Arguments.of(1, new Employee("Ivan", "Ivanov", 1, 12000.00)),
                Arguments.of(2, new Employee("Irina", "Leonova", 2, 32000.00)),
                Arguments.of(3, new Employee("Petr", "Yan", 3, 52000.00))
        );
    }

    public static Stream<Arguments> findEmployeeFromDepartmentTestParams() {
        return Stream.of(
                Arguments.of(1,
                        List.of(
                                new Employee("Ivan", "Ivanov", 1, 12000.00),
                                new Employee("Semen", "Semyonov", 1, 22000.00)
                        )
                ),
                Arguments.of(2,
                        List.of(
                                new Employee("Irina", "Leonova", 2, 32000.00),
                                new Employee("Roman", "Romanov", 2, 42000.00)
                        )
                ),
                Arguments.of(3,
                        Collections.singletonList(new Employee("Petr", "Yan", 3, 52000.00))
                ),
                Arguments.of(4,
                        Collections.emptyList()
                )
        );
    }

    @BeforeEach
    public void beforeEach() {
        List<Employee> employees = List.of(
                new Employee("Ivan", "Ivanov", 1, 12000.00),
                new Employee("Semen", "Semyonov", 1, 22000.00),
                new Employee("Irina", "Leonova", 2, 32000.00),
                new Employee("Roman", "Romanov", 2, 42000.00),
                new Employee("Petr", "Yan", 3, 52000.00)
        );
        Mockito.when(employeeService.getAll()).thenReturn(employees);
    }

    @ParameterizedTest
    @MethodSource("findEmployeeWithMaxSalaryFromDepartmentTestParams")
    public void findEmployeeWithMaxSalaryFromDepartmentTest(int department, Employee expected) {
        assertThat(departmentService.findEmployeeWithMaxSalaryFromDepartment(department))
                .isEqualTo(expected);
    }

    @Test
    public void findEmployeeWithMaxSalaryFromDepartmentWhenNotFoundTest() {
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> departmentService.findEmployeeWithMaxSalaryFromDepartment(4));
    }

    @ParameterizedTest
    @MethodSource("findEmployeeWithMinSalaryFromDepartmentTestParams")
    public void findEmployeeWithMinSalaryFromDepartmentTest(int department, Employee expected) {
        assertThat(departmentService.findEmployeeWithMinSalaryFromDepartment(department))
                .isEqualTo(expected);
    }

    @Test
    public void findEmployeeWithMinSalaryFromDepartmentWhenNotFoundTest() {
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> departmentService.findEmployeeWithMinSalaryFromDepartment(4));
    }

    @ParameterizedTest
    @MethodSource("findEmployeeFromDepartmentTestParams")
    public void findEmployeeFromDepartmentTest(int department, List<Employee> expected) {
        assertThat(departmentService.findEmployeeFromDepartment(department))
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void findEmployeesTest() {
        Map<Integer, List<Employee>> expected =
                Map.of(
                        1,
                        List.of(
                                new Employee("Ivan", "Ivanov", 1, 12000.00),
                                new Employee("Semen", "Semyonov", 1, 22000.00)
                        ),
                        2,
                        List.of(
                                new Employee("Irina", "Leonova", 2, 32000.00),
                                new Employee("Roman", "Romanov", 2, 42000.00)
                        ),
                        3,
                        Collections.singletonList(new Employee("Petr", "Yan", 3, 52000.00))
                );

        assertThat(departmentService.findEmployees()).containsExactlyInAnyOrderEntriesOf(expected);
    }
}