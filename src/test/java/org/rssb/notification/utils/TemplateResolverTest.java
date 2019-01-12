package org.rssb.notification.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(classes = {
    TemplateResolver.class
})
public class TemplateResolverTest {
  @ClassRule
  public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
  @Rule
  public final SpringMethodRule springMethodRule = new SpringMethodRule();
  private static final String TEMPLATE_NO_DEFAULT_VALUES = "Radha Soami ${sewadarName},\n"
      + "This is to inform you that Sewa Jatha for ${sewaDepartment} sewa is going to ${jathaVenue}\n"
      + "from ${sewaStartDate} to ${sewaEndDate}. Please submit your name to Sewa Samiti if you're willing to join.";
  private static final String TEMPLATE_DEFAULT_VALUES = "Radha Soami ${sewadarName!'Rohit'},\n"
      + "This is to inform you that Sewa Jatha for ${sewaDepartment!'Accomodation and Luggage'} sewa is going to ${jathaVenue!'Bangalore'}\n"
      + "from ${sewaStartDate!'12-1-2019'} to ${sewaEndDate!'16-1-2019'}. Please submit your name to Sewa Samiti if you're willing to join.";

  private static final Map<String, String> ALL_VALUES = Maps.newHashMap(ImmutableMap.of(
      "sewadarName",
      "Rohit",
      "sewaDepartment",
      "Accomodation",
      "jathaVenue",
      "Bangalore",
      "sewaStartDate",
      "12-1-2019",
      "sewaEndDate",
      "16-1-2019"));
  private static final Map<String, String> PARTIAL_VALUES = Maps.newHashMap(ImmutableMap.of(
      "sewadarName",
      "Rohit",
      "sewaDepartment",
      "Accomodation",
      "jathaVenue",
      "Bangalore"));

  @Autowired
  private TemplateResolver templateResolver;

  @Test
  @Parameters(method = "getPositiveTestData")
  @TestCaseName("[{index}] {method}: {params}")
  public void resolverTest_Positive(String template,
                                    Map<String, String> values,
                                    List<String> missingPlaceholders) {
    String resolvedTemplate = templateResolver.resolve(template, values, missingPlaceholders);
    System.out.println(resolvedTemplate);
    assertThat(missingPlaceholders).isEmpty();
  }

  @Test
  public void resolverTest_Negative_MissingPlaceholders() {
    List<String> missingPlaceholders = new ArrayList<>();
    String resolvedTemplate = templateResolver.resolve(TEMPLATE_NO_DEFAULT_VALUES,
                                                       new HashMap<>(),
                                                       missingPlaceholders);
    System.out.println(resolvedTemplate);
    assertThat(resolvedTemplate).contains("sewadarName",
                                          "sewaDepartment",
                                          "jathaVenue",
                                          "sewaStartDate",
                                          "sewaEndDate");
  }

  private Object[] getPositiveTestData() {
    return new Object[] {
        new Object[] {TEMPLATE_NO_DEFAULT_VALUES, ALL_VALUES, new ArrayList<>()},
        new Object[] {TEMPLATE_DEFAULT_VALUES, new HashMap<>(), new ArrayList<>()},
        new Object[] {TEMPLATE_DEFAULT_VALUES, PARTIAL_VALUES, new ArrayList<>()}
        // we can add more argument sets for further testing
    };
  }

}