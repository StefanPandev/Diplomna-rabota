package com.example.highspots.repositories;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.highspots.interfaces.UserDataListener;
import com.example.highspots.models.Spot;
import com.example.highspots.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserDataRepository {

    private static UserDataRepository instance;
    private List<UserDataListener> listeners;

    /* User Data */
    private String userID;
    private User user;
    private List<Spot> foundSpots;

    /* Database */
    private DatabaseReference usersDataReference;
    private DatabaseReference spotsDataReference;
    private StorageReference imageStorageReference;

    private UserDataRepository() {
        this.imageStorageReference = FirebaseStorage.getInstance().getReference().child("Images");
        this.spotsDataReference = FirebaseDatabase.getInstance("https://diplomna-rabota-7977a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Spots");
        this.usersDataReference = FirebaseDatabase.getInstance("https://diplomna-rabota-7977a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
        this.listeners = new ArrayList<>();
        this.foundSpots = new ArrayList<>();

        this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Fetch user data
        usersDataReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);

                if (user1 != null) {
                    user = user1;
                    notifyListenersOnUserDataChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Fetch user's found spots from db
        usersDataReference.child(userID).child("foundSpots").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                foundSpots.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String spotID = ds.getValue(String.class);
                    spotsDataReference.child(spotID).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            Spot spot = dataSnapshot.getValue(Spot.class);
                            if (spot != null) {
                                foundSpots.add(spot);
                                notifyListenersOnFoundSpotsDataChanged();
                            }
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static UserDataRepository getInstance() {
        if (instance == null) {
            instance = new UserDataRepository();
        }

        return instance;
    }

    public static void deleteCurrentUserData() {
        instance = null;
    }

    private void notifyListenersOnUserDataChanged() {
        for (UserDataListener listener : this.listeners) {
            listener.retrieveUserData();
        }
    }

    private void notifyListenersOnFoundSpotsDataChanged() {
        for (UserDataListener listener : this.listeners) {
            listener.retrieveFoundSpotsData();
        }
    }

    public void updateNickName(String newNickName) {
        usersDataReference.child(this.userID).child("nickName").setValue(newNickName);
    }

    public void updateUserInDB() {
        usersDataReference.child(this.userID).setValue(this.user);
    }

    public void updateSpotInDB(Spot spot) {
        spotsDataReference.child(spot.getDbID()).setValue(spot);
    }

    public Spot addNewSpot(Map<String, String> newSpotFeatures, double rating, String location, ImageView imageView) {

        // Extracting the image from the image view and uploading it to db
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataToUpload = baos.toByteArray();

        String imageName = UUID.randomUUID().toString();

        imageStorageReference.child(imageName).putBytes(dataToUpload);

        // Create new spot
        String newSpotID = spotsDataReference.push().getKey();
        int numberOfRatings = 1;
        Spot newSpot = new Spot(newSpotFeatures, location, newSpotID, rating, numberOfRatings, new HashMap<String, String>(), this.userID, imageName);
        newSpot.addVisitor(this.user.getDbID());

        // Update user
        this.user.addFoundSpot(newSpot.getDbID());
        this.user.addRatedSpot(newSpot.getDbID());
        updateUserInDB();

        // Save spot
        updateSpotInDB(newSpot);

        return newSpot;
    }

    public void visitSpot(Spot spot) {
        // Update user
        this.user.addVisitedSpot(spot.getDbID());
        updateUserInDB();

        // Update spot
        spot.addVisitor(this.user.getDbID());
        updateSpotInDB(spot);
    }

    public void rateSpot(Spot spot, double rating) {
        // Update spot
        spot.addNewRating(rating);
        updateSpotInDB(spot);

        // Update user
        this.user.addRatedSpot(spot.getDbID());
        updateUserInDB();
    }

    public void deleteUser() {
        for (String visitedSpotID : this.user.getVisitedSpots().keySet()) {
            spotsDataReference.child(visitedSpotID).child("visitors").child(user.getDbID()).setValue(null);
        }

        for (String foundSpotID : this.user.getFoundSpots().keySet()) {
            spotsDataReference.child(foundSpotID).child("visitors").child(user.getDbID()).setValue(null);
            spotsDataReference.child(foundSpotID).child("creatorID").setValue(null);
        }

        usersDataReference.child(this.user.getDbID()).setValue(null);
    }

    public void addListener(UserDataListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(UserDataListener listener) {
        this.listeners.remove(listener);
    }

    public User getUser() {
        return this.user;
    }

    public List<Spot> getFoundSpots() {
        return foundSpots;
    }
}