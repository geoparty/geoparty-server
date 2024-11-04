package com.geoparty.spring_boot.domain.organization.controller;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.organization.dto.request.OrgListRequest;
import com.geoparty.spring_boot.domain.organization.dto.request.OrgRequest;
import com.geoparty.spring_boot.domain.organization.dto.response.OrgListResponse;
import com.geoparty.spring_boot.domain.organization.dto.response.OrgResponse;
import com.geoparty.spring_boot.domain.organization.service.OrgService;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.security.jwt.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/orgs")
public class OrgController {

    private final OrgService orgService;

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
        OrgListResponse orgs = orgService.getOrganizations();
        return ResponseEntity.status(HttpStatus.OK).body(orgs);
    }

    @GetMapping("/{orgId}")
    @Operation(description = "환경단체 상세 조회")
    public ResponseEntity<OrgResponse> getOrganization(@PathVariable(value = "orgId") long orgId) {
        System.out.println(orgId);
        OrgResponse orgDetail = orgService.getDetail(orgId);
        return ResponseEntity.status(HttpStatus.OK).body(orgDetail);
    }

}