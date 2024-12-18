package com.geoparty.spring_boot.domain.organization.service;

import com.geoparty.spring_boot.domain.organization.dto.request.OrgRequest;
import com.geoparty.spring_boot.domain.organization.dto.response.OrgDTO;
import com.geoparty.spring_boot.domain.organization.dto.response.OrgResponse;
import com.geoparty.spring_boot.domain.organization.entity.File;
import com.geoparty.spring_boot.domain.organization.entity.Image;
import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.organization.repository.FileRepository;
import com.geoparty.spring_boot.domain.organization.repository.ImageRepository;
import com.geoparty.spring_boot.domain.organization.repository.OrganizationRepository;
import com.geoparty.spring_boot.domain.party.repository.PartyRepository;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.global.util.AWSS3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.geoparty.spring_boot.global.exception.BaseException;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrgServiceImpl implements OrgService {

    private final OrganizationRepository orgRepository;
    private final AWSS3Util s3Uploader;
    private final ImageRepository imageRepository;
    private final FileRepository fileRepository;
    private final PartyRepository partyRepository;


    @Override
    @Transactional
    public void createOrganization(OrgRequest request,MultipartFile thumbnail, List<MultipartFile> photos, MultipartFile pdf) {
        try {
            // 썸네일 이미지가 있을 경우 S3에 업로드
            String thumbnailUrl = null;
            if (thumbnail != null) {
                thumbnailUrl = s3Uploader.uploadFile(thumbnail);
            }
    
            // org 생성
            Organization org = request.toEntity(thumbnailUrl);
            orgRepository.save(org);

            // pdf file 업로드
            String fileUrl = null;
            if (pdf != null) {
                fileUrl = s3Uploader.uploadFile(pdf);
            }

            String originalFilename = pdf.getOriginalFilename();
            log.info("File URL length: {}", fileUrl.length());
            File file = new File(fileUrl, originalFilename ,org);
            fileRepository.save(file);

            // detailedPhotos 리스트 파일 업로드
            uploadDetailedPhotos(photos, org);


        } catch (Exception e) {
            throw new BaseException(ErrorCode.NOT_FOUND_DATA);
        }
    }

    public List<Image> uploadDetailedPhotos(List<MultipartFile> files, Organization org) {
        try {
            List<Image> detailedPhotos = new ArrayList<>();

            if (files != null) {
                for (MultipartFile file : files) {
                    String imageUrl = s3Uploader.uploadFile(file);
                    detailedPhotos.add(new Image(imageUrl, org));
                }
            }

            imageRepository.saveAll(detailedPhotos);

            return detailedPhotos;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.NOT_FOUND_DATA);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrgDTO> getOrganizations() {
        List<Organization> orgs = orgRepository.findAll();
        List<OrgDTO> dtos = new ArrayList<>();
        for (Organization org : orgs) {
            int partyNum = partyRepository.countByOrganizationId(org.getId());
            OrgDTO orgDTO = OrgDTO.builder()
                            .orgId(org.getId())
                            .orgTitle(org.getTitle())
                            .thumbnail(org.getThumbnail())
                            .orgSummary(org.getSummary())
                            .partyNum(partyNum)
                            .build();
            dtos.add(orgDTO);
        }

        return dtos;
    }

    @Override
    @Transactional
    public OrgResponse getDetail(Long orgId) {
        // 환경 단체 찾기
        Organization org = orgRepository.findById(orgId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA));

        // 환경 단체 디테일 사진
        List<OrgResponse.PhotoDTO> photos = imageRepository.findByOrganizationId(orgId)
                .stream()
                .map(p -> OrgResponse.PhotoDTO.builder()
                        .imgUrl(p.getImageUrl())
                        .build())
                .toList();

        // photos가 비어 있을 경우 빈 리스트로 초기화
        if (photos.isEmpty()) {
            photos = Collections.emptyList();
        }

        // 환경 단체 문서
        File docs = Optional.ofNullable(fileRepository.findByOrganizationId(orgId))
                .orElseThrow(()-> new BaseException(ErrorCode.NOT_FOUND_DATA));

        Integer partyNum = partyRepository.countByOrganizationId(org.getId());

        System.out.println("partyNum: " + partyNum);

        return OrgResponse.builder()
                .title(org.getTitle())
                .summary(org.getSummary())
                .detail(org.getDetail())
                .mainAct(org.getMainAct())
                .minDonation(org.getMinDonation())
                .photos(photos)
                .fileName(docs.getFileName())
                .fileURL(docs.getFileUrl())
                .partyNum(partyNum)
                .year(org.getYear())
                .rate(org.getRate())
                .build();
    }
}
