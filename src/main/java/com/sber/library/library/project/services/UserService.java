package com.sber.library.library.project.services;

import com.sber.library.library.project.dto.BookDTO;
import com.sber.library.library.project.dto.UserDTO;
import com.sber.library.library.project.model.Author;
import com.sber.library.library.project.model.Book;
import com.sber.library.library.project.model.Role;
import com.sber.library.library.project.model.User;
import com.sber.library.library.project.repository.RoleRepository;
import com.sber.library.library.project.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService extends GenericService<User, UserDTO> {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @Override
    public User update(User object) {
        return userRepository.save(object);
    }

    @Override
    public User updateFromDTO(UserDTO object, Long Id) {
        User user = userRepository.findById(Id).orElseThrow(
                () -> new NotFoundException("Author with such id " + Id + " not found!"));
        user.setUserAddress(object.getUserAddress());
        user.setUserBackUpEmail(object.getUserBackUpEmail());
        user.setUserDateBirth(object.getUserDateBirth());
        user.setUserFirstName(object.getUserFirstName());
        user.setUserLastName(object.getUserLastName());
        user.setUserMiddleName(object.getUserMiddleName());
        user.setUserLogin(object.getUserLogin());
        user.setUserPassword(object.getUserPassword());
        user.setUserPhone(object.getUserPhone());
        user.setUserAddress(object.getUserAddress());
        user.setRole(object.getRole());
        return userRepository.save(user);
    }

    @Override
    public User createFromDTO(UserDTO newDtoObject) {
        User newUser = new User();
        newUser.setUserAddress(newDtoObject.getUserAddress());
        newUser.setUserBackUpEmail(newDtoObject.getUserBackUpEmail());
        newUser.setUserDateBirth(newDtoObject.getUserDateBirth());
        newUser.setUserFirstName(newDtoObject.getUserFirstName());
        newUser.setUserLastName(newDtoObject.getUserLastName());
        newUser.setUserMiddleName(newDtoObject.getUserMiddleName());
        newUser.setUserLogin(newDtoObject.getUserLogin());
        newUser.setUserPassword(newDtoObject.getUserPassword());
        newUser.setUserPhone(newDtoObject.getUserPhone());
        newUser.setUserAddress(newDtoObject.getUserAddress());
        newUser.setCreatedBy(newDtoObject.getCreatedBy());
        newUser.setCreatedWhen(newDtoObject.getCreatedWhen());
        newUser.setRole(newDtoObject.getRole());
        return userRepository.save(newUser);
    }


    @Override
    public User createFromEntity(User newObject) {
        return userRepository.save(newObject);
    }

    @Override
    public void delete(Long objectId) {
        User user = userRepository.findById(objectId).orElseThrow(
                () -> new NotFoundException("User with such id " + objectId + " not found!"));
        userRepository.delete(user);
    }

    @Override
    public User getOne(Long objectId) {
        return userRepository.findById(objectId).orElseThrow(
                () -> new NotFoundException("User with such id " + objectId + " not found!"));
    }

    @Override
    public List<User> listAll() {
        return userRepository.findAll();
    }

    /**
     * Ищем все книги заданного автора
     *
     * @return List<Book> - список книг автора
     */
    public UserDTO addRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with such id " + userId + " not found!"));
        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new NotFoundException("Role with such id " + roleId + " not found!"));
        user.setRole(role);
        UserDTO userDTO = new UserDTO(user);
        return userDTO;
    }
}
