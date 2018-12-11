package thymeleafexamples.springsecurity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
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
import java.util.List;

@Controller
@SessionAttributes("sessionAttr")
public class ReportController {

    @Autowired
    public ReportGenerator reportGenerator;

    @Autowired
    private VKService vkService;

    @RequestMapping(value = "/project/statistic/paid/report/txt",method = RequestMethod.GET)
    public void exportTxtReport(@ModelAttribute("sessionAttr") SessionAttr sessionAttr, HttpServletResponse response, HttpServletRequest request) {
        try {

            OutputStream fout= response.getOutputStream();
            OutputStream bos = new BufferedOutputStream(fout);
            OutputStreamWriter outputwriter  = new OutputStreamWriter(bos);
            List<String> paidUserIds = vkService.getPaidUserIds(sessionAttr.currentProjectId);
            paidUserIds.forEach(
                    id -> {
                        try {
                            outputwriter.write(id + "\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=" +"paid_user_ids.txt"); //todo rename
            MediaType mediaType1 = MediaType.parseMediaType("application/octet-stream");
            response.setContentType(mediaType1.toString());
            outputwriter.flush();
            outputwriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/project/statistic/paid/report/xlsx",method = RequestMethod.GET)
    public void exportXlsxReport(@ModelAttribute("sessionAttr") SessionAttr sessionAttr, HttpServletResponse response, HttpServletRequest request) {
        try {
            OutputStream fout= response.getOutputStream();
            OutputStream bos = new BufferedOutputStream(fout);

            List<VkUserPaymentDTO> allPaidUsers = vkService.getAllPaidUsers(sessionAttr.currentProjectId);
            reportGenerator.generateXLSX(bos,allPaidUsers);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=" +"paid_user_ids.xlsx"); //todo rename
            MediaType mediaType1 = MediaType.parseMediaType("application/octet-stream");
            response.setContentType(mediaType1.toString());

            bos.flush();
            bos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
