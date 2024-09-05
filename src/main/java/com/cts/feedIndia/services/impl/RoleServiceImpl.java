package com.cts.feedIndia.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.feedIndia.entity.Role;
import com.cts.feedIndia.repository.RoleRepository;
import com.cts.feedIndia.services.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
    private RoleRepository roleRepository;

    public Role findDefaultRole() {
        String defaultRoleName = "DEFAULT";
        Role defaultRole = roleRepository.findByRole(defaultRoleName);
        if (defaultRole == null) {
            defaultRole = new Role();
            defaultRole.setRole(defaultRoleName);
            roleRepository.save(defaultRole);
        }
        return defaultRole;
    }

}
