package ru.myitschool.vsu2021.tribunskikh_a_o.cookbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListOfRecipiesActivity extends AppCompatActivity {
    private static class RecipesAdapter extends BaseAdapter {
        private ListOfRecipiesActivity activity;
        private List <Recipe> data = new ArrayList<>();
        private LayoutInflater lInflater;

        public RecipesAdapter(ListOfRecipiesActivity activity) {
            this.activity = activity;
            lInflater = (LayoutInflater) activity.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        }
        public void refresh (List <Recipe> newdata) {
            data.clear();
            data.addAll(newdata);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return data.get(position) .getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.recipe_list_item, parent, false);
            }
            Recipe r = data.get(position);
            TextView nameTV = view.findViewById(R.id.recipe_name);
            nameTV.setText(r.getName());
            return view;
        }
    };
    private ListView recipesLV;
    private RecipesAdapter adapter;
    public static final int RECIPE_SHOW_RESULT = 29;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_recipies);
        recipesLV = findViewById(R.id.recepies_list_lv) ;
        adapter = new RecipesAdapter(this);
        recipesLV.setAdapter(adapter);
        new LoadRecipies(this).execute();
        recipesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent a = new Intent(ListOfRecipiesActivity.this, ShowRecipeActivity.class);
                a.putExtra(ShowRecipeActivity.RECIPE_ID_ARG, id);
                startActivityForResult(a, RECIPE_SHOW_RESULT);
            }
        });


        FloatingActionButton fab = findViewById(R.id.add_recipe_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(ListOfRecipiesActivity.this);
                new AlertDialog.Builder(ListOfRecipiesActivity.this)
                .setView(et)
                .setTitle("Создание рецепта")
                .setMessage("Введите название нового рецепта")
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton("Окей", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = et.getText().toString();
                        if ("".equals(name.trim())) {
                            Toast.makeText(ListOfRecipiesActivity.this, "Введите название", Toast.LENGTH_LONG).show();
                            return;
                        }
                        new InsertRecipeTask(ListOfRecipiesActivity.this, name).execute();
                        dialog.dismiss();
                    }
                }).create().show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RECIPE_SHOW_RESULT) {
            new ListOfRecipiesActivity.LoadRecipies(this).execute();
        }
    }

    private static class LoadRecipies extends AsyncTask<Void, Void,  ArrayList<Recipe>> {
        private ListOfRecipiesActivity activity;

        public LoadRecipies(ListOfRecipiesActivity activity) {
            this.activity = activity;
        }

        @Override
        protected  ArrayList<Recipe> doInBackground(Void... voids) {
            CookbookStorage db = new CookbookStorage(activity);
            ArrayList<Recipe> r = db.selectAllRecipe();
            return r;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
            activity.adapter.refresh(recipes);
        }
    }
    private static class InsertRecipeTask extends AsyncTask<Void, Void, Void> {

        private ListOfRecipiesActivity activity;
        private String recipeName;

        public InsertRecipeTask(ListOfRecipiesActivity activity, String recipeName) {
            this.activity = activity;
            this.recipeName = recipeName;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            new LoadRecipies(activity).execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            CookbookStorage db = new CookbookStorage(activity);
            db.insert(recipeName, "");

            return null;
        }
    }

    public class RecipesShowAdapter {
    }
}
