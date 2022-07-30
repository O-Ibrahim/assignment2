package com.pwc.assignment.domain.model;

import com.pwc.assignment.enums.RolesEnum;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.sql.Time;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "DOCTOR_TABLE")
public class Doctor {
    @Id
    @GeneratedValue()
    private UUID id;

    @Column(name="name")
    private String name;

    @Column(name="username", unique = true)
    private String username;

    @Column(name="national_id")
    private String nationalId;

    @Column(name="password")
    private String password;

    @Column(name="speciality")
    private String speciality;

    @Column(name="starting_hour")
    private String startingHour;

    @Column(name="ending_hour")
    private String endingHour;

    public List<GrantedAuthority> getAuthoritiesEntities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(RolesEnum.DOCTOR.name()));
        return new ArrayList<>(roles);
    }

}
