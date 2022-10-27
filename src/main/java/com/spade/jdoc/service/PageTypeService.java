package com.spade.jdoc.service;

import com.spade.jdoc.model.PageType;
import com.spade.jdoc.utils.SearchList;
import com.spade.jdoc.utils.SearchPage;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class PageTypeService {

    @PersistenceContext
    EntityManager em;


    public PageType findById(int id) {
        return em.find(PageType.class, id);
    }

    public SearchList<PageType> findAll(SearchPage searchMessage) {
        SearchList<PageType> searchList = new SearchList<>();
        searchList.setList(findList(searchMessage));
        searchList.setCount(findTotal(searchMessage));
        return searchList;
    }

    public List<PageType> findList(SearchPage searchMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("select u from PageType u where u.userId=:userId and u.deleted=0");
        sb.append(" AND u.bookId=:bookId");
        String keyword = searchMessage.getData().get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            sb.append(" AND u.name=:name");
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

        var qb = em.createQuery(sb.toString(), PageType.class)
                .setParameter("userId", searchMessage.getUser().getId())
                .setMaxResults(searchMessage.getLength())
                .setFirstResult(searchMessage.getStart());
        qb.setParameter("bookId",searchMessage.getBookId());
        if (keyword != null && !keyword.isEmpty()) {
            qb.setParameter("name", keyword);
        }
        try {
            return qb.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Long findTotal(SearchPage searchMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(u) from PageType u where u.userId=:userId and u.deleted=0");
        sb.append(" AND u.bookId=:bookId");
        String keyword = searchMessage.getData().get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            sb.append(" AND u.name=:name");
        }
        var qb = em.createQuery(sb.toString(), Long.class)
                .setParameter("userId", searchMessage.getUser().getId())
                .setMaxResults(1);
        qb.setParameter("bookId",searchMessage.getBookId());
        if (keyword != null && !keyword.isEmpty()) {
            qb.setParameter("name", keyword);
        }
        try {
            return qb.getSingleResult();
        } catch (Exception e) {
            return 0L;
        }
    }

    @Transactional
    public PageType create(PageType pageType) {
        return save(pageType);
    }

    @Transactional
    public PageType update(PageType pageType) {
        return save(pageType);
    }

    @Transactional
    public PageType save(PageType pageType) {
        em.persist(pageType);
        em.flush();
        return pageType;
    }


}
