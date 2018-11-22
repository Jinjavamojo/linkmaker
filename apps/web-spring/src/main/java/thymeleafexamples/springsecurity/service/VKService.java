package thymeleafexamples.springsecurity.service;

import thymeleafexamples.springsecurity.entity.VkUser;

import java.util.List;

public interface VKService {

    VkUser getUserById(Long id);

    void saveUserIfNotExists(VkUser user);

    List<VkUser> getPaidUsers(int pageNumber, long projectId);

    List<VkUser> getUnpaidUsers(int pageNumber, long projectId);

    Long getPaidUsersCount(Long projectId);

    Long getUnaidUsersCount(Long projectId);



    }
