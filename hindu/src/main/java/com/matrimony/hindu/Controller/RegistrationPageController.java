package com.matrimony.hindu.Controller;

import com.matrimony.hindu.Repository.RepoSitory;
import com.matrimony.hindu.configure.SecurityConfig;
import com.matrimony.hindu.entity.NewUser;
import com.matrimony.hindu.entity.UserDashBoard;
import com.matrimony.hindu.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RegistrationPageController {

    @Autowired
    private RepoSitory repos;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(RegistrationPageController.class);

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        logger.info("inside showRegisterForm > ");
        model.addAttribute("user", new NewUser());
        return "register"; // This returns register.html
    }

    @PostMapping("/register")
    public String userRegistration(@ModelAttribute NewUser user, Model model) {
        logger.info("inside userRegistration ");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        NewUser savedUser = repos.save(user);

        // Create corresponding UserDashBoard
        UserDashBoard dashBoard = new UserDashBoard();
        dashBoard.setNewUser(savedUser);
        userProfileService.saveUserProfile(dashBoard);

        // Add confirmation message to model
        model.addAttribute("message", "User registered successfully!");

        return "registration-success";
    }

    @GetMapping("/choose-category")
    public String showCategoryPage() {
        return "choose-category"; // maps to choose-category.html
    }

    @GetMapping("/custom-login")
    public String showLoginForm(Model model) {
        logger.info("inside showLogginForm > ");
        model.addAttribute("user", new NewUser());
        return "register";
    }


   @PostMapping("/custom-login")
    public String userLogin(@ModelAttribute NewUser user, Model model, HttpSession session) {
        logger.info("inside custom-login method > ");
        NewUser existingUser = repos.findByEmail(user.getEmail());
        logger.info("find existing user > " + existingUser);

        if (existingUser != null  && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            logger.info("Inside IF condition check > 1 ");
            session.setAttribute("loggedInUser", existingUser); // ✅ Save user in session
            logger.info("Inside IF condition check > 2 ");
            return "redirect:/dashboard";
        } else {
            logger.info("Inside ELSE condition check > ");
            model.addAttribute("error", "Invalid email or password");
            return "register";
        }
    }


    @GetMapping("/login")
    public String loginPage() {
        logger.info("Inside loginPage >>> ");
        return "login";  // Return your login.html view name
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model, Authentication authentication) {
        logger.info("Inside dashboardPage >>> ");
        String username = authentication.getName();
        model.addAttribute("username", username);

        UserDashBoard userDashBoard = userProfileService.getUserDashBoardByUsername(username);
        model.addAttribute("userDashBoard", userDashBoard);

        return "dashboard";  // dashboard.html view
    }
}

