package com.sber.library.library.project.controller;

import com.sber.library.library.project.model.Role;
import com.sber.library.library.project.repository.RoleRepository;
import com.sber.library.library.project.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/roles")
//CORS Filters
@CrossOrigin(value = "*", allowedHeaders = "*")
@Tag(name = "Роли", description = "Контроллер для работы с ролями пользователей.")
public class RoleController {
    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Operation(description = "Получить информацию о роли пользователя", method = "getOne")
    @RequestMapping(value = "/getRole", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Role> getOne(@RequestParam(value = "oleId") Long roleId) {
        return ResponseEntity.status(HttpStatus.OK).body(roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Such role with id= " + roleId + "was not found")));
    }

    @Operation(description = "Получить информации обо всех ролях", method = "listAllRoles")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Role>> listAllRoles() {
        return ResponseEntity.status(HttpStatus.OK).body(roleRepository.findAll());
    }

    @Operation(description = "Добавить роль в систему", method = "add")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Role> add(@RequestBody Role newRole) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleRepository.save(newRole));
    }

    @Operation(description = "Изменить информацию о роли по id", method = "updateRole")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Role> updateRole(@RequestBody Role role,
                                           @RequestParam(value = "roleId") Long roleId) {
        Role oldRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Such role with id= " + roleId + "was not found"));
        oldRole.setRoleTitle(role.getRoleTitle());
        oldRole.setRoleDescription(role.getRoleDescription());
        return ResponseEntity.status(HttpStatus.OK).body(roleRepository.save(oldRole));
    }

    @Operation(description = "Удалить роль по id", method = "delete")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestParam(value = "roleId") Long roleId) {
        roleRepository.delete(roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Such role with id= " + roleId + "was not found")));
        return ResponseEntity.status(HttpStatus.OK).body("Роль успешно удалена");
    }
}
