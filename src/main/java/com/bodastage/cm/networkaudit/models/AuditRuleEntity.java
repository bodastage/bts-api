package com.bodastage.cm.networkaudit.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "audit_rule")
public class AuditRuleEntity {

	@GenericGenerator(
        name = "auditRuleSequenceGenerator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "audit_rule_sequence"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1")
        }
	)
	@Id
	@GeneratedValue(generator = "auditRuleSequenceGenerator")
	private Long pk;
	
	/**
	 * The name of the rule
	 */
	private String name;
	
	/**
	 * The parent category
	 */
	@Column(name="category_pk", updatable=false, insertable=false)
	private Long categoryPk;
	
	/**
	 * Whether the rule is custom or in built
	 */
	private Boolean inBuilt;
	
	private @Type(type="text") String notes;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="category_pk", nullable=false)
	private AuditCategoryEntity  auditCategoryEntity;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	
	@Column(nullable = false)
	private Long createdBy;
	
	@Column(nullable = false)
	private Long modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateModified;

	/**
	 * The SQL query for the rule
	 */
	private @Type(type="text") String sql;
	
	/**
	 * The name of the table containing the rules results
	 */
	private String tableName;
	
	/**
	 * The  date the rule was first run.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date firstRunDate;
	
	/**
	 * The  date the rule was last run.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastRunDate;
	
	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCategoryPk() {
		return categoryPk;
	}

	public void setCategoryPk(Long categoryPk) {
		this.categoryPk = categoryPk;
	}

	public Boolean getInBuilt() {
		return inBuilt;
	}

	public void setInBuilt(Boolean inBuilt) {
		this.inBuilt = inBuilt;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public AuditCategoryEntity getAuditCategoryEntity() {
		return auditCategoryEntity;
	}

	public void setAuditCategoryEntity(AuditCategoryEntity auditCategoryEntity) {
		this.auditCategoryEntity = auditCategoryEntity;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Date getFirstRunDate() {
		return firstRunDate;
	}

	public void setFirstRunDate(Date firstRunDate) {
		this.firstRunDate = firstRunDate;
	}

	public Date getLastRunDate() {
		return lastRunDate;
	}

	public void setLastRunDate(Date lastRunDate) {
		this.lastRunDate = lastRunDate;
	}
		
}
