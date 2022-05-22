package dev.mlukas.langreader.user;

import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRole(RoleType type) {
        return roleRepository.findByType(type)
                .orElseThrow(() ->
                        new RoleNotFoundException("The role '%s' doesn't exist.".formatted(type.getName()))
                );
    }
}
