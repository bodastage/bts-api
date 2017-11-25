package com.bodastage.cm.managedobjects.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.bodastage.cm.common.CustomDTQueries;
import com.bodastage.cm.common.DBTableColumn;
import com.bodastage.cm.managedobjects.hateoas.ManagedObjectResource;
import com.bodastage.cm.managedobjects.models.MOACINode;
import com.bodastage.cm.managedobjects.models.ManagedObjectEntity;
import com.bodastage.cm.managedobjects.models.ManagedObjectSchemaEntity;
import com.bodastage.cm.managedobjects.repositories.ManagedObjectRepository;
import com.bodastage.cm.managedobjects.repositories.ManagedObjectSchemaRepository;
import com.bodastage.cm.networkaudit.models.AuditRuleEntity;

@RestController
@RequestMapping("/api/managedobjects")
public class ManagedObjectsRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(ManagedObjectsRestController.class);
	
	@Autowired
	private ManagedObjectRepository managedObjectRepository;

	@Autowired
	private ManagedObjectSchemaRepository managedObjectSchemaRepository;
	
	@Autowired
    DataSource dataSource;
	
	
	/**
	 * @TODO: Add pagination
	 * 
	 * Get all managed objects.
	 * 
	 * @since 1.0.0
	 * @param userId Long PK of user current logged in
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Collection<ManagedObjectEntity>  getManagedObects(
			@RequestParam(value = "vendorPk",required = false) Long vendorPk,
			@RequestParam(value = "techPk",required = false) Long techPk,
			@RequestParam(value = "swversionPk",required = false) Long swversionPk,
			@RequestParam(value = "parentPk",required = false) Long parentPk
			){

		
		if( vendorPk != null && techPk != null && swversionPk == null && parentPk == null){
			return managedObjectRepository
					.findByVendorPkAndTechnologyPk(vendorPk,techPk);
		}

		if( vendorPk != null && techPk == null & swversionPk == null && parentPk == null){
			return managedObjectRepository.findByVendorPk(vendorPk);
		}


		if( vendorPk != null && techPk != null && swversionPk == null && parentPk != null){
			return managedObjectRepository
					.findByVendorPkAndTechnologyPkAndParentPk(vendorPk,techPk,parentPk);
		}
		
		//Return all
		return managedObjectRepository.findAll();
	}


	/**
	 * Get all managed objects.
	 * 
	 * @since 1.0.0
	 * @return
	 */
	@RequestMapping(value = "/tree/{parentPk}", method = RequestMethod.GET)
	public @ResponseBody List<MOACINode> getManagedObectACIEntries(
			@PathVariable Long parentPk,
			@RequestParam(value = "vendorPk",required = false) Long vendorPk,
			@RequestParam(value = "techPk",required = false) Long techPk,
			@RequestParam(value = "swversionPk",required = false) Long swversionPk,
			@RequestParam(value = "searchTerm",required = false) String searchTerm
			){
		
		List<MOACINode> moACITreeEntries = null;
		Collection<ManagedObjectEntity> managedObjectList = null;
		
		if( vendorPk != null && techPk != null && swversionPk == null && parentPk == null){
			managedObjectList = managedObjectRepository
					.findByVendorPkAndTechnologyPk(vendorPk,techPk);
		}
		
		
		if( vendorPk != null && techPk == null & swversionPk == null && parentPk == null){
			managedObjectList = managedObjectRepository
					.findByVendorPk(vendorPk);
		}

		if( vendorPk != null && techPk != null && swversionPk == null && parentPk != null && searchTerm.trim().length() == 0 ){
			managedObjectList = managedObjectRepository
					.findByVendorPkAndTechnologyPkAndParentPk(vendorPk,techPk,parentPk);
		}

		if( vendorPk != null && techPk != null && swversionPk == null && parentPk != null && searchTerm.trim().length() > 0 ){
			List<MOACINode> aciNodes = new ArrayList<MOACINode>();
			Stack<String> moChildren = new Stack();
			
			Map<String,MOACINode> aciNodesMap = new LinkedHashMap<String, MOACINode>();
			
			managedObjectList = managedObjectRepository
					.findByVendorPkAndTechnologyPkAndPkNotAndNameContainingIgnoreCase(vendorPk,techPk,parentPk,searchTerm);
			
			Iterator<ManagedObjectEntity> iter = managedObjectList.iterator();
			while(iter.hasNext()){
				ManagedObjectEntity e = iter.next();
				
				Long pPk = e.getParentPk();
				Long cPk = e.getPk();
				String cName = e.getName();

				Boolean isBeforeParent = false; //Mark is the mo is before the parent
				while(pPk != parentPk){
					ManagedObjectEntity mo = managedObjectRepository.findByPk(pPk);
					if( mo == null ){
						isBeforeParent = true;
						break;
					}
					pPk = mo.getParentPk();
					cPk = mo.getPk();
					cName = mo.getName();
				}
				
				if(isBeforeParent == true ){
					continue;
				}
				
				if(!moChildren.contains(cName)){
					MOACINode aciNode = new MOACINode();
					aciNode.setId(cPk.toString());
					aciNode.setLabel(cName);
					aciNode.setInode(true);
					aciNodes.add( aciNode );
					aciNode.setOpen(true);
					moChildren.push(cName);
				}
			}
			return aciNodes;
			
		}
		
		//Return all
		if( vendorPk != null && techPk != null && swversionPk != null && parentPk != null ){
			managedObjectList = managedObjectRepository.findAll();
		}	
		
		List<MOACINode> aciNodes = new ArrayList<MOACINode>();
		
		Iterator<ManagedObjectEntity> iter = managedObjectList.iterator();
		while(iter.hasNext()){
			ManagedObjectEntity e = iter.next();
			MOACINode aciNode = new MOACINode();
			aciNode.setId(e.getPk().toString());
			aciNode.setLabel(e.getName());
			aciNode.setInode(true);
			aciNodes.add( aciNode );
		}
		
		return aciNodes;
	}

	
	/**
	 * Get a particular managed object's details.
	 * 
	 * @param moPk
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{moPk}")
	public ManagedObjectEntity getManagedObject(@PathVariable Long moPk){
		return this.managedObjectRepository.findByPk(moPk);
	}

	/**
	 * Add a managed object.
	 * 
	 * @since 1.0.0
	 * @version 1.0.0
	 * @param input
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?>  addManagedObject( @RequestBody ManagedObjectEntity input){
		
		//@TODO: Get user id from authorization and authentication checks
		
		input.setDateAdded(new Date());
		input.setDateModified(new Date());
		ManagedObjectEntity managedObjectEntity = managedObjectRepository.save(input);
		
		Link forOneMO = new ManagedObjectResource(managedObjectEntity).getLink("self");
		
		return ResponseEntity.created(URI.create(forOneMO.getHref())).build(); 
	}
	
	/**
	 * Delete a managed object.
	 * 
	 * @since 1.0.0
	 * @version 1.0.0
	 * @param moPk
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{moPk}")
	ResponseEntity<?>  deleteManagedObject(@PathVariable Long moPk){
		
		ManagedObjectEntity managedObjectEntity = this.managedObjectRepository.findByPk(moPk) ; 
		managedObjectRepository.delete(managedObjectEntity);
		
		return ResponseEntity.status(HttpStatus.OK).build(); 
	}	
	

	/**
	 * Update managed object details.
	 * 
	 * @since 1.0.0
	 * @param moPk
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{moPk}")
	ResponseEntity<?>  updateManagedObject(@PathVariable Long moPk, @RequestBody ManagedObjectEntity input){
		
		input.setPk(moPk);
		
		managedObjectRepository.save(input);
		
		return ResponseEntity.status(HttpStatus.OK).build(); 
	}
	
	/**
	@JsonView(DataTablesOutput.View.class)
	@RequestMapping(method = RequestMethod.GET,value="/dt")
    public DataTablesOutput<ManagedObjectDataTableEntity> getDTData(@PathVariable Long userId, @Valid DataTablesInput input) {
        return managedObjectDataTableRepository.findAll(input);
    }
	**/
	
	/**
	 * Return all the columns in the table.
	 * 
	 * @param userId
	 * @param input
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="/mobrowser/columns/{moPk}")
    public List<DBTableColumn> getDTColumns( @PathVariable Long moPk, DataTablesInput input) {
		
		//@TODO: Add error checks. Does MO exists etc...
		
		ManagedObjectEntity moEntity = managedObjectRepository.findByPk(moPk);
		Collection<ManagedObjectSchemaEntity> moSchemaEntities = 
			managedObjectSchemaRepository.findByVendorPkAndTechnologyPk(moEntity.getVendorPk(), moEntity.getTechnologyPk());
				
		//@TODO: Add check for whether MO exists
		ManagedObjectSchemaEntity moSchemaEntity = moSchemaEntities.iterator().next();
				
		String tableName = moSchemaEntity.getSchemaName() + "." + moEntity.getName();
		
		CustomDTQueries customDTQueries = new CustomDTQueries();
		
		customDTQueries.setDataSource(this.dataSource);
		customDTQueries.setTablename(tableName);
		
		return customDTQueries.getColumns( new DataTablesInput());
    }
	
	@JsonView(DataTablesOutput.View.class)
	@RequestMapping(method = RequestMethod.POST, value = "/mobrowser/dt/{moPk}")
	public DataTablesOutput<Map<String,Object>> getMODTData(@PathVariable Long moPk, @Valid @RequestBody DataTablesInput input) {
		
		//@TODO: Add error checks. Does MO exists etc...
		
		ManagedObjectEntity moEntity = managedObjectRepository.findByPk(moPk);
		Collection<ManagedObjectSchemaEntity> moSchemaEntities = 
			managedObjectSchemaRepository.findByVendorPkAndTechnologyPk(moEntity.getVendorPk(), moEntity.getTechnologyPk());
				
		//@TODO: Add check for whether MO exists
		ManagedObjectSchemaEntity moSchemaEntity = moSchemaEntities.iterator().next();
				
		//Build qualified table name i.e. schema.table_name
		String tableName = moSchemaEntity.getSchemaName() + "." + moEntity.getName();
		
		CustomDTQueries customDTQueries = new CustomDTQueries();
		
		customDTQueries.setDataSource(this.dataSource);
		customDTQueries.setTablename(tableName);
		
		customDTQueries.setDoubleQuoteTableNames(true);
		
		return customDTQueries.findAll(input);
	}
	
	/**
	 * Return a managed objects data for dt.
	 * 
	 * @param userId
	 * @param input
	 * @return
	 *//**
	@RequestMapping(method = RequestMethod.GET,value="/mobrowser/dt/{moPk}")
    public DataTablesOutput<Map<String,Object>> getMOBrowserDTData(@PathVariable Long userId, @PathVariable Long moPk, DataTablesInput input) {
		//@TODO: authenticate user
		ManagedObjectEntity moEntity = managedObjectRepository.findByPk(moPk);
		List<MODatasourceEntity> moDSEntityList = (List<MODatasourceEntity>)moDatasourceRepository.findByTechnologyPkAndVendorPk(moEntity.getTechnologyPk(), moEntity.getVendorPk());
		MODatasourceEntity moDSEntity = moDSEntityList.get(0);
		DatasourceEntity dsEntity = datasourceRepository.findByPk(moDSEntity.getDatasourcePk());
		
		String databaseType = dsEntity.getDatabaseType();
		String hostname = dsEntity.getHostname();
		String port = dsEntity.getPort();
		String database = dsEntity.getDbname();
		String username = dsEntity.getUsername();
		String password = dsEntity.getPassword();
		String otherOptions = "";
		String driverName = "com.mysql.jdbc.Driver";
		if(databaseType.equals("mysql")){
			 driverName = "com.mysql.jdbc.Driver";
			 otherOptions = "?zeroDateTimeBehavior=convertToNull";
		}
		
		if(databaseType.equals("oracle")){
			driverName = "com.oracle.jdbc.Driver";
		}
		
		if(databaseType.equals("postgresql")){
			driverName = "com.postgresql.jdbc.Driver";
		}
		
		String connectionURL  = "jdbc:"+databaseType+"://"+hostname+":"+port+"/"+database+""+otherOptions  ;
				
		//@TODO: Get the repo settingsmoPkmoPk
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverName);
		dataSource.setUrl(connectionURL);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		
		GenericMORepository genericMORepository = new GenericMORepository();
		genericMORepository.setDataSource(dataSource);
		genericMORepository.setTablename(moEntity.getName());
		
		return genericMORepository.findAll(input);
    }
	
	**/
}