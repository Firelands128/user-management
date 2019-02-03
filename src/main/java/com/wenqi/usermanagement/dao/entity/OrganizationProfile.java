/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.dao.entity;

import com.wenqi.usermanagement.constants.OrganizationType;
import com.wenqi.usermanagement.dto.OrganizationUserDTO;

import javax.persistence.*;

@Entity
@Table(name = "organization_profile")
public class OrganizationProfile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "id_parent")
	private Long idParent;

	@Column(name = "officer_name")
	private String officerName;

	@Column(name = "officer_phone")
	private String officerPhone;

	@Column(name = "description")
	private String description;

	@Column(name = "license")
	private String license;

	@Column(name = "license_copy")
	private String licenseCopy;

	@Column(name = "type")
    private OrganizationType type;

	public OrganizationProfile() {

	}

	public OrganizationProfile(OrganizationUserDTO organizationUserDTO) {
		this.idParent = organizationUserDTO.idParent;
		this.officerName = organizationUserDTO.officerName;
		this.officerPhone = organizationUserDTO.officerPhone;
		this.description = organizationUserDTO.description;
		this.license = organizationUserDTO.license;
		this.licenseCopy = organizationUserDTO.licenseCopy;
		this.type = organizationUserDTO.type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getIdParent() {
		return idParent;
	}

	public void setIdParent(Long idParent) {
		this.idParent = idParent;
	}

	public String getOfficerName() {
		return officerName;
	}

	public void setOfficerName(String officerName) {
		this.officerName = officerName;
	}

	public String getOfficerPhone() {
		return officerPhone;
	}

	public void setOfficerPhone(String officerPhone) {
		this.officerPhone = officerPhone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getLicenseCopy() {
		return licenseCopy;
	}

	public void setLicenseCopy(String licenseCopy) {
		this.licenseCopy = licenseCopy;
	}

	public OrganizationType getType() {
		return type;
	}

	public void setType(OrganizationType type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		OrganizationProfile that = (OrganizationProfile) o;

		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return "OrganizationProfile{" +
                "id=" + id +
				", user=" + user +
                ", idParent=" + idParent +
				", officerName='" + officerName + '\'' +
				", officerPhone='" + officerPhone + '\'' +
				", description='" + description + '\'' +
				", license='" + license + '\'' +
				", licenseCopy='" + licenseCopy + '\'' +
				", type=" + type +
				'}';
	}
}
