package home.helpers;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class RegexExecution {
    private static final Logger logger = Logger.getLogger(RegexExecution.class);

    public List<String> returnValueByTag (String request, String tag) throws Exception {
        List<String> response=new ArrayList<>();
        Pattern regexp = Pattern.compile("(?<=<"+tag+">)+[\\S\\s]+?(?=</"+tag+">)");
        Matcher m = regexp.matcher(request);
        while (m.find()) {
            String s=m.group();
            logger.info(s);
            response.add(s);
        }
        if (response.size()==0)
            throw new Exception("Не нашел элемента с таким тэгом");
        return response;
    }

    public List<String> returnValueByTagNotEnd (String request, String tag) throws Exception {
        List<String> response=new ArrayList<>();
        Pattern regexp = Pattern.compile("(?<=<"+tag+">)+[\\S\\s]+?(?=>)");
        Matcher m = regexp.matcher(request);
        while (m.find()) {
            String s=m.group();
            logger.info(s);
            response.add(s);
        }
        if (response.size()==0)
            throw new Exception("Не нашел элемента с таким тэгом");
        return response;
    }

    public List<String> returnValueByAttribute (String request,String attributeName) throws Exception {
        try {
            List<String> response = new ArrayList<>();
            Pattern regexp = Pattern.compile("(?<=" + attributeName + "=\")+[\\S\\s]+?(?=\")");
            Matcher m = regexp.matcher(request);
            while (m.find()) {
                String s = m.group();
                logger.info(s);
                response.add(s);
            }
            if (response.size()==0)
                throw new Exception("Не нашел элемент с таким атрибутом");
            return response;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
            }
    }

    public String returnValueByRegex (String request,String regex) throws Exception {
        try {
            String response ="";
            Pattern regexp = Pattern.compile(regex);
            Matcher m = regexp.matcher(request);
            while (m.find()) {
                response = m.group();
            }
            return response;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }



}

