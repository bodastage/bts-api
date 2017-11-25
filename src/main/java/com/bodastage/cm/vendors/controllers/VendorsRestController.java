package com.bodastage.cm.vendors.controllers;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bodastage.cm.vendors.models.VendorEntity;
import com.bodastage.cm.vendors.repositories.VendorRepository;

@RestController
@RequestMapping("/api/vendors")
public class VendorsRestController {
	
	@Autowired
	private VendorRepository vendorRepository;


	/**
	 * Get all vendors.
	 * 
	 * @since 1.0.0
	 * 
	 * @return
	 * 
	 * @TODO: Add pagination
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Collection<VendorEntity> getVendors(){
		return vendorRepository.findAll();
		
	}
	
	/**
	 * Get vendor details
	 * 
	 * @param vendorPk
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{vendorPk}")
	public VendorEntity getVendor(@PathVariable Long vendorPk){
		return this.vendorRepository.findByPk(vendorPk);
	}

	/**
	 * Add a vendor.
	 * 
	 * @since 1.0.0
	 * @version 1.0.0
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?>  addVendor(@RequestBody VendorEntity input){
		
		VendorEntity vendorEntity = vendorRepository.save(input);
		
		return ResponseEntity.ok(vendorEntity); 
	}
	
	/**
	 * Delete a vendor.
	 * 
	 * @since 1.0.0
	 * @version 1.0.0
	 * 
	 * @param Long vendorPk
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{vendorPk}")
	ResponseEntity<?>  deleteVendor(@PathVariable Long vendorPk){
		
		VendorEntity vendorEntity = this.vendorRepository.findByPk(vendorPk) ; 
		vendorRepository.delete(vendorEntity);
		
		return ResponseEntity.status(HttpStatus.OK).build(); 
	}	
	
	/**
	 * Update vendor details.
	 * 
	 * @since 1.0.0
	 * @param Long vendorPk
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{vendorPk}")
	ResponseEntity<?>  updateVendor(@PathVariable Long vendorPk, @RequestBody VendorEntity input){
		
		input.setPk(vendorPk);
		
		vendorRepository.save(input);
		
		return ResponseEntity.status(HttpStatus.OK).build(); 
	}

}
