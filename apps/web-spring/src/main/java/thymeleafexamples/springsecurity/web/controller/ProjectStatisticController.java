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
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;
import thymeleafexamples.springsecurity.report.ReportUtils;
import thymeleafexamples.springsecurity.service.VKService;

import java.util.List;

@Controller
@SessionAttributes("sessionAttr")
public class ProjectStatisticController {

    @Autowired
    private VKService vkService;




    private ModelAndView showLinkedUsersView(int page, ModelAndView model, SessionAttr sessionAttr) {
        model.addObject("activeTab","statistic");
        model.setViewName("project/statistic_second_tab");

        Long currentProjectId = sessionAttr.currentProjectId;
        if (currentProjectId == null) {
            throw new RuntimeException("currentProjectId is null");
        }
        Long linkedUserCount = vkService.getLinkedUserCount(currentProjectId);
        Long pages = linkedUserCount / 10;
        if (linkedUserCount % 10  > 0) {
            pages++;
        }

        List<VkUserPaymentDTO> linkedUsers = vkService.getLinkedUsers(page, currentProjectId);
        model.addObject("payment_infos",linkedUsers);
        model.addObject("totalPages",pages);
        return model;
    }

    @RequestMapping(value = "/project/statistic/linked",method = RequestMethod.GET)
    public ModelAndView showLinkedUsers(ModelAndView model, SessionAttr sessionAttr) {
        return showLinkedUsersView(0, model,sessionAttr);
    }

    @RequestMapping(value = "/project/statistic/linked/page/{page}",method = RequestMethod.GET)
    public ModelAndView showLinkedUsers(@PathVariable("page") int page, ModelAndView model, SessionAttr sessionAttr) {
        return showLinkedUsersView(page, model,sessionAttr);
    }



    @RequestMapping(value = "/project/statistic/paid",method = RequestMethod.GET)
    public ModelAndView showPaidUsers(ModelAndView model, SessionAttr sessionAttr) {
        return getPaidUsersView(0, model,sessionAttr);
    }

    @RequestMapping(value = "/project/statistic/paid/page/{page}",method = RequestMethod.GET)
    public ModelAndView showPaidUsers(@PathVariable("page") int page, ModelAndView model, SessionAttr sessionAttr) {
        return getPaidUsersView(page, model,sessionAttr);
    }

    private ModelAndView getPaidUsersView(int page, ModelAndView model, SessionAttr sessionAttr) {
        model.addObject("activeTab","statistic");
        model.setViewName("project/statistic_first_tab");
        model.addObject("base","web-spring"); //TODO REPLACE BY PROPERTY

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
        model.addObject("report_types", ReportUtils.getTypes());
        return model;
    }





    @RequestMapping(value = "/project/statistic/user/{id}",method = RequestMethod.GET)
    public ModelAndView showUserPayments(@PathVariable("id") Long page, ModelAndView model, SessionAttr sessionAttr) {
        model.addObject("activeTab","statistic");
        model.setViewName("project/statistic_user_payments_details");
        return model;
    }



}
