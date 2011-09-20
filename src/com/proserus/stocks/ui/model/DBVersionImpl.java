package com.proserus.stocks.ui.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.proserus.stocks.bo.common.DBVersion;

@Entity(name = "Version")
public class DBVersionImpl implements DBVersion {

	@Id
	private Integer id = 1;
	
	@Column(nullable = false, unique = true)
	private Integer databaseVersion;

	public Integer getDatabaseVersion() {
		return databaseVersion;
	}

	public void setDatabaseVersion(Integer databaseVersion) {
		this.databaseVersion = databaseVersion;
	}

	@Override
    public void incrementVersion() {
		databaseVersion = databaseVersion + 1;
    }
}
