package com.example.admin.mapper;

import com.example.admin.bean.Department;

import java.util.List;

public interface DepartmentMapper {

    List<Department> getDepartmentsByParentId(Integer pid);

    void addDep(Department dep);

    void deleteDepById(Department dep);
}
