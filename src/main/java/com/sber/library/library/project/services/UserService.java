package com.sber.library.library.project.services;

import com.sber.library.library.project.dto.UserDTO;
import com.sber.library.library.project.model.Role;
import com.sber.library.library.project.model.User;
import com.sber.library.library.project.repository.RoleRepository;
import com.sber.library.library.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserService
        extends GenericService<User, UserDTO> {
    private static final String CHANGE_PASSWORD_URL = "http://localhost:9090/users/change-password/";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JavaMailSender javaMailSender;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.javaMailSender = javaMailSender;
    }


    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        user.setUserDateBirth(LocalDate.parse(object.getUserDateBirth()));
        user.setUserFirstName(object.getUserFirstName());
        user.setUserLastName(object.getUserLastName());
        user.setUserMiddleName(object.getUserMiddleName());
        user.setUserLogin(object.getUserLogin());
        user.setUserPassword(object.getUserPassword());
        user.setUserPhone(object.getUserPhone());
        return userRepository.save(user);
    }

    @Override
    public User createFromDTO(UserDTO newDtoObject) {
        User user = new User();
        Role role;
        if (newDtoObject.getRole() == null) {
            role = roleRepository.findById(1L).get();
        } else {
            role = roleRepository.findById(newDtoObject.getRole().getId()).get();
        }
        user.setUserAddress(newDtoObject.getUserAddress());
        user.setUserBackUpEmail(newDtoObject.getUserBackUpEmail());
        user.setUserDateBirth(LocalDate.parse(newDtoObject.getUserDateBirth()));
        user.setUserFirstName(newDtoObject.getUserFirstName());
        user.setUserLastName(newDtoObject.getUserLastName());
        user.setUserMiddleName(newDtoObject.getUserMiddleName());
        user.setUserLogin(newDtoObject.getUserLogin());
        user.setUserPassword(bCryptPasswordEncoder.encode(newDtoObject.getUserPassword()));
        user.setUserPhone(newDtoObject.getUserPhone());
        user.setUserAddress(newDtoObject.getUserAddress());
        user.setCreatedBy(newDtoObject.getCreatedBy());
        user.setCreatedWhen(newDtoObject.getCreatedWhen());
        user.setCreatedBy("REGISTRATION FORM");
        user.setCreatedWhen(LocalDateTime.now());
        user.setRole(role);
        return userRepository.save(user);
    }


    @Override
    public User createFromEntity(User newObject) {
        newObject.setUserPassword(bCryptPasswordEncoder.encode(newObject.getUserPassword()));
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
        update(user);
        return new UserDTO(user);
    }

    public UserDTO getUserByEmail(final String email) {
        return new UserDTO(userRepository.findUserByUserBackUpEmail(email));
    }

    public void sendChangePasswordEmail(final String userBackUpEmail, final Long userId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userBackUpEmail);
        message.setSubject("Восстановление пароля на сервисе \"Онлайн Библиотека\"");
        message.setText("Добрый день! Вы получили это письмо, так как с вашего аккаунта была отправлена заявка на восстановление пароля. " +
                "Для восстановления пароля, перейдите по ссылке: " + CHANGE_PASSWORD_URL + userId);
        javaMailSender.send(message);
    }

    public void ChangePassword(Long userId, String userPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with such id " + userId + " not found!"));
        user.setUserPassword(bCryptPasswordEncoder.encode(userPassword));
        userRepository.save(user);
    }

    public User getByUserName(final String name) {
        return userRepository.findUserByUserLogin(name);
    }

    public List<String> getUserEmailsWithDelayedRentDate() {
        return userRepository.getDelayedEmails();
    }

    public Page<User> listAllPaginated(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return new PageImpl<>(users.getContent(), pageable, users.getTotalElements());
    }

    public Page<User> findUsers(UserDTO user,
                                Pageable pageable) {
        List<User> users = userRepository.findUsers(user.getUserLogin(),
                user.getUserFirstName(),
                user.getUserLastName());
        return new PageImpl<>(users, pageable, users.size());
    }


}
