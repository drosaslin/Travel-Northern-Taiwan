package com.example.ahm12.anychart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Pie;
import com.anychart.chart.common.dataentry.ValueDataEntry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText shoppingEditText, foodEditText, accommodationEditText, souvenirEditText, ticketEditText;
    TextView shoppingTextView, foodTextView, accommodationTextView, souvenirTextView, ticketTextView;
    int shopping, food, accommodation, souvenir, ticket;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shoppingEditText = (EditText)findViewById(R.id.shopping_EditText);
        foodEditText = (EditText)findViewById(R.id.food_EditText);
        accommodationEditText = (EditText)findViewById(R.id.accommodation_EditText);
        souvenirEditText = (EditText)findViewById(R.id.souvenir_EditText);
        ticketEditText = (EditText)findViewById(R.id.ticket_EditText);

        submitBtn = (Button)findViewById(R.id.submit_Button);



        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                shopping = (shoppingEditText.getText().toString() != null && shoppingEditText.getText().toString() != "") ? Integer.valueOf(shoppingEditText.getText().toString()):0;
                food = (foodEditText.getText().toString() != null && foodEditText.getText().toString() != "") ? Integer.valueOf(foodEditText.getText().toString()):0;
                accommodation = (accommodationEditText.getText().toString() != null && accommodationEditText.getText().toString() != "") ? Integer.valueOf(accommodationEditText.getText().toString()):0;
                souvenir = (souvenirEditText.getText().toString() != null && souvenirEditText.getText().toString() != "") ? Integer.valueOf(souvenirEditText.getText().toString()):0;
                ticket = (ticketEditText.getText().toString() != null && ticketEditText.getText().toString() != "") ? Integer.valueOf(ticketEditText.getText().toString()):0;
                AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
                Pie pie = AnyChart.pie();
                List<DataEntry> data = new ArrayList<>();
                data.add(new ValueDataEntry("Shopping", shopping));
                data.add(new ValueDataEntry("Shopping", food));
                data.add(new ValueDataEntry("Accommodation", accommodation));
                data.add(new ValueDataEntry("Souvenir", souvenir));
                data.add(new ValueDataEntry("Ticket", ticket));

                pie.data(data);
                anyChartView.setChart(pie);
            }
        });



        /*List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Food", 10000));
        data.add(new ValueDataEntry("Shopping", 12000));
        data.add(new ValueDataEntry("Accommodation", 18000));
        data.add(new ValueDataEntry("Ticket", 18000));
        data.add(new ValueDataEntry("Souvenir", 18000));*/


    }
}
