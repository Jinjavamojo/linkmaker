package thymeleafexamples.springsecurity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.stream.Stream;

@Component
public class Utils {

    public static DecimalFormat df = new DecimalFormat("#");

    private static int paymentsCount;

    @Value("${stackTraceLogDepth}")
    public void setDatabase(int value) {
        paymentsCount = value;
    }

    public static String getStackTrace(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder b = new StringBuilder();
        int max = stackTrace.length <= paymentsCount ? stackTrace.length : paymentsCount;
        for (int i = 0; i < max; i++) {
             b.append("     at ")
                     .append(stackTrace[i].getClassName())
                     .append(".")
                     .append(stackTrace[i].getMethodName())
                     .append("(")
                     .append(stackTrace[i].getFileName())
                     .append(":")
                     .append(stackTrace[i].getLineNumber())
                     .append(")")
                     .append("\n");

        }
        return b.toString();
    }
    public static String readLineByLineJava8(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            File resource = new ClassPathResource(filePath).getFile();
            try (Stream<String> stream = Files.lines( Paths.get(resource.getPath()), StandardCharsets.UTF_8))
            {
                stream.forEach(s -> contentBuilder.append(s).append("\n"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}
