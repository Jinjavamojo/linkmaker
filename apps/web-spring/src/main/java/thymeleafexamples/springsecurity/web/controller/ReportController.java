package thymeleafexamples.springsecurity.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import thymeleafexamples.springsecurity.UserType;
import thymeleafexamples.springsecurity.Utils;
import thymeleafexamples.springsecurity.config.SessionAttr;
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;
import thymeleafexamples.springsecurity.report.ReportGenerator;
import thymeleafexamples.springsecurity.service.VKService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("sessionAttr")
public class ReportController {

    @Autowired
    public ReportGenerator reportGenerator;

    @Autowired
    private VKService vkService;

    @RequestMapping(value = "/project/statistic/paid/report/txt",method = RequestMethod.GET)
    public void exportPaidTxtReport(@ModelAttribute("sessionAttr") SessionAttr sessionAttr, HttpServletResponse response, HttpServletRequest request) {
            generateTextReport(UserType.PAID,"paid_user_ids_project_" + sessionAttr.projectName + ".txt",response,sessionAttr.currentProjectId);
    }

    @RequestMapping(value = "/project/statistic/visited/report/txt",method = RequestMethod.GET)
    public void exportVisitedTxtReport(@ModelAttribute("sessionAttr") SessionAttr sessionAttr, HttpServletResponse response, HttpServletRequest request) {
        generateTextReport(UserType.VISITED,"visited_user_ids_project_" + sessionAttr.projectName + ".txt",response,sessionAttr.currentProjectId);
    }

    @RequestMapping(value = "/project/statistic/paid/report/xlsx",method = RequestMethod.GET)
    public void exportPaidXlsxReport(@ModelAttribute("sessionAttr") SessionAttr sessionAttr, HttpServletResponse response, HttpServletRequest request) {
        generateXLSXReport(UserType.PAID,"paid_users_project_" + sessionAttr.projectName + ".xlsx",response,sessionAttr.currentProjectId);
    }

    @RequestMapping(value = "/project/statistic/visited/report/xlsx",method = RequestMethod.GET)
    public void exportVisitedXlsxReport(@ModelAttribute("sessionAttr") SessionAttr sessionAttr, HttpServletResponse response, HttpServletRequest request) {
        generateXLSXReport(UserType.VISITED,"visited_users_project_" + sessionAttr.projectName + ".xlsx",response,sessionAttr.currentProjectId);
    }


    private void generateXLSXReport(UserType userType, String fileName, HttpServletResponse response, Long currentProjectId) {
        try {
            OutputStream fout= response.getOutputStream();
            OutputStream bos = new BufferedOutputStream(fout);

            List<VkUserPaymentDTO> users = new ArrayList<>();
            switch (userType) {
                case PAID: {
                    users = vkService.getAllPaidUsers(currentProjectId);
                }break;
                case VISITED: {
                    users = vkService.getVisitedUsers(currentProjectId);

                }break;
            }
            reportGenerator.generateXLSX(bos,users);
            String transliterated = Utils.transliterate(fileName);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=" + transliterated);
            MediaType mediaType1 = MediaType.parseMediaType("application/octet-stream");
            response.setContentType(mediaType1.toString());

            bos.flush();
            bos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateTextReport(UserType userType, String fileName,HttpServletResponse response, Long currentProjectId) {
        try {

            OutputStream fout= response.getOutputStream();
            OutputStream bos = new BufferedOutputStream(fout);
            OutputStreamWriter outputwriter  = new OutputStreamWriter(bos);
            List<String> users = new ArrayList<>();
            switch (userType) {
                case PAID: {
                    users = vkService.getPaidUserIds(currentProjectId);
                }break;
                case VISITED: {
                    users = vkService.getVisitedUserIds(currentProjectId);

                }break;
            }
            users.forEach(
                    id -> {
                        try {
                            outputwriter.write(id + "\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            String transliterated = Utils.transliterate(fileName);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=" + transliterated);
            MediaType mediaType1 = MediaType.parseMediaType("application/octet-stream");
            response.setContentType(mediaType1.toString());
            outputwriter.flush();
            outputwriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
