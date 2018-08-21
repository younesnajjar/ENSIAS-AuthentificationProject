package com.example.younes.authentificationproject.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.younes.authentificationproject.R;
import com.example.younes.authentificationproject.models.Kids;
import com.example.younes.authentificationproject.models.Organisation;
import com.example.younes.authentificationproject.models.UserInfo;
import com.example.younes.authentificationproject.services.DatabaseHelper;
import com.example.younes.authentificationproject.ui.adapters.KidsRecyclerViewAdapter;
import com.example.younes.authentificationproject.ui.adapters.OrganisationsRecyclerViewAdapter;
import com.example.younes.authentificationproject.utils.Logger;
import com.example.younes.authentificationproject.utils.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
//This Activity may contain the organisation list or the kids list (Dynamic and Adaptative)

    // Views
    RecyclerView myOrganisationsRecyclerView;
    private ImageButton backArrowImageView;
    private ImageButton logoutImageView;
    private TransitionDrawable transition;

    //
    OrganisationsRecyclerViewAdapter.RecyclerViewClickListener listener;
    UserInfo mUserInfo;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDatabase;
    private LinearLayoutManager myOrganisationsLayoutManager;
    private OrganisationsRecyclerViewAdapter mOrganisationRecyclerViewAdapter;
    private KidsRecyclerViewAdapter mKidsRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mDBHelper = DatabaseHelper.getInstance(this);
        mDatabase  = mDBHelper.getWritableDatabase();

        //View Declarations
        backArrowImageView = findViewById(R.id.back_arrow);
        myOrganisationsRecyclerView = findViewById(R.id.my_list_recycler_view);
        logoutImageView = findViewById(R.id.logout_button);
        transition = (TransitionDrawable) myOrganisationsRecyclerView.getBackground();

        //Listners Settings
        listener = new OrganisationItemClickListner();
        logoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
                SaveSharedPreference.putAccessToken(getApplicationContext(), null);
                logout();
            }
        });

        // Layout Manager Settings
        myOrganisationsLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        myOrganisationsRecyclerView.setLayoutManager(myOrganisationsLayoutManager);


        // Adapter Settings

        setOrganisationAdapter();

    }

    private void setOrganisationAdapter() {

        mOrganisationRecyclerViewAdapter = new OrganisationsRecyclerViewAdapter(getOrganisationsList(),this,listener);
        myOrganisationsRecyclerView.setAdapter(mOrganisationRecyclerViewAdapter);
        runLayoutAnimation(myOrganisationsRecyclerView);
    }


    public List<Organisation> getOrganisationsList(){
        List<Organisation> myOrganisations = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM organisations", null);
        if(cursor.moveToFirst()){
            do{
                myOrganisations.add(Organisation.getOrganisationFromCursor(cursor));
            }while(cursor.moveToNext());
        }
        return myOrganisations;
    }
    public List<Kids> getOrganisationKidsList(int organisationId){
        List<Kids> selectedOrganisationKids = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM kids WHERE id_organisation = "+organisationId, null);
        if(cursor.moveToFirst()){
            do{
                selectedOrganisationKids.add(Kids.getKidFromCursor(cursor));
            }while(cursor.moveToNext());
        }
        return selectedOrganisationKids;
    }

    private class OrganisationItemClickListner implements OrganisationsRecyclerViewAdapter.RecyclerViewClickListener {
        @Override
        public void onClick(View view, Organisation organisation) {
            mKidsRecyclerViewAdapter = new KidsRecyclerViewAdapter(getOrganisationKidsList(organisation.getId()),UserListActivity.this);
            myOrganisationsRecyclerView.setAdapter(mKidsRecyclerViewAdapter);
            runLayoutAnimation(myOrganisationsRecyclerView);
            backArrowImageView.setVisibility(View.VISIBLE);
            transition.startTransition(500);
            backArrowImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOrganisationAdapter();
                    v.setOnClickListener(null);
                    v.setVisibility(View.INVISIBLE);
                    transition.reverseTransition(500);
                }
            });
        }
    }
    public void logout() {
        clearUserData();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //        Retrofit retrofit = RetrofitClient.getClient();
//        LoginServices loginServices = retrofit.create(LoginServices.class);
//        Call<Void> logout = loginServices.logout();
//
//        logout.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.code() == 200) {
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.e("TAG", "=======onFailure: " + t.toString());
//                t.printStackTrace();
//            }
//        });
    }

    private void clearUserData() {
        mDatabase.execSQL("DELETE FROM organisations");
        mDatabase.execSQL("DELETE FROM kids");
    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
        Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();
    }
}
