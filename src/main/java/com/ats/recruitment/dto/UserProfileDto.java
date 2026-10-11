package com.ats.recruitment.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class UserProfileDto {

    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank(message = "Mobile cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobile;

    @NotNull(message = "Experience cannot be empty")
    @Min(0)
    private Double experience;

    @NotBlank(message = "Gender cannot be empty")
    private String gender;

    @NotNull(message = "Age cannot be empty")
    @Min(18)
    private Integer age;

    @NotBlank(message = "Location cannot be empty")
    private String location;

    private String resumeOriginalName;
    private MultipartFile newResumeFile;

    public UserProfileDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Double getExperience() {
        return experience;
    }

    public void setExperience(Double experience) {
        this.experience = experience;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getResumeOriginalName() {
        return resumeOriginalName;
    }

    public void setResumeOriginalName(String resumeOriginalName) {
        this.resumeOriginalName = resumeOriginalName;
    }

    public MultipartFile getNewResumeFile() {
        return newResumeFile;
    }

    public void setNewResumeFile(MultipartFile newResumeFile) {
        this.newResumeFile = newResumeFile;
    }
}
