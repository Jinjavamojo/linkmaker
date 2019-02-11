package thymeleafexamples.springsecurity.dao;

import thymeleafexamples.springsecurity.entity.VkUser;
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;

import java.math.BigInteger;
import java.util.List;

public interface VKDao {

    void saveUserIfNotExists(VkUser user);

    VkUser getUserById(Long id);

    List<VkUserPaymentDTO> getPaidUsers(long projectId);
    List<VkUserPaymentDTO> getPaidUsersByPage(int pageNumber, long projectId);
    List<String> getPaidUserIds(long projectId);
    Long getPaidUsersCount(long projectId);
    Long getParticallyPaidUserCount(long projectId);

    Long getVisitedUsersCount(long projectId);
    List<VkUserPaymentDTO> getVisitedUsersByPage(int pageNumber, long projectId);
    List<String> getVisitedUserIds(long projectId);
    List<VkUserPaymentDTO> getVisitedUsers(long projectId);

}
