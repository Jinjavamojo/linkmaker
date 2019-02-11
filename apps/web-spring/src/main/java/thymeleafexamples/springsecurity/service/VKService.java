package thymeleafexamples.springsecurity.service;

import thymeleafexamples.springsecurity.entity.VkUser;
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;

import java.util.List;

public interface VKService {

    VkUser getUserById(Long id);

    void saveUserIfNotExists(VkUser user);

    List<String> getPaidUserIds(long projectId);
    List<VkUserPaymentDTO> getAllPaidUsers(long projectId);
    Long getPaidUsersCount(Long projectId);
    Long getParticallyPaidUserCount(Long projectId);
    List<VkUserPaymentDTO> getPaidUsers(int pageNumber, long projectId);

    List<String> getVisitedUserIds(long projectId);
    List<VkUserPaymentDTO> getVisitedUsers(long projectId);
    Long getLinkedUserCount(Long projectId);
    List<VkUserPaymentDTO> getLinkedUsers(int pageNumber, long projectId);

}
