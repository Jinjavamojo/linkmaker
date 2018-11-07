package thymeleafexamples.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thymeleafexamples.springsecurity.dao.VKDao;
import thymeleafexamples.springsecurity.entity.VkUser;

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
}
