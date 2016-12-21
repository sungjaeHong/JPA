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
    public static void main(String[] args){
        //엔티티 매니저 팩토리 생성
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
        }
    }

    private static void logic(EntityManager em){
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
        System.out.println("findMember=" + findMember.getUsername() + ", age="+ findMember.getAge());

        //목록 조회
        List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
        System.out.println("members.size=" + members.size());

        em.remove(member);
    }
}
