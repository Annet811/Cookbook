package ru.myitschool.vsu2021.tribunskikh_a_o.cookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditRecipeDescriptionActivity extends AppCompatActivity {
    public static final String RECIPE_DESCRIPTION_RESULT = "recipe_description";
    public static final String PREVIOUS_DESCRIPTION_ARG = "previous_description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe_description);
        Intent k = getIntent();
        final EditText et = findViewById(R.id.edit_description_recipe_et);
        et.setText(k.getStringExtra(PREVIOUS_DESCRIPTION_ARG));
        ImageButton sd = findViewById(R.id.edit_description_save_ib);
        sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(RECIPE_DESCRIPTION_RESULT, et.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        ImageButton ghd = findViewById(R.id.edit_description_back_ib);
        ghd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setResult(RESULT_CANCELED);
    }
}
