package org.rssb.notification.utils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;

@Service
public class TemplateResolver {
  private static final char SINGLE_QUOTE_CHAR = "'".charAt(0),
      DOLLAR_SIGN = '$',
      OPENING_BRACE = '{',
      CLOSING_BRACE = '}',
      EXCLAMATION = '!',
      SPACE_CHAR = ' ';
  private static final Predicate<Character> equalsSpace = Predicate.isEqual(SPACE_CHAR);
  private static final Predicate<Character> equalsOpeningBrace = Predicate.isEqual(OPENING_BRACE);
  private static final Predicate<Character> equalsClosingBrace = Predicate.isEqual(CLOSING_BRACE);
  private static final Predicate<Character> equalsExclamation = Predicate.isEqual(EXCLAMATION);
  private static final Predicate<Character> equalsDollar = Predicate.isEqual(DOLLAR_SIGN);
  private static final Predicate<Character> equalsSingleQuote = Predicate.isEqual(SINGLE_QUOTE_CHAR);

  public String resolve(String template,
                        Map<String, String> placeholders,
                        List<String> missingPlaceholders) {
    Objects.requireNonNull(missingPlaceholders, "list of missing placeholders not initialized");
    Objects.requireNonNull(placeholders, "map of placeholders is null");
    Objects.requireNonNull(template, "template reference is null");
    StringBuilder builder = getMissingTokensAndResolveTemplate(template,
                                                               placeholders,
                                                               missingPlaceholders);
    return builder.toString();
  }

  private int getNextIndexOfChar(char[] charArray, char charToFind, int index) {
    int length = charArray.length;
    if (index >= length) {
      return -1;
    }
    while (index < length && charArray[index] != charToFind) {
      index++;
    }
    if (index >= length) {
      return -1;
    }
    return index;
  }

  private int getNextSeperatorIndex(char[] array, int start) {
    while (start < array.length) {
      if (equalsExclamation.or(equalsClosingBrace)
          .test(array[start])) {
        return start;
      }
      start++;
    }
    return -1;
  }

  private StringBuilder getMissingTokensAndResolveTemplate(String template,
                                                           Map<String, String> values,
                                                           List<String> missingPlaceholders) {
    StringBuilder sb = new StringBuilder();
    char[] array = template.toCharArray();
    int length = array.length;
    int index = 0;
    while (index < length - 1) {
      if (array[index] == DOLLAR_SIGN && array[index + 1] == OPENING_BRACE) {
        int begin = index + 2;
        int end = -1;
        int seperator = getNextSeperatorIndex(array, begin);
        String placeholder;
        if (seperator == -1) {
          break;
        }
        placeholder = template.substring(begin, seperator);
        if (array[seperator] == CLOSING_BRACE) {
          // placeholder found
          end = seperator;
          if (values.containsKey(placeholder)) {
            // default value present
            sb.append(values.get(placeholder));
          } else {
            missingPlaceholders.add(placeholder);
            sb.append(array, index, seperator + 1 - index);
          }
          index = seperator + 1;
        } else {
          // seperator now is an Exclamation mark. find if value exists in map
          if (values.containsKey(placeholder)) {
            sb.append(values.get(placeholder));
            index = getNextIndexOfChar(array, CLOSING_BRACE, seperator) + 1;

          } else {
            // find default value
            if (seperator < length - 1 && array[seperator + 1] == SINGLE_QUOTE_CHAR) {
              int valueBegin = seperator + 2;
              int valueEnd = -1;
              valueEnd = getNextIndexOfChar(array, SINGLE_QUOTE_CHAR, valueBegin + 1);
              if (valueEnd == -1) {
                missingPlaceholders.add(placeholder);
                throw new IllegalStateException("error at missing single quote at location "
                                                    + seperator);
              }
              String value = template.substring(valueBegin, valueEnd);
              values.put(placeholder, value);
              sb.append(value);
              // skipping closing brace;
              index = valueEnd + 2;
            } else {
              missingPlaceholders.add(placeholder);
              throw new IllegalStateException("error at missing single quote at location "
                                                  + seperator);
            }
          }
        }
      } else {
        sb.append(array[index]);
        index++;
      }
    }
    if (index < length) {
      sb.append(array[index]);
    }
    return sb;
  }
}
