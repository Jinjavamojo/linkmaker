package thymeleafexamples.springsecurity.dao;

import thymeleafexamples.springsecurity.entity.VkUser;

public interface VKDao {

    void saveUserIfNotExists(VkUser user);

    VkUser getUserById(Long id);
}
