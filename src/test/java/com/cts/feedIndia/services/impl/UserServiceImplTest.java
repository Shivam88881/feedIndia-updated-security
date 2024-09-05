package com.cts.feedIndia.services.impl;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.feedIndia.entity.Role;
import com.cts.feedIndia.entity.User;
import com.cts.feedIndia.exception.AlreadyExistException;
import com.cts.feedIndia.exception.NotFoundException;
import com.cts.feedIndia.repository.RoleRepository;
import com.cts.feedIndia.repository.UserRepository;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser() throws AlreadyExistException {
        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.addUser(user);

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    public void testAddUser_AlreadyExistException() {
        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        assertThrows(AlreadyExistException.class, () -> {
            userService.addUser(user);
        });
    }

    @Test
    public void testFindUserById() throws NotFoundException {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        User result = userService.findUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    public void testFindUserById_NotFoundException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.findUserById(1);
        });
    }

    @Test
    public void testFindUserByRole() throws NotFoundException {
        Role role = new Role();
        role.setId(1);
        role.setRole("Admin");

        User user = new User();
        user.setRole(role);

        when(roleRepository.findByRole(anyString())).thenReturn(role);
        when(userRepository.findByRoleId(anyInt())).thenReturn(Collections.singletonList(user));

        List<User> result = userService.findUserByRole("Admin");

        assertFalse(result.isEmpty());
        assertEquals("Admin", result.get(0).getRole().getRole());
    }

    @Test
    public void testFindUserByRole_NotFoundException() {
        when(roleRepository.findByRole(anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            userService.findUserByRole("Admin");
        });
    }

    @Test
    public void testUpdateUserById() throws NotFoundException {
        User oldUser = new User();
        oldUser.setId(1);
        oldUser.setName("Old Name");

        User newUser = new User();
        newUser.setName("New Name");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.updateUserById(1, newUser);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
    }

    @Test
    public void testUpdateUserById_NotFoundException() {
        User newUser = new User();

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.updateUserById(1, newUser);
        });
    }

    @Test
    public void testFindUserByEmail() throws NotFoundException {
        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        User result = userService.findUserByEmail("test@test.com");

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    public void testFindUserByEmail_NotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            userService.findUserByEmail("test@test.com");
        });
    }
}

