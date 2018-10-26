package thymeleafexamples.springsecurity.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import thymeleafexamples.springsecurity.entity.User;

public interface UserService extends UserDetailsService {

    User findByUserName(String userName);

    //void save(CrmUser crmUser);
}
