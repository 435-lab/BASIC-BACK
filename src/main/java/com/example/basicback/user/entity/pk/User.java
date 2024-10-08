
package com.example.basicback.user.entity.pk;

import com.example.basicback.user.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "user")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "id", length = 60, nullable = false)
    @NotBlank
    private String id;

    @Column(name = "password", length = 60, nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "token")
    private String token;

    // 회원 정보 변경을 위한 update 메서드 수정
    public void update(UserDTO userDto) {
        if (userDto.getBirth() != null) {
            this.birth = userDto.getBirth();
        }
        if (userDto.getName() != null) {
            this.name = userDto.getName();
        }
        if (userDto.getGender() != null) {
            this.gender = userDto.getGender(); // 문자열로 설정
        }
    }

    // 사용자 로그인 시간 변경
    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now().withNano(0);
    }

    public void updateToken(String token) {
        this.token = token;
    }

    public UserDTO toDto() {
        return UserDTO.builder()
                .id(this.id)
                .birth(this.birth)
                .name(this.name)
                .gender(this.gender)
                .lastLogin(this.lastLogin)
                .build();
    }

}
