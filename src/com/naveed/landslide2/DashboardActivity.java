package com.naveed.landslide2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
 
import com.naveed.landslide2.library.UserFunctions;
 
public class DashboardActivity extends Activity {
    UserFunctions userFunctions;
    Button btnLogout;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        /**
         * Dashboard Screen for the application
         * */       
        // Check login status in database
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
       // user already logged in show databoard
            setContentView(R.layout.dashboard);
            btnLogout = (Button) findViewById(R.id.btnLogout);
           // button1 = (Button) findViewById(R.id.button1);
            
            
            btnLogout.setOnClickListener(new View.OnClickListener() {
                 
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    userFunctions.logoutUser(getApplicationContext());
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    // Closing dashboard screen
                    finish();
                }
            });
            
            //button1.setOnClickListener(new View.OnClickListener() {
				
				//@Override
			//	public void onClick(View v) {
					// TODO Auto-generated method stub
			//		 Intent report = new Intent(getApplicationContext(),ReportActivity.class);
	           //         startActivity(report);
			//	}
		//	});
            
            //This is where the rest of the stuff go
            
            String wlcm = "";
            TextView welcome;
            welcome = (TextView) findViewById(R.id.txtWelcome);
            Bundle extras = getIntent().getExtras();
            if(extras !=null) {
                wlcm = extras.getString("name");
            }
			welcome.setText("Welcome " + wlcm);
             
        }else{
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            finish();
        }        
    }
}