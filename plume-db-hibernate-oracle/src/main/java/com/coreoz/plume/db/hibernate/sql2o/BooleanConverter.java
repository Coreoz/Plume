package com.coreoz.plume.db.hibernate.sql2o;

import org.sql2o.converters.Converter;
import org.sql2o.converters.ConverterException;

/**
 * Reprend le converter de base fourni par Sql2o mais ajoute le cas avec 'O' pour Oui 
 */
public class BooleanConverter implements Converter<Boolean> {

    public Boolean convert(Object val) throws ConverterException {
        if (val == null) return null;

        if (val instanceof Boolean) {
            return (Boolean) val;
        }

        if (val instanceof Number) {
            return ((Number)val).intValue() != 0;
        }

        if (val instanceof Character) {
            // cast to char is required to compile with java 8
            return (char)val =='Y'
            		|| (char)val =='O' 
                    || (char)val =='T'
                    || (char)val =='J';
        }

        if (val instanceof String) {
            String strVal = ((String)val).trim();
            return "O".equalsIgnoreCase(strVal) || "Y".equalsIgnoreCase(strVal) || "YES".equalsIgnoreCase(strVal) || "TRUE".equalsIgnoreCase(strVal) ||
                    "T".equalsIgnoreCase(strVal) || "J".equalsIgnoreCase(strVal);
        }

        throw new ConverterException("Don't know how to convert type " + val.getClass().getName() + " to " + Boolean.class.getName());
    }

	@Override
	public Object toDatabaseParam(Boolean val) {
		return val;
	}
}
