package com.bodastage.cm.networkaudit.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.validation.Valid;
import javax.xml.ws.Response;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bodastage.cm.common.CustomDTQueries;
import com.bodastage.cm.common.DBTableColumn;
import com.bodastage.cm.networkaudit.ACINode;
import com.bodastage.cm.networkaudit.hateoas.AuditCategoryResource;
import com.bodastage.cm.networkaudit.models.AuditCategoryEntity;
import com.bodastage.cm.networkaudit.models.AuditRuleEntity;
import com.bodastage.cm.networkaudit.repositories.jpa.AuditCategoryRepository;
import com.bodastage.cm.networkaudit.repositories.jpa.AuditRuleRepository;
import com.fasterxml.jackson.annotation.JsonView;
import com.bodastage.cm.networkaudit.hateoas.AuditCategoryResource;
import com.bodastage.cm.networkaudit.hateoas.AuditRuleResource;

@RestController
@RequestMapping("/api/networkaudit")
public class NetworkAuditRestController {

	private static final Logger logger = LoggerFactory.getLogger(NetworkAuditRestController.class);

	@Autowired
    DataSource dataSource;
	
	@Autowired
	private AuditRuleRepository auditRuleRepository;

	@Autowired
	private AuditCategoryRepository auditCategoryRepository;
	
	@Autowired
	private ApplicationContext appContext;
	
	//Create embedded broker
	//ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
	
	
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
	public AuditCategoryResource getAuditCategory(@PathVariable Long categoryPk) {
		try {
			return new AuditCategoryResource(this.auditCategoryRepository.findByPk(categoryPk));
		} catch (Exception e) {
			return null;
		}

	}
	
	/**
	 * Get the rule category
	 * 
	 * @param rulePk
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rules/fields/{rulePk}")
	public @ResponseBody List<DBTableColumn> getAuditRuleFields(@PathVariable Long rulePk){
		AuditRuleEntity auditRule = this.auditRuleRepository.findByPk(rulePk);
		String ruleTableName = auditRule.getTableName();
		
		CustomDTQueries customDTQueries = new CustomDTQueries();
		
		customDTQueries.setDataSource(this.dataSource);
		customDTQueries.setTablename(ruleTableName);
		
		return customDTQueries.getColumns( new DataTablesInput());
	}
	
	/**
	 * Get audit rule given the rule id
	 * 
	 * @param rulePk
	 * @return AuditRuleEntity
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rule/{rulePk}")
	public AuditRuleResource getAuditRule(@PathVariable Long rulePk) {
		
		try {
			return new AuditRuleResource(this.auditRuleRepository.findByPk(rulePk));
		} catch (Exception e) {
			return null;
		}
	}
	
	@JsonView(DataTablesOutput.View.class)
	@RequestMapping(method = RequestMethod.POST, value = "/rule/dt_data/{rulePk}")
	public DataTablesOutput<Map<String,Object>> getAuditRuleData(@PathVariable Long rulePk, @Valid @RequestBody DataTablesInput input) {
		AuditRuleEntity auditRule = this.auditRuleRepository.findByPk(rulePk);
		String ruleTableName = auditRule.getTableName();
		
		CustomDTQueries customDTQueries = new CustomDTQueries();
		
		customDTQueries.setDataSource(this.dataSource);
		customDTQueries.setTablename(ruleTableName);
		return customDTQueries.findAll(input);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/rule/export/{rulePk}")
	public HttpStatus exportRule(@PathVariable Long rulePk) {
		AuditRuleEntity auditRule  = this.auditRuleRepository.findByPk(rulePk);
		String qry = "SELECT * FROM " + auditRule.getTableName();
		logger.info(auditRule.getSql());
		JmsTemplate jms = appContext.getBean(JmsTemplate.class);
		jms.convertAndSend("inbound.export-jobs", qry);
		
		return HttpStatus.OK;
	}
}
