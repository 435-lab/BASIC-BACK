package com.example.basicback.user.repository.service;

import com.example.basicback.user.dto.UserDTO;
import com.example.basicback.user.entity.pk.Message;
import com.example.basicback.user.entity.pk.User;
import com.example.basicback.user.enums.StatusEnum;
import com.example.basicback.user.jwt.JwtUtil;
import com.example.basicback.user.repository.mapping.jpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final jpaUserRepository jpaUserRepository;
    private final JwtUtil jwtUtil;

    public UserService(BCryptPasswordEncoder passwordEncoder, jpaUserRepository jpaUserRepository, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.jpaUserRepository = jpaUserRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<Message> login(UserDTO userDto) {
        Message message = new Message();
        User user = jpaUserRepository.findById(userDto.getId()).orElse(null);

        if (user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            user.updateLastLogin();
            jpaUserRepository.save(user);

            UserDTO loggedInUserDto = user.toDto(); // User 엔티티를 UserDTO로 변환

            message.setStatus(StatusEnum.OK.getStatusCode());
            message.setResult(true);
            message.setMessage("Success");
            message.setData(loggedInUserDto);
        } else {
            message.setStatus(StatusEnum.UNAUTHORIZED.getStatusCode());
            message.setResult(false);
            message.setMessage("Invalid credentials");
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
    public ResponseEntity<Message> register(UserDTO userDto) {
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
    public ResponseEntity<Message> changeInfo(String id, UserDTO userDto) {
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