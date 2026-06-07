package com.unir.socialhabits.services;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import com.unir.socialhabits.dto.*;
import com.unir.socialhabits.entities.*;
import com.unir.socialhabits.repositories.*;

import java.util.UUID;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final HabitRepository habitRepository;

    private final UserRepository userRepository;

    private final ProfessionalRepository professionalRepository;

    private Professional getLoggedProfessional() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return professionalRepository

                .findByEmail(email)

                .orElseThrow(
                        () -> new RuntimeException(
                                "Professional not found"
                        )
                );

    }

    public UserDetailDTO getUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return toDetailDTO(user);
    }

    public UserDetailDTO updateUser(UUID id, UpdateUserDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());

        userRepository.save(user);

        return toDetailDTO(user);
    }

    public void deleteUser(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow();

        userRepository.delete(user);
    }

    public UserDTO createUser(
            CreateUserDTO dto
    ) {

        Professional professional =
                getLoggedProfessional();

        User user = User.builder()

                .firstName(
                        dto.getFirstName()
                )

                .lastName(
                        dto.getLastName()
                )

                .age(
                        dto.getAge()
                )

                .phoneNumber(
                        dto.getPhoneNumber()
                )

                .generalObservations(
                        dto.getGeneralObservations()
                )

                .professional(
                        professional
                )

                .build();

        User savedUser =
                userRepository.save(
                        user
                );

        return mapToDTO(
                savedUser
        );

    }

    public UserDetailDTO getUserDetail(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetailDTO dto = new UserDetailDTO();

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAge(user.getAge());

        // habits
        dto.setHabits(
                user.getHabits().stream().map(h -> {
                    HabitDTO hd = new HabitDTO();
                    hd.setId(h.getId());
                    hd.setType(h.getType());
                    hd.setStatus(h.getStatus());
                    hd.setDescription(h.getDescription());
                    return hd;
                }).toList()
        );

        // observations
        dto.setObservations(
                user.getObservations().stream().map(o -> {
                    ObservationDTO od = new ObservationDTO();
                    od.setId(o.getId());
                    od.setContent(o.getContent());
                    od.setCreatedAt(o.getCreatedAt());
                    od.setProfessionalName(o.getProfessional().getName());
                    return od;
                }).toList()
        );

        return dto;
    }

    public Page<UserDTO> getUsers(
            String search,
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size
                );

        Professional professional =
                getLoggedProfessional();

        if (
                search == null ||
                        search.isBlank()
        ) {

            return userRepository

                    .findByProfessionalId(
                            professional.getId(),
                            pageable
                    )

                    .map(
                            this::mapToDTO
                    );

        }

        return userRepository

                .findByProfessionalIdAndFirstNameContainingIgnoreCase(

                        professional.getId(),

                        search,

                        pageable

                )

                .map(
                        this::mapToDTO
                );

    }

    public UserDetailDTO toDetailDTO(User user) {

        UserDetailDTO dto = new UserDetailDTO();

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAge(user.getAge());
        dto.setGeneralObservations(user.getGeneralObservations());
        dto.setHabitStatus(calculateStatus(user));
        dto.setHasRiskyHabitsToday(hasRiskyHabitsToday(user));

        dto.setHasMissingTodayHabits(
                habitRepository
                        .findByUserIdAndDate(user.getId(), LocalDate.now())
                        .isEmpty()
        );

        dto.setHabits(
                user.getHabits().stream().map(h -> {
                    HabitDTO hd = new HabitDTO();
                    hd.setId(h.getId());
                    hd.setType(h.getType());
                    hd.setStatus(h.getStatus());
                    hd.setDescription(h.getDescription());
                    hd.setDate(h.getDate());
                    return hd;
                }).toList()
        );

        dto.setObservations(
                user.getObservations().stream().map(o -> {
                    ObservationDTO od = new ObservationDTO();
                    od.setId(o.getId());
                    od.setContent(o.getContent());
                    od.setCreatedAt(o.getCreatedAt());
                    od.setProfessionalName(
                            o.getProfessional().getName()
                    );
                    return od;
                }).toList()
        );

        return dto;
    }

    private UserDTO mapToDTO(
            User user
    ) {

        UserDTO dto =
                new UserDTO();

        dto.setId(
                user.getId()
        );

        dto.setFirstName(
                user.getFirstName()
        );

        dto.setLastName(
                user.getLastName()
        );

        dto.setAge(
                user.getAge()
        );

        dto.setPhoneNumber(
                user.getPhoneNumber()
        );

        dto.setHabitStatus(calculateStatus(user));

        dto.setHasRiskyHabitsToday(hasRiskyHabitsToday(user));

        dto.setHasMissingTodayHabits(
                habitRepository
                        .findByUserIdAndDate(user.getId(), LocalDate.now())
                        .isEmpty()
        );

        return dto;
    }

    public void update(UUID id, UpdateUserDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setGeneralObservations(dto.getGeneralObservations());
        user.setPhoneNumber(dto.getPhoneNumber());

        userRepository.save(user);
    }

    public HabitGlobalStatus calculateStatus(User user) {

        List<Habit> habitsToday =
                habitRepository.findByUserIdAndDate(user.getId(), LocalDate.now());

        if (habitsToday.isEmpty()) {
            return HabitGlobalStatus.CRITICAL;
        }

        boolean hasRisk =
                habitsToday.stream()
                        .anyMatch(h -> h.getStatus() != HabitStatus.CORRECT);

        return hasRisk ? HabitGlobalStatus.WARNING : HabitGlobalStatus.OK;
    }

    public void updateUserStatus(User user) {
        userRepository.save(user);
    }

    private boolean hasRiskyHabitsToday(User user) {
        return user.getHabits()
                .stream()
                .filter(h -> LocalDate.now().equals(h.getDate()))
                .anyMatch(h -> h.getStatus() != HabitStatus.CORRECT);
    }
}