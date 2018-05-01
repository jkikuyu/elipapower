package com.ipayafrica.elipapower.util;
/**
 * ref: https://stackoverflow.com/questions/32165694/spring-hibernate-5-naming-strategy-configuration
 */
	import org.apache.commons.lang.StringUtils;
	import org.hibernate.boot.model.naming.Identifier;
	import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
	import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

	public class ImprovedNamingStrategy implements PhysicalNamingStrategy {
		public ImprovedNamingStrategy() {
			// TODO Auto-generated constructor stub
		}

	    @Override
	    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnv) {
	        return convert(identifier);
	    }

	    @Override
	    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnv) {
	        return convert(identifier);
	    }

	    @Override
	    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnv) {
	        return convert(identifier);
	    }

	    @Override
	    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnv) {
	        return convert(identifier);
	    }

	    @Override
	    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnv) {
	        return convert(identifier);
	    }

	    private Identifier convert(Identifier identifier) {
	        if (identifier == null || StringUtils.isBlank(identifier.getText())) {
	            return identifier;
	        }

	        String regex = "([a-z])([A-Z])";
	        String replacement = "$1_$2";
	        String newName = identifier.getText().replaceAll(regex, replacement).toLowerCase();
	        return Identifier.toIdentifier(newName);
	    }
	}

