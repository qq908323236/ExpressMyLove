package fu.mr.expressmylove.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fu.mr.expressmylove.R;

public class SelectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
    }

    public void login(View view){

    }

    public void register(View view){
        startActivity(new Intent(this,RegisterActivity.class));
    }
}
