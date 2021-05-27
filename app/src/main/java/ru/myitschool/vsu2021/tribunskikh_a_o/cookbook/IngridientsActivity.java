package ru.myitschool.vsu2021.tribunskikh_a_o.cookbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class IngridientsActivity extends AppCompatActivity {
    private IngridientsAdapter adapter;
    private ListView ingridients_list_lv;
    public static final String INGRIDIENT_ID_RESULT= "ingridient_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingridients);
        ingridients_list_lv = findViewById(R.id.activity_ingridients_lv);
        adapter = new IngridientsAdapter(this);
        ingridients_list_lv.setAdapter(adapter);
        setResult(RESULT_CANCELED);
        new IngridientsActivity.LoadAllIngridients(this).execute();
        FloatingActionButton b = findViewById(R.id.add_activity_ingridients_b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(IngridientsActivity.this);
                new AlertDialog.Builder(IngridientsActivity.this)
                        .setView(et)
                        .setTitle("Добавление ингредиента")
                        .setMessage("Добавьте ингредиент")
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
                                    Toast.makeText(IngridientsActivity.this, "Нельзя", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } else {
                                    new CreateIngridient(IngridientsActivity.this, name).execute();
                                    dialog.dismiss();
                                }
                            }
                        }).create().show();
            }
        });
        ingridients_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra(INGRIDIENT_ID_RESULT, id);
                    setResult(RESULT_OK, intent);
                    finish();
            }
        });
    }


    private class IngridientsAdapter extends BaseAdapter {
        private IngridientsActivity activity;
        private List<Ingridient> data = new ArrayList<>();
        private LayoutInflater lInflater;

        public IngridientsAdapter(IngridientsActivity activity) {
            this.activity = activity;
            lInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        public void refresh(List<Ingridient> newdata) {
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
            return data.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.ingridients_activity_list_item, parent, false);
            }
            final Ingridient r = data.get(position);
            TextView edit_nameTV = view.findViewById(R.id.name_of_ingridients_activity_tv);

            edit_nameTV.setText(r.getName());
            return view;
        }
    }

    private void refreshIngridients(List<Ingridient> i) {
        adapter.refresh(i);
    }

    private static class LoadAllIngridients extends AsyncTask<Void, Void, List<Ingridient>> {
        private IngridientsActivity activity;

        public LoadAllIngridients(IngridientsActivity activity) {
            this.activity = activity;
        }

        @Override
        protected List<Ingridient> doInBackground(Void... voids) {
            CookbookStorage db = new CookbookStorage(activity);
            List<Ingridient> i = db.selectAllIngridient();
            return i;
        }

        @Override
        protected void onPostExecute(List<Ingridient> ingridients) {
            activity.refreshIngridients(ingridients);
        }
    }

    private static class CreateIngridient extends AsyncTask<Void, Void, Void> {
        private IngridientsActivity activity;
        private String name;

        public CreateIngridient(IngridientsActivity activity, String name) {
            this.activity = activity;
            this.name = name;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            CookbookStorage db = new CookbookStorage(activity);
            db.insert(name);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new LoadAllIngridients(activity).execute();
        }
    }
}