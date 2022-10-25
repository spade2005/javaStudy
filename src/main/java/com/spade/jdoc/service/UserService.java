package com.spade.jdoc.service;

import com.spade.jdoc.model.User;
import com.spade.jdoc.model.UserToken;
import com.spade.jdoc.utils.CommonUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Map;

@Service
public class UserService {

    @PersistenceContext
    EntityManager em;


    public User findByUserName(String username) {
        String sql = "select u from User u where u.username=:username and u.deleted=0";
        var qb = em.createQuery(sql, User.class)
                .setParameter("username", username)
                .setMaxResults(1);
        try {
            return qb.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User findById(int id) {
        return em.find(User.class, id, Map.of("deleted", 111111,"status",1));
    }

    @Transactional
    public User createUser(User user) {
        em.persist(user);
        em.flush();
        return user;
    }

    /**
     * userToken
     **/


    @Transactional
    public UserToken getUserToken(User user) {
        UserToken userToken = findTokenByUserId(user.getId());
        if (userToken == null) {
            userToken = createToken(user);
        }
        return userToken;
    }

    public UserToken findTokenByUserId(int userId) {
        String sql = "select u from UserToken u where u.userId=:userId and u.deleted=0";
        var qb = em.createQuery(sql, UserToken.class)
                .setParameter("userId", userId).setMaxResults(1);
        try {
            return qb.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public UserToken createToken(User user) {
        Long time = CommonUtils.getTime();
        UserToken userToken = new UserToken();
        userToken.setUserId(user.getId());
        userToken.setToken("");
        userToken.setExpireAt(time + 3600);
        userToken.setIp("");
        userToken.setCreateAt(time);
        userToken.setUpdateAt(time);
        userToken.setDeleted(0);
        em.persist(userToken);
        em.flush();
        if (userToken.getId() > 0) {
            return userToken;
        }
        return null;
    }

    @Transactional
    public UserToken refreshToken(UserToken userToken) {
        //gen str
        userToken.setToken(CommonUtils.getRandomStr());
        userToken.setExpireAt(CommonUtils.getTime() + 3600);
        em.persist(userToken);
        em.flush();
        return userToken;
    }

    public UserToken findToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        String sql = "select u from UserToken u where u.token=:token and u.deleted=0";
        var qb = em.createQuery(sql, UserToken.class)
                .setParameter("token", token).setMaxResults(1);
        try {
            return qb.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public UserToken removeToken(UserToken userToken) {
        if (userToken == null) {
            return null;
        }
        userToken.setToken("");
        userToken.setExpireAt(CommonUtils.getTime());
        em.persist(userToken);
        em.flush();
        return userToken;
    }


}
