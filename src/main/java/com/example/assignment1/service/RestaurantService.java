package com.example.assignment1.service;

// ╔══════════════════════════════════════════════════════════════════╗
// ║            ✏️  YOUR TASK: IMPLEMENT THIS FILE                   ║
// ║  Fill in the method bodies below. Do NOT change signatures.     ║
// ╚══════════════════════════════════════════════════════════════════╝

import com.example.assignment1.dto.RestaurantRequest;
import com.example.assignment1.exception.InvalidRequestException;
import com.example.assignment1.exception.ResourceNotFoundException;
import com.example.assignment1.model.Restaurant;
import com.example.assignment1.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant createRestaurant(RestaurantRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new InvalidRequestException("Restaurant name cannot be empty");
        }
        if (request.getName().isBlank()) {
            throw new InvalidRequestException("Restaurant name cannot be blank");
        }
        if (request.getLocation() == null || request.getLocation().isEmpty()) {
            throw new InvalidRequestException("Restaurant location cannot be empty");
        }
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .location(request.getLocation())
                .build();
        return restaurantRepository.save(restaurant);
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not able to find the restaurant with id: " + id));
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant updateRestaurant(Long id, RestaurantRequest request) {

        Restaurant existingRestaurant = getRestaurantById(id);
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new InvalidRequestException("Restaurant name cannot be empty");
        }
        if (request.getLocation() == null || request.getLocation().isEmpty()) {
            throw new InvalidRequestException("Restaurant location cannot be empty");
        }
        existingRestaurant.setName(request.getName());
        existingRestaurant.setLocation(request.getLocation());
        return restaurantRepository.save(existingRestaurant);
    }

    public void deleteRestaurant(Long id) {
        // if the exception is not thrown, that means the restaurant exists and we can
        // delete it
        getRestaurantById(id);
        restaurantRepository.deleteById(id);
    }
}
