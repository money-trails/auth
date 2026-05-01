package com.money.trails.auth.service;

import com.money.trails.auth.conatants.AuditLogStatus;
import com.money.trails.auth.dto.AuthResponse;
import com.money.trails.auth.dto.SigninRequest;
import com.money.trails.auth.dto.SignupRequest;
import com.money.trails.auth.model.AuditLog;
import com.money.trails.auth.model.KafkaEvent;
import com.money.trails.auth.model.User;
import com.money.trails.auth.kafka.NotifyProducerService;
import com.money.trails.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final NotifyProducerService producerService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AuthResponse signin(SigninRequest request) {
        log.info("Sign in request={}", request);
        try {
            User user = userRepository.findByEmail(request.email())
                    .orElseThrow();
            log.info("Found user={}", user);
            auditLogService.log(AuditLog.builder()
                    .event("signin")
                    .message("successful signin")
                    .status(AuditLogStatus.SUCCESS)
                    .build()
            );
            producerService.publishNotify(KafkaEvent.builder()
                    .type("SIGNIN")
                    .event(request.toString())
                    .build()
            );
            return new AuthResponse("some-token");
        } catch (Exception ex) {
            producerService.publishNotify(KafkaEvent.builder()
                    .type("SIGNIN")
                    .event("{message: failed signin}")
                    .build()
            );
            auditLogService.log(AuditLog.builder()
                    .event("signin")
                    .message("failed signin message=" + ex.getMessage())
                    .status(AuditLogStatus.FAILED)
                    .build()
            );
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AuthResponse signup(SignupRequest request) {
        log.info("Sign up request={}", request);
        try {
            userRepository.save(convertSignupDTOToEntity(request));
            auditLogService.log(AuditLog.builder()
                    .event("signup")
                    .message("successful signup")
                    .status(AuditLogStatus.SUCCESS)
                    .build()
            );
            producerService.publishNotify(KafkaEvent.builder()
                    .type("SIGNUP")
                    .event(request.toString())
                    .build()
            );
            return null;
        } catch (Exception ex) {
            auditLogService.log(AuditLog.builder()
                    .event("signup")
                    .message("failed signup message=" + ex.getMessage())
                    .status(AuditLogStatus.FAILED)
                    .build()
            );
            producerService.publishNotify(KafkaEvent.builder()
                    .type("SIGNUP")
                    .event("{message: failed signup}")
                    .build()
            );
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "FAILED: " + ex.getMessage());
        }
    }

    private User convertSignupDTOToEntity(SignupRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();
    }

    private User convertSigninDTOToEntity(SigninRequest request) {
        return User.builder()
                .email(request.email())
                .password(request.password())
                .build();
    }
}