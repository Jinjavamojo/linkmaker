package thymeleafexamples.springsecurity.dao;

import thymeleafexamples.springsecurity.entity.VkUser;
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;

import java.util.List;

public interface VKDao {

    void saveUserIfNotExists(VkUser user);

    VkUser getUserById(Long id);

    List<VkUser> getPaidUsers(int pageNumber, long projectId);
    List<VkUserPaymentDTO> getLinkedUsers(int pageNumber, long projectId);

    Long getPaidUsersCount(long projectId);

    Long getLinkedUsersCount(long projectId);

}
