package com.gethighlow.highlowandroid.CustomViews.BaseComponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.gethighlow.highlowandroid.R;

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

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(layout, this, true);
            EditText textInput = findViewById(R.id.textInput);
            String hint = a.getString(R.styleable.TextInput_title);
            String inputType = a.getString(R.styleable.TextInput_inputType);
            textInput.setHint(hint);
            int inputTypeCode = InputType.TYPE_CLASS_TEXT;
            switch(inputType) {
                case "email":
                    inputTypeCode = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
                    break;
                case "password":
                    inputTypeCode = InputType.TYPE_TEXT_VARIATION_PASSWORD;
                    break;
                default:
                    break;
            }


            textInput.setInputType(inputTypeCode);
            if (inputTypeCode == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                textInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.textinput, this, true);
        }
        EditText textInput = findViewById(R.id.textInput);
        textInput.setOnFocusChangeListener(focusChangeListener);
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
}

