package com.example.detouradmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.detouradmin.Models.UserModel;
import com.example.detouradmin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersActivity extends AppCompatActivity {

    @BindView(R.id.recyclerviewUsers)
    RecyclerView usersList;
    @BindView(R.id.user_toolbar)
    Toolbar toolbar;

    private FirebaseFirestore firestore;
    private FirestoreRecyclerAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Users");

        firestore = FirebaseFirestore.getInstance();

        //Query
        Query query = firestore.collection("users");

        //RecyclerOptions
        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class)
                .build();

        userAdapter = new FirestoreRecyclerAdapter<UserModel, UserViewHolder>(options) {

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_list, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull UserModel model) {
                holder.userListName.setText(model.getUsername());
                holder.userListEmail.setText(model.getEmail());

            }
        };

        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new LinearLayoutManager(this));
        usersList.setAdapter(userAdapter);

    }

    //ViewHolder

    private class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView userListName;
        private TextView userListEmail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userListName = itemView.findViewById(R.id.listUsername);
            userListEmail = itemView.findViewById(R.id.listEmail);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        userAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        userAdapter.startListening();
    }

}
