package org.example.plzdrawing.domain.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m "
            + "where m.email=:email and m.provider=:provider")
    Optional<Member> findByEmailAndProvider(String email, Provider provider);
}
