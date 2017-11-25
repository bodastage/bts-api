package com.bodastage.cm.technologies.controllers;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

import com.bodastage.cm.technologies.models.TechnologyEntity;
import com.bodastage.cm.technologies.repositories.TechnologyRepository;
import com.bodastage.cm.vendors.models.VendorEntity;

@RestController
@RequestMapping("/api/technologies")
public class TechnologiesRestController {
	
	@Autowired
	private TechnologyRepository technologyRepository;
	

	/**
	 * Get all technologies.
	 * 
	 * @since 1.0.0
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Collection<TechnologyEntity>  getTechnologies(){
		
		return technologyRepository.findAll();
	}
	
	/**
	 * Get a particular technology's details.
	 * 
	 * @param Long techPk Technology primary key
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{techPk}")
	public TechnologyEntity getTechnology(@PathVariable Long techPk){
		return this.technologyRepository.findByPk(techPk);
	}

	/**
	 * Add a technology.
	 * 
	 * @since 1.0.0
	 * @version 1.0.0
	 * 
	 * @param TechnologyEntity input
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?>  addUser(@RequestBody TechnologyEntity input){
		
		//@TODO: Get user from authorization token 
		//input.setCreatedby(userId);
		//input.setModifiedby(userId);
		TechnologyEntity technologyEntity = technologyRepository.save(input);
		
		return ResponseEntity.ok(technologyEntity); 
	}
	
	/**
	 * Delete a technology.
	 * 
	 * @since 1.0.0
	 * @version 1.0.0
	 * 
	 * @param techPk
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{techPk}")
	ResponseEntity<?>  deleteVendor(@PathVariable Long techPk){
		
		TechnologyEntity technologyEntity = this.technologyRepository.findByPk(techPk) ; 
		technologyRepository.delete(technologyEntity);
		
		return ResponseEntity.status(HttpStatus.OK).build(); 
	}	
	
	/**
	 * Update technology details.
	 * 
	 * @since 1.0.0
	 * @version 1.0.0
	 * 
	 * @param Long techPk Technology primary key
	 * @param TechnologyEntity input Technology entity
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{techPk}")
	ResponseEntity<?>  updateTechnology(@PathVariable Long techPk, @RequestBody TechnologyEntity input){

		input.setPk(techPk);
		
		technologyRepository.save(input);
		
		return ResponseEntity.status(HttpStatus.OK).build(); 
	}
	
}
