package com.bodastage.cm.networkaudit.hateoas;

import org.springframework.hateoas.ResourceSupport;

import com.bodastage.cm.networkaudit.controllers.NetworkAuditRestController;
import com.bodastage.cm.networkaudit.models.AuditRuleEntity;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class AuditRuleResource extends ResourceSupport{
	private  AuditRuleEntity auditRule;
	
	public AuditRuleResource(AuditRuleEntity ruleEntity) {
		auditRule = ruleEntity;
		auditRule.setAuditCategoryEntity(null);
		
		this.add(
				linkTo(NetworkAuditRestController.class)
					.withRel("auditRules")
			);
		
		this.add(
				linkTo(
					methodOn(NetworkAuditRestController.class)
						.getAuditRule(ruleEntity.getPk())
				)
				.withSelfRel()
			);
	}
	
	public AuditRuleEntity getAuditRule(){
		return this.auditRule;
	}
}

