package org.example.plzdrawing.domain.member;

import java.util.Optional;
import org.example.plzdrawing.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m "
            + "where m.email=:email and m.provider=:provider")
    Optional<Member> findByEmailAndProvider(String email, Provider provider);

    @Query("select m from Member m "
            + "where m.role=:role and m.provider=:provider and m.email=:email")
    Optional<Member> findByRoleAndProviderAndEmail(@Param("role") Role role, @Param("provider") Provider provider, @Param("email") String email);


}
