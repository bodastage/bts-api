package com.bodastage.cm.networkaudit.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bodastage.cm.networkaudit.ACINode;
import com.bodastage.cm.networkaudit.hateoas.AuditCategoryResource;
import com.bodastage.cm.networkaudit.models.AuditCategoryEntity;
import com.bodastage.cm.networkaudit.models.AuditRuleEntity;
import com.bodastage.cm.networkaudit.repositories.AuditCategoryRepository;
import com.bodastage.cm.networkaudit.repositories.AuditRuleRepository;

@RestController
@RequestMapping("/api/networkaudit")
public class NetworkAuditRestController {

	private static final Logger logger = LoggerFactory.getLogger(NetworkAuditRestController.class);

	@Autowired
	private AuditRuleRepository auditRuleRepository;

	@Autowired
	private AuditCategoryRepository auditCategoryRepository;

	/**
	 * Get the aciTree entries for the rules and categories.
	 * 
	 * @param parentPk
	 * @param searchRules
	 * @param searchCategories
	 * @param parentPk
	 * @param searchTerm
	 * @return List<ACINode>
	 */
	@RequestMapping(value = "/acitree/categories/{parentPk}", method = RequestMethod.GET)
	public @ResponseBody List<ACINode> getCategoriesACINodes(@PathVariable Long parentPk,
			@RequestParam(value = "searchTerm", required = false) String searchTerm,
			@RequestParam(value = "searchCategories", required = false) String searchCategories,
			@RequestParam(value = "searchRules", required = false) String searchRules) {
		List<ACINode> aciTreeEntries = new ArrayList<ACINode>();

		Iterator<AuditCategoryEntity> iter = null;

		// Category searches
		if (!searchTerm.equals("") && searchCategories.equals("true")) {
			iter = auditCategoryRepository.findByNameContainingIgnoreCase(searchTerm).iterator();
		} else if (!searchTerm.equals("") && searchCategories.equals("false") && searchRules.equals("false")) {
			iter = auditCategoryRepository
					.findDistinctAuditCategoryEntityByNameContainingIgnoreCaseOrAuditRuleEntities_NameContainingIgnoreCase(
							searchTerm, searchTerm)
					.iterator();
		} else if (!searchTerm.equals("") && searchCategories.equals("false") && searchRules.equals("true")) {
			iter = auditCategoryRepository
					.findDistinctAuditCategoryEntityByAuditRuleEntities_NameContainingIgnoreCase(searchTerm).iterator();
		} else {
			iter = auditCategoryRepository.findByParentPk(parentPk).iterator();
		}

		while (iter.hasNext()) {
			AuditCategoryEntity e = iter.next();
			ACINode aciNode = new ACINode();
			aciNode.setId(e.getPk().toString());
			aciNode.setLabel(e.getName());
			aciNode.setInode(true);
			aciNode.setNodeType("category");
			aciTreeEntries.add(aciNode);
		}
		return aciTreeEntries;
	}

	/**
	 * Get the aciTree entries for the rules .
	 * 
	 * @param parentPk
	 * @param searchRules
	 * @param searchCategories
	 * @param parentPk
	 * @param searchTerm
	 * @return List<ACINode>
	 */
	@RequestMapping(value = "/acitree/rules/{parentPk}", method = RequestMethod.GET)
	public @ResponseBody List<ACINode> getRulesACINodes(@PathVariable Long parentPk,
			@RequestParam(value = "searchTerm", required = false) String searchTerm,
			@RequestParam(value = "searchCategories", required = false) String searchCategories,
			@RequestParam(value = "searchRules", required = false) String searchRules) {
		List<ACINode> aciTreeEntries = new ArrayList<ACINode>();

		Iterator<AuditRuleEntity> iter = null;

		// Category searches
		if (!searchTerm.equals("") && searchRules.equals("true")) {
			iter = auditRuleRepository.findByCategoryPkAndNameContainingIgnoreCase(parentPk, searchTerm).iterator();
		} else if (!searchTerm.equals("") && searchRules.equals("false") && searchCategories.equals("false")) {
			iter = auditRuleRepository.findByCategoryPkAndNameContainingIgnoreCase(parentPk, searchTerm).iterator();
		} else {
			iter = auditRuleRepository.findByCategoryPk(parentPk).iterator();
		}

		while (iter.hasNext()) {
			AuditRuleEntity e = iter.next();
			ACINode aciNode = new ACINode();
			aciNode.setId(e.getPk().toString());
			aciNode.setLabel(e.getName());
			aciNode.setInode(false);
			aciNode.setNodeType("rule");
			aciTreeEntries.add(aciNode);
		}
		return aciTreeEntries;

	}

	/**
	 * Get a category
	 * 
	 * @param categoryPk
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/category/{categoryPk}")
	public com.bodastage.cm.networkaudit.hateoas.AuditCategoryResource getAuditCategory(@PathVariable Long categoryPk) {
		try {
			return new AuditCategoryResource(this.auditCategoryRepository.findByPk(categoryPk));
		} catch (Exception e) {
			return null;
		}

	}
}
