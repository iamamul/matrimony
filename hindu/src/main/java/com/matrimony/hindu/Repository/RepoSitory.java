package com.matrimony.hindu.Repository;

import com.matrimony.hindu.entity.NewUser;
import com.matrimony.hindu.entity.UserDashBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoSitory extends JpaRepository<NewUser, Long> {

    NewUser findByEmail(String email);
}

