package thymeleafexamples.springsecurity.dao;


import thymeleafexamples.springsecurity.entity.User;

public interface UserDao {

    User findByUserName(String userName);
    
    void save(User user);
    
}
