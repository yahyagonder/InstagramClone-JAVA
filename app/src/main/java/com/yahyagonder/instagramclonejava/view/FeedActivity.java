package com.yahyagonder.instagramclonejava.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.yahyagonder.instagramclonejava.R;
import com.yahyagonder.instagramclonejava.adapter.PostAdapter;
import com.yahyagonder.instagramclonejava.databinding.ActivityFeedBinding;
import com.yahyagonder.instagramclonejava.model.Post;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private ActivityFeedBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    ArrayList<Post> postArrayList;

    PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postArrayList = new ArrayList<>();

        setSupportActionBar(binding.toolbar);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getData();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postArrayList);
        binding.recyclerView.setAdapter(postAdapter);

    }

    private void getData() {

        //DocumentReference documentReference = firebaseFirestore.collection("Posts").document("test");
        //CollectionReference documentReference = firebaseFirestore.collection("Posts");

        firebaseFirestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {

                    Toast.makeText(getApplicationContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                }

                if (value != null) {

                    for (DocumentSnapshot snapshot: value.getDocuments()) {

                        Map<String, Object> data = snapshot.getData();

                        //Casting
                        String userEmail = (String) data.get("useremail");
                        String comment = (String) data.get("comment");
                        String downloadURL = (String) data.get("downloadURL");

                        Post post = new Post(userEmail,comment,downloadURL);
                        postArrayList.add(post);

                    }

                    postAdapter.notifyDataSetChanged();

                }

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_post) {

            Intent intentToUpload = new Intent(getApplicationContext(), UploadActivity.class);
            startActivity(intentToUpload);

        } else if (item.getItemId() == R.id.sign_out) {

            auth.signOut();

            Intent intenToMain = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intenToMain);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}