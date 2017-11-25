package com.bodastage.cm.managedobjects.repositories;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import com.bodastage.cm.managedobjects.models.ManagedObjectSchemaEntity;

public interface ManagedObjectSchemaRepository extends CrudRepository<ManagedObjectSchemaEntity, Long> {
	Collection<ManagedObjectSchemaEntity> findAll();
	Collection<ManagedObjectSchemaEntity> findByVendorPk(Long vendorPk);
	Collection<ManagedObjectSchemaEntity> findByTechnologyPk(Long technologyPk);
	ManagedObjectSchemaEntity findByPk(Long pk);
	ManagedObjectSchemaEntity findBySchemaName(String schemaName);
	Collection<ManagedObjectSchemaEntity> findByVendorPkAndTechnologyPk(Long vendorPk, Long technologyPk);
}
