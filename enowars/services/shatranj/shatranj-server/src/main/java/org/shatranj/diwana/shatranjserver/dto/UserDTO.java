package org.shatranj.diwana.shatranjserver.dto;

import javax.validation.constraints.NotBlank;

public class UserDTO extends DTO {
    @NotBlank
    private String username;

    public UserDTO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
