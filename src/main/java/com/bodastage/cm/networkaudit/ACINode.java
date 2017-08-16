package com.bodastage.cm.networkaudit;

import java.util.List;

public class ACINode {
	private String id;
	
	private String label;
	
	private String icon;
	
	private boolean inode;
	
	private boolean open;
	
	private boolean checkbox;
	
	private boolean radio;
	
	private String nodeType;
	
	private List<ACINode> branch;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isInode() {
		return inode;
	}

	public void setInode(boolean inode) {
		this.inode = inode;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public boolean isRadio() {
		return radio;
	}

	public void setRadio(boolean radio) {
		this.radio = radio;
	}

	public List<ACINode> getBranch() {
		return branch;
	}

	public void setBranch(List<ACINode> branch) {
		this.branch = branch;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}	
	
}
