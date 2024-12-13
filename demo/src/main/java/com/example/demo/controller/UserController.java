package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.request.UserStaffRegistrationDTO;
import com.example.demo.dto.response.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/users") // set table name
public class UserController {

    @Autowired
    private UserService userService;

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setTelephoneNumber(user.getTelephoneNumber());

        return dto;
    }
    // ---

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userResponseDTOs = users.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTOs);
    }
    // ---

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.status(HttpStatus.OK).body(toDTO(user));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    // ---

    // Update user profile by ID
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserProfile(@PathVariable long id, @RequestBody UserDTO userDTO) {
        try {
            User updatedUser = userService.updateUserProfile(id, userDTO);
            return ResponseEntity.status(HttpStatus.OK).body(toDTO(updatedUser));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // ---

    // add staff - this is alloacted for Admin role
    @PostMapping("/add-staff")
    public ResponseEntity<Object> addStaff(@RequestBody UserStaffRegistrationDTO userStaffRegistrationDTO) {
        try {
            userService.addStaff(userStaffRegistrationDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Staff user successfully created", "status", HttpStatus.CREATED.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage(), "status", HttpStatus.BAD_REQUEST.value()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage(), "status", HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred", "status",
                            HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    // ---
}
