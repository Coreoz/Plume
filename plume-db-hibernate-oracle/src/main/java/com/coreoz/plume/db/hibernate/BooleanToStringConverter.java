package com.coreoz.plume.db.hibernate;

import javax.persistence.AttributeConverter;

public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

	@Override
	public String convertToDatabaseColumn(Boolean value) {        
		return (value != null && value) ? "O" : "N";            
	}    

	@Override
	public Boolean convertToEntityAttribute(String value) {
		return "O".equals(value);
	}
	
}