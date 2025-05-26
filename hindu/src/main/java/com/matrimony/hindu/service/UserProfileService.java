package com.matrimony.hindu.service;

import com.matrimony.hindu.Repository.UserDashBoardRepository;
import com.matrimony.hindu.dto.ProfileSearchDTO;
import com.matrimony.hindu.entity.NewUser;
import com.matrimony.hindu.entity.UserDashBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileService {

    @Autowired
    private UserDashBoardRepository userDashBoardRepository;

    @Autowired
    private UserDashBoardRepository newUserRepository; // To fetch user data based on userId

    // Save user profile dashboard information
    public void saveUserProfile(UserDashBoard userDashBoard) {
        // Save the dashboard information to the database
        userDashBoardRepository.save(userDashBoard);
    }

    // Retrieve user dashboard information by userId
    public UserDashBoard getUserProfile(Long userId) {
        // Retrieve the user's dashboard information
        return userDashBoardRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Dashboard not found for ID: " + userId));
    }

    public String encodePhotoToBase64(byte[] photoBytes) {
        return Base64.getEncoder().encodeToString(photoBytes);
    }



    public UserDashBoard findById(Long id) {
        return userDashBoardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

   /* public Page<UserDashBoard> getFilteredProfiles(ProfileSearchCriteria criteria, String currentUserGender, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        LocalDate today = LocalDate.now();
        LocalDate minDob = criteria.getMaxAge() != null ? today.minusYears(criteria.getMaxAge()) : null;
        LocalDate maxDob = criteria.getMinAge() != null ? today.minusYears(criteria.getMinAge()) : null;

        return userDashBoardRepository.searchOppositeGenderProfiles(currentUserGender, criteria.getCity(), minDob, maxDob, pageable);
    }*/

    public List<UserDashBoard> searchMatchingProfiles(String gender, ProfileSearchDTO dto) {
        return userDashBoardRepository.searchProfiles(
                gender,
                dto.getMinAge() != null ? dto.getMinAge() : 18,
                dto.getMaxAge() != null ? dto.getMaxAge() : 100,
                dto.getCity() != null && !dto.getCity().isEmpty() ? dto.getCity() : null
        );
    }

    public UserDashBoard getUserDashBoard(Long newUserId) {
        Optional<UserDashBoard> optionalDashboard = userDashBoardRepository.findByNewUserId(newUserId);
        return optionalDashboard.orElseGet(() -> {
            // If no dashboard found, optionally create a blank one or throw an exception
            UserDashBoard emptyDashboard = new UserDashBoard();
            // You can set the newUser here if needed or leave blank
            // emptyDashboard.setNewUser(...);
            return emptyDashboard;
        });
    }

    public UserDashBoard getUserDashBoardByUsername(String username) {
        // Find NewUser by username
        NewUser newUser = newUserRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find UserDashBoard by NewUser id
        return userDashBoardRepository.findByNewUserId(newUser.getId())
                .orElse(new UserDashBoard()); // Or throw exception or handle empty case
    }

    public List<UserDashBoard> searchFilteredUsers(Integer minAge, Integer maxAge, String city, String gender, Long currentUserId) {
        List<UserDashBoard> allProfiles = userDashBoardRepository.findAll();

        return allProfiles.stream()
                .filter(p -> p.getNewUser().getId() != currentUserId) // Exclude self
                .filter(p -> p.getNewUser().getGender().equalsIgnoreCase(gender)) // Opposite gender
                .filter(p -> {
                    int age = Period.between(p.getDob(), LocalDate.now()).getYears();
                    return (minAge == null || age >= minAge) &&
                            (maxAge == null || age <= maxAge);
                })
                .filter(p -> city == null || city.isBlank() || p.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }
}
