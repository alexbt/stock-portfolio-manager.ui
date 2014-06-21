package com.proserus.stocks.ui.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.proserus.stocks.bo.common.DBVersion;

/**
 * @author Alex
 *
 */
@Entity(name = "Version")
@NamedQueries( { @NamedQuery(name = "version.find", query = "SELECT v FROM Version v where Id='1'")})
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

	@Override
	public String toString() {
		return "DBVersionImpl [id=" + id + ", databaseVersion="
				+ databaseVersion + "]";
	}
}
