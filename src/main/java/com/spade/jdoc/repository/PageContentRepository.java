package com.spade.jdoc.repository;

import com.spade.jdoc.model.PageContent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageContentRepository extends CrudRepository<PageContent,Integer> {
}
