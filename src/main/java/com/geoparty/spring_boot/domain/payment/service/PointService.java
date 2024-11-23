package com.geoparty.spring_boot.domain.payment.service;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.domain.payment.dto.request.KakaopayApproveRequest;
import com.geoparty.spring_boot.domain.payment.dto.request.KakaopayReadyRequest;
import com.geoparty.spring_boot.domain.payment.dto.response.KakaopayApproveResponse;
import com.geoparty.spring_boot.domain.payment.dto.response.KakaopayReadyResponse;
import com.geoparty.spring_boot.domain.payment.entity.Point;
import com.geoparty.spring_boot.domain.payment.entity.PointLog;
import com.geoparty.spring_boot.domain.payment.repository.PointLogRepository;
import com.geoparty.spring_boot.domain.payment.repository.PointRepository;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.security.model.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PointService {

    private final KakaopayService kakaopayService;
    private final PointLogRepository pointLogRepository;
    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;

    public String readyKakao(PrincipalDetails details,Integer point) {
        Integer memberId = details.getMember().getMemberId();
        Long orderId = requestOrderInfo(point, details);
        ResponseEntity<KakaopayReadyResponse> payApprove = kakaopayService.sendApprove(
                KakaopayReadyRequest.builder()
                        .cid("TC0ONETIME")
                        .partner_order_id(String.valueOf(orderId))
                        .partner_user_id(String.valueOf(memberId))
                        .item_name("point")
                        .quantity("1")
                        .total_amount(String.valueOf(point))
                        .tax_free_amount("0")
                        .approval_url("https://developers.kakao.com/success")
                        .cancel_url("https://developers.kakao.com/cancel")
                        .fail_url("https://developers.kakao.com/fail").build());
        String tid = Objects.requireNonNull(payApprove.getBody()).getTid();
        setOrderTid(orderId, tid);
        String webURL = payApprove.getBody().getNextRedirectMobileUrl();
        return webURL;
    }

    public ResponseEntity<KakaopayApproveResponse> completeKakao(PrincipalDetails details, String pgToken, String tid) {
        Integer memberId = details.getMember().getMemberId();
        PointLog pointLog = getPointLog(tid);

        ResponseEntity<KakaopayApproveResponse> payComplete = kakaopayService.sendComplete(
                KakaopayApproveRequest.builder()
                        .cid("TC0ONETIME")
                        .tid(tid)
                        .partner_order_id(String.valueOf(pointLog.getTid()))
                        .partner_user_id(String.valueOf(memberId))
                        .pg_token(pgToken)
                        .build());

        if( payComplete.getStatusCode().equals(HttpStatus.OK.value())) {
            chargePoint(details,pointLog.getId());
        }

        return payComplete;
    }

    public Long requestOrderInfo(int point, PrincipalDetails details) {
        PointLog mileageLog = PointLog.builder()
                .point(point)
                .member(details.getMember())
                .pointLogStatus("charge")
                .build();

        return pointLogRepository.save(mileageLog).getId();
    }

    public PointLog getPointLog(String tid) {
        return pointLogRepository.findByTid(tid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
    }

    @Transactional
    public void setOrderTid(Long orderId, String tid) {
        PointLog pointLog = pointLogRepository.findById(orderId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        pointLog.setTid(tid);
        pointLogRepository.save(pointLog);
    }

    @Transactional
    public void chargePoint(PrincipalDetails details, Long orderId) {

        PointLog pointLog = pointLogRepository.findById(orderId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        Point point = Point.builder()
                .member(details.getMember())
                .pointBefore(details.getMember().getPoint())
                .pointAfter(details.getMember().getPoint() + pointLog.getPoint())
                .chargeAmount(pointLog.getPoint())
                .chargeContent("charge")
                .build();

        pointRepository.save(point);

        Member member = memberRepository.findUserByMemberId(details.getMember().getMemberId())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        member.setPoint(details.getMember().getPoint() + pointLog.getPoint());
        memberRepository.save(member);

        pointLog.setPointLogStatus("charge complete");
        pointLogRepository.save(pointLog);
    }

}
