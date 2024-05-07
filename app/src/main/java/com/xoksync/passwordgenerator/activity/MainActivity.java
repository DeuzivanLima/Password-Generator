package com.xoksync.passwordgenerator.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.xoksync.passwordgenerator.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private SeekBar seekbar_password_length;
    private TextView textview_password_length;
    private CheckBox checkbox_uppercase, checkbox_lowercase, checkbox_numbers, checkbox_symbols;
    private EditText edittext_password_result;
    private Button button_regenerate, button_copy;
    int password_length = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findAllView();
        setPasswordLength(password_length);
        checkboxEvent();
        updatePasswordLength();
        buttonsEvent();
    }

    private void findAllView() {
        seekbar_password_length = findViewById(R.id.seekbar_password_length);
        textview_password_length = findViewById(R.id.textview_password_length);
        checkbox_uppercase = findViewById(R.id.checkbox_uppercase);
        checkbox_lowercase = findViewById(R.id.checkbox_lowercase);
        checkbox_numbers = findViewById(R.id.checkbox_numbers);
        checkbox_symbols = findViewById(R.id.checkbox_symbols);
        edittext_password_result = findViewById(R.id.edittext_password_result);
        button_regenerate = findViewById(R.id.button_regenerate);
        button_copy = findViewById(R.id.button_copy);
    }

    private void buttonsEvent() {
        button_regenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regeneratePassword();
            }
        });

        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edittext_password_result.getText().toString();

                if(!content.isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("password generator", content);

                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(MainActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkboxEvent() {
        checkbox_lowercase.setChecked(true);
    }

    private void updatePasswordLength() {
        seekbar_password_length.setMax(256);

        seekbar_password_length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setPasswordLength(progress);
                regeneratePassword();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setPasswordLength(int length) {
        password_length = length;
        textview_password_length.setText(String.format("%s %s", getResources().getText(R.string.textview_password_length), String.valueOf(password_length)));
    }

    private void regeneratePassword() {
        if(password_length != 0 && (checkbox_lowercase.isChecked() || checkbox_uppercase.isChecked() || checkbox_numbers.isChecked() || checkbox_symbols.isChecked())) {
            edittext_password_result.setText(generatePassword());
        } else if (password_length == 0) {
            edittext_password_result.setText("Nothing to generate with 0 length...");
        } else {
            edittext_password_result.setText("Please, select some option...");
        }
    }

    private String generatePassword() {
        String ascii_to_use = "";

        if(checkbox_uppercase.isChecked()) {
            ascii_to_use += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        if(checkbox_lowercase.isChecked()) {
            ascii_to_use += "abcdefghijklmnopqrstuvwxyz";
        }
        if(checkbox_numbers.isChecked()) {
            ascii_to_use += "0123456789";
        }
        if(checkbox_numbers.isChecked()) {
            ascii_to_use += "!@#$%^&*";
        }

        ascii_to_use = mixString(ascii_to_use);

        char[] buffer = new char[password_length];
        int bound = ascii_to_use.length();
        Random rand = new Random(System.currentTimeMillis());

        for(int i = 0; i < password_length; i++) {
            int index = rand.nextInt(bound);
            buffer[i] = ascii_to_use.charAt(index);
        }

        return new String(buffer);
    }

    private String mixString(String text) {
        Random rand = new Random(System.currentTimeMillis());
        char[] text_array = text.toCharArray();

        for(int i = 0; i < text.length(); i++) {
            int index = rand.nextInt(text.length());
            while(index == i) {
                index = rand.nextInt(text.length());
            }
            char buffer = text_array[i];
            text_array[i] = text_array[index];
            text_array[index] = buffer;
        }

        return new String(text_array);
    }
}