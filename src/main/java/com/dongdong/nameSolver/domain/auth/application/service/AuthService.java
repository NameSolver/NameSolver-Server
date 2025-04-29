package com.dongdong.nameSolver.domain.auth.application.service;

import com.dongdong.nameSolver.domain.auth.application.dto.KeyDto;
import com.dongdong.nameSolver.domain.auth.application.dto.SignInDto;
import com.dongdong.nameSolver.domain.auth.application.dto.SignUpDto;
import com.dongdong.nameSolver.domain.auth.domain.repository.AuthRepository;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import com.dongdong.nameSolver.global.util.RedisUtil;
import com.dongdong.nameSolver.global.util.WebDriverUtil;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;

    public KeyDto generateKey(String solvedacName) {
        // TODO: 이미 있는 회원인지 확인

        // 랜덤 키 생성
        String randomKey = UUID.randomUUID().toString().substring(0, 8);

        // 키 redis에 저장 10분 동안 유효
        redisUtil.setData(solvedacName, randomKey, 600000L);

        // 키 반환
        return KeyDto.builder().key(randomKey).build();
    }

    @Transactional
    public void signUp(SignUpDto signUpDto) {
        // redis 에서 키 가져오기
        String key = redisUtil.getData(signUpDto.getSolvedacName());

        // solved.ac 크롤링 해서 맞는지 확인하기
        String crawledKey = extractKey(signUpDto.getSolvedacName());

        if(!key.equals(crawledKey)) {
            throw new RuntimeException("solvedAC 인증에 실패했습니다.");
        }

        // 이미 있는 회원인지 확인
        if(memberRepository.existsBySolvedacName(signUpDto.getSolvedacName())) {
            throw new RuntimeException("이미 가입한 회원입니다.");
        }

        // 아이디 중복 확인
        if(checkIdDuplication(signUpDto.getId())) {
            throw new RuntimeException("아이디가 중복됩니다.");
        }

        // 비밀번호 암호화
        String hashedPassword = passwordEncoder.encode(signUpDto.getPassword());

        // 유저 정보 저장
        Member member = Member.join(signUpDto, hashedPassword);

        // TODO: redis 에서 삭제
        memberRepository.save(member);
    }

    public String signIn(SignInDto signInDto) {
        // 아이디 확인

        // 비밀번호 일치 확인

        // 토큰 발급

        // 토큰 리턴
        return null;
    }

    public boolean checkIdDuplication(String id) {
        return memberRepository.existsById(id);
    }

    public String extractKey(String solvedacName) {
        WebDriver driver = WebDriverUtil.getChromeDriver();

        if (!ObjectUtils.isEmpty(driver)) {
            driver.get("https://solved.ac/profile/" + solvedacName);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            WebElement element = driver.findElement(By.cssSelector("#__next > div.css-1s1t70h > div.css-1948bce > div:nth-child(4) > div.css-0 > span"));
            return element.getText();
        }
        else {
            throw new RuntimeException("셀레니움 연결 실패");
        }
    }
}
