package com.devsuperior.dscatalog.DTO;

import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;

import java.util.List;


public class UserDTO {

    private Long id;

    @NotBlank(message = "Campo obrigatório")
    private String firstName;
    private String lastName;

    @Email(message = "Favor inserir um email válido")
    private String email;

    private List<RoleDTO> roles = new ArrayList<>();

    public UserDTO(){
    }

    public UserDTO(User entity){
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        email = entity.getEmail();
        for (Role role : entity.getRoles()){
            roles.add(new RoleDTO(role));
        }
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }
}
