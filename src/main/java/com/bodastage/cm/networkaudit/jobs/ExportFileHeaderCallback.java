package com.bodastage.cm.networkaudit.jobs;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class ExportFileHeaderCallback implements FlatFileHeaderCallback {

	private static final Logger logger = LoggerFactory.getLogger(ExportFileHeaderCallback.class);

	/**
	 * Field list array
	 */
    List<String> fields;
    
	@Override
	public void writeHeader(Writer writer) throws IOException {
		String fieldListString = "";
		for(int i = 0; i < this.fields.size(); i++) {
			fieldListString += fields.get(i) + ",";
		}
		writer.write(fieldListString);      
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

}
