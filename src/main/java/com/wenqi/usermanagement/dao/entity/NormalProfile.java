/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.dao.entity;

import com.wenqi.usermanagement.dto.NormalUserDTO;

import javax.persistence.*;

@Entity
@Table(name = "normal_profile")
public class NormalProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "id_org")
    private Long idOrganization;

    @Column(name = "id_employee")
    private Long idEmployee;

    @Column(name = "id_parent")
    private Long idParent;

    @Column(name = "school")
    private String school;

    @Column(name = "grade")
    private int grade;

    public NormalProfile() {

    }

    public NormalProfile(NormalUserDTO normalUserDTO) {
        this.idOrganization = normalUserDTO.idOrganization;
        this.idEmployee = normalUserDTO.idEmployee;
        this.idParent = normalUserDTO.idParent;
        this.school = normalUserDTO.school;
        this.grade = normalUserDTO.grade;
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

    public Long getIdOrganization() {
        return idOrganization;
    }

    public void setIdOrganization(Long idOrganization) {
        this.idOrganization = idOrganization;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public Long getIdParent() {
        return idParent;
    }

    public void setIdParent(Long idParent) {
        this.idParent = idParent;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NormalProfile that = (NormalProfile) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "NormalProfile{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", idOrganization='" + idOrganization + '\'' +
                ", idEmployee='" + idEmployee + '\'' +
                ", idParent='" + idParent + '\'' +
                ", school='" + school + '\'' +
                ", grade=" + grade +
                '}';
    }
}
