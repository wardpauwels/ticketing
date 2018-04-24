package be.ward.ticketing.service.user;

import be.ward.ticketing.data.user.RoleRepository;
import be.ward.ticketing.entities.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Iterable<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}