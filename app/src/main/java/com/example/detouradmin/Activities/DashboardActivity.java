package com.example.detouradmin.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.detouradmin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.dashboard_toolbar) Toolbar toolbarDashboard;
    @BindView(R.id.setsCardView) CardView setsCard;
    @BindView(R.id.usersCardView) CardView usersCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);

        setSupportActionBar(toolbarDashboard);
        getSupportActionBar().setTitle("Dashboard");

        setsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,SetsActivity.class);
                startActivity(intent);
            }
        });

        usersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,UsersActivity.class);
                startActivity(intent);
            }
        });
    }
}
