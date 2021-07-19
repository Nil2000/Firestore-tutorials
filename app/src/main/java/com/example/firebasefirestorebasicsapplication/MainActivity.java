package com.example.firebasefirestorebasicsapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    private static final String KEY_TITLE="Title";
    private static final String KEY_DESCRIPTION="Description";
    private EditText etTitle,etDescription;
    Button btnSave;
    private TextView tvDetails;
    private ListenerRegistration noteListener;

    //Firestore
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private DocumentReference noteRef=db.document("Notebook/My first Note");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTitle=findViewById(R.id.etTitle);
        etDescription=findViewById(R.id.etDescription);
        btnSave=findViewById(R.id.btnSave);
        tvDetails=findViewById(R.id.tvDetails);
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Here That will happen for clicking button
//            }
//        });
    }
    //Initially load button is used to see changes made
    //But this refresh can be done automatically using Snapshot listener
    //The code best placed inside onStart method mentioned below


    @Override
    protected void onStart() {
        super.onStart();
//        The code for updating automatically is given below
        //Initially the only argument in the below method was new EventListener
        //But adding first argument this activity is used
        // so that to detach listener automatically at the right time else
        //We need to use onStop() method
        noteListener=noteRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //If some error occurs
                if(error!=null){
                    Toast.makeText(MainActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, error.toString());
                    return;//This return helps in avoiding app crash
                }
                //Same code used for load button
                if(documentSnapshot.exists()){
                    String title=documentSnapshot.getString(KEY_TITLE);
                    String description=documentSnapshot.getString(KEY_DESCRIPTION);
                    tvDetails.setText("Title: "+title+"\nDescription: "+description);
                }
            }
        });
    }

    public void saveNote(View v){
        String title=etTitle.getText().toString();
        String description=etDescription.getText().toString();

        Map<String,Object> note=new HashMap<>();
        note.put(KEY_TITLE,title);
        note.put(KEY_DESCRIPTION,description);

        //db.collection("Notebook").document("My first Note")=noteRef
        noteRef.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Save Failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void loadNote(View v){
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String title=documentSnapshot.getString(KEY_TITLE);
                            String description=documentSnapshot.getString(KEY_DESCRIPTION);

//                            Map<String,Object> note=documentSnapshot.getData();Same as above 2 lines

                            tvDetails.setText("Title: "+title+"\nDescription: "+description);
                        }else {
                            Toast.makeText(MainActivity.this, "Document doesnot exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }
}