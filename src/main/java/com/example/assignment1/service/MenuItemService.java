package com.example.assignment1.service;

// ╔══════════════════════════════════════════════════════════════════╗
// ║            ✏️  YOUR TASK: IMPLEMENT THIS FILE                   ║
// ║  Fill in the method bodies below. Do NOT change signatures.     ║
// ╚══════════════════════════════════════════════════════════════════╝

import com.example.assignment1.dto.MenuItemRequest;
import com.example.assignment1.exception.InvalidRequestException;
import com.example.assignment1.exception.ResourceNotFoundException;
import com.example.assignment1.model.MenuItem;
import com.example.assignment1.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem createMenuItem(MenuItemRequest request) {

        if (request.getName() == null || request.getName().isEmpty()) {
            throw new InvalidRequestException("Menu item name cannot be empty");
        }

        if (request.getPrice() <= 0) {
            throw new InvalidRequestException("Menu item price must be greater than 0");
        }

        if (request.getCategory() == null) {
            throw new InvalidRequestException("Menu item category cannot be null");
        }

        MenuItem menuItem = MenuItem.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .vegetarian(request.isVegetarian())
                .category(request.getCategory())
                .build();

        return menuItemRepository.save(menuItem);
    }

    public MenuItem getMenuItemById(Long id) {
        Optional<MenuItem> menuItem = menuItemRepository.findById(id);
        if (menuItem.isPresent()) {
            return menuItem.get();
        }
        throw new ResourceNotFoundException("Menu item not found with id: " + id);
    }

    public List<MenuItem> findAllById(List<Long> ids) {
        return menuItemRepository.findAllByIds(ids);
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public MenuItem updateMenuItem(Long id, MenuItemRequest request) {

        Optional<MenuItem> existingMenuItem = menuItemRepository.findById(id);

        if (!existingMenuItem.isPresent()) {
            throw new ResourceNotFoundException("Menu item not found with id: " + id);
        }

        if (request.getName() == null || request.getName().isEmpty()) {
            throw new InvalidRequestException("Name cannot be empty");
        }

        if (request.getPrice() <= 0) {
            throw new InvalidRequestException("Price must be greater than 0");
        }

        if (request.getCategory() == null) {
            throw new InvalidRequestException("Category cannot be null");
        }

        MenuItem menuItem = existingMenuItem.get();
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setVegetarian(request.isVegetarian());

        return menuItemRepository.save(menuItem);
    }

    public void deleteMenuItem(Long id) {
        Optional<MenuItem> existingMenuItem = menuItemRepository.findById(id);
        if (!existingMenuItem.isPresent()) {
            throw new ResourceNotFoundException("Menu item not found with id: " + id);
        }
        menuItemRepository.deleteById(id);
    }
}
