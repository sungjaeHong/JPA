package com.tistory.devhong;

import org.h2.command.dml.Select;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Created by devHong on 2016. 12. 21..
 */
public class JpaMain {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");

    public static void main(String[] args) {
        // 1차 예제

        /*//엔티티 매니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        //엔티티 매니저 생성
        EntityManager em = emf.createEntityManager();
        //트랜잭션 획득
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            logic(em);
            tx.commit();
        } catch(Exception e){
            tx.rollback();
        } finally {
            em.close();
        }*/
        ;
        Member member = createMember("memberA", "회원");
        member.setUsername("회원명변경");
        mergeMember(member);

    }

    private static void logic(EntityManager em) {
        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("성재");
        member.setAge(2);

        //등록
        em.persist(member);

        //수정
        member.setAge(28);
        member.setUsername("홍성재");
        //한건 조회
        Member findMember = em.find(Member.class, id);
        System.out.println("findMember=" + findMember.getUsername() + ", age=" + findMember.getAge());

        //목록 조회
        List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
        System.out.println("members.size=" + members.size());

        //em.remove(member);
    }

    static Member createMember(String id, String username) {
        //===영속성 컨텍스트1 시작===///
        System.out.println("id : " + id + ", username : " + username);
        System.out.println(emf.getProperties());
        EntityManager em1 = emf.createEntityManager();
        EntityTransaction tx1 = em1.getTransaction();
        Member member = new Member();

        tx1.begin();
        try {
            member.setId(id);
            member.setUsername(username);
            em1.persist(member);

            tx1.commit();
        } catch (Exception e) {
            if (tx1.isActive())
                tx1.rollback();
        }

        em1.close();    //영속성 컨텍스트 종료
                        //member 엔티티는 준영속 상태가 됨
        //===영속성 컨텍트스1 종료===//
        return member;
    }

    public static void mergeMember(Member member) {
        //===영속성 컨텍스트 시작===//
        EntityManager em2 = emf.createEntityManager();
        EntityTransaction tx2 = em2.getTransaction();

        tx2.begin();
        try {
            Member mergeMember = em2.merge(member);
            //준영속 상태
            System.out.println("member = " + member.getUsername());

            //영속 상태
            System.out.println("mergeMember = " + mergeMember.getUsername());

            System.out.println("em2 contains member = " + em2.contains(member));
            System.out.println("em2 contains mergeMember = " + em2.contains(mergeMember));  //준영속상태의 객체를 merge했더니 영속상태로 되엇다.
        }catch(Exception e){
            if (tx2.isActive())
                tx2.rollback();

        }
        em2.close();
        //===영속성 컨텍스트2 종료===//
    }

}
