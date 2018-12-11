package thymeleafexamples.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thymeleafexamples.springsecurity.dao.VKDao;
import thymeleafexamples.springsecurity.entity.VkUser;
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;

import java.util.List;

@Service
@Transactional
public class VKServiceImpl implements VKService {

    @Autowired
    private VKDao vkDao;

    @Override
    @Transactional(readOnly = true)
    public VkUser getUserById(Long id) {
        return vkDao.getUserById(id);
    }

    @Override
    public void saveUserIfNotExists(VkUser user) {
        vkDao.saveUserIfNotExists(user);

    }

    @Override
    @Transactional(readOnly = true)
    public List<VkUserPaymentDTO> getPaidUsers(int pageNumber, long projectId) {
        return vkDao.getPaidUsers(pageNumber, projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VkUserPaymentDTO> getLinkedUsers(int pageNumber, long projectId) {
        return vkDao.getLinkedUsers(pageNumber, projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getPaidUserIds(long projectId) {
        return vkDao.getPaidUserIds(projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VkUserPaymentDTO> getAllPaidUsers(long projectId) {
        return vkDao.getAllPaidUsers(projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getPaidUsersCount(Long projectId) {
        return vkDao.getPaidUsersCount(projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getLinkedUserCount(Long projectId) {
        return vkDao.getLinkedUsersCount(projectId);
    }
}
