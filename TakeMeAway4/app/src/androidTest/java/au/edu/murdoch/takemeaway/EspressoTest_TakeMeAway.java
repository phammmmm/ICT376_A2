package au.edu.murdoch.takemeaway;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EspressoTest_TakeMeAway {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void espressoTest_TakeMeAway() {
        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withId(R.id.note_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(1);
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.content), withText("sample content"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.content), withText("sample content"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.content), withText("sample content"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.content), withText("sample content"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.content), withText("sample content"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("update content"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.content), withText("update content"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.title), withText("FIRST NOTE"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText7.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.title), withText("FIRST NOTE"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText8.perform(click());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.title), withText("FIRST NOTE"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("update NOTE"));

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.title), withText("update NOTE"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText10.perform(closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.dateTime), withText("Mar 20, 2020"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.dateTime), withText("Apr 03, 2020"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatImageButton")), withContentDescription("Previous month"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.DayPickerView")),
                                        childAtPosition(
                                                withClassName(is("com.android.internal.widget.DialogViewAnimator")),
                                                0)),
                                1)));
        appCompatImageButton.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.dateTime), withText("Mar 09, 2020"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatTextView4.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button2), withText("Cancel"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(R.id.textClock), withText("03:22"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatTextView5.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatImageButton")), withContentDescription("Switch to text input mode for the time input."),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                0),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatEditText")), withText("10"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.RelativeLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.TextInputTimePickerView")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatEditText11.perform(replaceText("12"));

        ViewInteraction appCompatEditText12 = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatEditText")), withText("12"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.RelativeLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.TextInputTimePickerView")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatEditText12.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction appCompatTextView6 = onView(
                allOf(withId(R.id.textClock), withText("12:00"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatTextView6.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button2), withText("Cancel"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(R.id.location), withText("Singapore"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatTextView7.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(android.R.id.button2), withText("CANCEL"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        appCompatButton6.perform(scrollTo(), click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.TakePhoto),
                        childAtPosition(
                                allOf(withId(R.id.note_details_layout),
                                        childAtPosition(
                                                withId(R.id.note_details_fragment_container),
                                                0)),
                                3),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.imvGallery),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.TakePhoto),
                        childAtPosition(
                                allOf(withId(R.id.note_details_layout),
                                        childAtPosition(
                                                withId(R.id.note_details_fragment_container),
                                                0)),
                                3),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.imvCamera),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatImageView2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.TakePhoto),
                        childAtPosition(
                                allOf(withId(R.id.note_details_layout),
                                        childAtPosition(
                                                withId(R.id.note_details_fragment_container),
                                                0)),
                                3),
                        isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.imvGallery),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.content), withText("update content"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText13.perform(replaceText("update contentu"));

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.content), withText("update contentu"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText14.perform(closeSoftKeyboard());

        pressBack();

        ViewInteraction floatingActionButton4 = onView(
                allOf(withId(R.id.button_save),
                        childAtPosition(
                                allOf(withId(R.id.note_details_layout),
                                        childAtPosition(
                                                withId(R.id.note_details_fragment_container),
                                                0)),
                                2),
                        isDisplayed()));
        floatingActionButton4.perform(click());

        ViewInteraction appCompatImageView4 = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatImageView")), withContentDescription("Search"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withId(R.id.search_btn),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageView4.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText("up"), closeSoftKeyboard());

        DataInteraction appCompatTextView8 = onData(anything())
                .inAdapterView(allOf(withId(R.id.note_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(0);
        appCompatTextView8.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.delete), withContentDescription("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(android.R.id.button1), withText("Yes"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton7.perform(scrollTo(), click());

        DataInteraction appCompatTextView9 = onData(anything())
                .inAdapterView(allOf(withId(R.id.note_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(0);
        appCompatTextView9.perform(click());

        ViewInteraction appCompatTextView10 = onView(
                allOf(withId(R.id.textClock), withText("05:22"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatTextView10.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton8.perform(scrollTo(), click());

        ViewInteraction appCompatTextView11 = onView(
                allOf(withId(R.id.dateTime), withText("Mar 20, 2020"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatTextView11.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton9.perform(scrollTo(), click());

        ViewInteraction floatingActionButton5 = onView(
                allOf(withId(R.id.TakePhoto),
                        childAtPosition(
                                allOf(withId(R.id.note_details_layout),
                                        childAtPosition(
                                                withId(R.id.note_details_fragment_container),
                                                0)),
                                3),
                        isDisplayed()));
        floatingActionButton5.perform(click());

        ViewInteraction appCompatImageView5 = onView(
                allOf(withId(R.id.imvGallery),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton6 = onView(
                allOf(withId(R.id.button_save),
                        childAtPosition(
                                allOf(withId(R.id.note_details_layout),
                                        childAtPosition(
                                                withId(R.id.note_details_fragment_container),
                                                0)),
                                2),
                        isDisplayed()));
        floatingActionButton6.perform(click());

        DataInteraction appCompatTextView12 = onData(anything())
                .inAdapterView(allOf(withId(R.id.note_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(0);
        appCompatTextView12.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        DataInteraction appCompatTextView13 = onData(anything())
                .inAdapterView(allOf(withId(R.id.note_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(0);
        appCompatTextView13.perform(click());

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.content), withText("sample content"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText15.perform(replaceText("sample content. writing"));

        ViewInteraction appCompatEditText16 = onView(
                allOf(withId(R.id.content), withText("sample content. writing"),
                        childAtPosition(
                                allOf(withId(R.id.myLinear),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText16.perform(closeSoftKeyboard());

        pressBack();

        pressBack();

        ViewInteraction floatingActionButton7 = onView(
                allOf(withId(R.id.button_save),
                        childAtPosition(
                                allOf(withId(R.id.note_details_layout),
                                        childAtPosition(
                                                withId(R.id.note_details_fragment_container),
                                                0)),
                                2),
                        isDisplayed()));
        floatingActionButton7.perform(click());

        DataInteraction appCompatTextView14 = onData(anything())
                .inAdapterView(allOf(withId(R.id.note_list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(0);
        appCompatTextView14.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.delete), withContentDescription("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(android.R.id.button2), withText("No"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        appCompatButton10.perform(scrollTo(), click());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.delete), withContentDescription("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        2),
                                0),
                        isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(android.R.id.button1), withText("Yes"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton11.perform(scrollTo(), click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
