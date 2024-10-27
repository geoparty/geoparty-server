package com.geoparty.spring_boot.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/s3")
public class TestController {

    @PostMapping
    public ResponseEntity<?> test(@RequestBody S3Test request) throws IOException {

        return ResponseEntity.ok().build();
    }
}