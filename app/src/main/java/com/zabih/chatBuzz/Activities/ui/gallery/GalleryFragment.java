package com.zabih.chatBuzz.Activities.ui.gallery;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.zabih.chatBuzz.Activities.SignIn;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), SignIn.class));
        getActivity().finish();
    }
}