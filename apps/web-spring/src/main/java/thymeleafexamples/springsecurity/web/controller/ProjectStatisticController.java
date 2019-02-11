package thymeleafexamples.springsecurity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import thymeleafexamples.springsecurity.Utils;
import thymeleafexamples.springsecurity.config.SessionAttr;
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;
import thymeleafexamples.springsecurity.report.ReportUtils;
import thymeleafexamples.springsecurity.service.VKService;

import java.util.List;

@Controller
@SessionAttributes("sessionAttr")
@ControllerAdvice
public class ProjectStatisticController extends BaseController {

    @Autowired
    private VKService vkService;

    @Autowired
    private Environment env;



    private ModelAndView showLinkedUsersView(int page, ModelAndView model, SessionAttr sessionAttr) {
        model.setViewName("project/statistic_second_tab");
        Long currentProjectId = sessionAttr.currentProjectId;
        if (currentProjectId == null) {
            throw new RuntimeException("currentProjectId is null");
        }
        Long linkedUserCount = vkService.getLinkedUserCount(currentProjectId);
        if (!generatePages(page,model,linkedUserCount)) {
            return model;
        }
        List<VkUserPaymentDTO> linkedUsers = vkService.getVisitedUsersByPage(page, currentProjectId);
        int uniqClickUsers = projectService.getUniqueVisitedUsersOfProject(currentProjectId);
        model.addObject("uniqClickUsers",uniqClickUsers);
        model.addObject("payment_infos",linkedUsers);
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
    //@ExceptionHandler(ControllerException.class)
    public ModelAndView showPaidUsers(ModelAndView model, SessionAttr sessionAttr) {
        Long currentProjectId = sessionAttr.currentProjectId;
        int uniqPaidUsers = projectService.getUniqPaidUsers(currentProjectId);
        double projectMoneySum = projectService.getProjectMoneySum(currentProjectId);
        model.addObject("paidUsersCount",uniqPaidUsers);
        model.addObject("projectMoney", Utils.df.format(projectMoneySum));
        return getPaidUsersView(0, model,sessionAttr);
    }

    @RequestMapping(value = "/project/statistic/paid/page/{page}",method = RequestMethod.GET)
    public ModelAndView showPaidUsers(@PathVariable("page") int page, ModelAndView model, SessionAttr sessionAttr) {
        Long currentProjectId = sessionAttr.currentProjectId;
        int uniqPaidUsers = projectService.getUniqPaidUsers(currentProjectId);
        double projectMoneySum = projectService.getProjectMoneySum(currentProjectId);
        model.addObject("paidUsersCount",uniqPaidUsers);
        model.addObject("projectMoney", Utils.df.format(projectMoneySum));
        return getPaidUsersView(page, model,sessionAttr);
    }

    private boolean generatePages(int displayedPage, ModelAndView model, Long userCount ) {
        int userPerPageDisplayed = Integer.valueOf(env.getProperty("userPerPageDisplayed"));
        Long pagesCount = userCount / userPerPageDisplayed;
        if (displayedPage > pagesCount) {
            model.setViewName("404");
            return false;
        }
        if (userCount % userPerPageDisplayed  > 0) {
            pagesCount++;
        }
        if (displayedPage == 0) {
            model.addObject("current_page", 0);
            model.addObject("next_page", 1);

            model.addObject("current_page_text", 1);
            model.addObject("next_page_text", 2);
        }
        if (displayedPage > 0 && displayedPage < pagesCount-1) {
            model.addObject("prev_page", displayedPage-1);
            model.addObject("current_page", displayedPage);
            model.addObject("next_page", displayedPage+1);

            model.addObject("prev_page_text", displayedPage);
            model.addObject("current_page_text", displayedPage+1);
            model.addObject("next_page_text", displayedPage+2);

        }
        if (displayedPage==pagesCount-1) {
            model.addObject("current_page", displayedPage);
            model.addObject("prev_page", displayedPage-1);

            model.addObject("current_page_text", displayedPage+1);
            model.addObject("prev_page_text", displayedPage);
        }
        model.addObject("totalPages",pagesCount);
        return true;
    }

    private ModelAndView getPaidUsersView(int displayedPage, ModelAndView model, SessionAttr sessionAttr) {
        model.setViewName("project/statistic_first_tab");
        model.addObject("base","web-spring"); //TODO REPLACE BY PROPERTY

        Long currentProjectId = sessionAttr.currentProjectId;
        if (currentProjectId == null) {
            throw new RuntimeException("currentProjectId is null");
        }
        Long paidUsersCount = vkService.getPaidUsersCount(currentProjectId);
        if (!generatePages(displayedPage,model,paidUsersCount)) {
            return model;
        }
        List<VkUserPaymentDTO> userPagination = vkService.getPaidUsers(displayedPage,currentProjectId);
        Long particallyPaidUserCount = vkService.getParticallyPaidUserCount(currentProjectId);
        model.addObject("vk_users",userPagination);
        model.addObject("particallyPaidUserCount",particallyPaidUserCount);
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
