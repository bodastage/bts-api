package com.bodastage.cm.common;

import java.io.Serializable;

public class ExportRequestMessage implements Serializable {
	private String query;
	private String type;
	private String outputFileName;
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOutputFileName() {
		return outputFileName;
	}
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	
}
