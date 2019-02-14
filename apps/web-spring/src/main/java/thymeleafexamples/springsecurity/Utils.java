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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class Utils {

    public static DecimalFormat df = new DecimalFormat("#");

    private static int stackTraceMaxCount;

    private static Map<Character,String> map;
    static {
        map = new HashMap<>();
        map.put('а',"a");
        map.put('б',"b");
        map.put('в',"v");
        map.put('г',"g");
        map.put('д',"d");
        map.put('е',"e");
        map.put('ё',"e");
        map.put('ж',"zh");
        map.put('з',"z");
        map.put('и',"i");
        map.put('й',"y");
        map.put('к',"k");
        map.put('л',"l");
        map.put('м',"m");
        map.put('н',"n");
        map.put('о',"o");
        map.put('п',"p");
        map.put('р',"r");
        map.put('с',"s");
        map.put('т',"t");
        map.put('у',"u");
        map.put('ф',"f");
        map.put('х',"h");
        map.put('ц',"ts");
        map.put('ч',"ch");
        map.put('ш',"sh");
        map.put('щ',"sch");
        map.put('ы',"i");
        map.put('ь',"");
        map.put('ъ',"");
        map.put('э',"e");
        map.put('ю',"ju");
        map.put('я',"ja");

        map.put('А',"A");
        map.put('Б',"B");
        map.put('В',"V");
        map.put('Г',"G");
        map.put('Д',"D");
        map.put('Е',"E");
        map.put('Ё',"E");
        map.put('Ж',"ZH");
        map.put('З',"Z");
        map.put('И',"I");
        map.put('Й',"Y");
        map.put('К',"K");
        map.put('Л',"L");
        map.put('М',"M");
        map.put('Н',"N");
        map.put('О',"O");
        map.put('П',"P");
        map.put('Р',"R");
        map.put('С',"S");
        map.put('Т',"T");
        map.put('У',"U");
        map.put('Ф',"F");
        map.put('Х',"H");
        map.put('Ц',"TS");
        map.put('Ч',"CH");
        map.put('Ш',"SH");
        map.put('Щ',"SCH");
        map.put('Ы',"I");
        map.put('Ь',"");
        map.put('Ъ',"");
        map.put('Э',"E");
        map.put('Ю',"JU");
        map.put('Я',"JA");
    }

    @Value("${stackTraceLogDepth}")
    public void setDatabase(int value) {
        stackTraceMaxCount = value;
    }

    public static String getStackTrace(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder b = new StringBuilder();
        int max = stackTrace.length <= stackTraceMaxCount ? stackTrace.length : stackTraceMaxCount;
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

    public static String transliterate(String message){

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            String replacement = map.get(c);
            if (replacement != null) {
                builder.append(replacement);
            } else {
                builder.append(c);
            }
        }
        return builder.toString();

    }
}
