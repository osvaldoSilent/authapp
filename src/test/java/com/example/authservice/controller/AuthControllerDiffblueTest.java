package com.example.authservice.controller;

import static org.mockito.Mockito.when;

import com.example.authservice.dto.user.UserDeleteRequestDTO;
import com.example.authservice.dto.user.UserFullResponseDTO;
import com.example.authservice.dto.user.UserRequestDTO;
import com.example.authservice.dto.user.UserRequestLoginDTO;
import com.example.authservice.dto.user.UserResponseDTO;
import com.example.authservice.dto.user.UserResponseLoginDto;
import com.example.authservice.dto.user.UserUpdateRequestDTO;
import com.example.authservice.exception.GlobalExceptionHandler;
import com.example.authservice.model.User;
import com.example.authservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class AuthControllerDiffblueTest {
    @Autowired
    private AuthController authController;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @MockitoBean
    private UserService userService;

    /**
     * Test {@link AuthController#register(UserRequestDTO)}.
     *
     * <p>Method under test: {@link AuthController#register(UserRequestDTO)}
     */
    @Test
    @DisplayName("Test register(UserRequestDTO)")
    void testRegister() throws Exception {
        // Arrange
        when(userService.register(Mockito.<UserRequestDTO>any()))
                .thenReturn(new UserResponseDTO("42", "janedoe", "Role"));
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setPassword("iloveyou");
        userRequestDTO.setRole("Role");
        userRequestDTO.setUsername("janedoe");
        String content = new ObjectMapper().writeValueAsString(userRequestDTO);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string("{\"id\":\"42\",\"username\":\"janedoe\",\"role\":\"Role\"}"));
    }

    /**
     * Test {@link AuthController#login(UserRequestLoginDTO)}.
     *
     * <p>Method under test: {@link AuthController#login(UserRequestLoginDTO)}
     */
    @Test
    @DisplayName("Test login(UserRequestLoginDTO)")
    void testLogin() throws Exception {
        // Arrange
        when(userService.login(Mockito.<UserRequestLoginDTO>any()))
                .thenReturn(new UserResponseLoginDto("janedoe", "Role", "ABC123"));
        UserRequestLoginDTO userRequestLoginDTO = new UserRequestLoginDTO();
        userRequestLoginDTO.setPassword("iloveyou");
        userRequestLoginDTO.setUsername("janedoe");
        String content = new ObjectMapper().writeValueAsString(userRequestLoginDTO);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string("{\"username\":\"janedoe\",\"role\":\"Role\",\"token\":\"ABC123\"}"));
    }

    /**
     * Test {@link AuthController#deleteUserByUsername(UserDeleteRequestDTO)} with {@code userD}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.
     * </ul>
     *
     * <p>Method under test: {@link AuthController#deleteUserByUsername(UserDeleteRequestDTO)}
     */
    @Test
    @DisplayName(
            "Test deleteUserByUsername(UserDeleteRequestDTO) with 'userD'; then status isNotFound()")
    void testDeleteUserByUsernameWithUserD_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(userService.deleteByUsername(Mockito.<String>any())).thenReturn(false);
        UserDeleteRequestDTO userDeleteRequestDTO = new UserDeleteRequestDTO();
        userDeleteRequestDTO.setUsername("janedoe");
        String content = new ObjectMapper().writeValueAsString(userDeleteRequestDTO);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.delete("/api/auth/delete/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Usuario no encontrado"));
    }

    /**
     * Test {@link AuthController#getAllUsers()}.
     *
     * <ul>
     *   <li>Then content string {@code
     *       [{"id":"42","username":"janedoe","password":"iloveyou","role":"Role"}]}.
     * </ul>
     *
     * <p>Method under test: {@link AuthController#getAllUsers()}
     */
    @Test
    @DisplayName(
            "Test getAllUsers(); then content string '[{\"id\":\"42\",\"username\":\"janedoe\",\"password\":\"iloveyou\",\"role\":\"Role\"}]'")
    void testGetAllUsers_thenContentStringId42UsernameJanedoePasswordIloveyouRoleRole()
            throws Exception {
        // Arrange
        User user = new User();
        user.setId("42");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(userService.getAllUsers()).thenReturn(userList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/auth/users");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string(
                                        "[{\"id\":\"42\",\"username\":\"janedoe\",\"password\":\"iloveyou\",\"role\":\"Role\"}]"));
    }

    /**
     * Test {@link AuthController#getAllUsers()}.
     *
     * <ul>
     *   <li>Then content string {@code []}.
     * </ul>
     *
     * <p>Method under test: {@link AuthController#getAllUsers()}
     */
    @Test
    @DisplayName("Test getAllUsers(); then content string '[]'")
    void testGetAllUsers_thenContentStringLeftSquareBracketRightSquareBracket() throws Exception {
        // Arrange
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/auth/users");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link AuthController#updateUserByUsername(UserUpdateRequestDTO)}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.
     * </ul>
     *
     * <p>Method under test: {@link AuthController#updateUserByUsername(UserUpdateRequestDTO)}
     */
    @Test
    @DisplayName("Test updateUserByUsername(UserUpdateRequestDTO); then status isNotFound()")
    void testUpdateUserByUsername_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(userService.updateByUsername(Mockito.<UserUpdateRequestDTO>any())).thenReturn(false);

        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();
        userUpdateRequestDTO.setCurrentPassword("iloveyou");
        userUpdateRequestDTO.setCurrentRole("Current Role");
        userUpdateRequestDTO.setCurrentUserName("janedoe");
        userUpdateRequestDTO.setNewPassword("iloveyou");
        userUpdateRequestDTO.setNewRole("New Role");
        userUpdateRequestDTO.setNewUserName("janedoe");
        String content = new ObjectMapper().writeValueAsString(userUpdateRequestDTO);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.put("/api/auth/update/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Usuario no actualizado"));
    }

    /**
     * Test {@link AuthController#updateUserByUsername(UserUpdateRequestDTO)}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.
     * </ul>
     *
     * <p>Method under test: {@link AuthController#updateUserByUsername(UserUpdateRequestDTO)}
     */
    @Test
    @DisplayName("Test updateUserByUsername(UserUpdateRequestDTO); then status isOk()")
    void testUpdateUserByUsername_thenStatusIsOk() throws Exception {
        // Arrange
        when(userService.updateByUsername(Mockito.<UserUpdateRequestDTO>any())).thenReturn(true);
        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO();
        userUpdateRequestDTO.setCurrentPassword("iloveyou");
        userUpdateRequestDTO.setCurrentRole("Current Role");
        userUpdateRequestDTO.setCurrentUserName("janedoe");
        userUpdateRequestDTO.setNewPassword("iloveyou");
        userUpdateRequestDTO.setNewRole("New Role");
        userUpdateRequestDTO.setNewUserName("janedoe");
        String content = new ObjectMapper().writeValueAsString(userUpdateRequestDTO);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.put("/api/auth/update/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Usuario actualizado correctamente"));
    }

    /**
     * Test {@link AuthController#getUser(UserDeleteRequestDTO)}.
     *
     * <p>Method under test: {@link AuthController#getUser(UserDeleteRequestDTO)}
     */
    @Test
    @DisplayName("Test getUser(UserDeleteRequestDTO)")
    void testGetUser() throws Exception {
        // Arrange
        UserFullResponseDTO userFullResponseDTO = new UserFullResponseDTO();
        userFullResponseDTO.setId("42");
        userFullResponseDTO.setPassword("iloveyou");
        userFullResponseDTO.setRole("Role");
        userFullResponseDTO.setUsername("janedoe");
        when(userService.getUserByUsername(Mockito.<String>any())).thenReturn(userFullResponseDTO);
        UserDeleteRequestDTO userDeleteRequestDTO = new UserDeleteRequestDTO();
        userDeleteRequestDTO.setUsername("janedoe");
        String content = new ObjectMapper().writeValueAsString(userDeleteRequestDTO);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/auth/getUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string(
                                        "{\"id\":\"42\",\"username\":\"janedoe\",\"role\":\"Role\",\"password\":\"iloveyou\"}"));
    }

    /**
     * Test {@link AuthController#deleteUserByUsername(UserDeleteRequestDTO)} with {@code userD}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.
     * </ul>
     *
     * <p>Method under test: {@link AuthController#deleteUserByUsername(UserDeleteRequestDTO)}
     */
    @Test
    @DisplayName("Test deleteUserByUsername(UserDeleteRequestDTO) with 'userD'; then status isOk()")
    void testDeleteUserByUsernameWithUserD_thenStatusIsOk() throws Exception {
        // Arrange
        when(userService.deleteByUsername(Mockito.<String>any())).thenReturn(true);
        UserDeleteRequestDTO userDeleteRequestDTO = new UserDeleteRequestDTO();
        userDeleteRequestDTO.setUsername("janedoe");
        String content = new ObjectMapper().writeValueAsString(userDeleteRequestDTO);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.delete("/api/auth/delete/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Usuario eliminado correctamente"));
    }

    /**
     * Test {@link AuthController#deleteUserByUsername(String)} with {@code username}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.
     * </ul>
     *
     * <p>Method under test: {@link AuthController#deleteUserByUsername(String)}
     */
    @Test
    @DisplayName("Test deleteUserByUsername(String) with 'username'; then status isNotFound()")
    void testDeleteUserByUsernameWithUsername_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(userService.deleteByUsername(Mockito.<String>any())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.delete("/api/auth/delete/user/{username}", "janedoe");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Usuario no encontrado"));
    }

    /**
     * Test {@link AuthController#deleteUserByUsername(String)} with {@code username}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.
     * </ul>
     *
     * <p>Method under test: {@link AuthController#deleteUserByUsername(String)}
     */
    @Test
    @DisplayName("Test deleteUserByUsername(String) with 'username'; then status isOk()")
    void testDeleteUserByUsernameWithUsername_thenStatusIsOk() throws Exception {
        // Arrange
        when(userService.deleteByUsername(Mockito.<String>any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.delete("/api/auth/delete/user/{username}", "janedoe");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Usuario eliminado correctamente"));
    }

    /**
     * Test {@link AuthController#dummy(UserRequestLoginDTO)}.
     *
     * <p>Method under test: {@link AuthController#dummy(UserRequestLoginDTO)}
     */
    @Test
    @DisplayName("Test dummy(UserRequestLoginDTO)")
    void testDummy() throws Exception {
        // Arrange
        UserRequestLoginDTO userRequestLoginDTO = new UserRequestLoginDTO();
        userRequestLoginDTO.setPassword("iloveyou");
        userRequestLoginDTO.setUsername("janedoe");
        String content = new ObjectMapper().writeValueAsString(userRequestLoginDTO);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/auth/dummytest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("test"));
    }
}
