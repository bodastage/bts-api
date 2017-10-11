package com.bodastage.cm.networkaudit.models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "audit_category")
public class AuditCategoryEntity {

	@GenericGenerator(
	        name = "auditCategorySequenceGenerator",
	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	                @Parameter(name = "sequence_name", value = "audit_category_sequence"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Id
	@GeneratedValue(generator = "auditCategorySequenceGenerator")
	private Long pk;
	
	private String name;
	
	//Parent category
	private Long parentPk; 
	
	private Boolean inBuilt;
	
	private @Type(type="text") String notes;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="auditCategoryEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<AuditRuleEntity> auditRuleEntities;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	
	@Column(nullable = false)
	private Long createdBy;
	
	@Column(nullable = false)
	private Long modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateModified;

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

	public Long getParentPk() {
		return parentPk;
	}

	public void setParentPk(Long parentPk) {
		this.parentPk = parentPk;
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

	public List<AuditRuleEntity> getAuditRuleEntities() {
		return auditRuleEntities;
	}

	public void setAuditRuleEntities(List<AuditRuleEntity> auditRuleEntities) {
		this.auditRuleEntities = auditRuleEntities;
	}
	
}
