package com.dongdong.nameSolver.domain.auth.application.service;

import com.dongdong.nameSolver.domain.auth.application.dto.request.CreateAuthKeyCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.request.ReissueCommand;
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
import org.openqa.selenium.devtools.v85.runtime.Runtime;
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
        String key = redisUtil.getData(signUpCommand.getSolvedacName()).orElseThrow(() -> {
            throw new RuntimeException("인증 시간이 지났습니다.");
        });

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
                .orElseThrow(()-> new RuntimeException("해당하는 유저가 없습니다."));

        // 비밀번호 확인
        if(!passwordEncoder.matches(signInDto.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 불일치합니다.");
        }

        // 토큰 발급
        AuthTokenResponse tokenResponse = jwtTokenHandler.generate(member);

        // 리프레쉬 토큰 저장
        member.storeRefreshToken(tokenResponse.getRefreshToken());
        memberRepository.save(member);

        // 토큰 리턴
        return tokenResponse;
    }

    /**
     * 토큰 재발급 메서드
     */
    @Transactional
    public AuthTokenResponse reissue(ReissueCommand reissueCommand) {
        // 멤버 존재 확인
        Member member = memberRepository.findByMemberId(reissueCommand.getMemberId())
                .orElseThrow(() -> new RuntimeException("해당하는 아이디가 없습니다."));

        // 토큰 일치 확인
        validate(reissueCommand.getRefreshToken(), member);

        // 토큰 재발급
        AuthTokenResponse tokenResponse = jwtTokenHandler.generate(member);
        log.info("Member[id: {}] token is reissued.", member.getId());

        // 토큰 저장
        member.storeRefreshToken(tokenResponse.getRefreshToken());
        return tokenResponse;
    }

    private void validate(String refreshToken, Member member) {
        // 유효한 리프레쉬인지 확인
        jwtTokenHandler.validateRefreshToken(refreshToken);

        // 유저와 일치하는 리프레쉬토큰인지 확인
        if (!member.getRefreshToken().equals(refreshToken)) {
            throw new RuntimeException("리프레시 토큰이 일치하지 않습니다.");
        }
    }

    public boolean checkIdDuplication(String id) {
        return memberRepository.existsById(id);
    }

    /**
     * solvedac 크롤링으로 인증 확인하는 메서드
     */
    public String extractKey(String solvedacName) {

        // 크롬 드라이버 연결
        WebDriver driver = WebDriverUtil.getChromeDriver();

        if(ObjectUtils.isEmpty(driver)) {
            throw new RuntimeException("셀레니움 연결 실패");
        }

        // 유저 프로필 페이지 접속
        driver.get("https://solved.ac/profile/" + solvedacName);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        try {
            WebElement element = driver.findElement(By.cssSelector("#__next > div.css-1s1t70h > div.css-1948bce > div:nth-child(4) > div.css-0 > span"));
            return element.getText();
        } catch (Exception e) {
            throw new RuntimeException("존재하지 않는 solvedAC닉네임입니다.");
        }
    }
}
