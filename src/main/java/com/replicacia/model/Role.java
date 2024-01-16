package com.replicacia.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The persistent class for the role database table.
 * 
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role implements Serializable {
	public static final String PRODUCT_LIST = "PRODUCT_LIST";
	private static final long serialVersionUID = 1L;
	public static final String PRODUCT_READ = "PRODUCT_READ";
	public static final String PRODUCT_WRITE = "PRODUCT_WRITE";
	public static final String PRODUCT_UPDATE = "PRODUCT_UPDATE";
	public static final String PRODUCT_DELETE = "PRODUCT_DELETE";

	public static final String CUSTOMER_LIST = "CUSTOMER_LIST";
	public static final String CUSTOMER_READ = "CUSTOMER_READ";
	public static final String CUSTOMER_WRITE = "CUSTOMER_WRITE";
	public static final String CUSTOMER_UPDATE = "CUSTOMER_UPDATE";
	public static final String CUSTOMER_DELETE = "CUSTOMER_DELETE";

	public static final String ROLE_LIST = "ROLE_LIST";
	public static final String ROLE_READ = "ROLE_READ";
	public static final String ROLE_WRITE = "ROLE_WRITE";
	public static final String ROLE_UPDATE = "ROLE_UPDATE";
	public static final String ROLE_DELETE = "ROLE_DELETE";

	public static final String USER_LIST = "USER_LIST";
	public static final String USER_READ = "USER_READ";
	public static final String USER_WRITE = "USER_WRITE";
    public static final String USER_UPDATE = "USER_UPDATE";
	public static final String USER_DELETE = "USER_DELETE";

	

	@Id
	@GeneratedValue
	private Long roleId;

	private String name;


	private String api;


	private String apiAccess;

	// bi-directional many-to-many association to User
	@JsonIgnore
	@ManyToMany(mappedBy = "roles",cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private Set<AppUser> appUser;

	public Role(String name) {
		this.name = name;
	}
	public Role(String name, String api, String apiAccess) {
		this.name = name;
		this.api = api;
		this.apiAccess = apiAccess;
	}

	public Role(String api, String apiAccess) {
		this.api = api;
		this.apiAccess = apiAccess;
	}

	@Override
	public boolean equals(Object obj) {
		return this.api.equalsIgnoreCase(((Role)obj).api) && this.apiAccess.equalsIgnoreCase(((Role)obj).apiAccess);
	}
}