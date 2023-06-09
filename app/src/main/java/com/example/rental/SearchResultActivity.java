package com.example.rental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rental.adapter.PostAdapter;
import com.example.rental.databinding.ActivitySearchResultBinding;
import com.example.rental.model.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    private ActivitySearchResultBinding binding;
    private PostAdapter adapter;

    private ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String searchTerm = getIntent().getStringExtra("searchTerm");
        search(searchTerm);
        binding.SearchTitle.setText(searchTerm);

        posts = new ArrayList<>();

        adapter = new PostAdapter(posts);
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.categoricalRecyclerPost.setLayoutManager(new LinearLayoutManager(this));
        binding.categoricalRecyclerPost.setAdapter(adapter);
    }

    private void search(String searchTerm) {
        FirebaseDatabase.getInstance().getReference("post").orderByChild("timeStamp").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //Log.v("SearchResultActivity", dataSnapshot.getChildrenCount() + "");
                    posts.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        Post post = ds.getValue(Post.class);
                        //System.out.println(post.getTitle() + " " + post.getDescription() + " " + post.getAddress());
                        if (post.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) || post.getDescription().toLowerCase().contains(searchTerm.toLowerCase()) || post.getAddress().toLowerCase().contains(searchTerm.toLowerCase())) {
                            posts.add(post);
                            //Log.d("SearchResultActivity", "search ");
                        }
                    }
                    if (posts.size() == 0) {
                        binding.noItemTv.setVisibility(View.VISIBLE);
                    } else {
                        binding.noItemTv.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}