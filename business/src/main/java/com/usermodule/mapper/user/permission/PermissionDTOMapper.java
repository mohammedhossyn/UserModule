package com.usermodule.mapper.user.permission;

import com.usermodule.dto.user.permission.PermissionResponseDTO;
import com.usermodule.model.user.PermissionEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PermissionDTOMapper implements Function<PermissionEntity, PermissionResponseDTO> {

    @Override
    public PermissionResponseDTO apply(@NonNull PermissionEntity permission) {
        return PermissionResponseDTO.builder()
                .permissionId(permission.getPermissionId())
                .code(permission.getCode())
                .description(permission.getDescription())
                .permissionParent(permission.getPermissionParentEntity() != null ?
                        apply(permission.getPermissionParentEntity()) : null)
                .build();
    }

    public PermissionEntity apply(@NonNull PermissionResponseDTO systemPermissionResponseDTO) {
        return PermissionEntity.builder()
                .permissionId(systemPermissionResponseDTO.permissionId())
                .code(systemPermissionResponseDTO.code())
                .description(systemPermissionResponseDTO.description())
                .permissionParentEntity(systemPermissionResponseDTO.permissionParent() != null ?
                        apply(systemPermissionResponseDTO.permissionParent()) : null)
                .build();
    }
}
