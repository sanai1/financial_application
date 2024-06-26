package com.example.financial_application.activity;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.example.financial_application.ConnectRealtimeDatabase;
import com.example.financial_application.DBHelper;
import com.example.financial_application.R;
import com.example.financial_application.authorization.AuthorizationActivity;
import com.example.financial_application.databinding.ActivityMainBinding;
import com.example.financial_application.databinding.AddCategoryBinding;
import com.example.financial_application.dialog_fragment.CategoryDialog;
import com.example.financial_application.users.Category;
import com.example.financial_application.users.History;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements CategoryDialog.DialogListenerAdd {
    ActivityMainBinding binding_activity_main;
    AddCategoryBinding binding_add_category;
    protected DBHelper dbHelper;
    protected SQLiteDatabase database;
    protected boolean expense_main = true;
    protected CategoryDialog dialog_category;
    protected Calendar calendar = Calendar.getInstance();
    protected String[] mas_name_category_expense = new String[50];
    protected String[] mas_name_category_income = new String[50];
    public static int count_category = 0;
    private DatabaseReference root;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding_activity_main = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding_activity_main.getRoot());

        binding_activity_main.includeMenu.textViewInfo.setText("Бюджет");
        binding_activity_main.navigatorViewId.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_main.drawerLayoutId.close();
                } else if (id == R.id.nav_goal){
                    binding_activity_main.drawerLayoutId.close();
                    Intent intent = new Intent(MainActivity.this, GoalActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_history) {
                    binding_activity_main.drawerLayoutId.close();
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_report) {
                    binding_activity_main.drawerLayoutId.close();
                    Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_categories) {
                    binding_activity_main.drawerLayoutId.close();
                    Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_feedback) {
                    binding_activity_main.drawerLayoutId.close();
                    Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_exit) {
                    binding_activity_main.drawerLayoutId.close();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, AuthorizationActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        dbHelper = new DBHelper(this);
        setInitialDate();
        dialog_category = new CategoryDialog();
        dialog_category.setMyDialogListener(this);

        root = FirebaseDatabase.getInstance().getReference().getRoot();
        firebaseAuth = FirebaseAuth.getInstance();

        get_mas_expense();
    }

    private void get_mas_expense() {
        database = dbHelper.getWritableDatabase();

        String command_expense = "select category_t_c from category where expense=1";
        String command_income = "select category_t_c from category where expense=0";
        Cursor cursor_expense = database.rawQuery(command_expense, null);
        Cursor cursor_income = database.rawQuery(command_income, null);

        String[] mas_test_expense = new String[50];
        String[] mas_test_income = new String[50];

        int ind_name_category_in_cursor = 0;
        while (cursor_expense.moveToNext())
            mas_test_expense[ind_name_category_in_cursor++] = cursor_expense.getString(0);
        cursor_expense.close();

        ind_name_category_in_cursor = 0;
        while (cursor_income.moveToNext())
            mas_test_income[ind_name_category_in_cursor++] = cursor_income.getString(0);
        cursor_income.close();

        int len_mas_expense = 0, len_mas_income = 0;
        for (int i = 0; mas_test_expense[i] != null; i++)
            len_mas_expense++;
        for (int i = 0; mas_test_income[i] != null; i++)
            len_mas_income++;
        mas_name_category_expense = new String[len_mas_expense];
        mas_name_category_income = new String[len_mas_income];
        for (int i = 0; i < mas_name_category_expense.length; i++)
            mas_name_category_expense[i] = mas_test_expense[i];
        for (int i = 0; i < mas_name_category_income.length; i++)
            mas_name_category_income[i] = mas_test_income[i];

        count_category = len_mas_expense + len_mas_income;

        ArrayAdapter<String> adapter;
        if (binding_activity_main.buttonExpense.isActivated()) {
            adapter = new ArrayAdapter(this, R.layout.spinner_item, mas_name_category_income);
        } else {
            adapter = new ArrayAdapter(this, R.layout.spinner_item, mas_name_category_expense);
        }

        adapter.setDropDownViewResource(R.layout.spinner_item);
        binding_activity_main.spinner.setAdapter(adapter);
    }

    public void income(View view) {
        binding_activity_main.buttonIncome.setEnabled(false);
        binding_activity_main.buttonExpense.setEnabled(true);
        binding_activity_main.buttonIncome.setBackgroundColor(ContextCompat.getColor(this, R.color.light_sky_blue));
        binding_activity_main.buttonIncome.setTextColor(ContextCompat.getColor(this, R.color.gray));
        binding_activity_main.buttonExpense.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        binding_activity_main.buttonExpense.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding_activity_main.checkBoxBidPurchase.setChecked(false);
        binding_activity_main.checkBoxBidPurchase.setText("разовый доход");
        expense_main = false;

        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_item, mas_name_category_income);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        binding_activity_main.spinner.setAdapter(adapter);

        Toast.makeText(this, "Доход", Toast.LENGTH_SHORT).show();
    }

    public void expense(View view) {
        binding_activity_main.buttonIncome.setEnabled(true);
        binding_activity_main.checkBoxBidPurchase.setEnabled(true);
        binding_activity_main.checkBoxBidPurchase.setChecked(false);
        binding_activity_main.buttonIncome.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        binding_activity_main.buttonIncome.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding_activity_main.buttonExpense.setBackgroundColor(ContextCompat.getColor(this, R.color.light_sky_blue));
        binding_activity_main.buttonExpense.setTextColor(ContextCompat.getColor(this, R.color.gray));
        binding_activity_main.checkBoxBidPurchase.setText("крупная покупка");
        binding_activity_main.buttonExpense.setEnabled(false);
        expense_main = true;

        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_item, mas_name_category_expense);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        binding_activity_main.spinner.setAdapter(adapter);

        Toast.makeText(this, "Расход", Toast.LENGTH_SHORT).show();
    }

    public void add_category_in_mainactivity(View view) {
        dialog_category.show(getSupportFragmentManager(), "dialogCategory");
    }
    @Override
    public void onDialogClickListener(String name_category, int expense) {
        binding_add_category = AddCategoryBinding.inflate(getLayoutInflater());
        database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String uuid = UUID.randomUUID().toString();
        contentValues.put(DBHelper.COLUMN_CATEGORY_ID, uuid);
        contentValues.put(DBHelper.COLUMN_EXPENSE, expense);
        contentValues.put(DBHelper.COLUMN_CATEGORY_T_C, name_category);

        database.insert(DBHelper.TABLE_CATEGORY, null, contentValues);

        // ---- добавление данных в Firebase
        Category category = new Category(uuid, name_category, expense);
        ConnectRealtimeDatabase.getInstance(this).saveCategory(firebaseAuth.getCurrentUser().getUid(), category);
        // --- данные добавлены

        if (expense == 1) {
            String[] mas_test_expense = new String[mas_name_category_expense.length + 1];
            for (int i = 0; i < mas_name_category_expense.length; i++)
                mas_test_expense[i] = mas_name_category_expense[i];
            mas_test_expense[mas_test_expense.length - 1] = name_category;
            mas_name_category_expense = mas_test_expense;
            if (expense_main) {
                ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_item, mas_name_category_expense);
                adapter.setDropDownViewResource(R.layout.spinner_item);
                binding_activity_main.spinner.setAdapter(adapter);
            }
        } else {
            String[] mas_test_income = new String[mas_name_category_income.length + 1];
            for (int i = 0; i < mas_name_category_income.length; i++)
                mas_test_income[i] = mas_name_category_income[i];
            mas_test_income[mas_test_income.length - 1] = name_category;
            mas_name_category_income = mas_test_income;
            if (!expense_main) {
                ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_item, mas_name_category_income);
                adapter.setDropDownViewResource(R.layout.spinner_item);
                binding_activity_main.spinner.setAdapter(adapter);
            }
        }
        Toast.makeText(this, "Категория добавлена", Toast.LENGTH_SHORT).show();
    }

    public void update_data(View view) {
        new DatePickerDialog(MainActivity.this, onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void setInitialDate() {
        binding_activity_main.textViewDate.setText(DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };

    public void menu(View view) {
        binding_activity_main.drawerLayoutId.openDrawer(GravityCompat.START);
    }

    public void save_expense(View view) {
        if (binding_activity_main.editTextNumberSum.getText().length() != 0) {
            database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            String command_get_category = "select " + DBHelper.COLUMN_CATEGORY_ID + " from " + DBHelper.TABLE_CATEGORY +
                    " where " + DBHelper.COLUMN_CATEGORY_T_C + " = '" + binding_activity_main.spinner.getSelectedItem().toString() + "'";
            Cursor cursorUid = database.rawQuery(command_get_category, null);
            cursorUid.moveToNext();
            String category_id = cursorUid.getString(0);
            cursorUid.close();
            contentValues.put(DBHelper.COLUMN_CATEGORY_UID, category_id);

            Integer is_big_purchase;
            if (binding_activity_main.checkBoxBidPurchase.isChecked()) {
                is_big_purchase = 1;
            } else {
                is_big_purchase = 0;
            }
            contentValues.put(DBHelper.COLUMN_IS_BIG_PURCHASE, is_big_purchase);

            double sum = Double.parseDouble(binding_activity_main.editTextNumberSum.getText().toString());
            contentValues.put(DBHelper.COLUMN_SUMMA, sum);

            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            if (month.length() == 1) {
                month = "0" + month;
            }
            if (day.length() == 1) {
                day = "0" + day;
            }
            String date = day + "." + month + "." + year;
            contentValues.put(DBHelper.COLUMN_ADD_DATA, date);
            String uid = UUID.randomUUID().toString();
            String comment = binding_activity_main.editTextComment.getText().toString();
            contentValues.put(DBHelper.COLUMN_UID, uid);
            contentValues.put(DBHelper.COLUMN_COMMENT, comment);
            database.insert(DBHelper.TABLE_HISTORY, null, contentValues);

            // добавление данных в Firebase
            History history = new History(uid, is_big_purchase, sum, date, category_id, comment);
            ConnectRealtimeDatabase.getInstance(this).saveHistory(firebaseAuth.getCurrentUser().getUid(), history);
            // данные добавлены

            binding_activity_main.editTextNumberSum.setText("");
            binding_activity_main.checkBoxBidPurchase.setChecked(false);
            binding_activity_main.editTextComment.setText("");
            Toast.makeText(this, "Запись сохранена", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Проверьте введеные данные", Toast.LENGTH_SHORT).show();
        }
    }

}