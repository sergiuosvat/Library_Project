package service.security;

import model.Right;
import model.Role;
import model.User;
import repository.security.RightsRolesRepository;

import java.util.List;

public class RightsRolesServiceImpl implements RightsRolesService{
    private final RightsRolesRepository rightsRolesRepository;
    public RightsRolesServiceImpl(RightsRolesRepository rightsRolesRepository)
    {
        this.rightsRolesRepository = rightsRolesRepository;
    }
    @Override
    public void addRole(String role) {
        rightsRolesRepository.addRole(role);
    }

    @Override
    public void addRight(String right) {
        rightsRolesRepository.addRight(right);
    }

    @Override
    public Role findRoleByTitle(String role) {
        return rightsRolesRepository.findRoleByTitle(role);
    }

    @Override
    public Role findRoleById(Long roleId) {
        return rightsRolesRepository.findRoleById(roleId);
    }

    @Override
    public Right findRightByTitle(String right) {
        return rightsRolesRepository.findRightByTitle(right);
    }

    @Override
    public void addRolesToUser(User user, List<Role> roles) {
        rightsRolesRepository.addRolesToUser(user, roles);
    }

    @Override
    public List<Role> findRolesForUser(Long userId) {
        return rightsRolesRepository.findRolesForUser(userId);
    }

    @Override
    public void addRoleRight(Long roleId, Long rightId) {
        rightsRolesRepository.addRoleRight(roleId,rightId);
    }

    @Override
    public String findRoleForUserString(Long userId) {
        return rightsRolesRepository.findRoleForUserString(userId);
    }
}
