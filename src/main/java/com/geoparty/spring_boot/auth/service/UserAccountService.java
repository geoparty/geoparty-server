package com.geoparty.spring_boot.auth.service;

import com.geoparty.spring_boot.domain.user.entity.UserAccount;
import com.geoparty.spring_boot.auth.dto.UserAccountDto;
import com.geoparty.spring_boot.domain.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public Optional<UserAccountDto> searchUser(String username) {
        return userAccountRepository.findById(username)
                .map(UserAccountDto::from);
    }

    public UserAccountDto saveUser(String username, String password, String email, String nickname) {
        return UserAccountDto.from(
                userAccountRepository.save(UserAccount.of(username, password, email, nickname, username))
        );
    }

}