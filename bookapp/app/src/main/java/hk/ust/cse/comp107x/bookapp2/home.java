package hk.ust.cse.comp107x.bookapp2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Padhavi on 28-06-2016.
 */
public class home extends Fragment

{
    int id;
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

            //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            // ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ACCOUNT");

            myView = inflater.inflate(R.layout.loginpage, container, false);
            return myView;


        }

    }
