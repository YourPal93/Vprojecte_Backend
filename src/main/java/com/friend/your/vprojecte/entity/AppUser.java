package com.friend.your.vprojecte.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@Entity
@Table(name="users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(
            name="name",
            nullable = false
    )
    private String name;
    @Column(
            name="password",
            nullable = false
    )
    private String password;
    @Column(
            name="login",
            nullable = false,
            unique = true
    )
    private String login;
    @Column(
            name="email",
            nullable = false,
            unique = true
    )
    private String email;
    @Column(name="birthdate")
    private LocalDate birthdate;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "user_id")
    private Set<Friend> friendList;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "user_id")
    private List<Chat> chatLog;
    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinTable(
            name = "users_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinTable(
            name = "users_group_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> groupRoles;

    public AppUser(
            String name,
            String password,
            String login,
            String email,
            LocalDate birthdate) {
        this.name = name;
        this.password = password;
        this.login = login;
        this.email = email;
        this.birthdate = birthdate;
    }
}
