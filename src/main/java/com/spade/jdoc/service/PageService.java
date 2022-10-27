package com.spade.jdoc.service;

import com.spade.jdoc.model.Page;
import com.spade.jdoc.model.PageContent;
import com.spade.jdoc.utils.SearchList;
import com.spade.jdoc.utils.SearchPage;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class PageService {

    @PersistenceContext
    EntityManager em;


    public Page findById(int id) {
        return em.find(Page.class, id);
    }

    public SearchList<Page> findAll(SearchPage searchMessage) {
        SearchList<Page> searchList = new SearchList<>();
        searchList.setList(findList(searchMessage));
        searchList.setCount(findTotal(searchMessage));
        return searchList;
    }

    public List<Page> findList(SearchPage searchMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("select u from Page u where u.userId=:userId and u.deleted=0");
        sb.append(" AND u.bookId=:bookId");
        String keyword = searchMessage.getData().get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            sb.append(" AND u.title=:title");
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

        var qb = em.createQuery(sb.toString(), Page.class)
                .setParameter("userId", searchMessage.getUser().getId())
                .setMaxResults(searchMessage.getLength())
                .setFirstResult(searchMessage.getStart());
        qb.setParameter("bookId", searchMessage.getBookId());
        if (keyword != null && !keyword.isEmpty()) {
            qb.setParameter("title", keyword);
        }
        try {
            return qb.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Long findTotal(SearchPage searchMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(u) from Page u where u.userId=:userId and u.deleted=0");
        sb.append(" AND u.bookId=:bookId");
        String keyword = searchMessage.getData().get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            sb.append(" AND u.title=:title");
        }
        var qb = em.createQuery(sb.toString(), Long.class)
                .setParameter("userId", searchMessage.getUser().getId())
                .setMaxResults(1);
        qb.setParameter("bookId", searchMessage.getBookId());
        if (keyword != null && !keyword.isEmpty()) {
            qb.setParameter("title", keyword);
        }
        try {
            return qb.getSingleResult();
        } catch (Exception e) {
            return 0L;
        }
    }

    @Transactional
    public Page create(Page page) {
        return save(page);
    }

    @Transactional
    public Page update(Page page) {
        return save(page);
    }

    @Transactional
    public Page save(Page page) {
        var tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(page);

            String hashCode = String.valueOf(page.getContent().hashCode());
            boolean insertNew = false;
            var newtmp = findLastPageContent(page.getId());
            if (newtmp == null) {
                insertNew = true;
            } else {
                if (newtmp.getHashCode().equals(hashCode)) {
                    insertNew = true;
                    newtmp.setType(3);
                    em.persist(newtmp);
                }
            }
            if (insertNew) {
                PageContent pageContent = new PageContent();
                pageContent.setPageId(page.getId());
                pageContent.setContent(page.getContent());
                pageContent.setHashCode(hashCode);
                pageContent.setType(1);
                pageContent.setCreateAt(page.getUpdateAt());
                pageContent.setDeleted(0);
                em.persist(page);
            }
            em.flush();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
        return page;
    }

    public PageContent findLastPageContent(int pageId) {
        String sb = "select u from PageContent u where u.deleted=0 and u.pageId=:pageId order by u.id desc";
        var qb = em.createQuery(sb, PageContent.class)
                .setParameter("pageId", pageId)
                .setMaxResults(1);
        try {
            return qb.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


}
