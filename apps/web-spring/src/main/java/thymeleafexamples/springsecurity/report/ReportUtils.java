package thymeleafexamples.springsecurity.report;

import java.util.Arrays;
import java.util.List;

public class ReportUtils {

    public static List<ReportType> getTypes() {
        return Arrays.asList(ReportType.values());
    }
}
