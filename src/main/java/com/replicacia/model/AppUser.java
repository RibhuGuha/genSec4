package com.replicacia.model;

import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	private String name;

	private String mobileNumber;

	private Boolean active;

	private String email;

	@JsonIgnore
	private Boolean superUser = false;

	@JsonIgnore
	private Integer loginAttempts;

	@JsonIgnore
	private Boolean resolveCaptcha = false;

	@Transient
	private String captcha; // captcha entered from UI.

	@Transient
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Boolean passwordChanged = false; // send as true when password changed.
	


	@ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	@EqualsAndHashCode.Exclude
	private Set<Role> roles;

	private String country;

	private Boolean isEmailVerified;


	public AppUser(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
