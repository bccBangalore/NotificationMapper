package org.rssb.notification.utils;

import java.io.StringWriter;
import java.util.*;
import java.util.function.Predicate;

public class TemplateResolver {
    private static String EMPTY_STRING = "";
    private static String SPACE_STRING = " ";
    private static String NULL_STRING = null;
    private static Predicate<String> equalsEmptyString = Predicate.isEqual(EMPTY_STRING);
    private static Predicate<String> equalsNull = Predicate.isEqual(NULL_STRING);
    private static Predicate<String> equalsNullOrEmpty = equalsNull.or(equalsEmptyString);
    public String resolve(String template,String seperator, Map<String, String> placeholders, List<String> missingPlaceholders) {
        Objects.requireNonNull(missingPlaceholders);
        Objects.requireNonNull(placeholders);
        Objects.requireNonNull(template);
        seperator = equalsNullOrEmpty.test(seperator) ? SPACE_STRING : seperator;

        StringWriter out = new StringWriter();
        missingPlaceholders = getMissingTokensAndResolveTemplate(out, template, seperator, placeholders);
        return out.toString();
    }

    private List<String> getMissingTokensAndResolveTemplate(StringWriter out, String template, String seperator, Map<String, String> placeholders){
        String[] tokens = template.split(SPACE_STRING);
        for (String token : tokens) {
            if ()
        }
    }

}
