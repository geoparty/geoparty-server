package com.geoparty.spring_boot.domain.payment.service;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.repository.MemberRepository;
import com.geoparty.spring_boot.domain.payment.dto.request.KakaopayApproveRequest;
import com.geoparty.spring_boot.domain.payment.dto.request.KakaopayReadyRequest;
import com.geoparty.spring_boot.domain.payment.dto.response.*;
import com.geoparty.spring_boot.domain.payment.entity.UserPointLog;
import com.geoparty.spring_boot.domain.payment.entity.SinglePointChargeLog;
import com.geoparty.spring_boot.domain.payment.repository.SinglePointChargeLogRepository;
import com.geoparty.spring_boot.domain.payment.repository.UserPointLogRepository;
import com.geoparty.spring_boot.global.exception.BaseException;
import com.geoparty.spring_boot.global.exception.ErrorCode;
import com.geoparty.spring_boot.security.model.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {

    private final KakaopayService kakaopayService;
    private final SinglePointChargeLogRepository singlePointChargeLogRepository;
    private final UserPointLogRepository userPointLogRepository;
    private final MemberRepository memberRepository;

    public ReadyInfoResponse readyKakao(PrincipalDetails details, Integer point) {
        Integer memberId = details.getMember().getMemberId();
        Long orderId = requestOrderInfo(point, details);
        ResponseEntity<KakaopayReadyResponse> payApprove = kakaopayService.sendApprove(
                KakaopayReadyRequest.builder()
                        .cid("TC0ONETIME")
                        .partner_order_id(String.valueOf(orderId))
                        .partner_user_id("admin")
                        .item_name("point")
                        .quantity("1")
                        .total_amount(String.valueOf(point))
                        .tax_free_amount("0")
                        .approval_url("https://dogeoparty.duckdns.org/api/payments/success")
                        .cancel_url("https://developers.kakao.com/api/payments/cancel")
                        .fail_url("https://developers.kakao.com/api/payments/fail").build());
        String tid = Objects.requireNonNull(payApprove.getBody()).getTid();
        setOrderTid(orderId, tid);
        String webURL = payApprove.getBody().getNextRedirectMobileUrl();

        ReadyInfoResponse response = ReadyInfoResponse.builder().tid(tid).webURL(webURL).build();
        return response;
    }

    public ResponseEntity<KakaopayApproveResponse> completeKakao(PrincipalDetails details, String pgToken, String tid) {
        Integer memberId = details.getMember().getMemberId();
        SinglePointChargeLog singlePointChargeLog = getPointLog(tid);

        ResponseEntity<KakaopayApproveResponse> payComplete = kakaopayService.sendComplete(
                KakaopayApproveRequest.builder()
                        .cid("TC0ONETIME")
                        .tid(tid)
                        .partner_order_id(String.valueOf(singlePointChargeLog.getId()))
                        .partner_user_id("admin")
                        .pg_token(pgToken)
                        .build());

        if( payComplete.getStatusCode().equals(HttpStatus.OK)) {
            chargePoint(details, singlePointChargeLog.getId());
        }
        return payComplete;
    }

    public Long requestOrderInfo(int point, PrincipalDetails details) {
        SinglePointChargeLog mileageLog = SinglePointChargeLog.builder()
                .point(point)
                .member(details.getMember())
                .pointLogStatus("charge")
                .build();

        return singlePointChargeLogRepository.save(mileageLog).getId();
    }

    public SinglePointChargeLog getPointLog(String tid) {
        return singlePointChargeLogRepository.findByTid(tid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
    }

    @Transactional
    public void setOrderTid(Long orderId, String tid) {
        SinglePointChargeLog singlePointChargeLog = singlePointChargeLogRepository.findById(orderId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        singlePointChargeLog.setTid(tid);
        singlePointChargeLogRepository.save(singlePointChargeLog);
    }

    @Transactional
    public void chargePoint(PrincipalDetails details, Long orderId) {
        System.out.println("point service chargepoint" + details.getMember().getMemberId());

        SinglePointChargeLog singlePointChargeLog = singlePointChargeLogRepository.findById(orderId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        UserPointLog userPointLog = UserPointLog.builder()
                .member(details.getMember())
                .pointBefore(details.getMember().getPoint())
                .pointAfter(details.getMember().getPoint() + singlePointChargeLog.getPoint())
                .chargeAmount(singlePointChargeLog.getPoint())
                .chargeContent("charge")
                .build();

        userPointLogRepository.save(userPointLog);

        Member member = memberRepository.findUserByMemberId(details.getMember().getMemberId())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        member.setPoint(details.getMember().getPoint() + singlePointChargeLog.getPoint());
        memberRepository.save(member);

        singlePointChargeLog.setPointLogStatus("charge complete");
        singlePointChargeLogRepository.save(singlePointChargeLog);
    }

    public List<UserPointLogResponse> getPointChargeLogByMember(Member member) {

        List<UserPointLog> userPointChargeLogs = userPointLogRepository.findAllByMember(member);

        return userPointChargeLogs.stream()
                .map( userPointLog-> UserPointLogResponse.from(userPointLog, member))
                .collect(Collectors.toList());
    }

}
