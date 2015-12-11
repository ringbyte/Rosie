/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.rosie.sample.comics.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.karumi.rosie.sample.R;
import com.karumi.rosie.sample.comics.ComicsModule;
import com.karumi.rosie.sample.comics.view.presenter.ComicSeriesDetailsPresenter;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesDetailsViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesViewModel;
import com.karumi.rosie.view.Presenter;
import com.karumi.rosie.view.RosieActivity;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class ComicSeriesDetailsActivity extends RosieActivity
    implements ComicSeriesDetailsPresenter.View {

  private static final String COMIC_SERIES_KEY_EXTRA = "ComicSeriesDetailsActivity.ComicSeriesKey";
  private static final int INVALID_COMIC_SERIES_KEY_EXTRA = -1;
  private static final int NUMBER_OF_COLUMNS = 3;

  @Bind(R.id.tv_toolbar_title) TextView toolbarTitleView;
  @Bind(R.id.ll_comic_detail) View comicSeriesView;
  @Bind(R.id.iv_cover) ImageView coverView;
  @Bind(R.id.tv_rating) TextView ratingView;
  @Bind(R.id.tv_description) TextView descriptionView;
  @Bind(R.id.loading) RotateLoading loadingView;
  @Bind(R.id.rv_comics) RecyclerView comicsView;

  @Inject @Presenter ComicSeriesDetailsPresenter presenter;

  private RVRendererAdapter<ComicSeriesViewModel> comicSeriesAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeComicsView();
    int comicKey = getIntent().getIntExtra(COMIC_SERIES_KEY_EXTRA, INVALID_COMIC_SERIES_KEY_EXTRA);
    presenter.setComicSeriesKey(comicKey);
  }

  @Override protected int getLayoutId() {
    return R.layout.activity_comic_series_details;
  }

  @Override protected List<Object> getActivityScopeModules() {
    return Arrays.asList((Object) new ComicsModule());
  }

  @Override public void hideLoading() {
    loadingView.stop();
    loadingView.setVisibility(View.GONE);
  }

  @Override public void showLoading() {
    loadingView.setVisibility(View.VISIBLE);
    loadingView.start();
  }

  @Override public void hideComicSeriesDetails() {
    comicSeriesView.setVisibility(View.GONE);
  }

  @Override public void showComicSeriesDetails(ComicSeriesDetailsViewModel comicSeries) {
    Picasso.with(this).load(comicSeries.getCoverUrl()).fit().centerCrop().into(coverView);
    String rating = getString(comicSeries.getRatingNameResourceId());
    toolbarTitleView.setText(comicSeries.getTitle());
    ratingView.setText(getString(R.string.marvel_rating_text, rating));
    descriptionView.setText(comicSeries.getDescription());
    comicSeriesView.setVisibility(View.VISIBLE);
  }

  @Override public void close() {
    finish();
  }

  @OnClick(R.id.iv_toolbar_back) public void onBackButtonClicked() {
    presenter.onBackButtonClicked();
  }

  public static void open(Context context, int comicSeriesKey) {
    Intent intent = new Intent(context, ComicSeriesDetailsActivity.class);
    intent.putExtra(COMIC_SERIES_KEY_EXTRA, comicSeriesKey);
    context.startActivity(intent);
  }

  private void initializeComicsView() {
    GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
    comicsView.setHasFixedSize(true);
    comicsView.setLayoutManager(layoutManager);
    initializeAdapter();
    comicsView.setAdapter(comicSeriesAdapter);
  }

  private void initializeAdapter() {
    //LayoutInflater layoutInflater = LayoutInflater.from(this);
    //RendererBuilder<ComicSeriesViewModel> rendererBuilder = new ComicSeriesRendererBuilder(presenter);
    //comicsCollection = new ComicsSeriesAdapteeCollection();
    //comicSeriesAdapter = new RVRendererAdapter<>(layoutInflater, rendererBuilder, comicsCollection);
  }
}
