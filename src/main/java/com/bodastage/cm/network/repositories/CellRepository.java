package com.bodastage.cm.network.repositories;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import com.bodastage.cm.network.models.CellEntity;

public interface CellRepository extends CrudRepository<CellEntity, Long> {
	Collection<CellEntity> findAll();
	Collection<CellEntity> findBySitePk( Long sitePk);
	
	/**
	 * @TODO: Query cells by vendor and technology
	Collection<CellEntity> findByVendorPk(Long vendorPk);
	Collection<CellEntity> findByTechnologyPk(Long technologyPk);
	**/
	
	CellEntity findByPk(Long pk);
	CellEntity findByName(String name);
}