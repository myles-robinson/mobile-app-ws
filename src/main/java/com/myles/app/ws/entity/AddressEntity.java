package com.myles.app.ws.entity;

import com.myles.app.ws.dto.UserDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "addresses")
public class AddressEntity implements Serializable {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 30)
    private String addressId;

    @Column(nullable = false, length = 15)
    private String city;

    @Column(nullable = false, length = 15)
    private String country;

    @Column(nullable = false, length = 100)
    private String streetName;

    @Column(nullable = false, length = 30)
    private String postalCode;

    @Column(nullable = false, length = 10)
    private String type;

    @ManyToOne // Many addresses can belong to one user
    @JoinColumn(name = "users_id") // name = *table-name*_*field-name*
    private UserEntity userDetails;
}
