package com.ctsi.ssdc.enums;

public enum EnumDatabaseName {
	/**
	 * Mysql
	 */
	MYSQL("MySQL", "Mysql"),
	/**
	 * SqlServer
	 */
	SQL_SERVER("Microsoft SQL Server", "SqlServer"),
	/**
	 * H2
	 */
	H2("H2", "H2"),
	/**
	 * Oracle
	 */
	ORACLE("Oracle", "Oracle");

	private EnumDatabaseName(String databaseProductName, String databaseName) {
		this.databaseProductName = databaseProductName;
		this.databaseName = databaseName;
	}
	/**
	 * databaseProductName
	 */
	private final String databaseProductName;
	/**
	 * databaseName
	 */
	private final String databaseName;

	public String getDatabaseProductName() {
		return databaseProductName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public static EnumDatabaseName getByType(String databaseProductName) {
		for (EnumDatabaseName enumDatabaseName : EnumDatabaseName.values()) {
			if (enumDatabaseName.getDatabaseProductName().equals(databaseProductName)) {
				return enumDatabaseName;
			}
		}
		return null;
	}
}
