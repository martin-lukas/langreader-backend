package net.langreader.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String username;
    private List<String> roles;

    public JwtResponse(String token, Integer id, String username, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}