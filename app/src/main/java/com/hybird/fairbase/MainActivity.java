package com.hybird.fairbase;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore objectFirebaseFirestore;
    private static final String COLLECTION_NAME="BSCS_5B";
    private static final String COLLECTION_NAME1="student";
    private Dialog objectDialog;
    private EditText documentIDET,studentNameET,studentCityET;

    private DocumentReference objectDocumentReference;
    private TextView downloadedDataTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objectFirebaseFirestore=FirebaseFirestore.getInstance();
        objectDialog=new Dialog(this);

        objectDialog.setContentView(R.layout.please_wait);
        objectDialog.setCancelable(false);

        documentIDET=findViewById(R.id.documentIDET);
        studentNameET=findViewById(R.id.studentNameET);

        studentCityET=findViewById(R.id.studentCityET);
        downloadedDataTV=findViewById(R.id.downloadedDataTV);
    }

    public void addValuesToFirebaseFirestore(View view)
    {
        try
        {
            if(!documentIDET.getText().toString().isEmpty() && !studentNameET.getText().toString().isEmpty()
                    && !studentCityET.getText().toString().isEmpty()) {
                objectDialog.show();
                final Map<String, Object> objectMap = new HashMap();
                objectMap.put("StuName", studentNameET.getText().toString());
                objectMap.put("StuCity", studentCityET.getText().toString());

                objectFirebaseFirestore.collection(COLLECTION_NAME)
                        .document(documentIDET.getText().toString())
                        .set(objectMap)
                        .addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        documentIDET.setText("");
                                        studentCityET.setText("");

                                        studentNameET.setText("");
                                        documentIDET.requestFocus();
                                        objectDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Data added to collection", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        )
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to add data to collection:" +
                                        e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else if(documentIDET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter document id", Toast.LENGTH_SHORT).show();
            }
            else if(studentNameET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter student name", Toast.LENGTH_SHORT).show();
            }
            else if(studentCityET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter student city", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "addValuesToFirebaseFirestore:"+
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataFromFirebaseFirestore(View view)
    {
        try
        {
            if(documentIDET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter valid document id", Toast.LENGTH_SHORT).show();
            }
            else {
                objectDialog.show();
                objectDocumentReference = objectFirebaseFirestore.collection(COLLECTION_NAME)
                        .document(documentIDET.getText().toString());

                objectDocumentReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    objectDialog.dismiss();
                                    String documentID = documentSnapshot.getId();
                                    String stuName = documentSnapshot.getString("StuName");

                                    String stuCity = documentSnapshot.getString("StuCity");
                                    downloadedDataTV.setText( "Document ID:"+documentID+
                                            "\n" +"Student Name:"+ stuName + "\n" +"Student City:"+
                                            stuCity);

                                    documentIDET.setText("");
                                    documentIDET.requestFocus();


                                }
                                else {
                                    objectDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "No Data Exists", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to get data from collection:" +
                                        e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }


        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "getDataFromFirebaseFirestore:"+
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateValuesOfDocumentField(View view)
    {
        try
        {

            if(documentIDET.getText().toString().isEmpty() && studentCityET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter the values in required fields", Toast.LENGTH_SHORT).show();
            }
            else {
                objectDialog.show();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("StuCity", studentCityET.getText().toString());
                objectMap.put("StuInformation", "Some information about the student");

                objectDocumentReference = objectFirebaseFirestore.collection(COLLECTION_NAME)
                        .document(documentIDET.getText().toString());

                objectDocumentReference.update(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Values Successfully Updated", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to update values :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "updateValuesOfDocumentField:"+
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteFieldValue(View  view)
    {
        try
        {
            if(documentIDET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter the value for document id", Toast.LENGTH_SHORT).show();
            }
            else {

                objectDialog.show();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("StuCity", FieldValue.delete());

                objectDocumentReference = objectFirebaseFirestore.collection(COLLECTION_NAME)
                        .document(documentIDET.getText().toString());

                objectDocumentReference.update(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Value Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to delete values :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "updateValuesOfDocumentField:"+
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void deletecollection(View view)
    {
        try
        {
            objectFirebaseFirestore.collection("student").document("class")
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            objectDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Value Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            objectDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Fails to delete values :" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "deletecollection:"+
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}

