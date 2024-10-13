package com.geoparty.spring_boot.auth.service;

import com.geoparty.spring_boot.domain.user.entity.UserAccount;
import com.geoparty.spring_boot.domain.user.dto.UserAccountDto;
import com.geoparty.spring_boot.domain.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {

    private final UserAccountRepository userRepository;

    @Transactional(readOnly = true)

    public UserAccountDto saveUser(Integer username, String email, String nickname, String userRefreshtoken, boolean userIsWithdraw, String socialId) {
        return UserAccountDto.from(
                userRepository.save(UserAccount.of(username, email, nickname, userRefreshtoken,userIsWithdraw,socialId))
        );
    }

}