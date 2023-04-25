package com.zfsoft.microservice.workflow.config;


import com.zfsoft.microservice.workflow.data.WorkflowAuthUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WorkflowAuthUserDetails authUserDetails = new WorkflowAuthUserDetails();
        authUserDetails.setUsername(username);
        return authUserDetails;
    }

}
