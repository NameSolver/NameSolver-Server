package com.dongdong.nameSolver.auth;

import com.dongdong.nameSolver.domain.auth.application.service.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CrawlingTest {

    @Autowired
    private AuthService authService;

    @Test
    void 존재하지_않는_유저() {
        Assertions.assertThatThrownBy(() -> {
            authService.extractKey("fffee");
        }).hasMessage("존재하지 않는 solvedAC닉네임입니다.");
    }

    @Test
    void 크롤링_성공() {
        String key = authService.extractKey("lmkn5342");
        Assertions.assertThat(key).isEqualTo("0b55c152");
    }
}
