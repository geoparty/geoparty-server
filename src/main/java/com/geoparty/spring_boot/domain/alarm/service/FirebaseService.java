package com.geoparty.spring_boot.domain.alarm.service;

import com.geoparty.spring_boot.domain.member.entity.Member;
import com.geoparty.spring_boot.domain.member.service.MemberService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FirebaseService {

    private final MemberService memberService;

    @Transactional
    public void updateTargetToken(String targetToken, Member member) {
        member.updateTargetToken(targetToken);
    }

    public String sendFCMMessage(String targetToken, String title, String body) {
        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(new Notification(title, body))
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            return "Message sent successfully: " + response;

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "Failed to send message";
        }
    }
}