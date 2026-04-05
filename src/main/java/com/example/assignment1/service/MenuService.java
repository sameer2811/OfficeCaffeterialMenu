package com.example.assignment1.service;

// ╔══════════════════════════════════════════════════════════════════╗
// ║            ✏️  YOUR TASK: IMPLEMENT THIS FILE                   ║
// ║  Fill in the method bodies below. Do NOT change signatures.     ║
// ║  The helper method toMenuResponse() is already done for you.    ║
// ╚══════════════════════════════════════════════════════════════════╝

import com.example.assignment1.dto.MenuRequest;
import com.example.assignment1.dto.MenuResponse;
import com.example.assignment1.exception.DuplicateResourceException;
import com.example.assignment1.exception.InvalidRequestException;
import com.example.assignment1.exception.ResourceNotFoundException;
import com.example.assignment1.model.Menu;
import com.example.assignment1.model.MenuItem;
import com.example.assignment1.model.Restaurant;
import com.example.assignment1.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;

    public MenuService(MenuRepository menuRepository,
            RestaurantService restaurantService,
            MenuItemService menuItemService) {
        this.menuRepository = menuRepository;
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
    }

    public MenuResponse createMenu(MenuRequest request) {

        if (request.getRestaurantId() == null) {
            throw new InvalidRequestException("Restaurant ID cannot be null");
        }
        if (request.getDate() == null) {
            throw new InvalidRequestException("Menu date cannot be null");
        }
        if (request.getMealType() == null) {
            throw new InvalidRequestException("Meal type cannot be null");
        }
        if (request.getMenuItemIds() == null || request.getMenuItemIds().isEmpty()) {
            throw new InvalidRequestException("Menu must contain at least one item");
        }
        Optional<Menu> existingMenu = menuRepository.findByRestaurantIdAndDateAndMealType(request.getRestaurantId(),
                request.getDate(), request.getMealType());
        if (existingMenu.isPresent()) {
            throw new DuplicateResourceException("Menu already exists for restaurant " + request.getRestaurantId()
                    + " on " + request.getDate() + " for " + request.getMealType());
        }

        Menu menu = Menu.builder()
                .restaurantId(request.getRestaurantId())
                .date(request.getDate())
                .mealType(request.getMealType())
                .menuItemIds(request.getMenuItemIds())
                .build();

        Menu savedMenu = menuRepository.save(menu);

        return toMenuResponse(savedMenu);
    }

    public MenuResponse getMenuById(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
        return toMenuResponse(menu);
    }

    public List<MenuResponse> getAllMenus() {
        return menuRepository.findAll().stream().map(this::toMenuResponse).collect(Collectors.toList());
    }

    public List<MenuResponse> getMenusByRestaurantId(Long restaurantId) {
        return menuRepository.findAll().stream().filter(menu -> menu.getRestaurantId().equals(restaurantId))
                .map(this::toMenuResponse).collect(Collectors.toList());
    }

    public List<MenuResponse> getMenusByDate(LocalDate date) {
        return menuRepository.findByDate(date).stream().map(this::toMenuResponse).collect(Collectors.toList());
    }

    public List<MenuResponse> getMenusByRestaurantIdAndDate(Long restaurantId, LocalDate date) {
        return menuRepository.findByRestaurantIdAndDate(restaurantId, date).stream().map(this::toMenuResponse)
                .collect(Collectors.toList());
    }

    public MenuResponse updateMenu(Long id, MenuRequest request) {
        Optional<Menu> existingMenu = menuRepository.findById(id);
        if (!existingMenu.isPresent()) {
            throw new ResourceNotFoundException("Menu not found with id: " + id);
        }
        if (request.getRestaurantId() == null) {
            throw new InvalidRequestException("Restaurant ID cannot be null");
        }
        if (request.getDate() == null) {
            throw new InvalidRequestException("Menu date cannot be null");
        }
        if (request.getMealType() == null) {
            throw new InvalidRequestException("Meal type cannot be null");
        }
        if (request.getMenuItemIds() == null || request.getMenuItemIds().isEmpty()) {
            throw new InvalidRequestException("Menu must contain at least one item");
        }

        Optional<Menu> checkForDuplicate = menuRepository.findByRestaurantIdAndDateAndMealType(
                request.getRestaurantId(), request.getDate(), request.getMealType());
        if (checkForDuplicate.isPresent() && checkForDuplicate.get().getId() != id) {
            throw new DuplicateResourceException("Menu already exists for restaurant " + request.getRestaurantId()
                    + " on " + request.getDate() + " for " + request.getMealType());
        }

        Menu menu = existingMenu.get();
        menu.setRestaurantId(request.getRestaurantId());
        menu.setDate(request.getDate());
        menu.setMealType(request.getMealType());
        menu.setMenuItemIds(request.getMenuItemIds());
        Menu savedMenu = menuRepository.save(menu);
        return toMenuResponse(savedMenu);
    }

    public void deleteMenu(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new ResourceNotFoundException("Menu not found with id: " + id);
        }
        menuRepository.deleteById(id);
    }

    private MenuResponse toMenuResponse(Menu menu) {
        Restaurant restaurant = restaurantService.getRestaurantById(menu.getRestaurantId());
        List<MenuItem> items = menu.getMenuItemIds().stream()
                .map(menuItemService::getMenuItemById)
                .toList();

        MenuResponse response = new MenuResponse();
        response.setId(menu.getId());
        response.setRestaurantName(restaurant.getName());
        response.setRestaurantLocation(restaurant.getLocation());
        response.setDate(menu.getDate());
        response.setMealType(menu.getMealType());
        response.setItems(items);
        return response;
    }
}
