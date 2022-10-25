package com.spade.jdoc.repository;

import com.spade.jdoc.model.PageType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageTypeRepository extends CrudRepository<PageType,Integer> {
}
