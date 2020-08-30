package io.github.dnowo.DoitApp.service;

import io.github.dnowo.DoitApp.model.User;
import io.github.dnowo.DoitApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceCustom implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            User user = userRepository.findByUsername(username);
            return UserDetailsCustom.create(user);
        }catch(Exception e){
            throw new UsernameNotFoundException("User not exists.");
        }
//                .orElseThrow(() -> new UsernameNotFoundException("User not exists."));

    }
}
