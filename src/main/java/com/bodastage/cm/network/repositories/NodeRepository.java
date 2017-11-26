package com.bodastage.cm.network.repositories;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import com.bodastage.cm.network.models.NodeEntity;

public interface NodeRepository extends CrudRepository<NodeEntity, Long> {
	Collection<NodeEntity> findAll();
	Collection<NodeEntity> findByType(String type);
	Collection<NodeEntity> findByVendor(String vendor);
	Collection<NodeEntity> findByTechnology(String technology);
	
	Long countByType(String type);
	
	NodeEntity findByPk(Long pk);
	NodeEntity findByName(String name);
}