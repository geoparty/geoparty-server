package com.geoparty.spring_boot.domain.organization.service;

import com.geoparty.spring_boot.domain.organization.dto.request.OrgListRequest;
import com.geoparty.spring_boot.domain.organization.dto.request.OrgRequest;
import com.geoparty.spring_boot.domain.organization.dto.response.OrgListResponse;
import com.geoparty.spring_boot.domain.organization.dto.response.OrgResponse;
import com.geoparty.spring_boot.domain.organization.entity.File;
import com.geoparty.spring_boot.domain.organization.entity.Image;
import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.organization.repository.FileRepository;
import com.geoparty.spring_boot.domain.organization.repository.ImageRepository;
import com.geoparty.spring_boot.domain.organization.repository.OrgRepository;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.global.util.AWSS3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.geoparty.spring_boot.global.exception.BaseException;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrgServiceImpl implements OrgService {

    private final OrgRepository orgRepository;
    private final AWSS3Util s3Uploader;
    private final ImageRepository imageRepository;
    private final FileRepository fileRepository;


    @Override
    @Transactional
    public void createOrganization(OrgRequest request) {
        try {
            // 썸네일 이미지가 있을 경우 S3에 업로드
            String thumbnailUrl = null;
            if (request.getThumbnail() != null) {
                thumbnailUrl = s3Uploader.uploadFile(request.getThumbnail());
            }

            // org 생성
            Organization org = request.toEntity(thumbnailUrl);
            orgRepository.save(org);

            // pdf file 업로드
            String fileUrl = null;
            if (request.getPdffile() != null) {
                fileUrl = s3Uploader.uploadFile(request.getPdffile());
            }

            File file = new File(fileUrl, org);
            fileRepository.save(file);

            // detailedPhotos 리스트 파일 업로드
            uploadDetailedPhotos(request.getDetailedPhotos(), org);


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
    public OrgListResponse getOrganizations(OrgListRequest request) {
        OrgListResponse response = null;
        try {
            Pageable pageable = PageRequest.of(request.getPageNum() - 1, //현재 페이지
                    request.getListNum(), // 페이지 당 개수
                    request.getDirection()); //내림차순
            Page<Organization> result = null;

            result = orgRepository.findAll(pageable);

            response = OrgListResponse.builder()
                    .orgs(result.getContent())
                    .pageNum(result.getNumber() + 1)
                    .length(result.getNumberOfElements())
                    .totalPage(result.getTotalPages())
                    .build();

        } catch (Exception e) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }
        return response;
    }

    @Override
    @Transactional
    public OrgResponse getDetail(Long orgId) {
        // 환경 단체 찾기
        Organization org = orgRepository.findById(orgId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_DATA));

        // 환경 단체 디테일 사진
        List<OrgResponse.PhotoDTO> photos =
                imageRepository.findByOrganizationId(orgId)
                        .stream().map(p ->
                                OrgResponse.PhotoDTO.builder()
                                        .imgUrl(p.getImageUrl())
                                        .build()
                        ).toList();

        // 환경 단체 문서
        String docs = fileRepository.findByOrganizationId(orgId).toString();

        return OrgResponse.builder()
                .title(org.getTitle())
                .summary(org.getSummary())
                .detail(org.getDetail())
                .mainAct(org.getMainAct())
                .minDonation(org.getMinDonation())
                .photos(photos)
                .file(docs)
                .build();
    }
}
