package com.unir.socialhabits.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.unir.socialhabits.dto.CreateUserDTO;
import com.unir.socialhabits.dto.UserDTO;
import com.unir.socialhabits.entities.User;
import com.unir.socialhabits.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO createUser(CreateUserDTO dto) {

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .age(dto.getAge())
                .phoneNumber(dto.getPhoneNumber())
                .generalObservations(dto.getGeneralObservations())
                .build();

        User savedUser = userRepository.save(user);

        return mapToDTO(savedUser);
    }

    public Page<UserDTO> getUsers(String search, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (search == null || search.isBlank()) {
            return userRepository.findAll(pageable)
                    .map(this::mapToDTO);
        }

        return userRepository
                .findByFirstNameContainingIgnoreCase(search, pageable)
                .map(this::mapToDTO);
    }

    private UserDTO mapToDTO(User user) {

        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAge(user.getAge());
        dto.setPhoneNumber(user.getPhoneNumber());

        return dto;
    }
}