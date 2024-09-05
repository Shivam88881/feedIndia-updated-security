package com.cts.feedIndia.controller;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.BadRequestException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.UserRepository;
import com.cts.feedIndia.services.impl.RoleServiceImpl;
import com.cts.feedIndia.services.impl.UserServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private RoleServiceImpl roleService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindUserById() throws NotFoundException {
        User user = new User();
        user.setId(1);

        when(userService.findUserById(anyInt())).thenReturn(user);

        ResponseEntity<User> result = userController.findUserById(1);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().getId());
    }

    @Test
    public void testFindUserById_NotFoundException() throws NotFoundException {
        when(userService.findUserById(anyInt())).thenThrow(new NotFoundException("No User found with the provided ID"));

        assertThrows(NotFoundException.class, () -> {
            userController.findUserById(1);
        });
    }

    @Test
    public void testUpdateUserById() throws NotFoundException, BadRequestException {
        User user = new User();
        user.setId(1);
        user.setName("Updated Name");

        when(userService.updateUserById(anyInt(), any(User.class))).thenReturn(user);

        ResponseEntity<User> result = userController.updateUserById(1, user);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Updated Name", result.getBody().getName());
    }

    @Test
    public void testUpdateUserById_NotFoundException() throws NotFoundException, BadRequestException {
        User user = new User();

        when(userService.updateUserById(anyInt(), any(User.class))).thenThrow(new NotFoundException("Failed to update the User"));

        assertThrows(NotFoundException.class, () -> {
            userController.updateUserById(1, user);
        });
    }

    @Test
    public void testDeleteUserById() throws NotFoundException, BadRequestException {
        when(userService.findUserById(anyInt())).thenReturn(new User());
        when(userService.deleteUserById(anyInt())).thenReturn(true);

        ResponseEntity<String> result = userController.deleteUserById(1);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User deleted successfully", result.getBody());
    }

    @Test
    public void testDeleteUserById_NotFoundException() throws NotFoundException, BadRequestException {
        when(userService.findUserById(anyInt())).thenThrow(new NotFoundException("No User exist with given id"));

        assertThrows(NotFoundException.class, () -> {
            userController.deleteUserById(1);
        });
    }

    @Test
    public void testFindUserByEmail() throws NotFoundException {
        User user = new User();
        user.setEmail("test@test.com");

        when(userService.findUserByEmail(anyString())).thenReturn(user);

        ResponseEntity<User> result = userController.findUserByEmail("test@test.com");

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("test@test.com", result.getBody().getEmail());
    }

    @Test
    public void testFindUserByEmail_NotFoundException() throws NotFoundException {
        when(userService.findUserByEmail(anyString())).thenThrow(new NotFoundException("No User found with the provided email"));

        assertThrows(NotFoundException.class, () -> {
            userController.findUserByEmail("test@test.com");
        });
    }

    @Test
    public void testFindUserByRole() throws NotFoundException {
        List<User> users = new ArrayList<>();
        users.add(new User());

        when(userService.findUserByRole(anyString())).thenReturn(users);

        ResponseEntity<List<User>> result = userController.findUserByRole("Admin");

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse(result.getBody().isEmpty());
    }

    @Test
    public void testFindUserByRole_NotFoundException() throws NotFoundException {
        when(userService.findUserByRole(anyString())).thenThrow(new NotFoundException("No Users found with the provided role"));

        assertThrows(NotFoundException.class, () -> {
            userController.findUserByRole("Admin");
        });
    }

}
