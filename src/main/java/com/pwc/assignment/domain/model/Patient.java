package com.pwc.assignment.domain.model;

import com.pwc.assignment.enums.RolesEnum;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "PATIENT_TABLE")
public class Patient {
    @Id
    @GeneratedValue()
    private UUID id;

    @Column(name="name")
    private String name;

    @Column(name="username")
    private String username;

    @Column(name="national_id")
    private String nationalId;

    @Column(name="DOB")
    private LocalDate dob;

    @Column(name="password")
    private String password;

    public List<GrantedAuthority> getAuthoritiesEntities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(RolesEnum.PATIENT.name()));
        return new ArrayList<>(roles);
    }
}