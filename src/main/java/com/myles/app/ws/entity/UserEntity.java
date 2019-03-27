package com.myles.app.ws.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

// Used to persist data to db table "users"
@Data
@Entity(name = "users")
public class UserEntity implements Serializable {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 3305495139239456226L;

    // Id (primary key) generated and auto incremented
    @Id
    @GeneratedValue
    private Long id;

    // field is required
    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 120) // unique = true
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    private String emailVerificationToken;

    @Column(nullable = false)
    private boolean emailVerificationStatus = false;

    @OneToMany(mappedBy = "userDetails", // user owns the address
            cascade = CascadeType.ALL) // This class owns userDetails, cascade makes action propagate based on persistence
    private List<AddressEntity> addresses;
}
