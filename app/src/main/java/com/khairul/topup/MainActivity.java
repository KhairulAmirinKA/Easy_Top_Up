package com.khairul.topup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private Button btnDial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init view
        initViews();

        //handle top up pin input
        handleTopUpPin();


        btnDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone= "*122*"+ editTextPhone.getText().toString() +"#";

                Log.d("PHONE", phone);
                Toast.makeText(MainActivity.this, "phone: "+ phone, Toast.LENGTH_SHORT).show();

                if (!phone.isEmpty()) {

                    //phone +="#";
                    //phone= "*122#"+ phone;

                    // Create an Intent to initiate the phone call
                    Intent intent = new Intent(Intent.ACTION_DIAL);

                    intent.setData(Uri.parse("tel:" + phone));

                    // Check if there's an activity to handle the Intent before starting it
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        // Handle the case where there's no activity to handle the phone call
                        // (e.g., on devices without a dialer app)
                    }
                } else {
                    // Handle the case where the phone number is empty
                }
            }
        });



    }



    private void initViews(){
        editTextPhone= findViewById(R.id.editTextPhone);

        btnDial= findViewById(R.id.btnDial);

    }


    //handle edit top up pin
    private void handleTopUpPin(){

        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                //remove existing separator
                String topUp_Pin= editable.toString().replaceAll("[-\\s]", "");

                //insert space every 4 digits
                StringBuilder formattedPin= new StringBuilder();

                for (int i=0; i<topUp_Pin.length(); i++){
                    formattedPin.append(topUp_Pin.charAt(i));

                    //space every 4 digits
                    if ( (i+1)%4==0 && (i+1)< topUp_Pin.length() ){
                        formattedPin.append(" ");
                    }
                }//for

                //update edit text with the formatted value
                //prevent triggering the edit text while updating
                editTextPhone.removeTextChangedListener(this);

                //set text
                editTextPhone.setText( formattedPin.toString() );

                //set cursor to the end. otherwise, the cursor appears in front
                editTextPhone.setSelection( formattedPin.length() );

                //reattach Text Watcher. otherwise, the digits are not separated
                editTextPhone.addTextChangedListener(this);

            }
        });
    }//handle

}