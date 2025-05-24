package com.matrimony.hindu.Repository;

import com.matrimony.hindu.entity.NewUser;
import com.matrimony.hindu.entity.UserDashBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface  UserDashBoardRepository extends JpaRepository<UserDashBoard, Long> {
    // Custom query methods can be added here if needed

    UserDashBoard findByNewUser(NewUser newUser);

    Optional<UserDashBoard> findByNewUserId(Long newUserId);

    @Query("SELECT u FROM NewUser u WHERE u.email = :email")
    Optional<NewUser> findByEmail(@Param("email") String email);

    /*@Query("SELECT u FROM UserDashBoard u " +
            "WHERE u.newUser.gender <> :currentUserGender " +
            "AND (:city IS NULL OR u.city = :city) " +
            "AND (:minDob IS NULL OR u.dob <= :minDob) " +
            "AND (:maxDob IS NULL OR u.dob >= :maxDob)")
    Page<UserDashBoard> searchOppositeGenderProfiles(@Param("currentUserGender") String currentUserGender,
                                                     @Param("city") String city,
                                                     @Param("minDob") LocalDate minDob,
                                                     @Param("maxDob") LocalDate maxDob,
                                                     Pageable pageable);*/

    @Query("SELECT u FROM UserDashBoard u WHERE " +
            "u.newUser.gender <> :currentGender AND " +
            "(YEAR(CURRENT_DATE) - YEAR(u.dob)) BETWEEN :minAge AND :maxAge AND " +
            "(:city IS NULL OR u.city = :city)")
    List<UserDashBoard> searchProfiles(@Param("currentGender") String currentGender,
                                       @Param("minAge") int minAge,
                                       @Param("maxAge") int maxAge,
                                       @Param("city") String city);
}
