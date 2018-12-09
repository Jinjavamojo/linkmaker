package thymeleafexamples.springsecurity.service;

import thymeleafexamples.springsecurity.entity.VkUser;
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;

import java.util.List;

public interface VKService {

    VkUser getUserById(Long id);

    void saveUserIfNotExists(VkUser user);

    List<VkUserPaymentDTO> getPaidUsers(int pageNumber, long projectId);

    List<VkUserPaymentDTO> getLinkedUsers(int pageNumber, long projectId);

    Long getPaidUsersCount(Long projectId);

    Long getLinkedUserCount(Long projectId);



    }
