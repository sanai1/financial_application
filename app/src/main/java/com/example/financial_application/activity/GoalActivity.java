package com.example.financial_application.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.financial_application.DBHelper;
import com.example.financial_application.R;
import com.example.financial_application.databinding.ActivityGoalBinding;
import com.example.financial_application.databinding.ActivityGoalStartBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class GoalActivity extends AppCompatActivity {
    protected ActivityGoalStartBinding binding_activity_goal_start;
    protected ActivityGoalBinding binding_activity_goal;
    protected DBHelper dbHelper;
    protected SQLiteDatabase database;
    private boolean start_activity = true;
    private boolean first_version_goal = true;
    private Integer number_start_sum = null;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding_activity_goal_start = ActivityGoalStartBinding.inflate(getLayoutInflater());
        binding_activity_goal = ActivityGoalBinding.inflate(getLayoutInflater());

        binding_activity_goal_start.includeMenu.textViewInfo.setText("Цель");
        binding_activity_goal.includeMenuGoal.textViewInfo.setText("Цель");

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        String command_test = "select * from " + DBHelper.TABLE_GOAL;
        Cursor cursor = database.rawQuery(command_test, null);
        start_activity = !cursor.moveToNext();
        cursor.close();

        if (start_activity) {
            create_menu_start();
            binding_activity_goal_start.textViewInfoUpdateGoal.setText("");
        } else {
            create_menu();
        }
    }
    private String getCommand() {
        String command = "select  t.*, " +
                "t.dohod - t.rash as delta " +
                "from ( " +
                "SELECT " +
                "sum((case when " + DBHelper.COLUMN_EXPENSE + " = 1 and " + DBHelper.COLUMN_IS_BIG_PURCHASE + " = 0 then " + DBHelper.COLUMN_SUMMA_GOAL + " else 0 end)) as rash, " +
                "sum((case when " + DBHelper.COLUMN_EXPENSE + " = 0 and " + DBHelper.COLUMN_IS_BIG_PURCHASE + " = 0 then " + DBHelper.COLUMN_SUMMA_GOAL + " else 0 end)) as dohod, " +
                "substr( " + DBHelper.COLUMN_ADD_DATA + " ,4,7) as my " +
                "from ( " +
                " select h." + DBHelper.COLUMN_SUMMA + ", h." + DBHelper.COLUMN_IS_BIG_PURCHASE + ", h." + DBHelper.COLUMN_ADD_DATA + ", c." + DBHelper.COLUMN_EXPENSE +
                " from " + DBHelper.TABLE_HISTORY + " h " +
                " left join " + DBHelper.TABLE_CATEGORY + " c on c." + DBHelper.COLUMN_CATEGORY_ID + " = " + DBHelper.COLUMN_CATEGORY_UID + ") " +
                "group by substr( " + DBHelper.COLUMN_ADD_DATA + " ,4,7)" +
                ") t ";

        return command;
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
                        String command = getCommand() + " where my = '" + month + "'";

                        Cursor cursor_delta = database.rawQuery(command, null);
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
                if (!start_activity) {
                    String command_calculation_info = "select * from " + DBHelper.TABLE_CALCULATION_INFO;
                    Cursor cursor = database.rawQuery(command_calculation_info, null);
                    String percent;
                    Thread thread;
                    if (cursor.moveToLast()) {
                        String text_add_date = cursor.getString(2);
                        binding_activity_goal.textViewDate.setText("Дата достижения цели: " + text_add_date);
                        String text_ps = "около " + cursor.getInt(1) + " месяцев (по рассчету на " + cursor.getString(0) + ")";
                        binding_activity_goal.textViewPS.setText(text_ps);
                        percent = cursor.getString(3);
                        if (percent != null) {
                            binding_activity_goal.textViewPercentGoal.setText("Процент выполнения: " + percent + "%");
                        }
                        List<Double> doubleList = getCoefficients();
                        thread = getThreadUpdate(cursor.getInt(1), doubleList.get(0), doubleList.get(1), doubleList.get(2));
                    } else {
                        thread = getThread();
                    }
                    cursor.close();
                    thread.start();
                }
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

    private List<Double> getCoefficients() {
        List<Double> doubleList = new ArrayList<>();
        String command = getCommand() + " order by my";

        Cursor cursor = database.rawQuery(command, null);

        ArrayList<Integer> mas = new ArrayList<>();
        while (cursor.moveToNext()) {
            mas.add(cursor.getInt(3));
        }
        cursor.close();

        String command_capital = "select * from " + DBHelper.TABLE_CAPITAL;
        Cursor cursor_capital = database.rawQuery(command_capital, null);
        cursor_capital.moveToLast();
        double capital_now = cursor_capital.getDouble(1);
        cursor_capital.close();

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

        doubleList.add(k);
        doubleList.add(b);
        doubleList.add(capital_now);
        doubleList.add((double) mas.size());
        return doubleList;
    }

    public void calculation(View view) {
        List<Double> doubleList = getCoefficients();
        double k = doubleList.get(0), b = doubleList.get(1);
        double capital_now = doubleList.get(2);
        double mas_size = doubleList.get(3);

        String command_goal_sum = "select * from " + DBHelper.TABLE_GOAL;
        Cursor cursor_goal_sum = database.rawQuery(command_goal_sum, null);
        cursor_goal_sum.moveToNext();

        double sum_capital = capital_now;
        double goal_sum = cursor_goal_sum.getDouble(1);
        cursor_goal_sum.close();

        if (mas_size < 2) {
            binding_activity_goal.textViewDate.setText(R.string.little_information);
            binding_activity_goal.textViewPS.setText("");

            String percent = get_percent(capital_now, goal_sum);
            binding_activity_goal.textViewPercentGoal.setText("Процент выполнения: " + percent + "%");
            return;
        }

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

        Thread thread = getThreadUpdate(cnt_month, k, b, capital_now);
        thread.start();
    }

    private void make_chart_start(Double summa) {
        Description description = new Description();
        description.setText("");
        description.setPosition(150f, 15f);

        lineChart.setDescription(description);
        lineChart.getAxisRight().setDrawLabels(false);
        List<String> stringList = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM");
        Double now_date = Double.valueOf(format.format(Calendar.getInstance().getTime())), fan_date = now_date;

        String str;
        for (int i = 0; i < 6; i++) {
            if (fan_date % 1 != 12) {
                fan_date += 0.01;
            } else {
                fan_date += 1;
                fan_date -= 0.12;
            }
            str = String.valueOf(fan_date % 1).substring(3, 4) + "." + String.valueOf(Integer.valueOf((int) (fan_date - (fan_date % 1))));
            if (str.length() < 7) {
                str = "0" + str;
            }
            stringList.add(str);
        }

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(stringList));
        xAxis.setLabelCount(6);
        xAxis.setGranularity(1f);

        YAxis yAxis = lineChart.getAxisLeft();
        double min_sum = summa/2;
        yAxis.setAxisMinimum((float) min_sum);
        double max_sum = summa*2 - min_sum;
        yAxis.setAxisMaximum((float) max_sum);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);

        List<Entry> entryList = new ArrayList<>();
        int sum = (int) (summa - summa % 1);
        for (int i = 0; i < 6; i++) {
            entryList.add(new Entry(i, sum));
        }

        LineDataSet dataSet = new LineDataSet(entryList, "капитал");
        dataSet.setColor(Color.BLUE);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    @NonNull
    private Thread getThreadUpdate(int count_month, double k, double b, double capital_now) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // индексы подписанных точек на оси X
                int[] indx = new int[6];
                indx[0] = 0;
                indx[5] = count_month;
                for (int i = 1;i < 5; i++) {
                    indx[i] = (count_month/5)*i;
                }

                // заполняем массив значениями оси X
                SimpleDateFormat format = new SimpleDateFormat("MM.yyyy");
                Double now_date = Double.valueOf(format.format(Calendar.getInstance().getTime())), fan_date = now_date;
                List<String> stringList = new ArrayList<>();

                String str;
                for (int i = 1; i < count_month; i++) {
                    if ((fan_date - fan_date % 1) != 12) {
                        fan_date += 1;
                    } else {
                        fan_date += 0.0001;
                        fan_date -= 11;
                    }

                    if ((fan_date - fan_date%1) < 10) {
                        str = "0" + String.valueOf(fan_date).substring(0, 6);;
                    } else {
                        str = String.valueOf(fan_date).substring(0, 7);
                    }

                    stringList.add(str);
                }

                // заполняем массив значениями оси Y
                List<Double> doubleArrayList = new ArrayList<>();
                double sum_now = capital_now;
                for (int i = 1; i < count_month; i++) {
                    sum_now += k*i + b;
                    doubleArrayList.add(sum_now);
                }

                make_chart(stringList, doubleArrayList);
            }
        };
        Thread thread = new Thread(runnable);
        return thread;
    }

    private void make_chart(List<String> stringList, List<Double> doubleArrayList) {
        Description description = new Description();
        description.setText("");
        description.setPosition(150f, 15f);

        lineChart.setDescription(description);
        lineChart.getAxisRight().setDrawLabels(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(stringList));
        xAxis.setLabelCount(stringList.size());
        xAxis.setGranularity(1f);

        YAxis yAxis = lineChart.getAxisLeft();
        double sum_min = Collections.min(doubleArrayList);
        double sum_max = Collections.max(doubleArrayList);
        yAxis.setAxisMinimum((float) (sum_min - sum_min/2));
        yAxis.setAxisMaximum((float)(sum_max + sum_min/2));
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);

        List<Entry> entryList = new ArrayList<>();
        for (int i = 0; i < doubleArrayList.size(); i++) {
            entryList.add(new Entry(i, (int) (doubleArrayList.get(i) - doubleArrayList.get(i) % 1)));
        }

        LineDataSet dataSet = new LineDataSet(entryList, "капитал");
        dataSet.setColor(Color.BLUE);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
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
        setContentView(binding_activity_goal_start.getRoot());
        lineChart = findViewById(R.id.line_chart);
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
                } else if (id == R.id.nav_feedback) {
                    binding_activity_goal_start.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Обратная связь", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, FeedbackActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    private void create_menu() {
        setContentView(binding_activity_goal.getRoot());
        lineChart = findViewById(R.id.line_chart);

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
                } else if (id == R.id.nav_feedback) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Обратная связь", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, FeedbackActivity.class);
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
            create_menu();
            binding_activity_goal.textViewPercentGoal.setText("Процент выполнения: не рассчитан");
            binding_activity_goal.textViewDate.setText("Дата достижения цели: не рассчитана");
            binding_activity_goal.textViewPS.setText("");
        } else {
            first_version_goal = false;
            boolean is_exception = false;
            Integer number_sum = null, number_percent = null, number_inflation;
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

                create_menu();
            }
        }
        Thread thread = getThread();
        thread.start();
    }

    @NonNull
    private Thread getThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String command = "select " + DBHelper.COLUMN_START_CAPITAL + " from " + DBHelper.TABLE_GOAL;
                Cursor cursor_goal = database.rawQuery(command, null);
                cursor_goal.moveToNext();
                Double summa = Double.valueOf(cursor_goal.getString(0));
                cursor_goal.close();

                make_chart_start(summa);
            }
        };
        Thread thread = new Thread(runnable);
        return thread;
    }
}
