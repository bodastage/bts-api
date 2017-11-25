package com.bodastage.cm.managedobjects.hateoas;

import org.springframework.hateoas.ResourceSupport;

import com.bodastage.cm.managedobjects.controllers.ManagedObjectsRestController;
import com.bodastage.cm.managedobjects.models.ManagedObjectEntity;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class ManagedObjectResource extends ResourceSupport {
	private  ManagedObjectEntity managedObject;
	
	//@TODO: Get the other user's ID.
	/**
	 * 
	 * @param managedObjectEntity
	 * @param userPk
	 */
	public ManagedObjectResource(ManagedObjectEntity managedObjectEntity) {
		this.managedObject = managedObjectEntity;
		
		this.add(linkTo(ManagedObjectsRestController.class ).withRel("managedobjects"));
		
	}
	
	public ManagedObjectEntity getManagedObject(){
		return this.managedObject;
	}
}