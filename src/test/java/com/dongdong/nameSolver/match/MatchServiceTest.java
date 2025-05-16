package com.dongdong.nameSolver.match;

import com.dongdong.nameSolver.domain.auth.application.dto.request.SignUpCommand;
import com.dongdong.nameSolver.domain.match.application.dto.request.CreateMatchCommand;
import com.dongdong.nameSolver.domain.match.application.dto.response.MatchResponse;
import com.dongdong.nameSolver.domain.match.application.service.MatchService;
import com.dongdong.nameSolver.domain.match.domain.constant.MatchType;
import com.dongdong.nameSolver.domain.match.domain.entity.Match;
import com.dongdong.nameSolver.domain.match.domain.entity.MatchCandidate;
import com.dongdong.nameSolver.domain.match.domain.repository.MatchRepository;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import com.dongdong.nameSolver.test.TestDataHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@SpringBootTest
@Import({TestDataHelper.class})
public class MatchServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TestDataHelper testDataHelper;

    private UUID memberId;
    private UUID accepterId;

//    @Transactional
//    void 유저_세팅() {
//        //회원 생성
//        SignUpCommand signUpCommand1 = new SignUpCommand("김동현", "lmkn", "abcd", "lmkn5342", "asdf");
//        String hashedPassword1 = passwordEncoder.encode(signUpCommand1.getPassword());
//        memberId = memberRepository.save(Member.join(signUpCommand1, hashedPassword1)).getMemberId();
//
//        SignUpCommand signUpCommand2 = new SignUpCommand("김동현", "lmkn", "qwer", "qwer", "asdf");
//        String hashedPassword2 = passwordEncoder.encode(signUpCommand2.getPassword());
//        accepterId = memberRepository.save(Member.join(signUpCommand2, hashedPassword2)).getMemberId();
//
//        SignUpCommand signUpCommand3 = new SignUpCommand("김동현", "lmkn", "test", "aabbcc", "asdf");
//        String hashedPassword3 = passwordEncoder.encode(signUpCommand3.getPassword());
//        memberRepository.save(Member.join(signUpCommand3, hashedPassword3));
//
//        SignUpCommand signUpCommand4 = new SignUpCommand("김도현", "1234", "1234", "1234", "asdf");
//        String hashedPassword4 = passwordEncoder.encode(signUpCommand4.getPassword());
//        memberRepository.save(Member.join(signUpCommand4, hashedPassword4));
//
//        SignUpCommand signUpCommand5 = new SignUpCommand("기도현", "asdf", "gdeff", "hhgg", "asdf");
//        String hashedPassword5 = passwordEncoder.encode(signUpCommand5.getPassword());
//        memberRepository.save(Member.join(signUpCommand5, hashedPassword5));
//    }

    @Test
    @Transactional
    void 대결_생성_동명이인() {
        // 동명이인끼리 대결 생성
        matchService.createMatch(memberId, new CreateMatchCommand(MatchType.SAME_FULL_NAME));

        // 동명이인들한테 대결 요청갔는지 확인
        List<MatchCandidate> byMatchType = matchRepository.findByMatchType(MatchType.SAME_FULL_NAME);
        Assertions.assertThat(byMatchType.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void 대결_생성_동성() {
        // 같은 성씨끼리 대결 생성
        matchService.createMatch(memberId, new CreateMatchCommand(MatchType.SAME_LAST_NAME));

        // 동성한테 대결 요청갔는지 확인
        List<MatchCandidate> byMatchType = matchRepository.findByMatchType(MatchType.SAME_LAST_NAME);
        Assertions.assertThat(byMatchType.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    void 대결_수락() {
        // 동명이인끼리 대결 생성
        MatchResponse match = matchService.createMatch(memberId, new CreateMatchCommand(MatchType.SAME_FULL_NAME));

        // 대결 수락
        matchService.acceptMatch(accepterId, match.getId());

        // 수락 확인
        Match acceptedMatch = matchRepository.findMatchById(match.getId()).get();
        Assertions.assertThat(acceptedMatch.getAccepter().getMemberId()).isEqualTo(accepterId);
    }

    @Test
    void 대결_수락_경쟁_상황() throws InterruptedException {
        List<UUID> memberIds = testDataHelper.setUser();

        // 동명이인끼리 대결 생성
        MatchResponse match = matchService.createMatch(memberIds.get(0), new CreateMatchCommand(MatchType.SAME_FULL_NAME));

        // 스레드 생성
        int nThread = 5;

        ExecutorService executorService = Executors.newFixedThreadPool(nThread);
        CountDownLatch countDownLatch = new CountDownLatch(nThread);
        List<Callable<Boolean>> requests = memberIds.stream().map(id -> (Callable<Boolean>) new ParticipateMatch(match.getId(), id, countDownLatch)).toList();

        List<Future<Boolean>> futures = executorService.invokeAll(requests);

        long count = futures.stream().map(e -> {
            try {
                return e.get();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            } catch (ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        }).filter(e -> e).count();

        Assertions.assertThat(count).isNotEqualTo(1L);
    }

    private class ParticipateMatch implements Callable<Boolean> {
        private Long matchId;
        private UUID memberId;
        private CountDownLatch countDownLatch;

        public ParticipateMatch(Long matchId, UUID memberId, CountDownLatch countDownLatch) {
            this.matchId = matchId;
            this.memberId = memberId;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public Boolean call() throws Exception {
            boolean result = matchService.acceptMatch(memberId, matchId);
            countDownLatch.countDown();
            return result;
        }
    }
}
