package com.ustadmobile.test.port.android.view;

import android.os.SystemClock;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.toughra.ustadmobile.R;
import com.ustadmobile.core.util.UMFileUtil;
import com.ustadmobile.lib.db.entities.OpdsEntryWithRelations;
import com.ustadmobile.port.android.view.BasePointActivity;
import com.ustadmobile.port.android.view.CatalogOPDSFragment;
import com.ustadmobile.test.core.ResourcesHttpdTestServer;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by mike on 3/5/18.
 */
@LargeTest
public class DownloadCrawlEspressoTest {

    @Rule
    public ActivityTestRule<BasePointActivity> mActivityRule =
            new ActivityTestRule<>(BasePointActivity.class);


    @BeforeClass
    public static void startResourcesServer() throws IOException {
        ResourcesHttpdTestServer.startServer();
    }

    @Before
    public void resetTimeout() {
        IdlingPolicies.setMasterPolicyTimeout(10, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10, TimeUnit.SECONDS);
    }


    @Test
    public void addLibraryAndDownloadAll() {
        onView(withText(R.string.my_libraries)).perform(click());
        SystemClock.sleep(1000);
        onView(withId(R.id.activity_basepoint_fab)).check(matches(isDisplayed()));

        onView(withId(R.id.activity_basepoint_fab)).perform(click());

        onView(withId(R.id.fragment_add_feed_dialog_spinner)).perform(click());
        onView(withText(containsString("URL"))).inRoot(isPlatformPopup()).perform(click());

        onView(withId(R.id.fragment_add_feed_url_text)).check(matches(isDisplayed()));

        String rootOpdsUrl = UMFileUtil.joinPaths(ResourcesHttpdTestServer.getHttpRoot(),
            "com/ustadmobile/test/sharedse/crawlme/index.opds");
        onView(withId(R.id.fragment_add_feed_url_text)).perform(typeText(rootOpdsUrl));
        onView(withText(R.string.add)).perform(click());

        SystemClock.sleep(2000);

        onView(allOf(
                isDescendantOfA(withTagValue(equalTo("entries:///my_library"))),
                withId(R.id.fragment_catalog_recyclerview)
        )).check(matches(hasDescendant(withText("Crawl Test"))));

        SystemClock.sleep(5000);

        onView(allOf(
                isDescendantOfA(withTagValue(equalTo("entries:///my_library"))),
                hasSibling(withText("Crawl Test")),
                withId(R.id.item_opds_entry_card_download_icon)
        )).perform(click());

        SystemClock.sleep(1000);

        onView(withId(R.id.fragment_start_download_size_text)).check(
                matches(withText(containsString("4 items"))));

        onView(withId(android.R.id.button1)).perform(click());


        SystemClock.sleep(10000);

        //check the icon is now odwnloaded
        onView(allOf(
                isDescendantOfA(withTagValue(equalTo("entries:///my_library"))),
                hasSibling(withText("Crawl Test")),
                withId(R.id.item_opds_entry_card_download_icon)
        )).check(matches(withContentDescription("Downloaded")));

    }

}
