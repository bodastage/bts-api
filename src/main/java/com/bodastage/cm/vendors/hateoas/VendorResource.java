package com.bodastage.cm.vendors.hateoas;

import org.springframework.hateoas.ResourceSupport;

import com.bodastage.cm.vendors.controllers.VendorsRestController;
import com.bodastage.cm.vendors.models.VendorEntity;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class VendorResource extends ResourceSupport {
	private  VendorEntity vendor;
	
	
	public VendorResource(VendorEntity vendorEntity) {
		this.vendor = vendorEntity;
		
		this.add(linkTo(VendorsRestController.class ).withRel("vendors"));
		
		this.add(linkTo(methodOn(VendorsRestController.class)
				.getVendor(vendorEntity.getPk())).withSelfRel());
	}
	
	public VendorEntity getVendor(){
		return this.vendor;
	}
	
}
