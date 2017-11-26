package com.bodastage.cm.network.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bodastage.cm.network.repositories.CellRepository;
import com.bodastage.cm.network.repositories.NodeRepository;
import com.bodastage.cm.network.repositories.SiteRepository;
import com.bodastage.cm.network.models.NetworkACINode;
import com.bodastage.cm.network.models.NodeEntity;

@RestController
@RequestMapping("/api/network")
public class NetworkRestController {
	private static final Logger logger = LoggerFactory.getLogger(NetworkRestController.class);
	
	@Autowired
	private NodeRepository nodeRepository;
	
	@Autowired
	private SiteRepository siteRepository;
	
	@Autowired
	private CellRepository cellRepository;
	
	
	/**
	 * 
	 * @since 1.0.0
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "tree")
	public @ResponseBody List<NetworkACINode> getNetworkNodes(
			@RequestParam(value = "vendorPk", required = false) Long vendorPk,
			@RequestParam(value = "swversionPk", required = false) Long swversionPk,
			@RequestParam(value = "parentPk", required = false) Long parentPk,
			@RequestParam(value = "source", required = false) String source,
			@RequestParam(value = "nodeType", required = false) String nodeType) {

		return this.getACINodes(null, vendorPk, swversionPk, parentPk, source, nodeType);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/tree/{parentId}")
	public @ResponseBody List<NetworkACINode> getNetworkNodesWithParentId(
			@PathVariable String parentId, @RequestParam(value = "vendorPk", required = false) Long vendorPk,
			@RequestParam(value = "swversionPk", required = false) Long swversionPk,
			@RequestParam(value = "parentPk", required = false) Long parentPk,
			@RequestParam(value = "source", required = false) String source,
			@RequestParam(value = "nodeType", required = false) String nodeType) {
		return this.getACINodes( parentId, vendorPk, swversionPk, parentPk, source, nodeType);
	}

	private List<NetworkACINode> getACINodes(String parentId, Long vendorPk, Long swversionPk, Long parentPk,
			String source, String nodeType) {

		List<NetworkACINode> aciNodes = new ArrayList<NetworkACINode>();

		// If parent is null, then it's the root
		if (parentId == null && source.equals("live") && parentPk == null) {
			// MSCs
			NetworkACINode aciMSCNode = new NetworkACINode();
			aciMSCNode.setId("msc_root");
			aciMSCNode.setLabel("MSC(" + nodeRepository.countByType("MSC") + ")");
			aciMSCNode.setInode(true);
			aciNodes.add(aciMSCNode);
			
			//RNCs
			NetworkACINode aciRNCNode = new NetworkACINode();
			aciRNCNode.setId("rnc_root");
			aciRNCNode.setLabel("RNC(" + nodeRepository.countByType("RNC") + ")");
			aciRNCNode.setInode(true);
			aciNodes.add(aciRNCNode);
			
			//BSCs
			NetworkACINode aciBSCNode = new NetworkACINode();
			aciBSCNode.setId("bsc_root");
			aciBSCNode.setLabel("BSC(" + nodeRepository.countByType("BSC") + ")");
			aciBSCNode.setInode(true);
			aciNodes.add(aciBSCNode);
			
			//ENodeBs
			NetworkACINode aciENodeBNode = new NetworkACINode();
			aciENodeBNode.setId("enodeb_root");
			//aciENodeBNode.setLabel("ENodeB(" + siteRepository.countByTechnologyPk(3) + ")"); //@TODO: Fix
			aciENodeBNode.setLabel("ENodeB(0)");
			aciENodeBNode.setInode(true);
			aciNodes.add(aciENodeBNode);
			
			return aciNodes;
		}

		//Get list of MSCs
		if(parentId.equals("msc_root") && parentPk == null && source.equals("live") && nodeType.equals("")){
			Collection<NodeEntity> mscList = nodeRepository.findByType("MSC");
			Iterator<NodeEntity> iter = mscList.iterator();
			while(iter.hasNext()){
				NodeEntity e = iter.next();
				//Long cnt = planMSCBSCMapRepository.countByMscPk(e.getPk());
				NetworkACINode aciNode = new NetworkACINode();
				aciNode.setId(e.getPk().toString());
				//aciNode.setLabel(e.getName()+"("+cnt.toString()+")");
				aciNode.setLabel(e.getName());
				aciNode.setInode(false);
				aciNode.set_nodeType("msc");
				aciNode.setSource("live");
				aciNodes.add( aciNode );
			}
			return aciNodes;
		}
		
		//Get BSCs
		if(parentId.equals("bsc_root")  && parentPk == null && source.equals("live") && nodeType.equals("")){
			Collection<NodeEntity> bscList = nodeRepository.findByType("BSC");
			Iterator<NodeEntity> iter = bscList.iterator();
			while(iter.hasNext()){
				NodeEntity e = iter.next();
				//Long cnt = planBTSRepository.countByParentPk(e.getPk());
				NetworkACINode aciNode = new NetworkACINode();
				aciNode.setId(e.getPk().toString() );
				//aciNode.setLabel(e.getName()+"("+cnt.toString()+")");
				aciNode.setLabel(e.getName());
				aciNode.setInode(true);
				aciNode.set_nodeType("bsc");
				aciNode.setSource("live");
				aciNodes.add( aciNode );
			}
			return aciNodes;
		}
		
		//Get RNCs
		if(parentId.equals("rnc_root")  && parentPk == null && source.equals("live") && nodeType.equals("")){
			Collection<NodeEntity> rncList = nodeRepository.findByType("RNC");
			Iterator<NodeEntity> iter = rncList.iterator();
			while(iter.hasNext()){
				NodeEntity e = iter.next();
				//Long cnt = planBTSRepository.countByParentPk(e.getPk());
				NetworkACINode aciNode = new NetworkACINode();
				aciNode.setId(e.getPk().toString() );
				aciNode.setLabel(e.getName());
				aciNode.setInode(true);
				aciNode.set_nodeType("rnc");
				aciNode.setSource("live");
				aciNodes.add( aciNode );
			}
			return aciNodes;
		}
		

		
		return aciNodes;
	}
	
}