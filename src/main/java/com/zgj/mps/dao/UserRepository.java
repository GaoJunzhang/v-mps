package com.zgj.mps.dao;

import com.zgj.mps.generator.base.BaseRepository;
import com.zgj.mps.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends BaseRepository<User,Long> {

    List<User> findAllByAccountAndIsDelete(String account, Short isDelete);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update `user` u set u.login_ip = :ip , u.login_time = NOW() where u.id = :id", nativeQuery = true)
    int updateUserLogin(@Param("id") Long id,@Param("ip") String ip);

    User findByAccountAndIsDelete(String account, Short isDelete);
}