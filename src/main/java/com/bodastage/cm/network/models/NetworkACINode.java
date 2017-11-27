package com.bodastage.cm.network.models;

/**
 * Acitree node
 * 
 * @author Bodastage Solutions <info@bodastage.com>
 *
 */
public class NetworkACINode {
	private String id;
	
	private String label;
	
	private String icon;
	
	private boolean inode;
	
	private boolean open;
	
	private boolean checkbox;
	
	private boolean radio;
	
	private String source;
	
	private String _nodeType;
	
	private Long _elementId = (long)0;

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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String get_nodeType() {
		return _nodeType;
	}

	public void set_nodeType(String _nodeType) {
		this._nodeType = _nodeType;
	}

	public Long get_elementId() {
		return _elementId;
	}

	public void set_elementId(Long _elementId) {
		this._elementId = _elementId;
	}
	
}
