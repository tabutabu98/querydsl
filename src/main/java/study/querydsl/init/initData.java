package study.querydsl.init;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.domain.Member;
import study.querydsl.domain.Team;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile({"local"})
@Component
@RequiredArgsConstructor
public class initData {

    private final InitMemberService initMemberService;

    @PostConstruct
    public void init() {
        initMemberService.initMembers();
    }

    @Component
    static class InitMemberService {

        @PersistenceContext
        private EntityManager em;


        @Transactional
        public void initMembers() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            em.persist(teamA);
            em.persist(teamB);

            for (int i = 0; i < 100; i++) {
                Team selectedTeam = i % 2 == 0 ? teamA : teamB;
                em.persist(new Member("member" + i, i, selectedTeam));
            }
        }
    }

}
