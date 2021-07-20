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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

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
    private CollectionReference notebookRef=db.collection("Notebook");

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
//        noteRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                //If some error occurs
//                if(error!=null){
//                    Toast.makeText(MainActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, error.toString());
//                    return;//This return helps in avoiding app crash
//                }
//                //Same code used for load button
//                if(documentSnapshot.exists()){
//                    Note note=documentSnapshot.toObject(Note.class);
//                    String title=note.getTitle();
//                    String description=note.getDescription();
//                    tvDetails.setText("Title: "+title+"\nDescription: "+description);
//                }else {
//                    //Will show nothing
//                    tvDetails.setText("");
//                }
//            }
//        });
        //THis method above is used using document reference
        //Below code is for using collection reference
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value,FirebaseFirestoreException error) {
                if(error!=null){
                    return;
                }
                String data="";
                for (QueryDocumentSnapshot documentSnapshot:value){
                    Note note=documentSnapshot.toObject(Note.class);
                    note.setDocumentId(documentSnapshot.getId());

                    String documentId=note.getDocumentId();
                    String title=note.getTitle();
                    String description=note.getDescription();

                    data+="ID: "+documentId+"\nTitle: "+title+"\nDescription: "+description+"\n\n";
//                    data+="Title: "+title+"\nDescription: "+description+"\n\n";
                    //To get reference to a particular document using documentID
                    //notebookRef.document(documentId).(update/delete/...)
                    //anything u want to apply
                }
                tvDetails.setText(data);
            }
        });
    }

//    public void saveNote(View v){
//        String title=etTitle.getText().toString();
//        String description=etDescription.getText().toString();
//
//        //Using custom object instead of HashMap
////        Map<String,Object> note=new HashMap<>();
////        note.put(KEY_TITLE,title);
////        note.put(KEY_DESCRIPTION,description);
//
//        Note note=new Note(title,description);
//
//        //db.collection("Notebook").document("My first Note")=noteRef
//        noteRef.set(note)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this, "Save Failed", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, e.toString());
//                    }
//                });
//    }

    public void addNote(View v){
        String title=etTitle.getText().toString();
        String description=etDescription.getText().toString();

        //Using custom object instead of HashMap
//        Map<String,Object> note=new HashMap<>();
//        note.put(KEY_TITLE,title);
//        note.put(KEY_DESCRIPTION,description);

        Note note=new Note(title,description);

        //db.collection("Notebook").document("My first Note")=noteRef
        notebookRef.add(note);

    }
//    public void deleteField(View v){
//        //This is deletion of Fields of document
////        Map<String,Object> note=new HashMap<>();
////        note.put(KEY_DESCRIPTION, FieldValue.delete());
////        noteRef.update(note);
//        //Upper code in short below
//        noteRef.update(KEY_DESCRIPTION,FieldValue.delete());
//        //After this update we can also add Successful or Failure Listener
//    }
//
//    public void deleteNote(View v){
//        //This is deletion of whole document
//        noteRef.delete();
//    }
//    public void updateNote(View v){
//        String description=etDescription.getText().toString();
//        Map<String,Object> note=new HashMap<>();
//        note.put(KEY_DESCRIPTION,description);
////        noteRef.set(note);
//        //Above Set method will overwrite document completely as a result by this method
//        //Only mentioned keys will be updated rest will be removed
//        //This can be avoided by adding one more argument given below
////        noteRef.set(note, SetOptions.merge());
//        //But if there is no element then only those keys are mentioned are added
//        //If we want to update if any is present then below code
//        noteRef.update(note);
//        //It will not update if the document is empty
//    }

//    public void loadNote(View v){
//        noteRef.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if(documentSnapshot.exists()){
////                            String title=documentSnapshot.getString(KEY_TITLE);
////                            String description=documentSnapshot.getString(KEY_DESCRIPTION);
//                            //Using custom object
//                            Note note=documentSnapshot.toObject(Note.class);
//                            String title=note.getTitle();
//                            String description=note.getDescription();
//
////                            Map<String,Object> note=documentSnapshot.getData();Same as above 2 lines
//
//                            tvDetails.setText("Title: "+title+"\nDescription: "+description);
//                        }else {
//                            Toast.makeText(MainActivity.this, "Document doesnot exist", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(MainActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, e.toString());
//                    }
//                });
//    }

    public void loadNotes(View v){
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //queryDocumentSnapshots contains all the multiple documents
                        //To iterate over all the other documents using for loop
                        String data="";
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            Note note=documentSnapshot.toObject(Note.class);
                            note.setDocumentId(documentSnapshot.getId());

                            String documentId=note.getDocumentId();
                            String title=note.getTitle();
                            String description=note.getDescription();

                            data+="ID: "+documentId+"\nTitle: "+title+"\nDescription: "+description+"\n\n";
                        }
                        tvDetails.setText(data);
                    }
                });
    }
}