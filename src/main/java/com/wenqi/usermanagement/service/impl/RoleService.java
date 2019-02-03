/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service.impl;

import com.wenqi.usermanagement.constants.RoleEnum;
import com.wenqi.usermanagement.dto.EmployeeUserDTO;
import com.wenqi.usermanagement.dto.NormalUserDTO;
import com.wenqi.usermanagement.dto.OrganizationUserDTO;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RoleService {
	/*
	 * Boolean to determinate if different kinds of user can visit or not
	 */

    // EmployeeProfile and NormalProfile
	public boolean canVisit(EmployeeUserDTO upperUser, NormalUserDTO lowerUser) {
        return Objects.equals(lowerUser.idEmployee, upperUser.userId);
    }

    // OrganizationProfile and EmployeeProfile
	public boolean canVisit(OrganizationUserDTO company, EmployeeUserDTO employee) {
        return Objects.equals(employee.idOrganization, company.userId);
	}

	/*
	 * Boolean to determinate if some kind of user can visit or not
	 */
	// Since we did not have details of level information, so we assume some common
	// case like following, so that it can be carry on in the future

    // NormalProfile and NormalProfile
	public boolean canVisit(NormalUserDTO normalUser1, NormalUserDTO normalUser2) {
        if (normalUser1.roles.contains(RoleEnum.TEACHER)) {
            return normalUser2.roles.contains(RoleEnum.STUDENT);
		}
		return false;
	}

	public boolean canVisit(EmployeeUserDTO employee1, EmployeeUserDTO employee2) {
        if (!Objects.equals(employee2.idOrganization, employee1.userId)) return false;
        if (employee1.roles.contains(RoleEnum.CEO)) {
            return employee2.roles.contains(RoleEnum.PRESIDENT);
        } else if (employee1.roles.contains(RoleEnum.PRESIDENT)) {
            return employee2.roles.contains(RoleEnum.VICE_PRESIDENT);
        } else if (employee1.roles.contains(RoleEnum.VICE_PRESIDENT)) {
            return employee2.roles.contains(RoleEnum.MANAGER);
		}
		return false;
	}

	public boolean canVisit(OrganizationUserDTO company1, OrganizationUserDTO company2) {
        if (!Objects.equals(company2.idParent, company1.userId)) return false;

		return false;
	}



}
