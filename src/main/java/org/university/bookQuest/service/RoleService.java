package org.university.bookQuest.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.university.bookQuest.entity.Role;
import org.university.bookQuest.repository.RoleRepository;

@Slf4j
@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role create(String name) {
        Role role = roleRepository.saveAndFlush(Role.of(name));
        log.info("registered role with name - {}", name);
        return role;
    }

    public Role readById(long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found!"));
        log.info("read role by id {}", role.getName());
        return role;
    }

    public Role readByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role not found!"));
        log.info("read role by name {}", role.getName());
        return role;
    }

    public Role getByUserId(long userId) {
        Role role = roleRepository.findByUsersId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found!"));
        log.info("read role by user id - {}", role.getName());
        return role;
    }

    public Role update(long id, String name) {
        Role oldRole = readById(id);
        log.info("update role name from {} to {}", oldRole.getName(), name);
        oldRole.setName(name);
        return roleRepository.save(oldRole);
    }

    public void delete(long id) {
        roleRepository.delete(readById(id));
        log.info("role with id {} successfully deleted", id);
    }
}
