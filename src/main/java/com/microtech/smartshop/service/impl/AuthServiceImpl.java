package com.microtech.smartshop.service.impl ;


import com.microtech.smartshop.entity.User;
import com.microtech.smartshop.enums.UserRole;
import com.microtech.smartshop.exception.UnauthorizedException;
import com.microtech.smartshop.repository.UserRepository;
import com.microtech.smartshop.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository ;
    private final HttpSession session ;

    private static final String USER_SESSION_KEY = "CURRENT_USER";


    @Override
    public User login(String username, String password) {
        // find user with username
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        ()-> new UnauthorizedException("Invalid credentials")
                );

        // check password
        if (!user.getPassword().equals(password)){
            throw new UnauthorizedException("Invalid credentials");
        }

        return user;
    }

    @Override
    public void logout() {
        User user = getCurrentUser();
        session.removeAttribute(USER_SESSION_KEY);
        session.invalidate();
        log.info("User logging {}", user != null ? user.getPassword() : "Unknow");
    }

    @Override
    public User getCurrentUser() {
        Object userObj = session.getAttribute(USER_SESSION_KEY);

        if (userObj instanceof User){
            return (User) userObj;
        }
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return getCurrentUser() != null;
    }

    @Override
    public boolean isAdmin() {
        User user = getCurrentUser();
        return user != null && user.getRole() == UserRole.ADMIN ;
    }
}