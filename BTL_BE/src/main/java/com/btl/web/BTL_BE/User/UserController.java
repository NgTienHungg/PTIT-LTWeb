package com.btl.web.BTL_BE.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class UserController {

    private UserDAO userDAO = new UserDAO();

    @GetMapping("user/login")
    public ResponseEntity<?> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        return userDAO.selectUser(username, password);
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userDAO.isUserExists(user.getUserName())) {
            return ResponseEntity.ok(false);
        } else {
            userDAO.addUser(user.getUserName(), user.getPassword(), user.getEmail());
            return ResponseEntity.ok(true);
        }
    }
}
