package com.geoparty.spring_boot.domain.organization.controller;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.organization.dto.request.OrgRequest;
import com.geoparty.spring_boot.domain.organization.service.OrgService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/orgs")
public class OrgController {

    private final OrgService orgService;

    @PostMapping
    @Operation(description = "환경단체를 등록한다.")
    public ResponseEntity<String> createOrganization(@RequestBody final OrgRequest request) {
        orgService.createOrganization(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("환경단체 등록이 완료되었습니다.");
    }

    @GetMapping
    @Operation(description = "환경단체 목록을 조회한다.")
    public ResponseEntity<String> getOrganizations() {
        return ResponseEntity.status(HttpStatus.OK).body("환경단체 목록을 조회하였습니다.");
    }


}