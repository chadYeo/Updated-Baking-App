package com.example.chadyeo.updatedbakingapp;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;


@RunWith(AndroidJUnit4.class)
public class BakingMainActivityTest {

    final String RECIPE_NAME_ONE = "Nutella Pie";

    @Rule public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickItem_recipeRecyclerView() {

        onView(withId(R.id.recipes_recyclerView))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(RECIPE_NAME_ONE)), click()));
    }

    @Test
    public void clickRecipeCardView_OpensDetailActivity(){

        Intents.init();

        try {
            onView(withId(R.id.recipes_recyclerView))
                    .perform(RecyclerViewActions.actionOnItem(
                            hasDescendant(withText(RECIPE_NAME_ONE)), click()));

            intended(hasComponent(DetailActivity.class.getName()));
        } finally {
            Intents.release();
        }

        onView(withId(R.id.detailView_steps_recyclerView)).check(matches(
                hasDescendant(allOf(isDisplayed(), withText("Recipe Introduction")))));

    }

}
