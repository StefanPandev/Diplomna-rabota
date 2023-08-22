package com.example.highspots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.highspots.models.Spot;
import com.example.highspots.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConsoleActivity extends AppCompatActivity {

    private final long MAX_IMAGE_SIZE_TO_DOWNLOAD = 1024 * 1024;

    /* Views */
    private EditText searchUserET;
    private TextView userIDTV;
    private TextView userEmailTV;
    private TextView userNicknameTV;
    private TextView userRoleTV;
    private TextView userNumVisitedSpotsTV;
    private TextView userNumFoundSpotsTV;
    private TextView userFoundSpotsTV;
    private Button userSearchBTN;
    private Button userClearBTN;
    private Button userDeleteBTN;

    private EditText searchSpotET;
    private TextView spotIDTV;
    private TextView spotCreatorIDTV;
    private TextView spotFeaturesTV;
    private TextView spotImageNameTV;
    private TextView spotLocationTV;
    private TextView spotNumberOfRatingsTV;
    private TextView spotRatingTV;
    private TextView spotNumOfVisitorsTV;
    private ImageView spotImageIV;
    private Button spotSearchBTN;
    private Button spotClearBTN;
    private Button spotDeleteBTN;

    /* Variables */
    private User lastFetchedUser;
    private Spot lastFetchedSpot;

    /* Database */
    private final DatabaseReference usersDataReference = FirebaseDatabase.getInstance("https://diplomna-rabota-7977a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
    private final DatabaseReference spotsDataReference = FirebaseDatabase.getInstance("https://diplomna-rabota-7977a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Spots");
    private final StorageReference imageStorageReference = FirebaseStorage.getInstance().getReference().child("Images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);
        this.getSupportActionBar().hide();

        initVars();
        initViews();
    }

    private void initVars() {

    }

    private void initViews() {
        // User related views
        this.searchUserET = findViewById(R.id.consoleSearchUserET);
        this.userIDTV = findViewById(R.id.consoleUserIDTV);
        this.userEmailTV = findViewById(R.id.consoleUserEmailTV);
        this.userNicknameTV = findViewById(R.id.consoleUserNicknameTV);
        this.userRoleTV = findViewById(R.id.consoleUserRoleTV);
        this.userNumVisitedSpotsTV = findViewById(R.id.consoleUserNumVisitedSpotsTV);
        this.userNumFoundSpotsTV = findViewById(R.id.consoleUserNumFoundSpotsTV);
        this.userFoundSpotsTV = findViewById(R.id.consoleUserFoundSpotsTV);

        this.userDeleteBTN = findViewById(R.id.consoleUserDeleteBTN);
        userDeleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(lastFetchedUser);
            }
        });

        this.userSearchBTN = findViewById(R.id.consoleUserSearchBTN);
        userSearchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchUserET.getText().toString().trim().isEmpty()) {
                    searchUserET.setError("Input user ID!");
                    return;
                }

                usersDataReference.child(searchUserET.getText().toString().trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        lastFetchedUser = task.getResult().getValue(User.class);

                        if (lastFetchedUser == null) {
                            Toast.makeText(ConsoleActivity.this, "User with this ID does not exist!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        userIDTV.setText("User ID: " + lastFetchedUser.getDbID());
                        userEmailTV.setText("User Email: " + lastFetchedUser.getEmail());
                        userNicknameTV.setText("User Nickname: " + lastFetchedUser.getNickName());
                        userRoleTV.setText("User role: " + lastFetchedUser.getRole());
                        userNumVisitedSpotsTV.setText("User number of visited spots: " + lastFetchedUser.getVisitedSpots().size());
                        userNumFoundSpotsTV.setText("User number of found spots: " + lastFetchedUser.getFoundSpots().size());
                        userFoundSpotsTV.setText("User found spots' IDs: " + lastFetchedUser.getFoundSpots().keySet().toString());
                        userDeleteBTN.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        this.userClearBTN = findViewById(R.id.consoleUserClearBTN);
        userClearBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUserET.setText("");
                userIDTV.setText("User ID: ");
                userEmailTV.setText("User Email: ");
                userNicknameTV.setText("User Nickname: ");
                userRoleTV.setText("User role: ");
                userNumVisitedSpotsTV.setText("User number of visited spots: ");
                userNumFoundSpotsTV.setText("User number of found spots: ");
                userFoundSpotsTV.setText("User found spots' IDs: ");
                userDeleteBTN.setVisibility(View.GONE);
            }
        });

        // Spot related views
        this.searchSpotET = findViewById(R.id.consoleSearchSpotET);
        this.spotIDTV = findViewById(R.id.consoleSpotIDTV);
        this.spotCreatorIDTV = findViewById(R.id.consoleSpotCreatorIDTV);
        this.spotFeaturesTV = findViewById(R.id.consoleSpotFeaturesTV);
        this.spotImageNameTV = findViewById(R.id.consoleSpotImageNameTV);
        this.spotLocationTV = findViewById(R.id.consoleSpotLocationTV);
        this.spotNumberOfRatingsTV = findViewById(R.id.consoleSpotNumOfRatingsTV);
        this.spotRatingTV = findViewById(R.id.consoleSpotRatingTV);
        this.spotNumOfVisitorsTV = findViewById(R.id.consoleSpotNumberOfVisitorsTV);
        this.spotImageIV = findViewById(R.id.consoleSpotImageIV);

        this.spotDeleteBTN = findViewById(R.id.consoleSpotDeleteBTN);
        spotDeleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSpot(lastFetchedSpot);
            }
        });

        this.spotSearchBTN = findViewById(R.id.consoleSpotSearchBTN);
        spotSearchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchSpotET.getText().toString().trim().isEmpty()) {
                    searchSpotET.setError("Input spot ID!");
                    return;
                }

                // Fetch spot data
                spotsDataReference.child(searchSpotET.getText().toString().trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        lastFetchedSpot = task.getResult().getValue(Spot.class);

                        if (lastFetchedSpot == null) {
                            Toast.makeText(ConsoleActivity.this, "Spot with this ID does not exist!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Insert values from spot
                        spotIDTV.setText("Spot ID: " + lastFetchedSpot.getDbID());
                        spotCreatorIDTV.setText("Spot creator ID: " + lastFetchedSpot.getCreatorID());
                        spotFeaturesTV.setText("Spot features: " + lastFetchedSpot.getFeatures().values().toString());
                        spotImageNameTV.setText("Spot image name: " + lastFetchedSpot.getImageName());
                        spotLocationTV.setText("Spot location: " + lastFetchedSpot.getLocation());
                        spotNumberOfRatingsTV.setText("Spot number of ratings: " + lastFetchedSpot.getNumberOfRatings());
                        spotRatingTV.setText("Spot rating: " + lastFetchedSpot.getRating());
                        spotNumOfVisitorsTV.setText("Spot number of visitors: " + lastFetchedSpot.getVisitors().size());
                        imageStorageReference.child(lastFetchedSpot.getImageName()).getBytes(MAX_IMAGE_SIZE_TO_DOWNLOAD).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                spotImageIV.setImageBitmap(bitmap);
                            }
                        });
                        spotDeleteBTN.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        this.spotClearBTN = findViewById(R.id.consoleSpotClearBTN);
        spotClearBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchSpotET.setText("");
                spotIDTV.setText("Spot ID: ");
                spotCreatorIDTV.setText("Spot creator ID: ");
                spotFeaturesTV.setText("Spot features: ");
                spotImageNameTV.setText("Spot image name: ");
                spotLocationTV.setText("Spot location: ");
                spotNumberOfRatingsTV.setText("Spot number of ratings: ");
                spotRatingTV.setText("Spot rating: ");
                spotNumOfVisitorsTV.setText("Spot number of visitors: ");
                spotImageIV.setImageDrawable(null);
                spotDeleteBTN.setVisibility(View.GONE);
            }
        });
    }

    private void deleteUser(User user) {
        if (user == null) {
            return;
        }

        // Delete user from the list of visitors of the spots they have visited
        for (String visitedSpotID : user.getVisitedSpots().keySet()) {
            spotsDataReference.child(visitedSpotID).child("visitors").child(user.getDbID()).setValue(null);
        }

        // Delete user from the list of visitors of the spots they have created
        for (String foundSpotID : user.getFoundSpots().keySet()) {
            spotsDataReference.child(foundSpotID).child("visitors").child(user.getDbID()).setValue(null);
            spotsDataReference.child(foundSpotID).child("creatorID").setValue(null);
        }

        // Delete user from the db
        usersDataReference.child(user.getDbID()).setValue(null);

        Toast.makeText(this, "User is deleted from db!", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Delete user from authentication!", Toast.LENGTH_LONG).show();
    }

    private void deleteSpot(Spot spot) {
        if (spot == null) {
            return;
        }

        // From each visitor delete the spot from the list of visited spots
        for (String visitor : spot.getVisitors().keySet()) {
            usersDataReference.child(visitor).child("visitedSpots").child(spot.getDbID()).setValue(null);
            usersDataReference.child(visitor).child("ratedSpots").child(spot.getDbID()).setValue(null);
        }

        // Delete the spot from its creators found spots list
        if (spot.getCreatorID() != null) {
            usersDataReference.child(spot.getCreatorID()).child("foundSpots").child(spot.getDbID()).setValue(null);
        }

        // Delete spot data from db
        spotsDataReference.child(spot.getDbID()).setValue(null);

        
        // Delete spot image from storage
        imageStorageReference.child(spot.getImageName()).delete();

        Toast.makeText(this, "Spot has been deleted successfully!", Toast.LENGTH_LONG).show();
    }
}