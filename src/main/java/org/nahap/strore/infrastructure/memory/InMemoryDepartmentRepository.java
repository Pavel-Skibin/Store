package org.nahap.strore.infrastructure.memory;

import org.nahap.strore.application.repository.DepartmentRepository;
import org.nahap.strore.domain.model.Department;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryDepartmentRepository implements DepartmentRepository {
    private final Map<Integer, Department> departments = new LinkedHashMap<>();
    private final AtomicInteger idSequence = new AtomicInteger(1);

    @Override
    public Department save(Department department) {
        int id = department.getId();
        if (id <= 0) {
            id = idSequence.getAndIncrement();
        }
        Department copy = new Department(id, department.getName(), department.getOpenTime(), department.getCloseTime());
        departments.put(id, copy);
        return copy;
    }

    @Override
    public Optional<Department> findById(int id) {
        return Optional.ofNullable(departments.get(id));
    }

    @Override
    public List<Department> findAll() {
        return new ArrayList<>(departments.values());
    }

    @Override
    public void deleteById(int id) {
        departments.remove(id);
    }

    @Override
    public boolean existsById(int id) {
        return departments.containsKey(id);
    }
}