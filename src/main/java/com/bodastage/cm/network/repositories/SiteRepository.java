package com.bodastage.cm.network.repositories;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import com.bodastage.cm.network.models.SiteEntity;

public interface SiteRepository extends CrudRepository<SiteEntity, Long> {
	Collection<SiteEntity> findAll();
	Collection<SiteEntity> findByNodePk( Long nodePk);
	Collection<SiteEntity> findByTechnologyPk( Long technologyPk);
	
	Long countByTechnologyPk(Long technologyPk);
	
	Long countByNodePk(Long nodePk);
	
	/**
	 * @TODO: Query sites by vendor and technology
	Collection<SiteEntity> findByVendorPk(Long vendorPk);
	Collection<SiteEntity> findByTechnologyPk(Long technologyPk);
	**/
	
	SiteEntity findByPk(Long pk);
	SiteEntity findByName(String name);
}