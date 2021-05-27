package ru.myitschool.vsu2021.tribunskikh_a_o.cookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button goToRecipeList=findViewById(R.id.go_to_recipe_list);
        goToRecipeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MainActivity.this, ListOfRecipiesActivity.class);
                startActivity(a);
            }
        });
    }
}
