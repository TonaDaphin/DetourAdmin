package com.example.detouradmin.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    private FirebaseAuth mAuth;

    Dialog myDialog;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Users");

        firestore = FirebaseFirestore.getInstance();
        myDialog = new Dialog(this);

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

    public void showPopUp(View v){

        ImageView cancelDialog;
        TextView userNameDialog;
        TextView userEmailDialog;
        TextView userMobileDialog;

        myDialog.setContentView(R.layout.item_user);

        cancelDialog =(ImageView) myDialog.findViewById(R.id.cancel);
        userNameDialog =(TextView) myDialog.findViewById(R.id.userName);
        userEmailDialog =(TextView) myDialog.findViewById(R.id.userEmail);
        userMobileDialog =(TextView) myDialog.findViewById(R.id.userMobile);


        for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (user.getProviderId().equals("facebook.com") || user.getProviderId().equals("google.com")) {
                final FirebaseUser mUser = mAuth.getCurrentUser();
                String userName = mUser.getDisplayName();
                String email = mUser.getEmail();
                String phone = mUser.getPhoneNumber();

                userNameDialog.setText(userName);
                userEmailDialog.setText(email);
                userMobileDialog.setText(phone);
            }
            else if (user.getProviderId().equals("password")) {
                userMobileDialog.setVisibility(View.VISIBLE);

                userID = mAuth.getCurrentUser().getUid();

                DocumentReference documentReference = firestore.collection("users").document(userID);
                documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        userNameDialog.setText(documentSnapshot.getString("Username"));
                        userEmailDialog.setText(documentSnapshot.getString("Email"));
                        userMobileDialog.setText(documentSnapshot.getString("Phone"));
                    }
                });
            }
        }

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

}
