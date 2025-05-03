package com.dongdong.nameSolver.domain.auth.application.service;

import com.dongdong.nameSolver.domain.auth.application.dto.request.CreateAuthKeyCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.request.SignInCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.request.SignUpCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.response.AuthTokenResponse;
import com.dongdong.nameSolver.domain.auth.application.dto.response.AuthKeyResponse;
import com.dongdong.nameSolver.domain.auth.application.dto.response.SignUpResponse;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import com.dongdong.nameSolver.global.jwt.JwtTokenHandler;
import com.dongdong.nameSolver.global.util.RedisUtil;
import com.dongdong.nameSolver.global.util.WebDriverUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenHandler jwtTokenHandler;

    /**
     * solvedac 사용자 인증용 키 발급 메서드
     */
    public AuthKeyResponse createKey(CreateAuthKeyCommand createAuthKeyCommand) {
        // 랜덤 키 생성
        String randomKey = UUID.randomUUID().toString().substring(0, 8);

        // 키 redis에 저장 10분 동안 유효
        redisUtil.setData(createAuthKeyCommand.getSolvedacName(), randomKey, 600000L);

        // 키 반환
        return new AuthKeyResponse(randomKey);
    }

    /**
     * 회원가입 메서드
     */
    @Transactional
    public SignUpResponse signUp(SignUpCommand signUpCommand) {
        // redis 에서 키 가져오기
        String key = redisUtil.getData(signUpCommand.getSolvedacName());

        // solved.ac 크롤링 해서 맞는지 확인하기
        String crawledKey = extractKey(signUpCommand.getSolvedacName());

        if(!key.equals(crawledKey)) {
            throw new RuntimeException("solvedAC 인증에 실패했습니다.");
        }

        // 이미 있는 회원인지 확인
        if(memberRepository.existsBySolvedacName(signUpCommand.getSolvedacName())) {
            throw new RuntimeException("이미 가입한 회원입니다.");
        }

        // 아이디 중복 확인
        if(checkIdDuplication(signUpCommand.getId())) {
            throw new RuntimeException("아이디가 중복됩니다.");
        }

        // 비밀번호 암호화
        String hashedPassword = passwordEncoder.encode(signUpCommand.getPassword());

        // 유저 정보 저장
        Member member = Member.join(signUpCommand, hashedPassword);
        memberRepository.save(member);

        // redis에서 키 삭제
        redisUtil.deleteData(signUpCommand.getSolvedacName());

        return new SignUpResponse(member.getMemberId().toString());
    }

    /**
     * 로그인 메서드
     */
    @Transactional
    public AuthTokenResponse signIn(SignInCommand signInDto) {
        // 아이디 확인
        Member member = memberRepository.findById(signInDto.getId())
                .orElseThrow(()-> new RuntimeException("해당하는 아이디가 없습니다."));

        // 비밀번호 확인
        if(!passwordEncoder.matches(signInDto.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 불일치합니다.");
        }

        // 토큰 발급
        AuthTokenResponse token = jwtTokenHandler.generate(member);

        // 리프레쉬 토큰 저장
        member.storeRefreshToken(token.getRefreshToken());
        memberRepository.save(member);

        // 토큰 리턴
        return token;
    }

    public boolean checkIdDuplication(String id) {
        return memberRepository.existsById(id);
    }

    /**
     * solvedac 크롤링으로 인증 확인하는 메서드
     */
    private String extractKey(String solvedacName) {
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
