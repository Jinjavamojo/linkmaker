package thymeleafexamples.springsecurity.dao;

import thymeleafexamples.springsecurity.entity.VkUser;
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;

import java.math.BigInteger;
import java.util.List;

public interface VKDao {

    void saveUserIfNotExists(VkUser user);

    VkUser getUserById(Long id);

    List<VkUserPaymentDTO> getPaidUsers(int pageNumber, long projectId);
    List<VkUserPaymentDTO> getLinkedUsers(int pageNumber, long projectId);

    List<String> getPaidUserIds(long projectId);

    Long getPaidUsersCount(long projectId);

    Long getLinkedUsersCount(long projectId);

    List<VkUserPaymentDTO> getAllPaidUsers(long projectId);

}
