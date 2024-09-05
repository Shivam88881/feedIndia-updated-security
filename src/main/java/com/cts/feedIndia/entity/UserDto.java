package com.cts.feedIndia.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private String dob;
    private String city;
    private Boolean verified;
    private Boolean emailVerified;
    private String phone;
    private String avatar;
    private String userDetail;
    private Role role;
    
    public void convertToDto(User user) {
        this.setId(user.getId());
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setDob(user.getDob());
        this.setCity(user.getCity());
        this.setVerified(user.getVerified());
        this.setEmailVerified(user.getEmailVerified());
        this.setPhone(user.getPhone());
        this.setAvatar(user.getAvatar());
        this.setUserDetail(user.getUserDetail());
        this.setRole(user.getRole());
    }


}

