package thymeleafexamples.springsecurity.report;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import thymeleafexamples.springsecurity.Utils;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;
import thymeleafexamples.springsecurity.service.ProjectService;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class ReportGenerator {

    @Autowired
    private ProjectService projectService;

    public void generateXLSXForPaidUsers(OutputStream stream, List<VkUserPaymentDTO> dtos, Long currentProjectId) {
        try {
            //OPCPackage pkg = OPCPackage.open(new File("template.xlsx"));
            //Workbook book = new HSSFWorkbook(fs,false);
            HSSFWorkbook book = new HSSFWorkbook();
            Sheet sheet = book.createSheet("test");

            Font h1 = book.createFont();
            h1.setFontHeightInPoints((short) 24);
            h1.setFontName("Times New Roman");

            Font h2 = book.createFont();
            h2.setFontHeightInPoints((short) 20);
            h2.setFontName("Times New Roman");

            Font h3 = book.createFont();
            h2.setFontHeightInPoints((short) 16);
            h2.setFontName("Times New Roman");
            h3.setBold(true);

            Font tableTitlesFont = book.createFont();
            tableTitlesFont.setFontHeightInPoints((short) 12);
            tableTitlesFont.setFontName("Times New Roman");
            tableTitlesFont.setColor(IndexedColors.WHITE.getIndex());
            tableTitlesFont.setBold(true);

            Font tableContentFont = book.createFont();
            tableContentFont.setFontHeightInPoints((short) 12);
            tableContentFont.setFontName("Times New Roman");
            tableContentFont.setColor(IndexedColors.BLACK.getIndex());


            CellStyle baseInfoStyleh1 = book.createCellStyle();
            baseInfoStyleh1.setAlignment(HorizontalAlignment.CENTER);
            baseInfoStyleh1.setVerticalAlignment(VerticalAlignment.CENTER);
            baseInfoStyleh1.setFont(h1);

            CellStyle baseInfoStyleh2 = book.createCellStyle();
            baseInfoStyleh2.setAlignment(HorizontalAlignment.CENTER);
            baseInfoStyleh2.setVerticalAlignment(VerticalAlignment.CENTER);
            baseInfoStyleh2.setFont(h2);

            CellStyle firstColumnNamesStyle = book.createCellStyle();
            firstColumnNamesStyle.setAlignment(HorizontalAlignment.RIGHT);
            firstColumnNamesStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            firstColumnNamesStyle.setFont(h3);

            Row row = sheet.createRow(0);
            row.setHeight((short) 500);

            Project byId = projectService.findById(currentProjectId);
            Cell projectNameKeyCell = row.createCell(0);
            projectNameKeyCell.setCellValue("Название проекта");
            projectNameKeyCell.setCellStyle(firstColumnNamesStyle);
            Cell projectNameValueCell = row.createCell(1);
            projectNameValueCell.setCellValue(byId.getName());
            projectNameValueCell.setCellStyle(baseInfoStyleh1);

            Row row2 = sheet.createRow(1);
            row2.setHeight((short) 500);

            Cell reportDateKeyCell = row2.createCell(0);
            reportDateKeyCell.setCellValue("Время формирования отчета");
            reportDateKeyCell.setCellStyle(firstColumnNamesStyle);

            Cell reportDateValueCell = row2.createCell(1);
            reportDateValueCell.setCellStyle(baseInfoStyleh2);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = now.format(formatter);
            reportDateValueCell.setCellValue(formatDateTime);

            Row row3 = sheet.createRow(2);
            row3.setHeight((short) 500);

            Cell projectPriceKeyCell = row3.createCell(0);
            projectPriceKeyCell.setCellValue("Стоимость");
            projectPriceKeyCell.setCellStyle(firstColumnNamesStyle);
            Cell projectPriceValueCell = row3.createCell(1);
            projectPriceValueCell.setCellValue(byId.getPrice());
            projectPriceValueCell.setCellStyle(baseInfoStyleh2);

            Row row4 = sheet.createRow(3);
            row4.setHeight((short) 500);
            Cell peopleCountKeyCell = row4.createCell(0);
            peopleCountKeyCell.setCellValue("Оплатило человек");
            peopleCountKeyCell.setCellStyle(firstColumnNamesStyle);
            Cell peopleCountValueCell = row4.createCell(1);
            peopleCountValueCell.setCellValue(projectService.getUniqPaidUsers(currentProjectId));
            peopleCountValueCell.setCellStyle(baseInfoStyleh2);


            Row row5 = sheet.createRow(4);
            row5.setHeight((short) 500);

            Cell totalMoneyCellKey = row5.createCell(0);
            totalMoneyCellKey.setCellValue("Всего денег");
            totalMoneyCellKey.setCellStyle(firstColumnNamesStyle);
            Cell totalMoneyCellValue = row5.createCell(1);
            totalMoneyCellValue.setCellValue(Utils.df.format(projectService.getProjectMoneySum(currentProjectId)));
            totalMoneyCellValue.setCellStyle(baseInfoStyleh2);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 4));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 4));
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 4));
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 4));
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 4));


            CellStyle tableTitlesStyle = book.createCellStyle();
            tableTitlesStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            tableTitlesStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            tableTitlesStyle.setBorderBottom(BorderStyle.THIN);
            tableTitlesStyle.setAlignment(HorizontalAlignment.CENTER);
            tableTitlesStyle.setFont(tableTitlesFont);
            tableTitlesStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            tableTitlesStyle.setBorderBottom(BorderStyle.THIN);
            tableTitlesStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            tableTitlesStyle.setBorderTop(BorderStyle.THIN);
            tableTitlesStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            tableTitlesStyle.setBorderLeft(BorderStyle.THIN);
            tableTitlesStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            tableTitlesStyle.setBorderRight(BorderStyle.THIN);
            tableTitlesStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());


            Row row6 = sheet.createRow(5);
            row6.setHeight((short) 500);
            Cell vkIDKeyCell = row6.createCell(0);
            vkIDKeyCell.setCellValue("VK ID");
            vkIDKeyCell.setCellStyle(tableTitlesStyle);

            Cell firstNameKeyCell = row6.createCell(1);
            firstNameKeyCell.setCellValue("Имя");
            firstNameKeyCell.setCellStyle(tableTitlesStyle);


            Cell lastNameKeyCell = row6.createCell(2);
            lastNameKeyCell.setCellValue("Фамилия");
            lastNameKeyCell.setCellStyle(tableTitlesStyle);


            Cell emailKeyCell = row6.createCell(3);
            emailKeyCell.setCellValue("Email");
            emailKeyCell.setCellStyle(tableTitlesStyle);


            Cell paidDateKeyCell = row6.createCell(4);
            paidDateKeyCell.setCellValue("Дата оплаты");
            paidDateKeyCell.setCellStyle(tableTitlesStyle);

            sheet.setColumnWidth(0, 10000);
            sheet.setColumnWidth(1, 10000);
            sheet.setColumnWidth(2, 10000);
            sheet.setColumnWidth(3, 10000);
            sheet.setColumnWidth(4, 10000);

            CellStyle contentRowLightGray = book.createCellStyle();
            contentRowLightGray.cloneStyleFrom(tableTitlesStyle);
            contentRowLightGray.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            contentRowLightGray.setFont(tableContentFont);

            CellStyle contentRowLightGray2 = book.createCellStyle();
            contentRowLightGray2.cloneStyleFrom(tableTitlesStyle);
            contentRowLightGray2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            contentRowLightGray2.setFont(tableContentFont);

            int initRowIndex = 6;
            IntStream.range(0, dtos.size())
                    .forEach(idx -> {
                                Row rr = sheet.createRow(initRowIndex + idx);
                                rr.setHeight((short) 500);
                                Cell cell = rr.createCell(0);
                                cell.setCellValue(dtos.get(idx).getVkUserIdString());

                                Cell cell2 = rr.createCell(1);
                                if (dtos.get(idx).getFirstName() != null) {
                                    cell2.setCellValue(dtos.get(idx).getFirstName());
                                }

                                Cell cell3 = rr.createCell(2);
                                if (dtos.get(idx).getLastName() != null) {
                                    cell3.setCellValue(dtos.get(idx).getLastName());
                                }

                                Cell cell4 = rr.createCell(3);
                                if (dtos.get(idx).getUserEmail() != null) {
                                    cell4.setCellValue(dtos.get(idx).getUserEmail());
                                }

                                Cell cell5 = rr.createCell(4);
                                if (dtos.get(idx).getPaymentCapturedAt() != null) {
                                    LocalDateTime localDateTime = ((Timestamp) dtos.get(idx).getPaymentCapturedAt()).toLocalDateTime();
                                    String format = localDateTime.format(formatter);
                                    cell5.setCellValue(format);
                                }

                                if (idx % 2 == 0) {
                                    cell.setCellStyle(contentRowLightGray);
                                    cell2.setCellStyle(contentRowLightGray);
                                    cell3.setCellStyle(contentRowLightGray);
                                    cell4.setCellStyle(contentRowLightGray);
                                    cell5.setCellStyle(contentRowLightGray);
                                } else {
                                    cell.setCellStyle(contentRowLightGray2);
                                    cell2.setCellStyle(contentRowLightGray2);
                                    cell3.setCellStyle(contentRowLightGray2);
                                    cell4.setCellStyle(contentRowLightGray2);
                                    cell5.setCellStyle(contentRowLightGray2);
                                }
                            }
                    )
            ;
            book.write(stream);
            book.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateXLSXForVisitedUsers(OutputStream stream, List<VkUserPaymentDTO> dtos, Long currentProjectId) {
        try {
            //OPCPackage pkg = OPCPackage.open(new File("template.xlsx"));
            //Workbook book = new HSSFWorkbook(fs,false);
            HSSFWorkbook book = new HSSFWorkbook();
            Sheet sheet = book.createSheet("test");

            Font h1 = book.createFont();
            h1.setFontHeightInPoints((short) 24);
            h1.setFontName("Times New Roman");

            Font h2 = book.createFont();
            h2.setFontHeightInPoints((short) 20);
            h2.setFontName("Times New Roman");

            Font h3 = book.createFont();
            h2.setFontHeightInPoints((short) 16);
            h2.setFontName("Times New Roman");
            h3.setBold(true);

            Font tableTitlesFont = book.createFont();
            tableTitlesFont.setFontHeightInPoints((short) 12);
            tableTitlesFont.setFontName("Times New Roman");
            tableTitlesFont.setColor(IndexedColors.WHITE.getIndex());
            tableTitlesFont.setBold(true);

            Font tableContentFont = book.createFont();
            tableContentFont.setFontHeightInPoints((short) 12);
            tableContentFont.setFontName("Times New Roman");
            tableContentFont.setColor(IndexedColors.BLACK.getIndex());


            CellStyle baseInfoStyleh1 = book.createCellStyle();
            baseInfoStyleh1.setAlignment(HorizontalAlignment.CENTER);
            baseInfoStyleh1.setVerticalAlignment(VerticalAlignment.CENTER);
            baseInfoStyleh1.setFont(h1);

            CellStyle baseInfoStyleh2 = book.createCellStyle();
            baseInfoStyleh2.setAlignment(HorizontalAlignment.CENTER);
            baseInfoStyleh2.setVerticalAlignment(VerticalAlignment.CENTER);
            baseInfoStyleh2.setFont(h2);

            CellStyle firstColumnNamesStyle = book.createCellStyle();
            firstColumnNamesStyle.setAlignment(HorizontalAlignment.RIGHT);
            firstColumnNamesStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            firstColumnNamesStyle.setFont(h3);

            Row row = sheet.createRow(0);
            row.setHeight((short) 500);

            Project byId = projectService.findById(currentProjectId);
            Cell projectNameKeyCell = row.createCell(0);
            projectNameKeyCell.setCellValue("Название проекта");
            projectNameKeyCell.setCellStyle(firstColumnNamesStyle);
            Cell projectNameValueCell = row.createCell(1);
            projectNameValueCell.setCellValue(byId.getName());
            projectNameValueCell.setCellStyle(baseInfoStyleh1);

            Row row2 = sheet.createRow(1);
            row2.setHeight((short) 500);

            Cell reportDateKeyCell = row2.createCell(0);
            reportDateKeyCell.setCellValue("Время формирования отчета");
            reportDateKeyCell.setCellStyle(firstColumnNamesStyle);

            Cell reportDateValueCell = row2.createCell(1);
            reportDateValueCell.setCellStyle(baseInfoStyleh2);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = now.format(formatter);
            reportDateValueCell.setCellValue(formatDateTime);

            Row row3 = sheet.createRow(2);
            row3.setHeight((short) 500);

            Cell projectPriceKeyCell = row3.createCell(0);
            projectPriceKeyCell.setCellValue("Стоимость");
            projectPriceKeyCell.setCellStyle(firstColumnNamesStyle);
            Cell projectPriceValueCell = row3.createCell(1);
            projectPriceValueCell.setCellValue(byId.getPrice());
            projectPriceValueCell.setCellStyle(baseInfoStyleh2);

            Row row4 = sheet.createRow(3);
            row4.setHeight((short) 500);
            Cell peopleCountKeyCell = row4.createCell(0);
            peopleCountKeyCell.setCellValue("Уникальных переходов");
            peopleCountKeyCell.setCellStyle(firstColumnNamesStyle);
            Cell peopleCountValueCell = row4.createCell(1);
            peopleCountValueCell.setCellValue(projectService.getUniqueVisitedUsersOfProject(currentProjectId));
            peopleCountValueCell.setCellStyle(baseInfoStyleh2);


            Row row5 = sheet.createRow(4);
            row5.setHeight((short) 500);

//            Cell totalMoneyCellKey = row5.createCell(0);
//            totalMoneyCellKey.setCellValue("Всего денег");
//            totalMoneyCellKey.setCellStyle(firstColumnNamesStyle);
//            Cell totalMoneyCellValue = row5.createCell(1);
//            totalMoneyCellValue.setCellValue(Utils.df.format(projectService.getProjectMoneySum(currentProjectId)));
//            totalMoneyCellValue.setCellStyle(baseInfoStyleh2);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 4));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 4));
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 4));
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 4));
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 4));


            CellStyle tableTitlesStyle = book.createCellStyle();
            tableTitlesStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            tableTitlesStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            tableTitlesStyle.setBorderBottom(BorderStyle.THIN);
            tableTitlesStyle.setAlignment(HorizontalAlignment.CENTER);
            tableTitlesStyle.setFont(tableTitlesFont);
            tableTitlesStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            tableTitlesStyle.setBorderBottom(BorderStyle.THIN);
            tableTitlesStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            tableTitlesStyle.setBorderTop(BorderStyle.THIN);
            tableTitlesStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            tableTitlesStyle.setBorderLeft(BorderStyle.THIN);
            tableTitlesStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            tableTitlesStyle.setBorderRight(BorderStyle.THIN);
            tableTitlesStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());


            Row row6 = sheet.createRow(5);
            row6.setHeight((short) 500);
            Cell vkIDKeyCell = row6.createCell(0);
            vkIDKeyCell.setCellValue("VK ID");
            vkIDKeyCell.setCellStyle(tableTitlesStyle);

            Cell firstNameKeyCell = row6.createCell(1);
            firstNameKeyCell.setCellValue("Имя");
            firstNameKeyCell.setCellStyle(tableTitlesStyle);


            Cell lastNameKeyCell = row6.createCell(2);
            lastNameKeyCell.setCellValue("Фамилия");
            lastNameKeyCell.setCellStyle(tableTitlesStyle);


            Cell emailKeyCell = row6.createCell(3);
            emailKeyCell.setCellValue("Email");
            emailKeyCell.setCellStyle(tableTitlesStyle);


            Cell visitedDataKeyCell = row6.createCell(4);
            visitedDataKeyCell.setCellValue("Дата перехода");
            visitedDataKeyCell.setCellStyle(tableTitlesStyle);

            sheet.setColumnWidth(0, 10000);
            sheet.setColumnWidth(1, 10000);
            sheet.setColumnWidth(2, 10000);
            sheet.setColumnWidth(3, 10000);
            sheet.setColumnWidth(4, 10000);

            CellStyle contentRowLightGray = book.createCellStyle();
            contentRowLightGray.cloneStyleFrom(tableTitlesStyle);
            contentRowLightGray.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            contentRowLightGray.setFont(tableContentFont);

            CellStyle contentRowLightGray2 = book.createCellStyle();
            contentRowLightGray2.cloneStyleFrom(tableTitlesStyle);
            contentRowLightGray2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            contentRowLightGray2.setFont(tableContentFont);

            int initRowIndex = 6;
            IntStream.range(0, dtos.size())
                    .forEach(idx -> {
                                Row rr = sheet.createRow(initRowIndex + idx);
                                rr.setHeight((short) 500);
                                Cell cell = rr.createCell(0);
                                cell.setCellValue(dtos.get(idx).getVkUserIdString());

                                Cell cell2 = rr.createCell(1);
                                if (dtos.get(idx).getFirstName() != null) {
                                    cell2.setCellValue(dtos.get(idx).getFirstName());
                                }

                                Cell cell3 = rr.createCell(2);
                                if (dtos.get(idx).getLastName() != null) {
                                    cell3.setCellValue(dtos.get(idx).getLastName());
                                }

                                Cell cell4 = rr.createCell(3);
                                if (dtos.get(idx).getUserEmail() != null) {
                                    cell4.setCellValue(dtos.get(idx).getUserEmail());
                                }

                                Cell cell5 = rr.createCell(4);
                                if (dtos.get(idx).getCreatedAt() != null) {
                                    LocalDateTime localDateTime = ((Timestamp) dtos.get(idx).getCreatedAt()).toLocalDateTime();
                                    String format = localDateTime.format(formatter);
                                    cell5.setCellValue(format);
                                }

                                if (idx % 2 == 0) {
                                    cell.setCellStyle(contentRowLightGray);
                                    cell2.setCellStyle(contentRowLightGray);
                                    cell3.setCellStyle(contentRowLightGray);
                                    cell4.setCellStyle(contentRowLightGray);
                                    cell5.setCellStyle(contentRowLightGray);
                                } else {
                                    cell.setCellStyle(contentRowLightGray2);
                                    cell2.setCellStyle(contentRowLightGray2);
                                    cell3.setCellStyle(contentRowLightGray2);
                                    cell4.setCellStyle(contentRowLightGray2);
                                    cell5.setCellStyle(contentRowLightGray2);
                                }
                            }
                    )
            ;
            book.write(stream);
            book.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
