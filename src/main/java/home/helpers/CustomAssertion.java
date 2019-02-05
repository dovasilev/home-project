package home.helpers;

import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.StringDescription;

/**
 * Кастомный assertion
 */
public class CustomAssertion extends MatcherAssert {
    public static final Logger logger = Logger.getLogger(CustomAssertion.class);
    public CustomAssertion()  {
    }

    @Step("Сравниваю {0} и {1}")
    public static <T> void assertThat(T actual, Matcher<? super T> matcher) {
        logger.info("Сравниваю "+actual+" и "+matcher);
        assertThat("", actual, matcher);
    }

    @Step("Сравниваю {1} и {2}")
    public static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) {
       logger.info("Сравниваю "+actual+" и "+matcher);
        if (!matcher.matches(actual)) {
            Description description = new StringDescription();
            description.appendText(reason).appendText("\n").appendText("Expected: ").appendDescriptionOf(matcher).appendText("\n").appendText("But: ");
            matcher.describeMismatch(actual, description);
            throw new AssertionError(description.toString());
        }
    }

    public static void assertThat(String reason, boolean assertion) {
        if (!assertion) {
            throw new AssertionError(reason);
        }
    }
}
