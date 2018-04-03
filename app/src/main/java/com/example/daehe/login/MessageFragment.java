package com.example.daehe.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;

public class MessageFragment extends Fragment {
    View myView;
    ArrayList<Message> msgs;

    private static final String TAG = "Message Fragment: ";
    private FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.fragment_message,container,false);

        loadInbox();

        loadButton();

        return myView;
    }

    private void loadInbox(){
        ListView lvInbox = (ListView) myView.findViewById(R.id.lvInbox);
        msgs = new ArrayList<Message>();

        db = FirebaseFirestore.getInstance();

        DocumentReference doc = db.collection("Users").document();

        db.collection("Users")
                .document(((PEActionBarActivity)getActivity()).getUser().getID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            User user = documentSnapshot.toObject(User.class);
                            msgs = user.getMessages();

                            Toast.makeText(getApplicationContext(), "Messages are loaded", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        MessageAdapter adapter = new MessageAdapter(getActivity(), msgs);

        lvInbox.setAdapter(adapter);

        lvInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ReadMessageFragment rmf = new ReadMessageFragment();
                rmf.setMessage(msgs.get(i));
                rmf.setReadOnly(true);
                getActivity().getSupportFragmentManager()
                        .beginTransaction( )
                        .replace(R.id.contentframe, rmf)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void loadButton(){
        ImageButton btn = (ImageButton) myView.findViewById(R.id.ibCompose);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadMessageFragment rmf = new ReadMessageFragment();
                getActivity().getSupportFragmentManager().beginTransaction( )
                        .replace(R.id.contentframe, rmf, "Read Message")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
