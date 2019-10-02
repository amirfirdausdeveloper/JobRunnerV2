package com.jobrunner.app.Common;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TypeFaceClass {

    public static void setTypeFaceEditText(EditText editText, Context context){
        editText.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/Avenir-Medium-09.ttf"));
    }

    public static void setTypeFaceTextView(TextView textView, Context context){
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/Avenir-Medium-09.ttf"));
    }
    public static void setTypeFaceButton(Button button, Context context){
        button.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/Avenir-Medium-09.ttf"));
    }

    public static void setTypeFaceTextInputEditText(TextInputEditText textInputEditText, Context context){
        textInputEditText.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/Avenir-Medium-09.ttf"));
    }


    //FOR BOLD FONT

    public static void setTypeFaceEditTextBOLD(EditText editText, Context context){
        editText.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/Avenir-Black-03.ttf"));
    }

    public static void setTypeFaceTextViewBOLD(TextView textView, Context context){
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/Avenir-Black-03.ttf"));
    }
    public static void setTypeFaceButtonBOLD(Button button, Context context){
        button.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/Avenir-Black-03.ttf"));
    }

    public static void setTypeFaceTextInputEditTextBOLD(TextInputEditText textInputEditText, Context context){
        textInputEditText.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/Avenir-Black-03.ttf"));
    }
}
