package com.usermodule.service.security;

import com.usermodule.dto.user.UserDetailsDTO;
import com.usermodule.exception.BusinessException;
import com.usermodule.model.user.PermissionEntity;
import com.usermodule.model.user.UserRolePermissionEntity;
import com.usermodule.repository.user.UserRepository;
import com.usermodule.service.system.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.usermodule.exception.CodeException.Auth;

@Service
@RequiredArgsConstructor
public class UserDetailsImplService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OptionService optionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var systemUserEntityOptional = userRepository.findByUsername(username);
        if (systemUserEntityOptional.isPresent()) {
            var userEntity = systemUserEntityOptional.get();

            List<UserRolePermissionEntity> userRolePermissionEntities = userEntity.getUserRolePermissions().stream().toList();
            UserRolePermissionEntity userRolePermission;
            if (userRolePermissionEntities.isEmpty()){
                userRolePermission = new UserRolePermissionEntity();
            } else {
                userRolePermission = userRolePermissionEntities.get(0);
            }


            String systemOptionAccessUsers = optionService.getStringValueByCode("SYSTEM_OPTION_ACCESS_USERS");
            if (systemOptionAccessUsers != null && !systemOptionAccessUsers.isEmpty()) {
                String[] users = systemOptionAccessUsers.split(",");
                for (String user : users) {
                    if (userEntity.getUsername().equals(user)) {
                        userRolePermission.getPermissions().add(PermissionEntity.builder()
                                .permissionId(null)
                                .code("System Option List")
                                .description(null)
                                .build());
                        userRolePermission.getPermissions().add(PermissionEntity.builder()
                                .permissionId(null)
                                .code("Update System Option")
                                .description(null)
                                .build());
                        userRolePermission.getPermissions().add(PermissionEntity.builder()
                                .permissionId(null)
                                .code("Reload System Option")
                                .description(null)
                                .build());
                        userRolePermission.getPermissions().add(PermissionEntity.builder()
                                .permissionId(null)
                                .code("Search System Errors")
                                .description(null)
                                .build());
                        userRolePermission.getPermissions().add(PermissionEntity.builder()
                                .permissionId(null)
                                .code("Search System Error")
                                .description(null)
                                .build());
                        break;
                    }
                }
            }
            return UserDetailsDTO.builder()
                    .user(userEntity)
                            .build();
        } else {
            throw new BusinessException(Auth.INVALID_USERNAME_OR_PASSWORD);
        }

    }
}
