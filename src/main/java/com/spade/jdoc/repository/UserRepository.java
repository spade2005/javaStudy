package com.spade.jdoc.repository;

import com.spade.jdoc.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(value = "select u.* from com_user u where u.username=?1 ;", nativeQuery = true)
    public List<User> findByAll(String username);

    @Query(value = "select u.id as \"id\",u.username as username,u.nick_name as nick_name from com_user u where u.username=?1 ;", nativeQuery = true)
    public List<Map> findByTest(String username);
}
