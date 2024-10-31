package io.github.deuzivanlima.passwordgenerator.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
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
import io.github.deuzivanlima.passwordgenerator.R;
import io.github.deuzivanlima.passwordgenerator.core.RandomString;


public class MainActivity extends AppCompatActivity {
    private SeekBar seekbar_password_length;
    private TextView textview_password_length;
    private CheckBox checkbox_uppercase, checkbox_lowercase, checkbox_numbers, checkbox_symbols;
    private EditText edittext_password_result;
    private Button button_regenerate, button_copy, button_increment_length, button_decrement_length, button_128, button_512, button_1028;
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
        updateSeekbar();
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
        button_128 = findViewById(R.id.button_increment_128);
        button_512 = findViewById(R.id.button_increment_512);
        button_1028 = findViewById(R.id.button_increment_1028);
        button_increment_length = findViewById(R.id.button_increment_length);
        button_decrement_length = findViewById(R.id.button_decrement_length);
    }

    private void updateSeekbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            seekbar_password_length.setProgress(password_length, true);
        } else {
            seekbar_password_length.setProgress(password_length);
        }
    }


    private void buttonsEvent() {
        checkbox_lowercase.setChecked(true);

        button_128.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordLength(128);
                updateSeekbar();
                generatePassword();
            }
        });

        button_512.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordLength(512);
                updateSeekbar();
                generatePassword();
            }
        });

        button_1028.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordLength(1028);
                updateSeekbar();
                generatePassword();
            }
        });

        button_increment_length.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password_length < seekbar_password_length.getMax()) {
                    setPasswordLength(password_length + 1);
                    updateSeekbar();
                    generatePassword();
                }
            }
        });

        button_decrement_length.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password_length > 1)
                {
                    setPasswordLength(password_length - 1);
                    updateSeekbar();
                    generatePassword();
                }
            }
        });

        button_regenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePassword();
            }
        });

        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edittext_password_result.getText().toString();

                if(!content.isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Password Generator", content);

                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(MainActivity.this, "Successfully Copied!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePasswordLength() {
        seekbar_password_length.setMax(1028);

        seekbar_password_length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setPasswordLength(progress);
                generatePassword();
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

    private void generatePassword() {
        if(password_length != 0 && (checkbox_lowercase.isChecked() || checkbox_uppercase.isChecked() || checkbox_numbers.isChecked() || checkbox_symbols.isChecked())) {
            edittext_password_result.setText(generateString());
        } else if (password_length == 0) {
            edittext_password_result.setText("Nothing to generate with 0 length...");
        } else {
            edittext_password_result.setText("Please, select some option...");
        }
    }

    private String generateString() {
        RandomString random_string = new RandomString(password_length);

        return random_string.generate(
                checkbox_lowercase.isChecked(),
                checkbox_numbers.isChecked(),
                checkbox_symbols.isChecked(),
                checkbox_uppercase.isChecked()
        );
    }
}