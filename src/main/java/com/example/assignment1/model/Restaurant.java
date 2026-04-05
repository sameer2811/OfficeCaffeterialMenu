package com.example.assignment1.model;

// ======================== DO NOT MODIFY THIS FILE ========================

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Represents a cafeteria/restaurant in the office.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity(name = "Restaurant")
public class Restaurant extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;
}
