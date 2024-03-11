package com.example.financial_application;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.financial_application.databinding.ActivityGoalBinding;
import com.example.financial_application.databinding.ActivityGoalStartBinding;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class GoalActivity extends AppCompatActivity {
    protected ActivityGoalStartBinding binding_activity_goal_start;
    protected ActivityGoalBinding binding_activity_goal;
    protected DBHelper dbHelper;
    protected SQLiteDatabase database;
    private boolean start_activity = true;
    private boolean first_version_goal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding_activity_goal_start = ActivityGoalStartBinding.inflate(getLayoutInflater());
        binding_activity_goal = ActivityGoalBinding.inflate(getLayoutInflater());

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        String command_test = "select * from " + DBHelper.TABLE_GOAL;
        Cursor cursor = database.rawQuery(command_test, null);
        start_activity = !cursor.moveToNext();
        cursor.close();

        if (start_activity) {
            create_menu_start();
        } else {
            create_goal();
        }

    }

    protected void onStart() {
        // TODO: добавить обновление таблицы при изменении истории (меняется история -> меняются записи истории капитала)
        super.onStart();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO: выполнение кода в данном потоке не проверено (протестировать)
                if (!start_activity) {
                    Calendar calendar = new GregorianCalendar();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM");
                    String time_now = simpleDateFormat.format(calendar.getTime());

                    String command_write = "select * from " + DBHelper.TABLE_CAPITAL;
                    Cursor cursor_write = database.rawQuery(command_write, null);
                    ArrayList<String> mas_month = new ArrayList<>();
                    ArrayList<Double> mas_capital = new ArrayList<>();

                    int cnt = 0;
                    while (cursor_write.moveToNext()) {
                        mas_month.add(cursor_write.getString(0));
                        mas_capital.add(cursor_write.getDouble(1));
                        cnt++;
                    }
                    cursor_write.close();

                    double now_month = Double.valueOf(time_now);
                    double last_month = (cnt == 0) ? Double.valueOf(mas_month.get(0)) : Double.valueOf(mas_month.get(cnt - 1));

                    if (now_month - last_month > 0.01) {
                        double month;
                        if (now_month % 1 != 0.01) {
                            month = now_month - 0.01;
                        } else {
                            month = now_month - 1 + 0.11;
                        }
                        String command_delta = "select t.*, " +
                                " t.dohod - t.rash as delta " +
                                " from ( " +
                                " select " +
                                " sum((case when " + DBHelper.COLUMN_IS_EXPENSE + " = 1 and " + DBHelper.COLUMN_IS_BIG_PURCHASE + " = 0 then " + DBHelper.COLUMN_SUMMA + " else 0 end)) as rash, " +
                                " sum((case when " + DBHelper.COLUMN_IS_EXPENSE + " = 0 and " + DBHelper.COLUMN_IS_BIG_PURCHASE + " = 0 then " + DBHelper.COLUMN_SUMMA + " else 0 end)) as dohod " +
                                " substr( " + DBHelper.COLUMN_ADD_DATA + " ,4,7) as my " +
                                " from " + DBHelper.TABLE_HISTORY + " h " +
                                " group by substr( " + DBHelper.COLUMN_ADD_DATA + " ,4,7) " +
                                " ) t where my = '" + month + "'";
                        Cursor cursor_delta = database.rawQuery(command_delta, null);
                        cursor_delta.moveToNext();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.COLUMN_MONTH, String.valueOf(month));
                        contentValues.put(DBHelper.COLUMN_CAPITAL_SUM, cursor_delta.getDouble(1));
                        cursor_delta.close();

                        database.insert(DBHelper.TABLE_CAPITAL, null, contentValues);
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    protected void onResume() {
        super.onResume();
        binding_activity_goal.includeMenuGoal.buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding_activity_goal.goalDrawerLayout.openDrawer(GravityCompat.START);
                print();
            }
        });
        binding_activity_goal_start.includeMenu.buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding_activity_goal_start.goalDrawerLayout.openDrawer(GravityCompat.START);
                print();
            }
        });

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String command_calculation_info = "select * from " + DBHelper.TABLE_CALCULATION_INFO;
                Cursor cursor = database.rawQuery(command_calculation_info, null);
                if(cursor.moveToLast()) {
                    String text_add_date = cursor.getString(2);
                    binding_activity_goal.textViewDate.setText("Дата достижения цели: " + text_add_date);
                    String text_ps = "около " + cursor.getInt(1) + " месяцев (по рассчету на " + cursor.getString(0) + ")";
                    binding_activity_goal.textViewPS.setText(text_ps);
                    String percent = cursor.getString(3);
                    if (percent != null) {
                        binding_activity_goal.textViewPercentGoal.setText("Процент выполнения: " + percent + "%");
                    }
                }
                cursor.close();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private String get_percent(double capital_now, double goal_sum) {
        double percent_double = (capital_now / goal_sum) * 100.0;
        String percent = String.valueOf(percent_double);
        try {
            if (percent_double >= 100) {
                percent = percent.substring(0, 5);
            } else {
                percent = percent.substring(0, 4);
            }
        } catch (Exception ex) {
            percent = percent;
        } finally {
            if (Double.valueOf(percent) < 0.01) {
                percent = "менее 0.01";
            }
        }
        return percent;
    }
    public void calculation(View view) {
        String command = "select  t.*," +
                "t.dohod - t.rash as delta " +
                "from ( " +
                "SELECT " +
//                "sum((case when " + DBHelper.COLUMN_IS_BIG_PURCHASE + " = 1 and " + DBHelper.COLUMN_IS_EXPENSE + " = 1 then " + DBHelper.COLUMN_SUMMA_GOAL + " else 0 end)) as rash_kp, " +
                "sum((case when " + DBHelper.COLUMN_IS_EXPENSE + " = 1 and " + DBHelper.COLUMN_IS_BIG_PURCHASE + " = 0 then " + DBHelper.COLUMN_SUMMA_GOAL + " else 0 end)) as rash, " +
                "sum((case when " + DBHelper.COLUMN_IS_EXPENSE + " = 0 and " + DBHelper.COLUMN_IS_BIG_PURCHASE + " = 0 then " + DBHelper.COLUMN_SUMMA_GOAL + " else 0 end)) as dohod, " +
                "substr( " + DBHelper.COLUMN_ADD_DATA + " ,4,7) as my " +
                "from " + DBHelper.TABLE_HISTORY + " as h " +
                "group by substr( " + DBHelper.COLUMN_ADD_DATA + " ,4,7)" +
                ") t order by my";

        Cursor cursor = database.rawQuery(command, null);

        ArrayList<Integer> mas = new ArrayList<>();
        while (cursor.moveToNext()) {
            mas.add(cursor.getInt(3));
        }
        cursor.close();

        String command_capital = "select * from " + DBHelper.TABLE_CAPITAL;
        String command_goal_sum = "select * from " + DBHelper.TABLE_GOAL;

        Cursor cursor_capital = database.rawQuery(command_capital, null);
        Cursor cursor_goal_sum = database.rawQuery(command_goal_sum, null);
        cursor_capital.moveToLast();
        cursor_goal_sum.moveToNext();

        double capital_now = cursor_capital.getDouble(1);
        double sum_capital = capital_now;
        double goal_sum = cursor_goal_sum.getDouble(1);
        cursor_capital.close();
        cursor_goal_sum.close();

        if (mas.size() < 2) {
            binding_activity_goal.textViewDate.setText(R.string.little_information);
            binding_activity_goal.textViewPS.setText("");

            String percent = get_percent(capital_now, goal_sum);
            binding_activity_goal.textViewPercentGoal.setText("Процент выполнения: " + percent + "%");
            return;
        }
        double x_sum = 0, y_sum = 0, x2_sum = 0, xy_sum = 0, n = mas.size();
        for(int i = 0; i < n; i++) {
            x_sum += i+1;
            y_sum += mas.get(i);
            x2_sum += (i+1) * (i+1);
            xy_sum += (i+1) * mas.get(i);
        }
        double k, b;
        k = (n * xy_sum  -  x_sum * y_sum) / (n * x2_sum  -  x_sum * x_sum);
        b = (y_sum  -  k * x_sum) / n;

        int cnt_month = 0;
        String text_goal = "К сожалению для достижения вашей цели понадобиться более 50 лет";

        while (cnt_month < 600){
            if (cnt_month == 0 && sum_capital >= goal_sum){
                text_goal = "Ваша цель уже достигнута!!!";
                break;
            }
            cnt_month++;
            sum_capital += k*cnt_month + b;
            if (sum_capital >= goal_sum) {
                break;
            }
        }

        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM");
        ContentValues contentValues = new ContentValues();
        double time_now = Double.valueOf(format.format(calendar.getTime())), time_finish;

        if (cnt_month > 0 && cnt_month < 1200) {
            long year = cnt_month / 12;
            time_finish = time_now + year;
            time_finish += 0.01 * (cnt_month % 12);
            if (time_finish % 1 > 0.12) {
                double delta_dr = time_finish % 1;
                long delta_cheloe = (long) time_finish;
                double number = delta_dr / 0.12;
                long num = delta_cheloe + (long) number;
                time_finish = num + (number % 1)*0.12;
            }
            String time = String.valueOf(time_finish).substring(0, 7);
            binding_activity_goal.textViewPS.setText("около " + String.valueOf(cnt_month) + " месяцев (по рассчету на " + time_now + ")");
            binding_activity_goal.textViewDate.setText("Дата достижения цели: " + String.valueOf(time));
            contentValues.put(DBHelper.COLUMN_DATE_FINISH, time);

        } else if (cnt_month == 1200){
            binding_activity_goal.textViewPS.setText("");
            binding_activity_goal.textViewDate.setText(text_goal);
            contentValues.put(DBHelper.COLUMN_DATE_FINISH, "более чем через 100 лет");
        } else { // cnt_month = 0
            binding_activity_goal.textViewDate.setText(text_goal);
            contentValues.put(DBHelper.COLUMN_DATE_FINISH, "уже достигнута");
        }
        String percent = get_percent(capital_now, goal_sum);
        binding_activity_goal.textViewPercentGoal.setText("Процент выполнения: " + percent + "%");

        contentValues.put(DBHelper.COLUMN_DATE_CALCULATION, time_now);
        contentValues.put(DBHelper.COLUMN_TEMP_GOAL, cnt_month);

        contentValues.put(DBHelper.COLUMN_PERCENT_DATE, percent);
        database.insert(DBHelper.TABLE_CALCULATION_INFO, null, contentValues);
    }

    public void name_goal(View view) {
        create_menu_start();

        binding_activity_goal_start.textViewCreateGoal.setText("Отредактируйте свою цель");
        binding_activity_goal_start.textViewCreateGoal.setTextSize(23);
        String command_info_goal = "select * from " + DBHelper.TABLE_GOAL;
        Cursor cursor = database.rawQuery(command_info_goal, null);
        cursor.moveToNext();

        String name_goal = cursor.getString(0);
        String summa = cursor.getString(1);
        String start_capital = cursor.getString(2);
        String percent = cursor.getString(3);
        String inflation = cursor.getString(4);
        cursor.close();

        binding_activity_goal_start.editTextTextName.setText(name_goal);
        binding_activity_goal_start.editTextNumberSum.setText(summa);
        binding_activity_goal_start.editTextNumberStartSum.setText(start_capital);
        binding_activity_goal_start.editTextNumberPercent.setText(percent);
        binding_activity_goal_start.editTextNumberInflation.setText(inflation);

        first_version_goal = false;
    }

    private void print() {
        Toast.makeText(this, "Меню", Toast.LENGTH_SHORT).show();
    }

    private void create_menu_start() {
        setContentView(R.layout.activity_goal_start);
        setContentView(binding_activity_goal_start.getRoot());

        binding_activity_goal_start.goalNavigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_goal_start.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Главная", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_goal) {
                    binding_activity_goal_start.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Цель", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_history) {
                    binding_activity_goal_start.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "История", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, HistoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_report) {
                    binding_activity_goal_start.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Отчет", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_categories) {
                    binding_activity_goal_start.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Категории", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, CategoryActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    private void create_goal() {
        setContentView(R.layout.activity_goal);
        setContentView(binding_activity_goal.getRoot());

        String command_info = "select * from " + DBHelper.TABLE_GOAL;
        Cursor cursor_info = database.rawQuery(command_info, null);

        cursor_info.moveToNext();
        binding_activity_goal.textViewNameGoal.setText("Название цели: " + cursor_info.getString(0));
        binding_activity_goal.textViewSumGoal.setText("Сумма цели : " + cursor_info.getInt(1));
        binding_activity_goal.textViewPercentGoal.setText("Процент выполнения: не рассчитан");
        binding_activity_goal.textViewDate.setText("Дата достижения цели: не рассчитана");
        binding_activity_goal.textViewPS.setText("");
        cursor_info.close();

        binding_activity_goal.goalNavigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Главная", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_goal) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Цель", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_history) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "История", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, HistoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_report) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Отчет", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_categories) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Категории", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, CategoryActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    private void update_goal() {
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues_capital = new ContentValues();
        String new_name_goal, new_summa, new_start_capital, new_percent, new_inflation;

        new_name_goal = binding_activity_goal_start.editTextTextName.getText().toString();
        new_summa = binding_activity_goal_start.editTextNumberSum.getText().toString();
        new_start_capital = binding_activity_goal_start.editTextNumberStartSum.getText().toString();
        new_percent = binding_activity_goal_start.editTextNumberPercent.getText().toString();
        new_inflation = binding_activity_goal_start.editTextNumberInflation.getText().toString();

        contentValues.put(DBHelper.COLUMN_NAME, new_name_goal);
        contentValues.put(DBHelper.COLUMN_SUMMA_GOAL, new_summa);
        contentValues.put(DBHelper.COLUMN_START_CAPITAL, new_start_capital);
        contentValues.put(DBHelper.COLUMN_PERCENT, new_percent);
        contentValues.put(DBHelper.COLUMN_INFLATION, new_inflation);

        contentValues_capital.put(DBHelper.COLUMN_CAPITAL_SUM, new_start_capital);

        database.update(DBHelper.TABLE_GOAL, contentValues, DBHelper.COLUMN_GOAL_UID + " = 1", null);
        database.update(DBHelper.TABLE_CAPITAL, contentValues_capital, DBHelper.COLUMN_UID_CAPITAL + " = 1", null);

        Toast.makeText(this, "Цель изменена", Toast.LENGTH_SHORT).show();
    }

    public void save_goal(View view) {
        if (!first_version_goal) {
            update_goal();
            create_goal();
            binding_activity_goal.textViewPercentGoal.setText("Процент выполнения: не рассчитан");
            binding_activity_goal.textViewDate.setText("Дата достижения цели: не рассчитана");
            binding_activity_goal.textViewPS.setText("");
            return;
        }
        first_version_goal = false;
        boolean is_exception = false;
        Integer number_sum = null, number_start_sum = null, number_percent = null, number_inflation;
        try {
            number_sum = Integer.valueOf(String.valueOf(binding_activity_goal_start.editTextNumberSum.getText()));
            number_start_sum = Integer.valueOf(String.valueOf(binding_activity_goal_start.editTextNumberStartSum.getText()));
            number_percent = Integer.valueOf(String.valueOf(binding_activity_goal_start.editTextNumberPercent.getText()));
        } catch (Exception ise) {
            Toast.makeText(this, "Проверьте введенные данные", Toast.LENGTH_SHORT).show();
            is_exception = true;
        }

        String name_goal = binding_activity_goal_start.editTextTextName.getText().toString();
        if (!is_exception && name_goal.length() != 0) {
            try {
                number_inflation = Integer.valueOf(String.valueOf(binding_activity_goal_start.editTextNumberInflation.getText()));
            } catch (Exception ise) {
                number_inflation = 5;
            }

            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            ContentValues contentValues_capital = new ContentValues();

            Calendar calendar = new GregorianCalendar();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM");
            double date_now = Double.valueOf(simpleDateFormat.format(calendar.getTime()));
            if (date_now % 1 != 0.01) {
                date_now -= 0.01;
            } else {
                date_now = date_now - 1 + 0.11;
            }

            contentValues.put(DBHelper.COLUMN_NAME, name_goal);
            contentValues.put(DBHelper.COLUMN_SUMMA_GOAL, number_sum);
            contentValues.put(DBHelper.COLUMN_START_CAPITAL, number_start_sum);
            contentValues.put(DBHelper.COLUMN_PERCENT, number_percent);
            contentValues.put(DBHelper.COLUMN_INFLATION, number_inflation);
            contentValues.put(DBHelper.COLUMN_GOAL_UID, 1);

            contentValues_capital.put(DBHelper.COLUMN_MONTH, String.valueOf(date_now));
            contentValues_capital.put(DBHelper.COLUMN_CAPITAL_SUM, number_start_sum);
            contentValues_capital.put(DBHelper.COLUMN_UID_CAPITAL, 1);

            database.insert(DBHelper.TABLE_GOAL, null, contentValues);
            database.insert(DBHelper.TABLE_CAPITAL, null, contentValues_capital);

            Toast.makeText(this, "Цель добавлена", Toast.LENGTH_SHORT).show();

            create_goal();
        }
    }
}
