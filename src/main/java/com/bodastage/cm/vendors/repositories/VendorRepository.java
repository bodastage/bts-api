package com.bodastage.cm.vendors.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bodastage.cm.vendors.models.VendorEntity;

public interface VendorRepository extends CrudRepository<VendorEntity, Long> {
	Collection<VendorEntity> findAll();
	
	@Query("select u from VendorEntity u where u.pk = :pk")
	Optional<VendorEntity> findByVendorId(@Param("pk") Long pk);
	
	VendorEntity findByPk(Long pk);
	VendorEntity findByName(String name);
}