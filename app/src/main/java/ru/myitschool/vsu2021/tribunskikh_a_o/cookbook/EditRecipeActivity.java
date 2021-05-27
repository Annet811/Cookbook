package ru.myitschool.vsu2021.tribunskikh_a_o.cookbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EditRecipeActivity extends AppCompatActivity {
    private TextView edit_name_of_recipe_tv;
    private TextView edit_recipe_description_tv;

    private ListView edit_recipeLv;
    private EditIngridientInRecipeAdapter adapter;
    private static final int CHOOSE_INGRIDIENT=24;
    private static final int EDIT_DESCRIPTION = 28;
    public static final int RESULT_RECIPE_DELETE = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        edit_name_of_recipe_tv = findViewById(R.id.edit_name_of_recipe_tv);
        edit_recipe_description_tv = findViewById(R.id.edit_name_description_tv);
        edit_recipeLv = findViewById(R.id.edit_recipe_ingridients_lv);
        adapter = new EditIngridientInRecipeAdapter(this);
        edit_recipeLv.setAdapter(adapter);

        Intent f = getIntent();
        recipeId = f.getLongExtra(RECIPE_ID_ARG, 0);
        new EditRecipeActivity.LoadRecipe(this, recipeId).execute();
        Button addIngridientButton = findViewById(R.id.edit_add_ingridient_b);
        addIngridientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(EditRecipeActivity.this, IngridientsActivity.class);
                startActivityForResult(add, CHOOSE_INGRIDIENT);

            }
        });
        FloatingActionButton goHomeFloatingActionButton = findViewById(R.id.edit_go_home_fab);
        goHomeFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageButton reNameButton = findViewById(R.id.edit_add_recipe_ib);
        reNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(EditRecipeActivity.this);
                new AlertDialog.Builder(EditRecipeActivity.this)
                        .setView(et)
                        .setTitle("Переименование рецепта")
                        .setMessage("Введите новое название рецепта")
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Окей", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = et.getText().toString();
                                if ("".equals(name)) {
                                    Toast.makeText(EditRecipeActivity.this, "Нельзя", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } else {
                                    new ReName(EditRecipeActivity.this, recipeId, name).execute();
                                    dialog.dismiss();
                                }
                            }
                        }).create().show();
            }
        });
        FloatingActionButton deleteButton = findViewById(R.id.edit_delete_recipe);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditRecipeActivity.this)
                        .setTitle("Удаление рецепта")
                        .setMessage("Вы действительно хотите удалить рецепт?")
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Окей", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteRecipe(EditRecipeActivity.this, recipeId).execute();
                                dialog.dismiss();

                            }
                        }).create().show();
            }
        });
        ImageButton cd = findViewById(R.id.edit_start_description_ib);
        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o=new Intent(EditRecipeActivity.this, EditRecipeDescriptionActivity.class);
                o.putExtra(EditRecipeDescriptionActivity.PREVIOUS_DESCRIPTION_ARG, edit_recipe_description_tv.getText().toString());
                startActivityForResult(o, EDIT_DESCRIPTION);
            }
        });
        setResult(RESULT_OK);

    }

    private long recipeId;
    public static final String RECIPE_ID_ARG = "recipeId";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CHOOSE_INGRIDIENT) {
            if (resultCode==RESULT_OK) {
                final Long ingridientId = data.getLongExtra(IngridientsActivity.INGRIDIENT_ID_RESULT, 0);
                final EditText et=new EditText(EditRecipeActivity.this);
                new AlertDialog.Builder(EditRecipeActivity.this)
                        .setTitle("Количество ингредиента")
                        .setMessage("Введите количество ингредиента")
                        .setView(et)
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Окей", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ConnectRecipeAndIngridients(EditRecipeActivity.this, ingridientId, et.getText().toString()).execute();
                            }
                        }).create().show();
            }
        }else if (requestCode==EDIT_DESCRIPTION) {
            if (resultCode==RESULT_OK) {
                String descriptionValue = data.getStringExtra(EditRecipeDescriptionActivity.RECIPE_DESCRIPTION_RESULT);
                new ChangeDescription(EditRecipeActivity.this, recipeId, descriptionValue).execute();
            }
        }
    }

    private class EditIngridientInRecipeAdapter extends BaseAdapter {
        private EditRecipeActivity activity;
        private List<IngridientWithQuantity> data = new ArrayList<>();
        private LayoutInflater lInflater;

        public EditIngridientInRecipeAdapter(EditRecipeActivity activity) {
            this.activity = activity;
            lInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void refresh(List<IngridientWithQuantity> newdata) {
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
                view = lInflater.inflate(R.layout.activity_edit_ingridient_list_item, parent, false);
            }
            final IngridientWithQuantity r = data.get(position);
            TextView edit_nameTV = view.findViewById(R.id.dit_name_of_ingridient_tv);
            TextView edit_quantityTV = view.findViewById(R.id.edit_quantity_of_ingridient_tv);

            edit_nameTV.setText(r.getFood().getName());
            edit_quantityTV.setText(r.getQuantity());
            ImageButton quantityButton = view.findViewById(R.id.edit_list_change_recipe_ib);
            quantityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText et = new EditText(activity);
                    new AlertDialog.Builder(activity)
                            .setTitle("Количество ингредиентов")
                            .setView(et)
                            .setMessage("Введите количество ингредиента")
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setPositiveButton("Окей", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String value = et.getText().toString();
                                    if ("".equals(value)) {
                                        Toast.makeText(activity, "Нельзя", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    } else {
                                        new ChangeQuantity(activity, r.getId(), value).execute();
                                        dialog.dismiss();
                                    }
                                }
                            }).create().show();
                }
            });
            ImageButton deleteButton = view.findViewById(R.id.edit_list_delete_ingridient_ib);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(activity)
                            .setTitle("Удаление ингредиента")
                            .setMessage("Вы действительно хотите удалить ингредиент?")
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setPositiveButton("Окей", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new DeleteIngridient(activity, r.getId(), recipeId).execute();
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            });
            return view;
        }
    }

    private void refreshRecipe(Recipe r) {
        edit_name_of_recipe_tv.setText(r.getName());
        edit_recipe_description_tv.setText(r.getDescription());
        new LoadIngridients(this, recipeId).execute();
    }

    private void refreshIngridients(List<IngridientWithQuantity> i) {
        adapter.refresh(i);
    }

    private static class LoadRecipe extends AsyncTask<Void, Void, Recipe> {
        private EditRecipeActivity activity;

        public LoadRecipe(EditRecipeActivity activity, long id) {
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

    private static class LoadIngridients extends AsyncTask<Void, Void, List<IngridientWithQuantity>> {
        private EditRecipeActivity activity;

        public LoadIngridients(EditRecipeActivity activity, long id) {
            this.activity = activity;
            this.id = id;
        }

        private long id;


        @Override
        protected List<IngridientWithQuantity> doInBackground(Void... voids) {
            CookbookStorage db = new CookbookStorage(activity);
            List<IngridientInRecipe> d = db.selectAllIngridientInRecipe(id);
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

    private static class ReName extends AsyncTask<Void, Void, Void> {
        private EditRecipeActivity activity;

        public ReName(EditRecipeActivity activity, long id, String name) {
            this.activity = activity;
            this.id = id;
            this.name = name;
        }

        private long id;
        private String name;

        @Override
        protected Void doInBackground(Void... voids) {
            CookbookStorage db = new CookbookStorage(activity);
            db.reNameRecipe(id, name);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new EditRecipeActivity.LoadRecipe(activity, id).execute();
        }
    }

    private static class ChangeQuantity extends AsyncTask<Void, Void, Void> {
        private EditRecipeActivity activity;

        public ChangeQuantity(EditRecipeActivity activity, long id, String value) {
            this.activity = activity;
            this.id = id;
            this.value = value;
        }

        private long id;
        private String value;

        @Override
        protected Void doInBackground(Void... voids) {
            CookbookStorage db = new CookbookStorage(activity);
            db.changeQuantity(id, value);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new EditRecipeActivity.LoadRecipe(activity, activity.recipeId).execute();
        }
    }

    private static class ChangeDescription extends AsyncTask<Void, Void, Void> {
        private EditRecipeActivity activity;

        public ChangeDescription(EditRecipeActivity activity, long id, String description) {
            this.activity = activity;
            this.id = id;
            this.description = description;
        }

        private long id;
        private String description;

        @Override
        protected Void doInBackground(Void... voids) {
            CookbookStorage db = new CookbookStorage(activity);
            db.changeDescription(id, description);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new EditRecipeActivity.LoadRecipe(activity, id).execute();
        }
    }
    private static class DeleteIngridient extends AsyncTask<Void, Void, Void> {
        private EditRecipeActivity activity;

        public DeleteIngridient(EditRecipeActivity activity, long id, long recipeId) {
            this.activity=activity;
            this.id=id;
            this.recipeId=recipeId;
        }
        private long id;
        private long recipeId;
        @Override
        protected Void doInBackground(Void... voids) {
            CookbookStorage db = new CookbookStorage(activity);
            db.deleteIngridientInRecipe(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new LoadIngridients(activity, recipeId).execute();
        }
    }
    private static class DeleteRecipe extends AsyncTask<Void, Void, Void> {
        private EditRecipeActivity activity;

        public DeleteRecipe(EditRecipeActivity activity, long id) {
            this.activity=activity;
            this.id=id;
        }
        private long id;
        @Override
        protected Void doInBackground(Void... voids) {
            CookbookStorage db = new CookbookStorage(activity);
            db.deleteRecipe(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            activity.setResult(RESULT_RECIPE_DELETE);
            activity.finish();
        }
    }
    private static class ConnectRecipeAndIngridients extends AsyncTask<Void, Void, Void> {
        private EditRecipeActivity activity;

        public ConnectRecipeAndIngridients(EditRecipeActivity activity, long id, String value) {
            this.activity=activity;
            this.id=id;
            this.value=value;
        }
        private long id;
        private String value;
        @Override
        protected Void doInBackground(Void... voids) {
            CookbookStorage db = new CookbookStorage(activity);
            db.addIngridientToRecipe(id, activity.recipeId, value);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new LoadRecipe(activity, activity.recipeId).execute();
        }
    }
}




