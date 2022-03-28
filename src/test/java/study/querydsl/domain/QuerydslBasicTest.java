package study.querydsl.domain;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.domain.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    void before(){
        jpaQueryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    void startJPQL() throws Exception {
        // member1 찾기
        String qlString =
                "select m from Member m " +
                "where m.username = :username";
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void startQuerydsl() throws Exception {
        Member findMember = jpaQueryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void search() throws Exception {
        Member findMember = jpaQueryFactory.selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void searchAndParam() throws Exception {
        Member findMember = jpaQueryFactory.selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.between(10, 30)
                )
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
    
    @Test
    public void resultFetch() throws Exception {
        // 리스트 조회
//        List<Member> fetch = jpaQueryFactory.selectFrom(member)
//                .fetch();
//
        // 단 건 조회
//        Member fetchOne = jpaQueryFactory.selectFrom(QMember.member)
//                .fetchOne();
//
        // limit(1).fetchOne()
//        Member fetchFirst = jpaQueryFactory.selectFrom(QMember.member)
//                .fetchFirst();

        // 페이징 정보 포함
//        QueryResults<Member> results = jpaQueryFactory.selectFrom(member)
//                .fetchResults();
//        results.getTotal(); // total을 위한 count query 를 날린다.
//        List<Member> content = results.getResults();

        long total = jpaQueryFactory.selectFrom(member)
                .fetchCount();
    }    
}
