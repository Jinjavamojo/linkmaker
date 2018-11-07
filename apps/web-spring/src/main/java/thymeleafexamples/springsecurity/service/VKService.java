package thymeleafexamples.springsecurity.service;

import thymeleafexamples.springsecurity.entity.VkUser;

public interface VKService {

    VkUser getUserById(Long id);

    void saveUserIfNotExists(VkUser user);


}
