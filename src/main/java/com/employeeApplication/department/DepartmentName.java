package com.employeeApplication.department;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum DepartmentName {
    HR,
    FINANCE,
    TECH,
    SALES;

    public static boolean isValidDepartmentName(String departmentName){
        for(DepartmentName dname:values()){
            if(dname.name().equalsIgnoreCase(departmentName)){
                return true;
            }
        }
        return false;
    }

}