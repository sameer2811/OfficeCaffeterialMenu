package com.example.assignment1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

// ======================== DO NOT MODIFY THIS FILE ========================

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Represents a single food/drink item that can appear on a menu.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity(name = "MenuItem")
public class MenuItem extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "vegetarian", nullable = false)
    private boolean vegetarian;

    @Column(name = "category", nullable = false)
    private ItemCategory category;
}
