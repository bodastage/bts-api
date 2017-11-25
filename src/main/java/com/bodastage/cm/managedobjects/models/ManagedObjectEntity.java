package com.bodastage.cm.managedobjects.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;


@Entity
@Table(name="managedobjects")
public class ManagedObjectEntity {
	@GenericGenerator(
	        name = "managedObjectsSequenceGenerator",
	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	                @Parameter(name = "sequence_name", value = "seq_managedobjects_pk"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Id
	@GeneratedValue(generator = "managedObjectsSequenceGenerator")
	private Long pk;
	
	@Column(name = "vendor_pk", nullable = false)
	private Long vendorPk;
	
	@Column(name = "tech_pk", nullable = false)
	private Long technologyPk;

	@Column(name = "parent_pk", nullable = false)
	private Long parentPk;
	
	private String label;
	
	@Column(nullable = false)
	private String name;
		
	private @Type(type="text") String notes;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date datecreated;
	
	@Column(nullable = false)
	private int createdby;		
	
	@Column(nullable = false)
	private int modifiedby;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date datemodified;
	
	public ManagedObjectEntity(){}

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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getCreatedby() {
		return createdby;
	}

	public void setCreatedby(int createdby) {
		this.createdby = createdby;
	}

	public int getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(int modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Long getVendorPk() {
		return vendorPk;
	}

	public void setVendorPk(Long vendorPk) {
		this.vendorPk = vendorPk;
	}

	public Long getTechnologyPk() {
		return technologyPk;
	}

	public void setTechnologyPk(Long technologyPk) {
		this.technologyPk = technologyPk;
	}

	public Date getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}

	public Date getDatemodified() {
		return datemodified;
	}

	public void setDatemodified(Date datemodified) {
		this.datemodified = datemodified;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Long getParentPk() {
		return parentPk;
	}

	public void setParentPk(Long parentPk) {
		this.parentPk = parentPk;
	}	
	
}
