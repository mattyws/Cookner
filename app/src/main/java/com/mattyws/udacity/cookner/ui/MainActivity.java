package com.mattyws.udacity.cookner.ui;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.ui.fragments.RecipeListFragment;
import com.mattyws.udacity.cookner.viewmodel.RecipesViewModel;
import com.mattyws.udacity.cookner.viewmodel.RecipesViewModelFactory;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        RecipeListFragment.RecipeListFragmentListener
{

    private static final int ADD_NEW_RECIPE = 1;
    private static final int RC_SIGN_IN = 11;

    private RecipeListFragment mRecipeListFragment;
    private RecipesViewModel mViewModel;

    /**
     * Firebase auth members
     */
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mUser;

    /**
     * Firebase AdMob
     *
     */
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                AlertDialog.Builder alert = buildAddRecipeDialog();
                alert.show();
            }
        });
        mInterstitialAd.loadAd(request);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    onSignedInInitialize(firebaseUser);
                } else {
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Result ok", Toast.LENGTH_SHORT).show();
            } else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void fab_onClick(View view) {
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        } else {
            AlertDialog.Builder alert = buildAddRecipeDialog();
            alert.show();
        }
    }

    private AlertDialog.Builder buildAddRecipeDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Insert Name");
        alert.setMessage("Recipe name");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alert.setView(input);
        alert.setIcon(R.drawable.ic_add_black_24dp);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Here actually start the GoLauncher
                        Intent addRecipeIntent = new Intent(MainActivity.this, FormRecipeActivity.class);
                        addRecipeIntent.putExtra(FormRecipeActivity.RECIPE_NAME, input.getText().toString());
                        addRecipeIntent.putExtra(FormRecipeActivity.USER_ID, mUser.getEmail());
                        startActivity(addRecipeIntent);
                    }
                });
        alert.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return alert;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
//        if(mChildEventListener != null)
//            mDatabaseReference.removeEventListener(mChildEventListener);
    }


    private void onSignedOutCleanup() {
        mUser = null;
//        mMessageAdapter.clear();
//        if(mChildEventListener != null)
//            mDatabaseReference.removeEventListener(mChildEventListener);
    }

    private void onSignedInInitialize(FirebaseUser firebaseUser) {
        mUser = firebaseUser;
        RecipesViewModelFactory factory = new RecipesViewModelFactory(this, mUser.getEmail());
        mViewModel = ViewModelProviders.of(this, factory).get(RecipesViewModel.class);
        mRecipeListFragment = RecipeListFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_list_holder, mRecipeListFragment)
                .commit();

//        mChildEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                FriendlyMessage message = dataSnapshot.getValue(FriendlyMessage.class);
//                mMessageAdapter.add(message);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public void onRecipeDelete(Recipe deletedRecipe) {
        mViewModel.delete(deletedRecipe);
    }
}
