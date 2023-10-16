package com.example.pallavi.kshamata;

import android.app.Application;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by pallavi on 09-07-2017.
 */
public class Manager extends Application {
    static ArrayList<String> listofwomen = new ArrayList<String>(Arrays.asList("Anitha", "Mangala" , "Sunitha" , "Poorvi", "Soumya","Bharathi","Mohini","Bhagirathi"));
    static ArrayList<String> listofvolunteers = new ArrayList<String>(Arrays.asList("Balu", "Praveen" , "Manju" , "Vinay", "Varun"));
    static ArrayList<String> womengiven = new ArrayList<String>(Arrays.asList("Anitha", "Mangala" , "Sunitha" , "Poorvi"));
}
