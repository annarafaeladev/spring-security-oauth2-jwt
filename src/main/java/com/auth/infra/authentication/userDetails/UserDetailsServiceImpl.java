package com.auth.infra.authentication.userDetails;

import com.auth.respository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public com.auth.infra.authentication.userDetails.UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
       var user =userRepository.findByDocument(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com document: " + username));
	   
	   return new com.auth.infra.authentication.userDetails.UserDetailsImpl(user);
    }

}
