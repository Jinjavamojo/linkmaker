package thymeleafexamples.springsecurity.dao;

import thymeleafexamples.springsecurity.entity.VkUser;

import java.util.List;

public interface VKDao {

    void saveUserIfNotExists(VkUser user);

    VkUser getUserById(Long id);

    List<VkUser> getPaidUsers(int pageNumber, long projectId);
    List<VkUser> getUnpaidUsers(int pageNumber, long projectId);

    Long getPaidUsersCount(long projectId);

    Long getUnpaidUsers(long projectId);

}
