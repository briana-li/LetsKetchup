package edu.brandeis.cs.brianali.letsketchuptest.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.brandeis.cs.brianali.letsketchuptest.MainActivity;
import edu.brandeis.cs.brianali.letsketchuptest.R;
import edu.brandeis.cs.brianali.letsketchuptest.model.Chat;
import edu.brandeis.cs.brianali.letsketchuptest.model.Message;
import edu.brandeis.cs.brianali.letsketchuptest.model.User;
import edu.brandeis.cs.brianali.letsketchuptest.utils.Constants;
import edu.brandeis.cs.brianali.letsketchuptest.utils.EmailEncoding;


public class ChatListActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mChatDatabaseReference;
    private ChildEventListener mChildEventListener;

    private ListView mChatListView;
    private FirebaseListAdapter mChatAdapter;
    private String mUsername;
    private ValueEventListener mValueEventListener;
    private DatabaseReference mUserDatabaseReference;
    private ImageView addConversationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Nav to ChatListActivity
                    createUser(user);
                    onSignedInInitialize(user);
                } else {
                    // User is signed out
                    //onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    public void createNewChat(View view){
        Intent intent = new Intent(this, CreateChat.class);
        startActivity(intent);
    }

    //You cant have a chat if you have no friends
    private void hideShowAddChatButton(FirebaseUser user) {
        addConversationButton = (ImageView) findViewById(R.id.add_conversation);
        final String userLoggedIn = user.getEmail();
        final DatabaseReference friendsCheckRef = mFirebaseDatabase.getReference(Constants.FRIENDS_LOCATION
                + "/" + EmailEncoding.commaEncodePeriod(userLoggedIn));
        friendsCheckRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long size = dataSnapshot.getChildrenCount();
                String strLong = Long.toString(size);
                if (size > 0) {
                    addConversationButton.setVisibility(View.VISIBLE);
                } else {
                    addConversationButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //removes user from chat when - button is pressed
    //if chat is 2 people, deletes chat for both users
    //if chat is greater than 2 people, only deletes chat for the user that clicked on the - button
    private void removeChat(String chatName){
        //Get current user logged in by email
        final String userLoggedIn = mFirebaseAuth.getCurrentUser().getEmail();
        //Log.e(TAG, "User logged in is: " + userLoggedIn);
        final DatabaseReference userRef = mFirebaseDatabase.getReference(Constants.USERS_LOCATION
                + "/" + EmailEncoding.commaEncodePeriod(userLoggedIn));
        userRef.child("chats").child(chatName).removeValue();

    }



    //Make initial main screen
    private void onSignedInInitialize(FirebaseUser user) {
        mUsername = user.getDisplayName();
        mChatDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constants.USERS_LOCATION
                        + "/" + EmailEncoding.commaEncodePeriod(user.getEmail()) + "/"
                        + Constants.CHAT_LOCATION );
        mUserDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constants.USERS_LOCATION);

         hideShowAddChatButton(user);

        //Initialize screen variables
        mChatListView = (ListView) findViewById(R.id.chatListView);

        mChatAdapter = new FirebaseListAdapter<Chat>(this, Chat.class, R.layout.chat_item, mChatDatabaseReference) {
            @Override
            protected void populateView(final View view, final Chat chat, final int position) {
                //Log.e("TAG", "");
                //final Friend addFriend = new Friend(chat);
                ((TextView) view.findViewById(R.id.messageTextView)).setText(chat.getChatName());

                //Fetch last message from chat
                final DatabaseReference messageRef =
                        mFirebaseDatabase.getReference(Constants.MESSAGE_LOCATION
                                + "/" + chat.getUid());

                final TextView dateInfo = (TextView)view.findViewById(R.id.nameTextView);
                final String subTitle = chat.getChatDate();
                final String chatName= chat.getUid();

                messageRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        Message newMsg = dataSnapshot.getValue(Message.class);
                        //Make subtext in chat here
                        dateInfo.setText(subTitle);

                        mUserDatabaseReference.child(newMsg.getSender())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User msgSender = dataSnapshot.getValue(User.class);
                                        if(msgSender != null && msgSender.getProfilePicLocation() != null){
                                            StorageReference storageRef = FirebaseStorage.getInstance()
                                                    .getReference().child(msgSender.getProfilePicLocation());
//                                            Glide.with(view.getContext())
//                                                    .using(new FirebaseImageLoader())
//                                                    .load(storageRef)
//                                                    .bitmapTransform(new CropCircleTransformation(view.getContext()))
//                                                    .into(senderPic);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

//                (view.findViewById(R.id.removeChat)).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.w(TAG, "Clicking row: " + position);
//                        Log.w(TAG, "Clicking user: " + chatName);
//                        //Removes user from chat
//                        removeChat(chatName);
//                    }
//                });

                //Replace this with the most recent message from the chat

            }
        };



        mChatListView.setAdapter(mChatAdapter);
        //Add on click listener to line items
        mChatListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String messageLocation = mChatAdapter.getRef(position).toString();

                if(messageLocation != null){
                    Intent intent = new Intent(view.getContext(), ChatMessagesActivity.class);
                    String messageKey = mChatAdapter.getRef(position).getKey();
                    intent.putExtra(Constants.MESSAGE_ID, messageKey);
                    Chat chatItem = (Chat)mChatAdapter.getItem(position);
                    intent.putExtra(Constants.CHAT_NAME, chatItem.getChatName());
                    startActivity(intent);
                }

                //Log.e("TAG", mChatAdapter.getRef(position).toString());
            }
        });

        mValueEventListener = mChatDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                //Check if any chats exists
                if (chat == null) {
                    //finish();
                    return;
                }
                mChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (resultCode == RESULT_OK) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sign_out) {
            AuthUI.getInstance()
                    .signOut(this);
        }
        if (id == R.id.listFriends) {
            //Open up activity where a user can add and view friends
            Intent intent = new Intent(this, FriendsListActivity.class);
            startActivity(intent);
        }

        if (id == R.id.profilePage) {
            //Open up activity where a user can add and view friends
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }

        if (id == R.id.settings) {
            //open settings
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        if (id == R.id.about) {
            //open about
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        if (id == R.id.venmo) {
            //open venmo
            Uri uri = Uri.parse("https://venmo.com/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        if (id == R.id.credits){
            //open credits
            Intent intent = new Intent(this, Credits.class);
            startActivity(intent);
        }

        return true;
    }

    private void createUser(FirebaseUser user) {
        final DatabaseReference usersRef = mFirebaseDatabase.getReference(Constants.USERS_LOCATION);
        final String encodedEmail = EmailEncoding.commaEncodePeriod(user.getEmail());
        final DatabaseReference userRef = usersRef.child(encodedEmail);
        final String username = user.getDisplayName();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    User newUser = new User(username, encodedEmail);
                    userRef.setValue(newUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
