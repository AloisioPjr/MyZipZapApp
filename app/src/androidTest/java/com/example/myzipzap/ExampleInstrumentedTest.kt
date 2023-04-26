package com.example.myzipzap

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

//import androidx.test.ext.junit.runners.AndroidJUnit4;
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.myzipzap", appContext.packageName)
    }
/*
    @Test
    fun test_isActivityInView() {
        val activityScenario: ActivityScenario<*> = ActivityScenario.launch(QRScanner::class.java)
        onView(withId(R.id.scanner_Screen)).check(matches(isDisplayed()))
    }
*/
    @Test
    fun display_user_balance_test() {
        val activityScenario: ActivityScenario<*> = ActivityScenario.launch(QRScanner::class.java)
        onView(withId(R.id.balContent)).check(matches(isDisplayed()))

    }
}