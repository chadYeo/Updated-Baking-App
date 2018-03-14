package com.example.chadyeo.updatedbakingapp;


import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.Espresso.*;



@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {
    @Rule public IntentsTestRule<MainActivity> mMainActivityTestRule =
            new IntentsTestRule<MainActivity>(MainActivity.class);

    @Test
    public void clickRecipeCardView_IntentDetailActivity(){
    }
}
