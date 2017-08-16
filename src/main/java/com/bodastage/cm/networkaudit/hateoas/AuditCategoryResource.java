package com.bodastage.cm.networkaudit.hateoas;

import org.springframework.hateoas.ResourceSupport;

import com.bodastage.cm.networkaudit.controllers.NetworkAuditRestController;
import com.bodastage.cm.networkaudit.models.AuditCategoryEntity;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class AuditCategoryResource extends ResourceSupport{
	private  AuditCategoryEntity auditCategory;
	
	public AuditCategoryResource(AuditCategoryEntity categoryEntity) {
		auditCategory = categoryEntity;
		
		this.add(
				linkTo(NetworkAuditRestController.class)
					.withRel("auditCategories")
			);
		
		this.add(
				linkTo(
					methodOn(NetworkAuditRestController.class)
						.getAuditCategory(categoryEntity.getPk())
				)
				.withSelfRel()
			);
	}
	
	public AuditCategoryEntity getCategory(){
		return this.auditCategory;
	}
}

