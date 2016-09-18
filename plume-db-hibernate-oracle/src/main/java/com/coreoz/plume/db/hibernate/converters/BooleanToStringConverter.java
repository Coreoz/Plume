package com.coreoz.plume.db.hibernate.converters;

import javax.persistence.AttributeConverter;

public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

	@Override
	public String convertToDatabaseColumn(Boolean value) {
		return (value != null && value) ? "Y" : "N";
	}

	@Override
	public Boolean convertToEntityAttribute(String value) {
		return "Y".equals(value)
			|| "T".equals(value)
			|| "O".equals(value)
			|| "1".equals(value);
	}

}