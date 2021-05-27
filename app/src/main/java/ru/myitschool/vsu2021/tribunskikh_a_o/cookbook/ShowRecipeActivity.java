package ru.myitschool.vsu2021.tribunskikh_a_o.cookbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ShowRecipeActivity extends AppCompatActivity {
    private TextView name_tv;
    private TextView description_tv;

    private ListView ingridientLV;
    private IngridientsInRecipeAdapter adapter;
    public static final int RECIPE_EDIT_RESULT = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);
        name_tv = findViewById(R.id.name_of_recipe_tv);
        description_tv = findViewById(R.id.recipe_description_tv);
        ingridientLV = findViewById(R.id.recipe_ingridients_lv);
        adapter = new IngridientsInRecipeAdapter(this);
        ingridientLV.setAdapter(adapter);
        FloatingActionButton goToListOfRecepies=findViewById(R.id.back_to_list_of_recepies_b);
        goToListOfRecepies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton editRecipeB = findViewById(R.id.edit_recipe_b);
        editRecipeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r=new Intent(ShowRecipeActivity.this, EditRecipeActivity.class);
                r.putExtra(RECIPE_ID_ARG, recipeId);
                startActivityForResult(r, RECIPE_EDIT_RESULT);
            }
        });

        Intent f = getIntent();
        recipeId = f.getLongExtra(RECIPE_ID_ARG, 0);
        new LoadRecipe(this, recipeId).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECIPE_EDIT_RESULT) {
            if (resultCode == RESULT_OK) {
                new LoadRecipe(this, recipeId).execute();
            } else if (resultCode == EditRecipeActivity.RESULT_RECIPE_DELETE) {
                finish();
            }
        }
    }

    private long recipeId;
    public static final String RECIPE_ID_ARG = "recipeId";

    private void refreshRecipe(Recipe r) {
        name_tv.setText(r.getName());
        description_tv.setText(r.getDescription());
        new LoadIngridients(this, recipeId).execute();
    }


    private static class LoadRecipe extends AsyncTask<Void, Void, Recipe> {
        private ShowRecipeActivity activity;

        public LoadRecipe(ShowRecipeActivity activity, long id) {
            this.activity = activity;
            this.id = id;
        }

        private long id;


        @Override
        protected Recipe doInBackground(Void... voids) {
            CookbookStorage db = new CookbookStorage(activity);
            Recipe r = db.selectRecipe(id);
            return r;
        }

        @Override
        protected void onPostExecute(Recipe recipes) {
            activity.refreshRecipe(recipes);
        }
    }
    private static class IngridientsInRecipeAdapter extends BaseAdapter {
        private ShowRecipeActivity activity;
        private List<IngridientWithQuantity> data = new ArrayList<>();
        private LayoutInflater lInflater;

        public IngridientsInRecipeAdapter (ShowRecipeActivity activity) {
            this.activity = activity;
            lInflater = (LayoutInflater) activity.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        }
        public void refresh (List <IngridientWithQuantity> newdata) {
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
            return data.get(position).getFood().getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.ingridient_list_item, parent, false);
            }
            IngridientWithQuantity r = data.get(position);
            TextView nameTV = view.findViewById(R.id.name_of_ingridient_tv);
            TextView quantityTV = view.findViewById(R.id.quantity_of_ingridient_tv);
            nameTV.setText(r.getFood().getName());
            quantityTV.setText(r.getQuantity());
            return view;
        }
    }



    private void refreshIngridients(List<IngridientWithQuantity> i) {
        adapter.refresh(i);

    }
    private static class LoadIngridients extends AsyncTask<Void, Void, List<IngridientWithQuantity>> {
        private ShowRecipeActivity activity;

        public LoadIngridients(ShowRecipeActivity activity, long id) {
            this.activity = activity;
            this.id = id;
        }
        private long id;


        @Override
        protected List<IngridientWithQuantity> doInBackground(Void... voids) {
            CookbookStorage db = new CookbookStorage(activity);
            List <IngridientInRecipe> d = db.selectAllIngridientInRecipe(id);
            List<IngridientWithQuantity> i = new ArrayList<>();
            for (IngridientInRecipe f : d) {
                Ingridient g = db.selectIngridient(f.getIngridientId());
                i.add(new IngridientWithQuantity(g, f.getQuantity(), f.getId()));
            }
            return i;
        }

        @Override
        protected void onPostExecute(List<IngridientWithQuantity> recipes) {
            activity.refreshIngridients(recipes);
        }
    }
}


