package com.zedled.app.view.activities;

import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.zedled.app.R;
import com.zedled.app.service.model.PopularTag;
import com.zedled.app.util.Constants;
import com.zedled.app.util.SharedPrefUtil;
import com.zedled.app.view.adapters.QuestionPagerAdapter;
import com.zedled.app.view.adapters.TagAdapter;
import com.zedled.app.view.fragments.QuestionFragment;
import com.zedled.app.viewmodel.HomeViewModel;
import com.zedled.app.databinding.ActivityHomeBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends BaseActivity implements TagAdapter.TagClickListener {

    ActivityHomeBinding binding;

    private HomeViewModel viewModel;
    private QuestionPagerAdapter pagerAdapter;
    private TagAdapter tagAdapter;

    private String currentInterest = null;
    private String currentTag = null;

    private int currentPos = 0;
    private int lastPos = 0;

    private Typeface custom_font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

    }

    @Override
    protected void onStart() {
        super.onStart();

        setupActionBar();
        setupNavigation();
        setupHomeView();

    }

    private void setupActionBar() {

        setSupportActionBar(binding.homeView.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

    }

    private void setupHomeView() {

        pagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), this);
        pagerAdapter.addFragment(QuestionFragment.instance(1), "FOLLOWED");
        pagerAdapter.addFragment(QuestionFragment.instance(2), "HOT");


        binding.homeView.pagerContainer.questionPager.setAdapter(pagerAdapter);
        binding.homeView.pagerContainer.questionPager.setOffscreenPageLimit(2);
        binding.homeView.tabLayout.setupWithViewPager(binding.homeView.pagerContainer.questionPager);
    }

    private void setupNavigation() {

        viewModel.getUserInterest().observe(this, userInterests -> {

            if (!(userInterests.size() < 4)) {

                currentInterest = userInterests.get(0).getUserInterest();
                currentTag = userInterests.get(0).getUserInterest().toLowerCase();

                binding.contentNav.interestOne.setText(userInterests.get(0).getUserInterest());
                binding.contentNav.interestOne.setTypeface(custom_font);
                binding.contentNav.interestTwo.setText(userInterests.get(1).getUserInterest());
                binding.contentNav.interestTwo.setTypeface(custom_font);
                binding.contentNav.interestThree.setText(userInterests.get(2).getUserInterest());
                binding.contentNav.interestThree.setTypeface(custom_font);
                binding.contentNav.interestFour.setText(userInterests.get(3).getUserInterest());
                binding.contentNav.interestFour.setTypeface(custom_font);

                getRelatedTags(currentInterest.toLowerCase());
            } else {
                startActivity(new Intent(this, InterestActivity.class));
                finish();
            }
        });

        binding.contentNav.interestTagList.setLayoutManager(new LinearLayoutManager(this));
        tagAdapter = new TagAdapter(this);
        binding.contentNav.interestTagList.setAdapter(tagAdapter);

    }

    private void updateTagBack(int position) {
        removePastSeletedBack();
        currentPos = position;
        updateSelectedBack();
    }

    private void removePastSeletedBack() {
        switch (currentPos) {
            case 0:
                binding.contentNav.interestOne.setBackgroundResource(0);
                break;
            case 1:
                binding.contentNav.interestTwo.setBackgroundResource(0);
                break;
            case 2:
                binding.contentNav.interestThree.setBackgroundResource(0);
                break;
            case 3:
                binding.contentNav.interestFour.setBackgroundResource(0);
                break;
        }
    }

    private void updateSelectedBack() {
        switch (currentPos) {
            case 0:
                binding.contentNav.interestOne.setBackground(getResources().getDrawable(R.drawable.seleted_tag_back));
                break;
            case 1:
                binding.contentNav.interestTwo.setBackground(getResources().getDrawable(R.drawable.seleted_tag_back));
                break;
            case 2:
                binding.contentNav.interestThree.setBackground(getResources().getDrawable(R.drawable.seleted_tag_back));
                break;
            case 3:
                binding.contentNav.interestFour.setBackground(getResources().getDrawable(R.drawable.seleted_tag_back));
                break;
        }
    }


    public void interestClicked(View view) {

        if (view instanceof TextView) {
            TextView interestTv = (TextView) view;
            currentInterest = interestTv.getText().toString().toLowerCase();
            updateTagBack(Integer.parseInt((String) interestTv.getTag()));
            getRelatedTags(currentInterest);
        }
    }

    private void getRelatedTags(String interest) {

        binding.contentNav.tagProgressbar.setVisibility(View.VISIBLE);
        binding.contentNav.interestTagList.setVisibility(View.INVISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put(Constants.QueryParam.PAGE, "1");
        map.put(Constants.QueryParam.PAGE_SIZE, "10");
        map.put(Constants.QueryParam.ORDER, "desc");
        map.put(Constants.QueryParam.SORT, "popular");
        map.put(Constants.QueryParam.INNAME, interest);
        map.put(Constants.QueryParam.SITE, "stackoverflow");
        map.put(Constants.QueryParam.KEY, SharedPrefUtil.instance().getString(SharedPrefUtil.ACCESS_KEY));

        viewModel.getPopularTag(map).observe(this, popularTagResponseList -> {
            if (popularTagResponseList != null && popularTagResponseList.getItems().size() > 0) {

                binding.contentNav.tagProgressbar.setVisibility(View.GONE);
                binding.contentNav.interestTagList.setVisibility(View.VISIBLE);

                List<PopularTag> popularTags = popularTagResponseList.getItems();
                tagAdapter.swapData(popularTags);
                viewModel.setTag(popularTags.get(0).getName());
            } else {
                showToastMessage("getRelatedTags issue occurred");
            }
        });

    }


    private void closeDrawers() {
        if (binding.drawerLayout.isDrawerOpen(binding.navView)) {
            binding.drawerLayout.closeDrawers();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                binding.drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean isHomeAsUpEnabled() {
        return true;
    }

    @Override
    public boolean isToolbarRequired() {
        return false;
    }

    @Override
    public void onBackPressed() {
        closeDrawers();
    }

    @Override
    public void tagClicked(String tag) {
        if (binding.homeView.pagerContainer.questionPager.getCurrentItem() != 0) {
            binding.homeView.pagerContainer.questionPager.setCurrentItem(0, true);
        }
        currentTag = tag;
        viewModel.setTag(currentTag);
        closeDrawers();
    }
}

