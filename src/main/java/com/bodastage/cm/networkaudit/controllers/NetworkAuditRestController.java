package com.bodastage.cm.networkaudit.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.validation.Valid;
import javax.xml.ws.Response;
import javax.xml.ws.http.HTTPBinding;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bodastage.cm.BodaOpenCmApplication;
import com.bodastage.cm.common.CustomDTQueries;
import com.bodastage.cm.common.DBTableColumn;
import com.bodastage.cm.common.ExportRequestMessage;
import com.bodastage.cm.networkaudit.ACINode;
import com.bodastage.cm.networkaudit.hateoas.AuditCategoryResource;
import com.bodastage.cm.networkaudit.models.AuditCategoryEntity;
import com.bodastage.cm.networkaudit.models.AuditRuleEntity;
import com.bodastage.cm.networkaudit.repositories.jpa.AuditCategoryRepository;
import com.bodastage.cm.networkaudit.repositories.jpa.AuditRuleRepository;
import com.fasterxml.jackson.annotation.JsonView;

import io.netty.handler.codec.http.HttpResponse;

import com.bodastage.cm.networkaudit.hateoas.AuditCategoryResource;
import com.bodastage.cm.networkaudit.hateoas.AuditRuleResource;
import com.bodastage.cm.networkaudit.jobs.JobStatus;

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
	
	@Autowired
	@Qualifier("fileExportJob")
	Job exportjob;
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	JobRepository jobRepository;
	
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
	public JobStatus exportRule(@PathVariable Long rulePk) {
		AuditRuleEntity auditRule  = this.auditRuleRepository.findByPk(rulePk);
		
		/**
		String qry = "SELECT * FROM " + auditRule.getTableName();
		logger.info(auditRule.getSql());
		JmsTemplate jms = appContext.getBean(JmsTemplate.class);
		
		ExportRequestMessage exportRequest = new ExportRequestMessage();
		exportRequest.setOutputFileName(auditRule.getName());
		exportRequest.setQuery(qry);
		exportRequest.setType("csv");
		jms.getMessageConverter();
		jms.convertAndSend("inbound.export-jobs", exportRequest);
		**/
		 
		 try {
			 
			 //Create timestamp to attach to file name
			 Date dateNow = new Date();
			 SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddhhmmss");
			 String timestamp = ft.format(dateNow);
			 
			 //Create file name of the export file
			 //Format: <rule_some_rule_name_20171023121034.csv>
			 String exportFileName = auditRule.getName().replace(" ", "_").toLowerCase() + "_" + timestamp + ".csv";

			 //Create job parameters
			 JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
			 jobParametersBuilder.addString("instance_id", UUID.randomUUID().toString(), true);
			 jobParametersBuilder.addString("file_name", exportFileName, true);
			 jobParametersBuilder.addString("query", "SELECT * FROM networkaudit.rule_missing_externals", true);
			 
			 JobParameters jobParameters = jobParametersBuilder.toJobParameters();
			 
			 jobLauncher.run(exportjob, jobParameters);
			 
			 JobExecution lastJobExecution = jobRepository.getLastJobExecution(exportjob.getName(), jobParameters);
			 
			 JobStatus jobStatus = new JobStatus();
			 
			 jobStatus.setId(lastJobExecution.getJobId());
			 jobStatus.setStatus(lastJobExecution.getStatus().toString());
			 
			 CodeSource codeSource = BodaOpenCmApplication.class.getProtectionDomain().getCodeSource();
			 File jarFile = new File(codeSource.getLocation().toURI().getPath());
			 String jarDir = jarFile.getParentFile().getPath();
			 
			 //jobStatus.setMeta("{ \\\"file_name\\\": \\\"rule_missing_externals.csv\\\", "
			 //		+ "\\\"file_path\\\": \\\""+ jarDir + File.separator + "exports" + File.separator + "rule_missing_externals.csv\\\"}");
			 
			 jobStatus.setMeta("{\"file_name\": \""+exportFileName+"\"}");
			 
			 return jobStatus;
			 
		 }catch(Exception e) {
			 logger.error(e.getStackTrace().toString()); 
		 }
		 
		 
		return null;
		//return HttpStatus.OK;
	}
	
	@RequestMapping(value = "/download/{file_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileSystemResource downloadFile(@PathVariable("file_name") String fileName) {
		File file = new File("./exports/" + fileName);
		return new FileSystemResource(file); 
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/rule/evaluate/{rulePk}")
	public HttpResponse evaluateRule(@PathVariable Long rulePk) {
		AuditRuleEntity auditRule  = this.auditRuleRepository.findByPk(rulePk);
		
		String qry = "SELECT * FROM " + auditRule.getTableName();
		logger.info(auditRule.getSql());
		JmsTemplate jms = appContext.getBean(JmsTemplate.class);
		
		ExportRequestMessage exportRequest = new ExportRequestMessage();
		exportRequest.setOutputFileName(auditRule.getName());
		exportRequest.setQuery(qry);
		exportRequest.setType("csv");
		jms.getMessageConverter();
		
		Map map = new HashMap();
		map.put("sql", qry);
		map.put("pk", auditRule.getPk());
		map.put("name", auditRule.getName());
		jms.convertAndSend("inQueue", map);
		
		return null;
	}
}
