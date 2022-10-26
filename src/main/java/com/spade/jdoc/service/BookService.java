package com.spade.jdoc.service;

import com.spade.jdoc.model.Book;
import com.spade.jdoc.utils.SearchList;
import com.spade.jdoc.utils.SearchMessage;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    @PersistenceContext
    EntityManager em;


    public Book findById(int id) {
        return em.find(Book.class, id);
    }

    public SearchList<Book> findAll(SearchMessage searchMessage) {
        SearchList<Book> searchList = new SearchList<>();
        searchList.setList(findList(searchMessage));
        searchList.setCount(findTotal(searchMessage));
        return searchList;
    }

    public List<Book> findList(SearchMessage searchMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("select u from Book u where u.userId=:userId and u.deleted=0");
        String keyword = searchMessage.getData().get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            sb.append(" AND u.name=:name");
        }
        if (searchMessage.getSortData().size() > 0) {
            sb.append(" ORDER BY");
            for (Map.Entry entry : searchMessage.getSortData().entrySet()) {
                sb.append(" ").append(entry.getKey())
                        .append(" ").append(entry.getValue());
            }
        } else {
            sb.append(" ORDER BY id DESC");
        }

        var qb = em.createQuery(sb.toString(), Book.class)
                .setParameter("userId", searchMessage.getUser().getId())
                .setMaxResults(searchMessage.getLength())
                .setFirstResult(searchMessage.getStart());
        if (keyword != null && !keyword.isEmpty()) {
            qb.setParameter("name", keyword);
        }
        try {
            return qb.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Long findTotal(SearchMessage searchMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(u) from Book u where u.userId=:userId and u.deleted=0");
        String keyword = searchMessage.getData().get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            sb.append(" AND u.name=:name");
        }
        var qb = em.createQuery(sb.toString(), Long.class)
                .setParameter("userId", searchMessage.getUser().getId())
                .setMaxResults(1);
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
    public Book createBook(Book book) {
        return saveBook(book);
    }

    @Transactional
    public Book updateBook(Book book) {
        return saveBook(book);
    }

    @Transactional
    public Book saveBook(Book book) {
        em.persist(book);
        em.flush();
        return book;
    }


}
