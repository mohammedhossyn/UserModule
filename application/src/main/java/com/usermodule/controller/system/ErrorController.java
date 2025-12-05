package com.usermodule.controller.system;

import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.dto.common.PaginationRequestDTO;
import com.usermodule.dto.system.ErrorSearchRequestDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.service.system.ErrorService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/error")
public class ErrorController {
    private final ErrorService errorService;
    private final ApiResponseInspector apiResponseInspector;

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('System Error Management') or hasAuthority('Search System Errors')")
    public ApiResponseDTO search(PaginationRequestDTO paginationRequestDTO,
                                 @RequestBody ErrorSearchRequestDTO errorSearchRequestDTO) {
        var listDTO = errorService.search(paginationRequestDTO
                .getPageable(), errorSearchRequestDTO);
        return apiResponseInspector.apiResponseBuilder(listDTO, "");
    }

    @GetMapping("/getSystemError/{systemErrorId}")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('System Error Management') or hasAuthority('Search System Error')")
    public ApiResponseDTO getSystemError(@NonNull @PathVariable Long systemErrorId) {
        var errorDetailResponseDTO = errorService.getError(systemErrorId);
        return apiResponseInspector.apiResponseBuilder(errorDetailResponseDTO, "");
    }

}
