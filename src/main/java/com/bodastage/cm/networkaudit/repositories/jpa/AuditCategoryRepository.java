package com.bodastage.cm.networkaudit.repositories.jpa;


import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import com.bodastage.cm.networkaudit.models.AuditCategoryEntity;

public interface AuditCategoryRepository extends CrudRepository<AuditCategoryEntity, Long> {
	Collection<AuditCategoryEntity> findAll();
	
	Collection<AuditCategoryEntity> findByParentPk(Long parentPk );
	
	Collection<AuditCategoryEntity> findByNameContainingIgnoreCase(String name);
	
	AuditCategoryEntity findByPk(Long pk);
	
	//@Query("select count(r) from AuditRuleEntity r where r.categoryPk = ?1")
	Collection<AuditCategoryEntity> findDistinctAuditCategoryEntityByAuditRuleEntities_NameContainingIgnoreCase(String name);
	
	Collection<AuditCategoryEntity> findDistinctAuditCategoryEntityByNameContainingIgnoreCaseOrAuditRuleEntities_NameContainingIgnoreCase(String catName, String ruleName);
}