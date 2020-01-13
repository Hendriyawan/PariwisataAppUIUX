package com.hdev.pariwisata.view;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.hdev.pariwisata.R;
import com.hdev.pariwisata.view.adapter.FotoWisataAdapter;
import com.hdev.pariwisata.view.adapter.WisataAdapter;
import com.hdev.pariwisata.view.model.Wisata;
import com.hdev.pariwisata.view.utils.AssetManager;
import com.ramotion.cardslider.CardSliderLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottom_sheet_main)
    LinearLayout linearLayoutBottomSheet;
    @BindView(R.id.tv_bottom_sheet_name_wisata)
    TextView textViewNamaWisata;
    @BindView(R.id.tv_bottom_sheet_deskripsi_wisata)
    TextView textViewDeskripsiWisata;
    @BindView(R.id.tv_profile_name)
    TextView textViewProfileName;

    @BindView(R.id.image_foto_profile)
    ImageView imageViewFotoProfile;

    //RecyclerView
    @BindView(R.id.recycler_view_wisata)
    RecyclerView recyclerViewWisata;
    @BindView(R.id.recycler_view_foto)
    RecyclerView recyclerViewFoto;
    @BindView(R.id.layout_content_detail_wisata)
    LinearLayout linearLayoutContentDetail;
    @BindView(R.id.layout_detail_wisata)
    LinearLayout linearLayoutDetailWisata;
    @BindView(R.id.layout_content_about_me)
    LinearLayout linearLayoutContentAboutMe;
    @BindView(R.id.layout_content_about_me_bio)
    LinearLayout linearLayoutAboutMeBio;

    private WisataAdapter wisataAdapter;
    private BottomSheetBehavior bottomSheetBehavior;
    private CardSliderLayoutManager cardSliderLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    /*
    OnClick close Bottom sheet
     */
    @OnClick(R.id.image_view_close_bottom_sheet)
    public void onCloseBottomSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    showBottomSheet("about", null, null, null);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //Init Views
    private void initViews() {
        //android Toolbar
        initToolbar();
        initBottomSheet();
        initRecyclerViewWisata();

    }

    //init Toolbar
    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    //Init RecyclerView Wisata
    private void initRecyclerViewWisata() {
        List<Wisata> wisataList = getDataWisata();
        Log.d(TAG, String.valueOf(wisataList.size()));
        wisataAdapter = new WisataAdapter(this, wisataList);
        recyclerViewWisata.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewWisata.setAdapter(wisataAdapter);
        wisataAdapter.setOnWisataClick(new WisataAdapter.OnWisataClick() {
            @Override
            public void onClick(Wisata wisata) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    showBottomSheet("detail", wisata.getFoto().split(","), wisata.getNama(), wisata.getDeskripsi());
                }
            }
        });
    }

    //Init RecyclerView foto
    private void initRecyclerViewFoto(String[] files) {
        cardSliderLayoutManager = new CardSliderLayoutManager(this);
        recyclerViewFoto.setLayoutManager(cardSliderLayoutManager);
        FotoWisataAdapter fotoWisataAdapter = new FotoWisataAdapter(this);
        fotoWisataAdapter.setFotos(files);
        recyclerViewFoto.setAdapter(fotoWisataAdapter);
        //new CardSnapHelper().attachToRecyclerView(recyclerViewFoto);
    }

    //Init BottomSheet
    private void initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayoutBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    //get data from assets
    private List<Wisata> getDataWisata() {
        List<Wisata> wisataList = new ArrayList<>();
        Wisata wisata;
        try {
            JSONObject jsonObject = new JSONObject(AssetManager.loadJson(this));
            JSONArray jsonArray = jsonObject.getJSONArray("pariwisata");
            //Log.d(TAG, String.valueOf(jsonArray.length()));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject values = jsonArray.getJSONObject(i);
                wisata = new Wisata();
                wisata.setNama(values.getString("nama"));
                wisata.setFoto(values.getString("foto"));
                wisata.setDeskripsi(values.getString("deskripsi"));
                wisataList.add(wisata);
            }

            //handle exception
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return wisataList;
    }

    //show BottomSheet
    private void showBottomSheet(String type, String[] files, String nama_wisata, String deskripsi_wisata) {
        if (type.equalsIgnoreCase("detail")) {
            linearLayoutContentDetail.setVisibility(View.VISIBLE);
            linearLayoutContentAboutMe.setVisibility(View.GONE);

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            textViewNamaWisata.setText(nama_wisata);
            textViewDeskripsiWisata.setText(deskripsi_wisata);
            textViewNamaWisata.setTypeface(AssetManager.loadTypeface(this, "open-sans-extrabold.ttf"));
            recyclerViewFoto.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
            linearLayoutDetailWisata.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));
            initRecyclerViewFoto(files);

        } else {
            linearLayoutContentDetail.setVisibility(View.GONE);
            linearLayoutContentAboutMe.setVisibility(View.VISIBLE);

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            linearLayoutAboutMeBio.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));
            imageViewFotoProfile.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
            textViewProfileName.setTypeface(AssetManager.loadTypeface(this, "open-sans-extrabold.ttf"));
        }
    }
}
