package com.example.filesdemo;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    EditText enterFilename;
    EditText enterFileText;
    Button writeFileButton;
    Button readFileButton;
    TextView readFileText;
    Button extStorageButton;
    TextView extStorageText;

    String fileName;
    String dataToWrite;
    String dataRead;

    File   internalDir;
    File   externalDir;
    File    externalPrivDir;
    File    myExternalFile;

    File    newDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterFilename = findViewById(R.id.editFileName);
        enterFileText = findViewById(R.id.editFileText);
        writeFileButton = findViewById(R.id.writeFileButton);
        readFileButton = findViewById(R.id.readFileButton);
        readFileText = findViewById(R.id.readFileText);
        extStorageButton = findViewById(R.id.extStorageButton);
        extStorageText = findViewById(R.id.extStorageText);

        internalDir = this.getFilesDir();
        externalDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        externalPrivDir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        // externalPrivDir = this.getExternalFilesDir("BhupiDocs");

        // finding multiple external storage
        File extMultipleDirs[];
        extMultipleDirs = ContextCompat.getExternalFilesDirs(this, null);
        Log.i("myLog",   String.valueOf(extMultipleDirs.length));
        for (int i = 0; i < extMultipleDirs.length; i++) {
            Log.i("myLog", extMultipleDirs[i].getPath());
            Log.i("myLog", extMultipleDirs[i].getAbsolutePath());
            // Log.i("myLog", extMultipleDirs[i].getCanonicalPath());
            Log.i("myLog", "Total Space: " + Long.toString(extMultipleDirs[i].getTotalSpace()));
            Log.i("myLog", "Available Space: " + Long.toString(extMultipleDirs[i].getFreeSpace()));
            Log.i("myLog", "Usable Space: " + Long.toString(extMultipleDirs[i].getUsableSpace()));
        }
    }

    public void onClickWriteFile (View v) {

        fileName=enterFilename.getText().toString();
        dataToWrite=enterFileText.getText().toString();

        try {
            FileOutputStream foStream = openFileOutput(fileName, MODE_PRIVATE);
            foStream.write(dataToWrite.getBytes());
            foStream.close();
            Toast.makeText(getBaseContext(),internalDir.getPath() + ": " + fileName + ": saved",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        enterFilename.setText("");
        enterFileText.setText("");

    }

    public void onClickReadFile (View v) {

        fileName=enterFilename.getText().toString();
        try {
            FileInputStream fin = openFileInput(fileName);
            int c;
            String temp="";
            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            readFileText.setText(temp);
            Toast.makeText(getBaseContext(), internalDir.getPath() + ": " + fileName + ": read",Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public void onClickExternalStorage(View v) {

        String tmpString;

        // permissions in manifest file

        // verify that external storage is enabled
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            Log.i("myLog", extStorageState + " : Media mounted read only");
            // you can read only
        } else if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            Log.i("myLog", extStorageState + ": Media mounted read write");
            // you can read and write
        }

        // use external public directory, note path
        Log.i("myLog", "External Path is: " + externalDir.getPath());

        // user external private directory, note path
        Log.i("myLog", "External Private Path is: " + externalPrivDir.getPath());


        // newDir = new File(externalPrivDir, "bhupiDocs");
        // newDir.mkdir();
        // Log.i("myLog", "New Directory is: " + newDir.getPath());

        // write into file
        myExternalFile = new File(externalPrivDir, "sample.txt");
        Log.i("myLog", "External File Path is: " + myExternalFile.getPath());
        try {
            FileOutputStream fos = new FileOutputStream(myExternalFile);
            fos.write(enterFileText.getText().toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read from file
        try {
            // use the  FileInputStream -> DataInputStream -> BufferedReader
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            String myData = "";
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
            in.close();

            Log.i("myLog", myData);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
