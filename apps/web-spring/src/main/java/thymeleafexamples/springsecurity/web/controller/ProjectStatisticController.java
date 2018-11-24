package thymeleafexamples.springsecurity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import thymeleafexamples.springsecurity.config.SessionAttr;
import thymeleafexamples.springsecurity.entity.VkUser;
import thymeleafexamples.springsecurity.service.VKService;

import java.util.List;

@Controller
@SessionAttributes("sessionAttr")
public class ProjectStatisticController {

    @Autowired
    private VKService vkService;


    @RequestMapping(value = "/project/statistic",method = RequestMethod.GET)
    public ModelAndView showPaidUsers(ModelAndView model, SessionAttr sessionAttr) {
        return getPaidUsersView(0, model,sessionAttr);
    }

    @RequestMapping(value = "/project/statistic/unpaid",method = RequestMethod.GET)
    public ModelAndView showUnpaidUsers(ModelAndView model, SessionAttr sessionAttr) {
        model.addObject("activeTab","statistic");
        model.addObject("activeContentTab","unpaid");
        model.setViewName("project/statistic_second_tab");

        Long currentProjectId = sessionAttr.currentProjectId;
        if (currentProjectId == null) {
            throw new RuntimeException("currentProjectId is null");
        }
        Long paidUsersCount = vkService.getPaidUsersCount(currentProjectId);
        Long pages = paidUsersCount / 10;
        if (paidUsersCount % 10  > 0) {
            pages++;
        }

//        List<VkUser> userPagination = vkService.getPaidUsers(page,currentProjectId);
//        model.addObject("vk_users",userPagination);
//        model.addObject("totalPages",pages);
        return model;
    }



    @RequestMapping(value = "/project/statistic/page/{page}",method = RequestMethod.GET)
    public ModelAndView showPaidUsers(@PathVariable("page") int page, ModelAndView model, SessionAttr sessionAttr) {
        return getPaidUsersView(page, model,sessionAttr);
    }

    private ModelAndView getPaidUsersView(int page, ModelAndView model, SessionAttr sessionAttr) {
        model.addObject("activeTab","statistic");
        model.setViewName("project/statistic_first_tab");

        Long currentProjectId = sessionAttr.currentProjectId;
        if (currentProjectId == null) {
            throw new RuntimeException("currentProjectId is null");
        }
        Long paidUsersCount = vkService.getPaidUsersCount(currentProjectId);
        Long pages = paidUsersCount / 10;
        if (paidUsersCount % 10  > 0) {
            pages++;
        }

        List<VkUser> userPagination = vkService.getPaidUsers(page,currentProjectId);
        model.addObject("vk_users",userPagination);
        model.addObject("totalPages",pages);
        return model;
    }
}
