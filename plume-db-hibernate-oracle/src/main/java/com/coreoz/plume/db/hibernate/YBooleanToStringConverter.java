package com.coreoz.plume.db.hibernate;

import javax.persistence.AttributeConverter;

public class YBooleanToStringConverter implements AttributeConverter<Boolean, String> {

	@Override
	public String convertToDatabaseColumn(Boolean value) {        
		return (value != null && value) ? "Y" : "N";            
	}    

	@Override
	public Boolean convertToEntityAttribute(String value) {
		return "Y".equals(value);
	}

}