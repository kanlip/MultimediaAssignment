package sl770.csci366au20.lab03;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LearnActivity extends Activity implements View.OnClickListener {

    TextView textView;
    int skillLevel;
    String value;
    Random random = new Random();
    int number1 =random.nextInt(10);
    int number2 =random.nextInt(10);
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        List<Integer> buttons = Arrays.asList(R.id.one, R.id.two, R.id.three,
                R.id.four, R.id.five, R.id.six, R.id.seven,
                R.id.eight, R.id.nine, R.id.zero, R.id.delete);
        textView = (TextView) findViewById(R.id.math);
        textView.setText(number1 + " * "+number2+" = ");
        skillLevel = getIntent().getExtras().getInt(User.USER_SKILL_POINTS);
        for(Integer i: buttons) {
            View b = findViewById(i);
            b.setOnClickListener(this); // calling onClick() method

        }
        value= "";
    }

    public void onClick(View view) {
        String result =number1 + " * "+number2+" = ";
        switch (view.getId()) {
            case R.id.one:
                value += "1";
                break;
            case R.id.two:
                value += "2";
                break;
            case R.id.three:
                value += "3";
                break;
            case R.id.four:
                value += "4";
                break;
            case R.id.five:
                value += "5";
                break;
            case R.id.six:
                value += "6";
                break;
            case R.id.seven:
                value += "7";
                break;
            case R.id.eight:
                value += "8";
                break;
            case R.id.nine:
                value += "9";
                break;
            case R.id.zero:
                value += "0";
                break;
            case R.id.delete:
                System.out.println(value.length());
                if(value.length() >= 2){
                    value = value.substring(0,value.length()-1);
                }else{
                    value = "";
                }
                break;
            // TODO your logic to evaluate the indivual button
        }
        textView.setText(result.concat(value));
        if(!value.isEmpty() && Integer.parseInt(value) == (number1 * number2)){
            finish();
        }
    }
    @Override
    public void finish() {

        Intent intent = new Intent();

        skillLevel = skillLevel + 5;
        intent.putExtra(User.USER_SKILL_POINTS, skillLevel);

        setResult(RESULT_OK, intent);

        super.finish();
    }
}