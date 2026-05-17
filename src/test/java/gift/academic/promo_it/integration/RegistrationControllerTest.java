package gift.academic.promo_it.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.academic.promo_it.dtos.auth.RegisterRequestDto;
import gift.academic.promo_it.exceptions.DuplicateException;
import gift.academic.promo_it.exceptions.LoginOccupiedException;
import gift.academic.promo_it.exceptions.WeakPasswordException;
import gift.academic.promo_it.services.UserService;
import gift.academic.promo_it.validators.PasswordValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    private String getUrl() {
        return "/auth/register";
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PasswordValidator passwordValidator;

    /**
     * 1. Название: Успешная регистрация пользователя.
     * 2. Сценарий: Проверка полной цепочки регистрации при валидных данных.
     * 3. На входе: RegisterRequestDto (login: "user123", password: "StrongPassword123!").
     * 4. Ожидаемый результат: HTTP 201 Created, корректный JSON с данными пользователя.
     */
    @Test
    @DisplayName("POST /auth/register - Успех")
    @WithMockUser
    void registerSuccess() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto();
        requestDto.setLogin("user123");
        requestDto.setPassword("StrongPassword123!");
        requestDto.setRole("ordinary");

        gift.academic.promo_it.models.User mockUser = mock(gift.academic.promo_it.models.User.class);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getLogin()).thenReturn("user123");
        when(mockUser.getRole()).thenReturn(gift.academic.promo_it.constants.Role.ORDINARY);

        doNothing().when(passwordValidator).validatePassword(anyString());
        when(userService.createUser(any(RegisterRequestDto.class))).thenReturn(mockUser);

        mockMvc.perform(post(getUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login").value("user123"));

        verify(passwordValidator).validatePassword("StrongPassword123!");
        verify(userService).createUser(any());
    }

    /**
     * 1. Название: Ошибка — Слабый пароль.
     * 2. Сценарий: PasswordValidator прерывает выполнение, выбрасывая WeakPasswordException.
     * 3. На входе: RegisterRequestDto с паролем "123".
     * 4. Ожидаемый результат: HTTP 400 Bad Request, UserService не вызывается.
     */
    @Test
    @DisplayName("POST /auth/register - Слабый пароль")
    @WithMockUser
    void registerWeakPassword() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto();
        requestDto.setLogin("test");
        requestDto.setPassword("123");

        doThrow(new WeakPasswordException()).when(passwordValidator).validatePassword("123");

        mockMvc.perform(post(getUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any());
    }

    /**
     * 1. Название: Ошибка — Логин занят.
     * 2. Сценарий: Пароль проходит валидацию, но UserService сообщает, что логин уже существует.
     * 3. На входе: RegisterRequestDto с существующим в базе логином.
     * 4. Ожидаемый результат: HTTP 400 Bad Request.
     */
    @Test
    @DisplayName("POST /auth/register - Логин занят")
    @WithMockUser
    void registerLoginOccupied() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto();
        requestDto.setLogin("occupied");
        requestDto.setPassword("ValidPass123!");

        doNothing().when(passwordValidator).validatePassword(anyString());
        when(userService.createUser(any())).thenThrow(new LoginOccupiedException("occupied"));

        mockMvc.perform(post(getUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict());
    }

    /**
     * 1. Название: Ошибка — Повторная регистрация администратора.
     * 2. Сценарий: Попытка создать пользователя с ролью "admin", когда он уже есть в системе.
     * 3. На входе: RegisterRequestDto (role: "admin").
     * 4. Ожидаемый результат: HTTP 409 Conflict.
     */
    @Test
    @DisplayName("POST /auth/register - Конфликт ролей")
    @WithMockUser
    void registerAdminConflict() throws Exception {
        RegisterRequestDto requestDto = new RegisterRequestDto();
        requestDto.setLogin("admin");
        requestDto.setPassword("AdminPass123!");
        requestDto.setRole("admin");

        doNothing().when(passwordValidator).validatePassword(anyString());
        when(userService.createUser(any())).thenThrow(new DuplicateException());

        mockMvc.perform(post(getUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict());
    }
}