package com.spade.jdoc.service;

import com.spade.jdoc.model.User;
import com.spade.jdoc.model.UserToken;
import com.spade.jdoc.utils.CommonUtils;
import com.spade.jdoc.utils.SearchList;
import com.spade.jdoc.utils.SearchMessage;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
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


    public SearchList<User> findAll(SearchMessage searchMessage) {
        SearchList<User> searchList = new SearchList<>();
        searchList.setList(findList(searchMessage));
        searchList.setCount(findTotal(searchMessage));
        return searchList;
    }

    public List<User> findList(SearchMessage searchMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("select u from User u where u.deleted=0");
        String keyword = searchMessage.getData().get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            sb.append(" AND u.username=:username");
        }
        if (searchMessage.getSortData().size() > 0) {
            sb.append(" ORDER BY");
            for (Map.Entry<String, String> entry : searchMessage.getSortData().entrySet()) {
                sb.append(" ").append(entry.getKey())
                        .append(" ").append(entry.getValue());
            }
        } else {
            sb.append(" ORDER BY id DESC");
        }

        var qb = em.createQuery(sb.toString(), User.class)
                .setMaxResults(searchMessage.getLength())
                .setFirstResult(searchMessage.getStart());
        if (keyword != null && !keyword.isEmpty()) {
            qb.setParameter("username", keyword);
        }
        try {
            return qb.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Long findTotal(SearchMessage searchMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(u) from User u where u.deleted=0");
        String keyword = searchMessage.getData().get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            sb.append(" AND u.username=:username");
        }
        var qb = em.createQuery(sb.toString(), Long.class)
                .setMaxResults(1);
        if (keyword != null && !keyword.isEmpty()) {
            qb.setParameter("username", keyword);
        }
        try {
            return qb.getSingleResult();
        } catch (Exception e) {
            return 0L;
        }
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
