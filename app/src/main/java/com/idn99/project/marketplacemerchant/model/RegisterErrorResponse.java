package com.idn99.project.marketplacemerchant.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RegisterErrorResponse {
    @SerializedName("email")
    List<String> emailError = new ArrayList<>();

    @SerializedName("password")
    List<String> passwordError = new ArrayList<>();

    public List<String> getEmailError() {
        return emailError;
    }

    public List<String> getPasswordError() {
        return passwordError;
    }
}

