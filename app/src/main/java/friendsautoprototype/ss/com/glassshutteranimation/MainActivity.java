package friendsautoprototype.ss.com.glassshutteranimation;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


public class MainActivity extends FragmentActivity {

    private RelativeLayout relativeLayout;

    private Button btnanimate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = (RelativeLayout)findViewById(R.id.container);
        relativeLayout.setDrawingCacheEnabled(true);
        relativeLayout.buildDrawingCache();
        relativeLayout.buildDrawingCache(true);

        btnanimate = (Button)findViewById(R.id.btnanimate);
        final GlassShatteringAnimator animator = new GlassShatteringAnimator(relativeLayout);

        btnanimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animator.start();

            }
        });
    }


}
