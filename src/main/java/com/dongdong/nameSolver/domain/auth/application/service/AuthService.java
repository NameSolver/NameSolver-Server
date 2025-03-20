package com.dongdong.nameSolver.domain.auth.application.service;

import com.dongdong.nameSolver.domain.auth.application.dto.KeyDto;
import com.dongdong.nameSolver.domain.auth.domain.repository.AuthRepository;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final MemberRepository memberRepository;

    public KeyDto generateKey(String name) {
        // 랜덤 키 생성
        UUID randomKey = UUID.randomUUID();

        // 키 DB에 저장
        authRepository.save(randomKey, name);

        // 키 반환
        return KeyDto.builder().key(randomKey).build();
    }

    public void signIn() {
        // solved.ac 닉네임, 키, 비밀번호 가져오기
        String name = "";
        String password = "";
        // DB에서 키 가져오기
        UUID key = authRepository.findKeyByName(name);
        // solved.ac 크롤링 해서 맞는지 확인하기

        // 비밀번호 암호화

        // 유저 정보 저장
        memberRepository.save(name, password);
        // 되면 true 반환
    }

    public String extractKey(String name) throws IOException {
        Document doc = Jsoup.connect("https://solved.ac/profile/" + name).get();
        System.out.println(doc.title());
        Elements profile = doc.selectXpath("//*[@id=\"__next\"]/div[3]/div[2]/div[4]/div[1]/span");
        return profile.get(0).text();
    }
}
