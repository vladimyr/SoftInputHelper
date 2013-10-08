package co.profi.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;
import co.profi.R;
import co.profi.input.SoftInputHero;

public class MyActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SoftInputHero.from(this).registerSoftInputCallback(new SoftInputHero.SoftInputCallback() {
            @Override
            public void onShow() {
                final Toast msg = Toast.makeText(MyActivity.this, "SoftInput showed!", Toast.LENGTH_SHORT);
                msg.setGravity(Gravity.TOP, 0, 40);
                msg.show();
            }
        });
    }
}
