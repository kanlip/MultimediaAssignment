package sl770.csci366au20.lab03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateUserActivity extends AppCompatActivity {

    private EditText userName;
    private boolean gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        userName = (EditText) findViewById(R.id.username);
        View viewById = findViewById(R.id.Female);
        viewById.setAlpha(0.4f);
    }
    public void onRadioClick(View view){
        View female = findViewById(R.id.Female);
        View male = findViewById(R.id.Male);
        switch(view.getId()){
            case R.id.Male:
                female.setAlpha(0.4f);
                male.setAlpha(1.0f);
                gender = true;
                break;
            case R.id.Female:
                male.setAlpha(0.4f);
                female.setAlpha(1.0f);
                gender = false;
                break;

        }
    }
    // TODO more code
    public void onClick(View view) {
        finish();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(User.USER_NAME, userName.getText().toString());
        intent.putExtra(User.USER_GENDER, gender);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
