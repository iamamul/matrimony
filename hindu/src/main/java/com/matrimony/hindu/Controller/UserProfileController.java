package com.matrimony.hindu.Controller;

import com.matrimony.hindu.Repository.RepoSitory;
import com.matrimony.hindu.Repository.UserDashBoardRepository;
import com.matrimony.hindu.dto.ProfileSearchDTO;
import com.matrimony.hindu.entity.NewUser;
import com.matrimony.hindu.entity.UserDashBoard;
import com.matrimony.hindu.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private RepoSitory repo;

    @Autowired
    private UserDashBoardRepository userDashBoardRepository;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("photo");  // Ignore binding of 'photo'
    }

    // Save Profile
    @PostMapping("/saveProfile")
    public String saveProfile(@ModelAttribute UserDashBoard dash, @RequestParam("newUserId") Long userId) {
        NewUser user = repo.findById(userId).orElse(null);
        if (user == null) {
            // Handle user not found scenario
            return "redirect:/error";
        }

        // Check if a UserDashBoard already exists for this NewUser
        UserDashBoard existingDashBoard = userDashBoardRepository.findByNewUser(user);
        if (existingDashBoard != null) {
            // Update the existing UserDashBoard if one exists
            existingDashBoard.setDob(dash.getDob());
            existingDashBoard.setDegree(dash.getDegree());
            existingDashBoard.setInstitute(dash.getInstitute());
            existingDashBoard.setLinkedInProfile(dash.getLinkedInProfile());
            existingDashBoard.setHeight(dash.getHeight());
            existingDashBoard.setWeight(dash.getWeight());
            existingDashBoard.setHairColor(dash.getHairColor());
            existingDashBoard.setFaceColor(dash.getFaceColor());
            existingDashBoard.setBloodGroup(dash.getBloodGroup());
            existingDashBoard.setCountry(dash.getCountry());
            existingDashBoard.setState(dash.getState());
            existingDashBoard.setCity(dash.getCity());
            existingDashBoard.setPostalCode(dash.getPostalCode());
            existingDashBoard.setCurrentAddress(dash.getCurrentAddress());
            existingDashBoard.setPermanentAddress(dash.getPermanentAddress());
            existingDashBoard.setDesignation(dash.getDesignation());
            existingDashBoard.setCompany(dash.getCompany());
            existingDashBoard.setCompanyCountry(dash.getCompanyCountry());
            existingDashBoard.setCompanyState(dash.getCompanyState());
            existingDashBoard.setCompanyCity(dash.getCompanyCity());
            existingDashBoard.setSalary(dash.getSalary());
            existingDashBoard.setLanguagesKnown(dash.getLanguagesKnown());
            existingDashBoard.setInterests(dash.getInterests());
            existingDashBoard.setFatherName(dash.getFatherName());
            existingDashBoard.setMotherName(dash.getMotherName());
            existingDashBoard.setSiblings(dash.getSiblings());
            existingDashBoard.setDisability(dash.isDisability());

            // Save the updated UserDashBoard
            userDashBoardRepository.save(existingDashBoard);
        }

        return "redirect:/dashboard";
    }



    /*@GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        NewUser newUser = (NewUser) session.getAttribute("loggedInUser");

        if (newUser == null) {
            return "redirect:/login"; // If not logged in, redirect to login page
        }

        // Debugging: Print the name to check if it's properly populated
        System.out.println("Logged in user: " + newUser.getFirstName());

        UserDashBoard userDashBoard = userDashBoardRepository.findByNewUser(newUser);

        userDashBoard.setNewUser(newUser);
        model.addAttribute("userDashBoard", userDashBoard);
        return "dashboard";
    }*/

    /*@GetMapping("/searchProfile")
    public String profiles(@RequestParam(required = false) String city,
                                 @RequestParam(required = false) Integer minAge,
                                 @RequestParam(required = false) Integer maxAge,
                                 @RequestParam(defaultValue = "0") int page,
                                 Model model,
                                 Principal principal) {

        System.out.println("Inside search filter > ");
        NewUser currentUser = repo.findByEmail(principal.getName()); // get logged in user
        String gender = currentUser.getGender();

        ProfileSearchCriteria criteria = new ProfileSearchCriteria();
        criteria.setCity(city);
        criteria.setMinAge(minAge);
        criteria.setMaxAge(maxAge);

        Page<UserDashBoard> resultPage = userProfileService.getFilteredProfiles(criteria, gender, page, 10);

        System.out.println("result page > " +resultPage);

        model.addAttribute("profiles", resultPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", resultPage.getTotalPages());
        return "dashboard";
    }*/



    @GetMapping("/user-photos/{userId}")
    @ResponseBody
    public ResponseEntity<byte[]> getUserPhoto(@PathVariable Long userId) {
        UserDashBoard user = userProfileService.findById(userId);
        byte[] image = user.getPhoto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // or detect dynamically
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    @GetMapping("/search")
    public String showSearchPage(Model model) {
        model.addAttribute("searchDTO", new ProfileSearchDTO());
        return "search"; // points to search.html
    }

    @PostMapping("/search")
    public String searchProfiles(@ModelAttribute("searchDTO") ProfileSearchDTO searchDTO,
                                 Model model,
                                 Principal principal) {

        System.out.println("geName > " +principal.getName());
        NewUser currentUser = repo.findByEmail(principal.getName());
        String currentGender = currentUser.getGender();

        List<UserDashBoard> results = userProfileService.searchMatchingProfiles(currentGender, searchDTO);
        model.addAttribute("results", results);
        return "dashboard";
    }
}
