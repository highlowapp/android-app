package com.gethighlow.highlowandroid.CustomViews.BaseComponents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gethighlow.highlowandroid.R;
import com.gethighlow.highlowandroid.model.Services.SetActivityTheme;

public class TextInput extends LinearLayout {
    public TextInput(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        setup(context, attributeSet);
    }

    public TextInput(Context context) {
        super(context);
        setup(context, null);
    }

    private void setup(Context context, @Nullable AttributeSet attributeSet) {
        setOrientation(LinearLayout.VERTICAL);

        if (attributeSet != null) {
            TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.TextInput, 0, 0);

            String theme = a.getString(R.styleable.TextInput_inputTheme);
            int layout = R.layout.textinput;
            if (theme != null && theme.equals("plain")) {
                layout = R.layout.textinput_white;
            }
            String currentTheme = SetActivityTheme.getTheme(getContext());

            /*LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(layout, this, true);
            EditText textInput = findViewById(R.id.textInput);
            String hint = a.getString(R.styleable.TextInput_title);
            String inputType = a.getString(R.styleable.TextInput_inputType);
            textInput.setHint(hint);*/



            if(currentTheme.equals("light")){
                final ContextThemeWrapper layoutTheme = new ContextThemeWrapper(context, R.style.LightTheme);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LayoutInflater localInflater = inflater.cloneInContext(layoutTheme);
                localInflater.inflate(layout, this, true);

            }else{
                final ContextThemeWrapper layoutTheme = new ContextThemeWrapper(context, R.style.DarkTheme);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LayoutInflater localInflater = inflater.cloneInContext(layoutTheme);
                localInflater.inflate(layout, this, true);
            }

            EditText textInput = findViewById(R.id.textInput);
            String hint = a.getString(R.styleable.TextInput_title);
            String inputType = a.getString(R.styleable.TextInput_inputType);

            if(currentTheme.equals("light")) {
                textInput.setHintTextColor(getResources().getColor(R.color.gray));
            }else{
                textInput.setHintTextColor(getResources().getColor(R.color.light_grey));
            }

            textInput.setHint(hint);



            int inputTypeCode = InputType.TYPE_CLASS_TEXT;
            if ("email".equals(inputType)) {
                inputTypeCode = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
            } else if ("password".equals(inputType)) {
                inputTypeCode = InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }


            textInput.setInputType(inputTypeCode);
            if (inputTypeCode == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                textInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        } else {
            String currentTheme = SetActivityTheme.getTheme(getContext());
            if(currentTheme.equals("light")){
                final ContextThemeWrapper layoutTheme = new ContextThemeWrapper(context, R.style.LightTheme);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LayoutInflater localInflater = inflater.cloneInContext(layoutTheme);
                localInflater.inflate(R.layout.textinput, this, true);

            }else{
                final ContextThemeWrapper layoutTheme = new ContextThemeWrapper(context, R.style.DarkTheme);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LayoutInflater localInflater = inflater.cloneInContext(layoutTheme);
                localInflater.inflate(R.layout.textinput, this, true);
            }
        }
        EditText textInput = findViewById(R.id.textInput);
        textInput.setOnFocusChangeListener(focusChangeListener);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(themeReceiver, new IntentFilter("theme-updated"));

    }

    public String getText() {
        EditText textInput = findViewById(R.id.textInput);
        return textInput.getText().toString();
    }

    public TextInputDelegate delegate;

    private View.OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (delegate != null) {
                delegate.onFocusChange(view, b);
            }
        }
    };

    private BroadcastReceiver themeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setup(context, null);
        }
    };
}

