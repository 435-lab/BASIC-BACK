package com.example.basicback.repository.service;

import com.example.basicback.dto.UserDto;
import com.example.basicback.model.DisasterMessage;
import com.example.basicback.model.pk.Message;
import com.example.basicback.model.pk.User;
import com.example.basicback.enums.StatusEnum;
import com.example.basicback.repository.DisasterMessageRepository;
import com.example.basicback.repository.mapping.jpaUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final jpaUserRepository jpaUserRepository;
    private final DisasterMessageRepository repository;

    // 로그인
    public ResponseEntity<Message> login(UserDto userDto) {
        Message message = new Message();

        User user = jpaUserRepository.findById(userDto.getId()).orElse(null);

        // 로그인 성공
        if (user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            user.updateLastLogin();

            if (userDto.getToken() != null && !userDto.getToken().equals(user.getToken())) {
                user.updateToken(userDto.getToken());
            }

            jpaUserRepository.save(user);

            message.setStatus(StatusEnum.OK.getStatusCode());
            message.setResult(true);
            message.setMessage("Success");
            message.setData(user);
        } else if (user == null) {
            // 사용자 존재 X
            message.setStatus(StatusEnum.UNAUTHORIZED.getStatusCode());
            message.setResult(false);
            message.setMessage("No User");
            message.setData(null);
        } else {
            // ID or Password 불일치
            message.setStatus(StatusEnum.UNAUTHORIZED.getStatusCode());
            message.setResult(false);
            message.setMessage("ID or password does not match");
            message.setData(null);
        }

        return ResponseEntity.ok(message);
    }

    // 아이디 중복 검사
    public ResponseEntity<Message> idValidation(String id) {
        Message message = new Message();

        User user = jpaUserRepository.findById(id).orElse(null);

        if (user == null) {
            message.setStatus(StatusEnum.OK.getStatusCode());
            message.setResult(true);
            message.setMessage("Success");
        } else {
            message.setStatus(StatusEnum.CONFLICT.getStatusCode());
            message.setResult(false);
            message.setMessage("Duplicate ID.");
        }

        return ResponseEntity.ok(message);
    }

    // 회원가입
    public ResponseEntity<Message> register(UserDto userDto) {
        Message message = new Message();

        User user = jpaUserRepository.findById(userDto.getId()).orElse(null);

        // DB에 사용자가 없으면 회원가입 성공
        if (user == null) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
/*            User newUser = User.builder()
                    .id(userDto.getId())
                    .password(userDto.getPassword())
                    .token(userDto.getToken())
                    .build();
*/
            User newUser = userDto.toEntity();
            jpaUserRepository.save(newUser);

            message.setStatus(StatusEnum.OK.getStatusCode());
            message.setResult(true);
            message.setMessage("Success");
            message.setData(newUser);
        } else {
            // 회원가입 실패 시
            message.setStatus(StatusEnum.BAD_REQUEST.getStatusCode());
            message.setResult(false);
            message.setMessage("Cannot Register");
            message.setData(null);
        }

        return ResponseEntity.ok(message);
    }

    // 사용자 정보 변경
    public ResponseEntity<Message> changeInfo(String id, UserDto userDto) {
        Message message = new Message();
        User user = jpaUserRepository.findById(id).orElse(null);

        if (user != null) {
            user.update(userDto);
            jpaUserRepository.save(user);

            message.setStatus(StatusEnum.OK.getStatusCode());
            message.setResult(true);
            message.setMessage("Success");
            message.setData(user);
        } else {
            message.setStatus(StatusEnum.BAD_REQUEST.getStatusCode());
            message.setResult(false);
            message.setMessage("Cannot Change");
            message.setData(null);
        }

        return ResponseEntity.ok(message);
    }
    // 사용자 정보 조회
    public ResponseEntity<Message> information(String id) {

        Message message = new Message();

        User user = jpaUserRepository.findById(id).orElse(null);

        if (user != null) {

            message.setStatus(StatusEnum.OK.getStatusCode());
            message.setResult(true);
            message.setMessage("Success");
            message.setData(user);
        } else {

            message.setStatus(StatusEnum.BAD_REQUEST.getStatusCode());
            message.setResult(true);
            message.setMessage("Can't get Data");
            message.setData(null);

        }

        return ResponseEntity.ok(message);
    }
}

