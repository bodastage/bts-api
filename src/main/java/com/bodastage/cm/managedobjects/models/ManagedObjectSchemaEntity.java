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

/**
 * Maps technology and vendor to a schema containing the managed objects
 * 
 * @author emmanuel.ssebaggala@bodastage.com
 *
 */
@Entity
@Table(name="managedobjects_schemas")
public class ManagedObjectSchemaEntity {
	@GenericGenerator(
	        name = "managedObjectsSchemasSequenceGenerator",
	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	                @Parameter(name = "sequence_name", value = "seq_managedobjects_schemas_pk"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Id
	@GeneratedValue(generator = "managedObjectsSchemasSequenceGenerator")
	private Long pk;
	
	@Column(name = "vendor_pk", nullable = false)
	private Long vendorPk;
	
	@Column(name = "tech_pk", nullable = false)
	private Long technologyPk;

	
	@Column(nullable = false)
	private String schemaName;
		
	private @Type(type="text") String notes;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateAdded;
	
	@Column(nullable = false)
	private int addedBy;		
	
	@Column(nullable = false)
	private int modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateModified;
	
	public ManagedObjectSchemaEntity(){}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(int addedBy) {
		this.addedBy = addedBy;
	}

	public int getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(int modifiedBy) {
		this.modifiedBy = modifiedBy;
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

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Date getDatemodified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Date getDateModified() {
		return dateModified;
	}

}
