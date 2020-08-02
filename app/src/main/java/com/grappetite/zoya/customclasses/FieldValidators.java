package com.grappetite.zoya.customclasses;


import androidx.annotation.NonNull;
import android.util.Patterns;
import android.widget.TextView;

public class FieldValidators {
    public static boolean isNameValid(@NonNull TextView tv) {
        String txt = tv.getText().toString();
        if (txt.trim().isEmpty())
            tv.setError("Enter Name");
        else if (txt.trim().length()<2)
            tv.setError("Name can't be less than 3 characters");
        else
            return true;
        return false;
    }
    public static boolean isEmailValid(@NonNull TextView tv) {
        String txt = tv.getText().toString();
        if (txt.trim().isEmpty())
            tv.setError("Enter  Your Email Address");
        else if (!Patterns.EMAIL_ADDRESS.matcher(txt).matches())
            tv.setError("Enter A Valid Email Address");
        else
            return true;
        return false;
    }

    public static boolean isValidPassword(@NonNull TextView tv)
    {
        String txt = tv.getText().toString();
        if(txt.trim().isEmpty())
            tv.setError("Enter password");
        else if (txt.trim().length()<6)
            tv.setError("Password should be at least of 6 characters");
        else
            return true;
        return false;
    }


    public static boolean isSubjectValid(@NonNull TextView tv) {
        String txt = tv.getText().toString();
        if (txt.trim().isEmpty())
            tv.setError("Enter Subject");
        else if (txt.trim().length()<2)
            tv.setError("Subject can't be less than 3 characters");
        else
            return true;
        return false;
    }


    public static boolean isContactUsMailValid(@NonNull TextView tv) {
        String txt = tv.getText().toString();
        if (txt.trim().isEmpty())
            tv.setError("Enter Email");
        else if (txt.trim().length()<2)
            tv.setError("Email can't be less than 3 characters");
        else
            return true;
        return false;
    }


}
