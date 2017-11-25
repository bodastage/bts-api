package com.bodastage.cm.technologies.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.bodastage.cm.technologies.models.TechnologyEntity;
import com.bodastage.cm.technologies.controllers.TechnologiesRestController;
import com.bodastage.cm.vendors.models.VendorEntity;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class TechnologyResource extends ResourceSupport {
	private  TechnologyEntity technology;
	
	public TechnologyResource(TechnologyEntity technologyEntity) {
		this.technology = technologyEntity;
		
		this.add(linkTo(TechnologiesRestController.class ).withRel("technologies"));
		
		this.add(linkTo(methodOn(TechnologiesRestController.class)
				.getTechnology( technologyEntity.getPk())).withSelfRel());
	}
	
	public TechnologyEntity getTechnology(){
		return this.technology;
	}
	
}
