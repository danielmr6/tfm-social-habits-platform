package com.unir.socialhabits.services;

import com.unir.socialhabits.dto.LoginRequestDTO;
import com.unir.socialhabits.dto.LoginResponseDTO;
import com.unir.socialhabits.dto.RegisterProfessionalDTO;
import com.unir.socialhabits.entities.PasswordResetToken;
import com.unir.socialhabits.entities.Professional;
import com.unir.socialhabits.repositories.PasswordResetTokenRepository;
import com.unir.socialhabits.repositories.ProfessionalRepository;
import com.unir.socialhabits.security.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String EMAIL = "admin@test.com";
    private static final String PASSWORD = "12345678";
    private static final String TOKEN = "jwt-token";
    private static final String ENCODED_PASSWORD = "encoded";

    @Mock
    private JwtService jwtService;

    @Mock
    private ProfessionalRepository professionalRepository;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void init() {

        ReflectionTestUtils.setField(
                authService,
                "frontendUrl",
                "http://localhost:3000"
        );

    }

    private Professional professional() {

        return Professional.builder()
                .name("Daniel")
                .email(EMAIL)
                .password(ENCODED_PASSWORD)
                .loginAttempts(0)
                .build();
    }

    @Test
    void login_shouldReturnJwt_whenCredentialsAreValid() {

        Professional professional = professional();

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);

        when(professionalRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(professional));

        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD))
                .thenReturn(true);

        when(jwtService.generateToken(EMAIL))
                .thenReturn(TOKEN);

        LoginResponseDTO response =
                authService.login(request);

        assertNotNull(response);
        assertEquals(TOKEN, response.getToken());

        verify(professionalRepository).save(professional);
    }

    @Test
    void login_shouldThrow401_whenEmailDoesNotExist() {

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(EMAIL);

        when(professionalRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> authService.login(request)
        );

    }

    @Test
    void login_shouldIncreaseAttempts_whenPasswordIsIncorrect() {

        Professional professional = professional();

        professional.setLoginAttempts(2);

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(EMAIL);
        request.setPassword("bad");

        when(professionalRepository.findByEmail(any()))
                .thenReturn(Optional.of(professional));

        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        assertThrows(
                ResponseStatusException.class,
                () -> authService.login(request)
        );

        assertEquals(3, professional.getLoginAttempts());

        verify(professionalRepository).save(professional);

    }

    @Test
    void login_shouldLockAccount_afterFiveAttempts() {

        Professional professional = professional();

        professional.setLoginAttempts(4);

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(EMAIL);
        request.setPassword("bad");

        when(professionalRepository.findByEmail(any()))
                .thenReturn(Optional.of(professional));

        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        assertThrows(
                ResponseStatusException.class,
                () -> authService.login(request)
        );

        assertNotNull(professional.getLockUntil());

    }

    @Test
    void register_shouldSaveProfessional() {

        RegisterProfessionalDTO dto =
                new RegisterProfessionalDTO();

        dto.setName("Daniel");
        dto.setEmail(EMAIL);
        dto.setPassword(PASSWORD);

        when(professionalRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(PASSWORD))
                .thenReturn(ENCODED_PASSWORD);

        authService.register(dto);

        ArgumentCaptor<Professional> captor =
                ArgumentCaptor.forClass(Professional.class);

        verify(professionalRepository)
                .save(captor.capture());

        Professional saved = captor.getValue();

        assertEquals("Daniel", saved.getName());
        assertEquals(EMAIL, saved.getEmail());
        assertEquals(ENCODED_PASSWORD, saved.getPassword());

    }

    @Test
    void register_shouldThrowConflict_whenEmailExists() {

        RegisterProfessionalDTO dto =
                new RegisterProfessionalDTO();

        dto.setEmail(EMAIL);

        when(professionalRepository.findByEmail(any()))
                .thenReturn(Optional.of(new Professional()));

        assertThrows(
                ResponseStatusException.class,
                () -> authService.register(dto)
        );

    }

    @Test
    void sendPasswordReset_shouldCreateTokenAndSendEmail() {

        Professional professional = professional();

        when(professionalRepository.findByEmail(any()))
                .thenReturn(Optional.of(professional));

        authService.sendPasswordReset(EMAIL);

        verify(tokenRepository).deleteByEmail(EMAIL);

        verify(tokenRepository).save(any());

        verify(emailService).send(
                eq(EMAIL),
                any(),
                contains("reset-password")
        );

    }

    @Test
    void sendPasswordReset_shouldDoNothing_whenEmailDoesNotExist() {

        when(professionalRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        authService.sendPasswordReset("unknown@test.com");

        verify(emailService, never()).send(any(), any(), any());

        verify(tokenRepository, never()).save(any());

    }

    @Test
    void resetPassword_shouldUpdatePassword() {

        PasswordResetToken token =
                new PasswordResetToken();

        token.setToken("abc");
        token.setEmail(EMAIL);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(5));

        Professional professional = professional();

        when(tokenRepository.findByToken("abc"))
                .thenReturn(Optional.of(token));

        when(professionalRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(professional));

        when(passwordEncoder.encode("newPassword"))
                .thenReturn(ENCODED_PASSWORD);

        authService.resetPassword(
                "abc",
                "newPassword"
        );

        assertEquals(
                ENCODED_PASSWORD,
                professional.getPassword()
        );

        verify(professionalRepository).save(professional);

        verify(tokenRepository).delete(token);

    }

    @Test
    void resetPassword_shouldThrowException_whenTokenExpired() {

        PasswordResetToken token =
                new PasswordResetToken();

        token.setToken("abc");
        token.setEmail(EMAIL);
        token.setExpiryDate(LocalDateTime.now().minusMinutes(1));

        when(tokenRepository.findByToken("abc"))
                .thenReturn(Optional.of(token));

        assertThrows(
                ResponseStatusException.class,
                () -> authService.resetPassword("abc", PASSWORD)
        );

    }

}