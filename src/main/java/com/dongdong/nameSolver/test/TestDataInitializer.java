package com.dongdong.nameSolver.test;

import com.dongdong.nameSolver.domain.auth.application.dto.request.SignUpCommand;
import com.dongdong.nameSolver.domain.match.domain.constant.MatchType;
import com.dongdong.nameSolver.domain.match.domain.entity.Match;
import com.dongdong.nameSolver.domain.match.domain.repository.MatchRepository;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestDataInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MatchRepository matchRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        List<Member> members = setUser();
        setMatch(members);
    }

    @Transactional
    public List<Member> setUser() {
        List<Member> memberIds = new ArrayList<>();
        memberIds.add(createUser("김동현", "lmkn"));
        memberIds.add(createUser("김동현", "lmkn5342"));
        memberIds.add(createUser("김동현", "1234"));
        memberIds.add(createUser("김도현", "jlkj"));
        memberIds.add(createUser("기도현", "asdf"));
        return memberIds;
    }

    @Transactional
    public void setMatch(List<Member> members) {
        LocalDateTime localDateTime = LocalDateTime.of(2025, 5, 28, 14, 53);
        Match match1 = new Match(LocalDateTime.of(2025, 5, 25, 12, 30), localDateTime, MatchType.SAME_FULL_NAME, members.get(0), members.get(1), 30, 50);
        Match match2 = new Match(LocalDateTime.of(2025, 5, 25, 12, 30), localDateTime, MatchType.SAME_FULL_NAME, members.get(0), members.get(1), 30, 50);
        Match match3 = new Match(LocalDateTime.of(2025, 5, 25, 12, 30), localDateTime, MatchType.SAME_FULL_NAME, members.get(0), members.get(1), 30, 50);
        Match match4 = new Match(LocalDateTime.of(2025, 5, 25, 12, 30), localDateTime, MatchType.SAME_FULL_NAME, members.get(0), members.get(1), 30, 50);
        Match match5 = new Match(LocalDateTime.of(2025, 5, 25, 12, 30), localDateTime, MatchType.SAME_FULL_NAME, members.get(0), members.get(1), 30, 50);
        Match match6 = new Match(LocalDateTime.of(2025, 5, 25, 12, 30), localDateTime, MatchType.SAME_FULL_NAME, members.get(0), members.get(1), 30, 50);
        Match match7 = new Match(LocalDateTime.of(2025, 5, 25, 12, 30), localDateTime, MatchType.SAME_FULL_NAME, members.get(0), members.get(1), 30, 50);
        Match match8 = new Match(LocalDateTime.of(2025, 5, 25, 12, 30), localDateTime, MatchType.SAME_FULL_NAME, members.get(0), members.get(1), 30, 50);
        Match match9 = new Match(LocalDateTime.of(2025, 5, 25, 12, 30), localDateTime, MatchType.SAME_FULL_NAME, members.get(0), members.get(1), 30, 50);

        matchRepository.save(match1);
        matchRepository.save(match2);
        matchRepository.save(match3);
        matchRepository.save(match4);
        matchRepository.save(match5);
        matchRepository.save(match6);
        matchRepository.save(match7);
        matchRepository.save(match8);
        matchRepository.save(match9);
    }

    private Member createUser(String name, String id) {
        SignUpCommand signUpCommand = new SignUpCommand(name, UUID.randomUUID().toString(), UUID.randomUUID().toString(), id, "5342");
        String hashedPassword = passwordEncoder.encode(signUpCommand.getPassword());
        return memberRepository.save(Member.join(signUpCommand, hashedPassword));
    }
}