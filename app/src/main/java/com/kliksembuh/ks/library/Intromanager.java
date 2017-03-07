package com.kliksembuh.ks.library;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Trio Andianto on 1/5/2017.
 */
public class Intromanager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    public Intromanager(Context context){
        this.context=context;
        pref=context.getSharedPreferences("first",0);
        editor = pref.edit();
    }
    public void setFirst(boolean isFirst){
        editor.putBoolean("check",isFirst);
        editor.commit();
    }
    public boolean Check(){
        return pref.getBoolean("check",true);
    }
}
