package com.geoparty.spring_boot.domain.organization.service;

import com.geoparty.spring_boot.domain.organization.dto.request.OrgRequest;
import com.geoparty.spring_boot.domain.organization.entity.Image;
import com.geoparty.spring_boot.domain.organization.entity.Organization;
import com.geoparty.spring_boot.domain.organization.repository.ImageRepository;
import com.geoparty.spring_boot.domain.organization.repository.OrgRepository;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.global.util.AWSS3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.geoparty.spring_boot.global.exception.BaseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrgServiceImpl implements OrgService {

    private final OrgRepository orgRepository;
    private final AWSS3Util s3Uploader;
    private final ImageRepository imageRepository;


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

            // detailedPhotos 리스트 파일 업로드
            List<Image> detailedPhotos = new ArrayList<>();
            if (request.getDetailedPhotos() != null) {
                for (MultipartFile file : request.getDetailedPhotos()) {
                    String imageUrl = s3Uploader.uploadFile(file);
                    detailedPhotos.add(new Image(imageUrl, org));
                }
            }
            // detailedPhotos에 추가된 Image 엔티티들 저장
            imageRepository.saveAll(detailedPhotos);

        } catch (Exception e) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Organization> getOrganizations(Pageable pageable) {
        Page<Organization> result = null;
        try{
            result = orgRepository.findAll(pageable);
        }catch(Exception e){
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }
        return result;
    }
}
