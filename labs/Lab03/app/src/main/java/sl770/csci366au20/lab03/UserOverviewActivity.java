package sl770.csci366au20.lab03;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserOverviewActivity extends AppCompatActivity {
    public static final int SUB_ACTIVITY_CREATE_USER = 10;
    private static final int REQUEST_LEARN_MATH = 20;
    int skillLevel = 0;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_overview);

        boolean userExists=false;
        // if no user found, create a new one
        if (!userExists){

            Intent intent = new Intent(this, CreateUserActivity.class);
            startActivityForResult(intent, SUB_ACTIVITY_CREATE_USER);
        }
    }

    // This is the callback for the started sub-activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SUB_ACTIVITY_CREATE_USER && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                String name = extras.getString(User.USER_NAME);
                boolean gender = extras.getBoolean(User.USER_GENDER);
                user = new User(name, gender);
                updateUserInterface();
            }
        }
        else if (resultCode == RESULT_OK && requestCode == REQUEST_LEARN_MATH) {

            if (data.hasExtra(User.USER_SKILL_POINTS)) {
                int result = data.getExtras().getInt(User.USER_SKILL_POINTS);
                user.skillPoints = result;
                System.out.println(result);
                Toast.makeText(this, "New Skill level " + String.valueOf(result), Toast.LENGTH_SHORT).show();
                updateUserInterface();
            }
        }
    }


    public void onClick(View view) {
        Intent i = new Intent(this, LearnActivity.class);
        i.putExtra(User.USER_SKILL_POINTS, user.skillPoints);
        System.out.println(user.skillPoints);
        startActivityForResult(i, REQUEST_LEARN_MATH);
    }
    private void updateUserInterface() {
        TextView gender = (TextView) findViewById(R.id.gender);
        TextView name = (TextView) findViewById(R.id.username);
        TextView textViewLevel = (TextView) findViewById(R.id.level);
        name.setText(user.name);
        if(user.gender == true){
            gender.setText("Male");
        }
        else{
            gender.setText("Female");
        }
       textViewLevel.setText(String.valueOf(user.skillPoints));
    }

}