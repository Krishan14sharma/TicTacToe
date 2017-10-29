package com.ticktacktock.tictactoe;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ticktacktock.tictactoe.ui.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by krishan on 25/10/17.
 */
@RunWith (AndroidJUnit4.class)
public class TicTacToeFunctionalTest {

    private ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Before
    public void setup() {

    }

    @Test
    public void testInitialState() {
        rule.launchActivity(new Intent());
        onView(withText("TicTacToe")).check(matches(isDisplayed()));

    }


}