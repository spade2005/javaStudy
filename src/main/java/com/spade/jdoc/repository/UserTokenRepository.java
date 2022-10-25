package com.spade.jdoc.repository;

import com.spade.jdoc.model.UserToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends CrudRepository<UserToken,Integer> {
}
