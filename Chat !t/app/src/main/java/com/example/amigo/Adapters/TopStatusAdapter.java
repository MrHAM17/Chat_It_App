package com.example.amigo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.amigo.Activities.MainActivity;
import com.example.amigo.Models.Status;
import com.example.amigo.Models.UserStatus;
import com.example.amigo.R;
import com.example.amigo.databinding.ItemStatusBinding;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class TopStatusAdapter extends RecyclerView.Adapter<TopStatusAdapter.TopStatusViewHolder> {

    Context context;
    ArrayList<UserStatus> userStatuses;

    //  Constructer is created.
    public TopStatusAdapter(Context context, ArrayList<UserStatus> userStatuses) {
        this.context = context;
        this.userStatuses = userStatuses;
    }

    @NonNull
    @Override
    public TopStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_status, parent, false);
        return new TopStatusViewHolder(view);
    }

//  Status: Process 2.9:showing the saved stories (uploaded by the user) from database into the app.            // process 2.10: at 46.
    @Override
    public void onBindViewHolder(@NonNull TopStatusViewHolder holder, int position) {

   ///  Status: Process 2.10: To show the last uploaded status on story (So one can almost see the last status without open it.)    // process 2.9: at 42   // process 2.11: at 54
        UserStatus userStatus = userStatuses.get(position);    // Took the statuses from user.

        Status lastStatus = userStatus.getStatuses().get(userStatus.getStatuses().size() - 1);

        Glide.with(context).load(lastStatus.getImageUrl()).into(holder.binding.image);
   ///.

   //   Status: Process 2.11: To make cuts in the border of status so one can get the sence of "How many images/videos uploaded by the person"    // Process 2.10: at 46
        holder.binding.circularStatusView.setPortionsCount(userStatus.getStatuses().size());
   //.  (NOTE: Till now stories are visible & any one can see it by open it. But the problem is, "Too much gap is there in between 2 stories.")

        // Implemaintation of used library.
        holder.binding.circularStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MyStory> myStories = new ArrayList<>();
                for(Status status : userStatus.getStatuses()) {
                    myStories.add(new MyStory(status.getImageUrl()));
                }

                new StoryView.Builder(((MainActivity)context).getSupportFragmentManager())
                        .setStoriesList(myStories) // Required
                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                        .setTitleText(userStatus.getName()) // Default is Hidden
                        .setSubtitleText("") // Default is Hidden
                        .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
                        .setStoryClickListeners(new StoryClickListeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                //your action
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {
                                //your action
                            }
                        }) // Optional Listeners
                        .build() // Must be called before calling show method
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public class TopStatusViewHolder extends RecyclerView.ViewHolder {

        ItemStatusBinding binding;

        public TopStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStatusBinding.bind(itemView);
        }
    }
}