package com.licoforen.calculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity implements OnItemSelectedListener {

	public static String EXTRA_MESSAGE = "com.licoforen.calculator.MESSAGE";
	public int operation = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Spinner spinner = (Spinner) findViewById(R.id.spnSign);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.signs, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void calculate(View view)
	{
		EditText num1 = (EditText) findViewById(R.id.number1);
		EditText num2 = (EditText) findViewById(R.id.number2);
		if(num1.getText().length()==0) num1.setText("0");
		if(num2.getText().length()==0) num2.setText("0");
		
		Intent intent = new Intent(this, DisplayResult.class);
		double result;
		if(operation==0)
			result = Double.parseDouble(num1.getText().toString()) + Double.parseDouble(num2.getText().toString());
		else if(operation==1)
			result = Double.parseDouble(num1.getText().toString()) - Double.parseDouble(num2.getText().toString());
		else if(operation==2)
			result = Double.parseDouble(num1.getText().toString()) * Double.parseDouble(num2.getText().toString());
		else
			result = Double.parseDouble(num1.getText().toString()) / Double.parseDouble(num2.getText().toString());
		intent.putExtra(EXTRA_MESSAGE, result);
		startActivity(intent);
	}

	public void clear(View view)
	{
		EditText num1 = (EditText) findViewById(R.id.number1);
		EditText num2 = (EditText) findViewById(R.id.number2);
		num1.setText("");
		num2.setText("");
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if(parent.getItemAtPosition(pos).equals(parent.getItemAtPosition(0)))
        	operation=0;
        else if(parent.getItemAtPosition(pos).equals(parent.getItemAtPosition(1)))
        	operation=1;
        else if(parent.getItemAtPosition(pos).equals(parent.getItemAtPosition(2)))
        	operation=2;
        else
        	operation=3;
    }
	
	public void onNothingSelected(AdapterView<?> parent) {
        operation=0;
    }
}
