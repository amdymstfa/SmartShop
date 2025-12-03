package com.microtech.smartshop.service;

import com.microtech.smartshop.entity.User;

import jakarta.servlet.http.HttpSession;

public interface AuthService {

    /**
     * Authenticates a user
     */

    User login(String username, String password);

    /**
     * Logs out the current user
     */

    void logout();

    /**
     * Retrieves the currently logged-in user
     */
    User getCurrentUser();

    /**
     * Checks if a user is authenticated
     */
    boolean isAuthenticated();

    /**

     * Checks if the current user is an administrator
     */
    boolean isAdmin();

}
