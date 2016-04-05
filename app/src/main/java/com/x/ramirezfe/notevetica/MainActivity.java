package com.x.ramirezfe.notevetica;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialcab.MaterialCab;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
    Starting activity for app
    Displays notes in a recycler view
 */

public class MainActivity extends AppCompatActivity implements MaterialCab.Callback {

    private List<Note> notes = new ArrayList<>();
    private RecyclerView recyclerView;
    MainAdapter adapter;
    private static String TAG = MainActivity.class.getSimpleName();
    private MaterialCab mCab;
    private Toolbar toolbar;
    private TextView toolbarsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarsTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide toolbar title

        // RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        // Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true); // If the view won't be changing, set to true
        recyclerView.setLayoutManager(linearLayoutManager);


        loadTestData();
        initializeAdapter();

        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNote();
                // addTestData();
            }
        });

    }

    // Called from MainAdapter.java
    // *You can add parameters to the method if you need to pass them from the adapter in the future
    public void onClickCalledFromRecyclerView() {
        createContextualToolbar();
    }

    @Override
    public boolean onCabCreated(MaterialCab cab, Menu menu) {
        // Makes the icons in the overflow menu visible
        if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                Field field = menu.getClass().getDeclaredField("mOptionalIconsVisible");
                field.setAccessible(true);
                field.setBoolean(menu, true);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean onCabItemClicked(MenuItem item) {
        Notify.message(getApplicationContext(), (String) item.getTitle());
        return true;
    }

    @Override
    public boolean onCabFinished(MaterialCab cab) {
        // Clear selected in adapter
        toolbar.setVisibility(View.VISIBLE);
        toolbarsTitle.setVisibility(View.VISIBLE);
        return true; // allow destruction
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mCab != null && mCab.isActive()) {
            mCab.finish();
            mCab = null;
            toolbar.setVisibility(View.VISIBLE);
            toolbarsTitle.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            refreshAdapter();
            Notify.message(getApplicationContext(), "Refreshing");
//            // clearAllNotes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createContextualToolbar() {
        mCab = new MaterialCab(this, R.id.cab_stub)
                .setTitleRes(R.string.app_name) // TODO: Show selected item count as you click on a row
                .setMenu(R.menu.note_context_menu)
                .setPopupMenuTheme(R.style.ThemeOverlay_AppCompat_Light)
                .setContentInsetStartRes(R.dimen.mcab_default_content_inset)
                .setBackgroundColorRes(R.color.statusBarPrimary)
                .setCloseDrawableRes(R.drawable.mcab_nav_back)
                .start(this);
        // Hide toolbar and its items, and update the colors
        toolbar.setVisibility(View.GONE);
        toolbarsTitle.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.statusBarPrimaryDark));
        }
    }

    private void loadTestData() {
        notes.add(new Note("Binge on with Benefits", "Video streaming has become the #1 way people use data. And these days, streaming video means you’ll get slammed with overages from those other carriers. Now on qualifying plans, Binge On lets you stream unlimited video for FREE without watching your data. Plus, all T-Mobile customers can enjoy the benefits of Binge On through streaming optimized for mobile devices. So now you can watch up to 3Xs more video, and use the same amount of high-speed data."));
        notes.add(new Note("Free Streaming", "Stream as much video as you want from your favorite providers without using a drop of your high-speed data. Qualifying plan required."));
        notes.add(new Note("3 times more video", "We’ve optimized video streaming for all T-Mobile customers, so you can now watch up to 3X more video, stretching your high-speed data farther. Included on all Simple Choice Plans."));
        notes.add(new Note("Medium Sized", getResources().getString(R.string.lorem_ipsum_medium)));
        notes.add(new Note("Thanksgiving Dinner RSVPs", "Eric Jones. Bryan Smith. Lauren Far. Jovanni Aikens."));
        notes.add(new Note("Gift Ideas", "Bike, candles, coffe mug."));
        notes.add(new Note("Reminder", "Remember to take out the trash"));
        notes.add(new Note("Long Size", getResources().getString(R.string.lorem_ipsum_long)));
        notes.add(new Note("Book Ideas", "Robert Jordans meets Ian McEwan meets Donna Tartt"));
        notes.add(new Note("TC's", "Limited time offers; on qualifying plans; subject to change. Taxes and fees additional. Not all features available on all devices. Unlimited talk & text features for direct communications between 2 people. Minimum 4 lines required; max of 10 lines. Not for extended international use; you must reside in the U.S. and primary usage must occur on our U.S. network. Service may be terminated or restricted for excessive roaming. Communications with premium-rate numbers not included. Data Stash: Up to 20 GB of on-network data from past 12 months carries over to next billing cycle for as long as you maintain qualifying postpaid service. Family Plans: Up to 10 lines with qualifying credit and plan. All lines must be activated in same T-Mobile market with same billing address. Music Streaming from included services does not count towards high speed data allotment on T-Mobile’s network or in Canada/Mexico; music streamed using Smartphone Mobile HotSpot (tethering) service might. Song downloads, video content, and non-music audio content excluded. For included services, see http://www.t-mobile.com/offer/free-music-streaming.html. Binge On: Limited time offers; on qualifying plans and with capable devices. May affect speed of video downloads. May not be compatible with services like game consoles and Apple TV. Video streaming from participating services (see list) does not count toward full-speed data allotment on our U.S. network. Third party content and subscription charges may apply. Some content, e.g. ads, may be excluded. Once full-speed data allotment is reached, all usage slowed to up to 2G speeds until end of bill cycle. See streaming services’ terms & conditions. All marks the property of their respective owners. Up to 30% off for new Sling TV customers on “Best of Live TV” package only; may appear as bill credit or 3rd party discount. After 12-mos, your credit card may be charged at full price. Not avail. in Puerto Rico. Free movie: one SD rental/mo. on selected service via 30-day promo code; must be in good standing on qualifying Unlimited LTE plan. Streaming services’ terms and conditions apply. See T-Mobile.com for details. Coverage not available in some areas. Network Management: Service may be slowed, suspended, terminated, or restricted for misuse, abnormal use, interference with our network or ability to provide quality service to other users, or significant roaming. Customers who use an extremely high amount of data in a bill cycle will have their data usage de-prioritized compared to other customers for that bill cycle at locations and times when competing network demands occur, resulting in relatively slower speeds. See T-Mobile.com/OpenInternet for details. T-Mobile and the magenta color are registered trademarks of Deutsche Telekom AG. ©2016 T-Mobile USA, Inc."));
    }

    private void initializeAdapter() {
        adapter = new MainAdapter(notes);
        recyclerView.setAdapter(adapter);
    }

    private void addTestData() {
        Log.d(TAG, "Adding test note");
        notes.add(new Note("Note Title", "Note Description"));
        refreshAdapter();
    }

    private void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                String passedTitle = data.getStringExtra(CreateNoteActivity.EXTRA_TITLE);
                String passedDescription = data.getStringExtra(CreateNoteActivity.EXTRA_DESCRIPTION);
                int passedID = data.getIntExtra("ID", CreateNoteActivity.EXTRA_ID);
                Note note = new Note();
                // TODO: Move last edited note to the top of the list
                if (CreateNoteActivity.didClick) { // User is saving an existing note
                    note.setTitle(passedTitle);
                    note.setDescription(passedDescription);
                    notes.remove(passedID); // Remove note so I can put that same one at the top
                    notes.add(0, note); // Put note at top of list
                    CreateNoteActivity.didClick = false;
                    recyclerView.scrollToPosition(0); // Scroll to top
                } else { // User is creating a new note
                    note.setTitle(passedTitle);
                    note.setDescription(passedDescription);
                    notes.add(0, note);
                    recyclerView.scrollToPosition(0);
                }
            }

            refreshAdapter();

            if (resultCode == Activity.RESULT_CANCELED) {
                // Do something if it's cancelled. Happens when you click the back button for example
            }
        }
    }

    // Called when user clicks the FAB
    public void createNote() {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivityForResult(intent, 123);
    }

    public void clearAllNotes() {
        for (Iterator<Note> iter = notes.listIterator(); iter.hasNext(); ) {
            Note note = iter.next();
            iter.remove();
        }
    }

}
