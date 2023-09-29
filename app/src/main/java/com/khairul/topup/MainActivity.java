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

    //telco provider code
    public static final String CELCOM = "*122*";
    public static final String HOTLINK= "*111";

    private EditText editTextPin;
    private Button btnDial, btnClear;

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
                dialTopUp(CELCOM);
            }
        });


        //clear edit text
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPin.getText().clear();
            }
        });


    }


    //find view by id
    private void initViews(){
        editTextPin = findViewById(R.id.editTextPin);

        btnDial= findViewById(R.id.btnDial);
        btnClear= findViewById(R.id.btnClear);
    }


    //handle edit top up pin
    private void handleTopUpPin(){

        editTextPin.addTextChangedListener(new TextWatcher() {
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
                editTextPin.removeTextChangedListener(this);

                //set text
                editTextPin.setText( formattedPin.toString() );

                //set cursor to the end. otherwise, the cursor appears in front
                editTextPin.setSelection( formattedPin.length() );

                //reattach Text Watcher. otherwise, the digits are not separated
                editTextPin.addTextChangedListener(this);

            }
        });
    }//handle top up pin

    //dial the top up
    private void dialTopUp(String telcoProvider){

        //get from edit text
       String topUpPin= editTextPin.getText().toString();

       //remove except digits
        topUpPin= topUpPin.replaceAll("\\D","");

       //only works for 16 digits pin
        if (topUpPin.length()==16){

            String pinToDial= telcoProvider + topUpPin+ "#";

           // Toast.makeText(this, "works", Toast.LENGTH_SHORT).show();

            //create intent to dial
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+ pinToDial) );

            //check if there is activity to handle intent
            if (intent.resolveActivity( getPackageManager() ) != null    ){
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Wait for a moment", Toast.LENGTH_SHORT).show();
            }

        }

        else { //handles pin
            Toast.makeText(this, "Please enter the correct pin", Toast.LENGTH_SHORT).show();
        }

    }

}