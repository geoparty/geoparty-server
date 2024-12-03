package com.geoparty.spring_boot.domain.organization.controller;

import com.geoparty.spring_boot.domain.organization.dto.request.OrgRequest;
import com.geoparty.spring_boot.domain.organization.dto.response.OrgDTO;
import com.geoparty.spring_boot.domain.organization.dto.response.OrgListResponse;
import com.geoparty.spring_boot.domain.organization.dto.response.OrgResponse;
import com.geoparty.spring_boot.domain.organization.service.OrgServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/orgs")
public class OrgController {

    private final OrgServiceImpl orgService;

    @PostMapping(consumes = "multipart/form-data")
    @Operation(description = "환경단체를 등록한다.")
    public ResponseEntity<String> createOrganization(@RequestPart(value = "orgRequest") final OrgRequest request,
                                                     @RequestPart(value = "thumbnail") final MultipartFile thumbnail,
                                                     @RequestPart(value = "photos") final List<MultipartFile> photos,
                                                     @RequestPart(value = "pdf") final MultipartFile pdf) {
        orgService.createOrganization(request,thumbnail,photos,pdf);
        return ResponseEntity.status(HttpStatus.CREATED).body("환경단체 등록이 완료되었습니다.");
    }

    @GetMapping
    @Operation(description = "환경단체 목록을 조회한다.")
    public ResponseEntity<OrgListResponse> getOrganizations() {
        List<OrgDTO> orgs = orgService.getOrganizations();
        OrgListResponse response = OrgListResponse.builder()
                .orgs(orgs)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{orgId}")
    @Operation(description = "환경단체 상세 조회")
    public ResponseEntity<OrgResponse> getOrganization(@PathVariable(value = "orgId") long orgId) {

        OrgResponse orgDetail = orgService.getDetail(orgId);

        return ResponseEntity.status(HttpStatus.OK).body(orgDetail);
    }

}