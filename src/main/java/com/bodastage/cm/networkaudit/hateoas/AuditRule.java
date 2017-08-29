package com.bodastage.cm.networkaudit.hateoas;

import java.util.Date;

public class AuditRule {
	private Long pk;
	private String name;
	private Long categoryPk;
	private Boolean inBuilt;
	private Date dateCreated;
	private Long createdBy;
	private Long modifiedBy;
	private Date dateModified;
	private String sql;
	private String tableName;
	private Date firstRunDate;
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
