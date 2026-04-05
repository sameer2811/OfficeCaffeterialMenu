package com.example.assignment1.model;

import jakarta.persistence.Column;

// ======================== DO NOT MODIFY THIS FILE ========================

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a menu for a specific restaurant, date, and meal type.
 * A menu ties together a restaurant, a date, a meal type
 * (breakfast/lunch/dinner),
 * and a list of menu item IDs that are available.
 *
 * CONSTRAINT: There can only be ONE menu per (restaurantId + date + mealType)
 * combination.
 * For example, "Cafeteria A" can have only one BREAKFAST menu on 2026-02-10.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity(name = "Menu")
public class Menu extends BaseEntity {
    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @Column(name = "menu_item_ids", nullable = false)
    private List<Long> menuItemIds;
}
