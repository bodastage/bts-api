package com.bodastage.cm.technologies.repositories;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import com.bodastage.cm.technologies.models.TechnologyEntity;
import com.bodastage.cm.vendors.models.VendorEntity;

public interface TechnologyRepository extends CrudRepository<TechnologyEntity, Long> {
	Collection<TechnologyEntity> findAll();
	
	TechnologyEntity findByPk(Long pk);
	TechnologyEntity findByName(String name);
}