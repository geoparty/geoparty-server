package com.geoparty.spring_boot.domain.organization.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Builder
public class OrgResponse {
    private String title;
    private String summary;
    private String detail;
    private String mainAct;
    private Integer minDonation;
    private List<PhotoDTO> photos;
    private String fileName;
    private String fileURL;
    private String year;
    private Integer rate;

    @Getter
    @Setter
    @Builder
    public static class PhotoDTO {
        private String imgUrl;
    }

}
