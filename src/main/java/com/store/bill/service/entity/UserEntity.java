package com.store.bill.service.entity;

import com.store.bill.service.entity.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private UserType type;
    private LocalDate joiningDate;
}
