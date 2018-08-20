package com.example.younes.authentificationproject.ui.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
//This Activity may contain the organisation list or the kids list (Dynamic and Adaptative)

    UserInfo mUserInfo;
    RecyclerView myOrganisationsRecyclerView;

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
        myOrganisationsRecyclerView = findViewById(R.id.my_list_recycler_view);
        myOrganisationsLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        myOrganisationsRecyclerView.setLayoutManager(myOrganisationsLayoutManager);
//        OrganisationsRecyclerViewAdapter.RecyclerViewClickListener listener = (view, position) -> {
//            Toast.makeText(UserListActivity.this, "Position " + position, Toast.LENGTH_SHORT).show();
//        };
        OrganisationsRecyclerViewAdapter.RecyclerViewClickListener listener = new OrganisationsRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, Organisation organisation) {
                Toast.makeText(UserListActivity.this, "Organisation Id " + organisation.getId(), Toast.LENGTH_SHORT).show();
                mKidsRecyclerViewAdapter = new KidsRecyclerViewAdapter(getOrganisationKidsList(organisation.getId()),UserListActivity.this);

                myOrganisationsRecyclerView.setAdapter(mKidsRecyclerViewAdapter);
            }
        };
        mOrganisationRecyclerViewAdapter = new OrganisationsRecyclerViewAdapter(getOrganisationsList(),this,listener);

        myOrganisationsRecyclerView.setAdapter(mOrganisationRecyclerViewAdapter);

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
}
