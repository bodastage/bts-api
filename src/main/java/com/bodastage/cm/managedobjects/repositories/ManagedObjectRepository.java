package com.bodastage.cm.managedobjects.repositories;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import com.bodastage.cm.managedobjects.models.ManagedObjectEntity;

public interface ManagedObjectRepository extends CrudRepository<ManagedObjectEntity, Long> {
	Collection<ManagedObjectEntity> findAll();
	Collection<ManagedObjectEntity> findByVendorPk(Long vendorPk);
	Collection<ManagedObjectEntity> findByTechnologyPk(Long technologyPk);
	ManagedObjectEntity findByPk(Long pk);
	ManagedObjectEntity findByName(String name);
	Collection<ManagedObjectEntity> findByVendorPkAndTechnologyPk(Long vendorPk, Long technologyPk);
	Collection<ManagedObjectEntity> findByVendorPkAndTechnologyPkAndParentPk(Long vendorPk, Long technologyPk, Long parentPk);
	Collection<ManagedObjectEntity> findByVendorPkAndTechnologyPkAndParentPkAndNameContainingIgnoreCase(Long vendorPk, Long technologyPk, Long parentPk, String name);
	Collection<ManagedObjectEntity> findByVendorPkAndTechnologyPkAndNameContainingIgnoreCase(Long vendorPk, Long technologyPk, String name);
	Collection<ManagedObjectEntity> findByVendorPkAndTechnologyPkAndPkNotAndNameContainingIgnoreCase(Long vendorPk, Long technologyPk, Long Pk, String name);
}
