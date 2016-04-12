package com.x.ramirezfe.notevetica;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.afollestad.materialcab.MaterialCab;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
    Starting activity for app
    Displays notes in a recycler view
 */

public class MainActivity extends AppCompatActivity implements MaterialCab.Callback {

    // Views
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_title)
    TextView toolbarsTitle;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<Note> notes = new ArrayList<>();
    private NotesAdapter adapter;
    private static String TAG = MainActivity.class.getSimpleName();
    private MaterialCab editModeToolbar;
    // Intent Extras
    public final static String EXTRA_NOTE_TITLE = "com.x.ramirezfe.notevetica.NOTE_TITLE";
    public final static String EXTRA_NOTE_DESCRIPTION = "com.x.ramirezfe.notevetica.NOTE_DESCRIPTION";
    public final static String EXTRA_NOTE_UUID = "com.x.ramirezfe.notevetica.NOTE_UUID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ButterKnife
        ButterKnife.bind(this);

        // Initialize Backendless
        String appVersion = "v1";
        Backendless.initApp(this, Constants.APP_ID, Constants.SECRET_KEY, appVersion);

        // Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide toolbar title

        // RecyclerView
        adapter = new NotesAdapter(notes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true); // If the view won't be changing, set to true
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Note note = notes.get(position);
                // Pass note data to CreateNoteActivity so it can resave the note if the user chooses too
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
                intent.putExtra(EXTRA_NOTE_TITLE, note.getTitle());
                intent.putExtra(EXTRA_NOTE_DESCRIPTION, note.getDescription());
                intent.putExtra(EXTRA_NOTE_UUID, note.getObjectId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Note note = notes.get(position);
                Notify.message(getApplicationContext(), "Long Clicked: " + note.getTitle());
            }
        }));

        // prepareTestNotesData();
        refreshBackendNotesData();

        // Create note FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNote();
            }
        });

        // Swipe to delete
//        SwipeableRecyclerViewTouchListener swipeTouchListener =
//                new SwipeableRecyclerViewTouchListener(recyclerView,
//                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
//                            @Override
//                            public boolean canSwipeLeft(int position) {
//                                return true;
//                            }
//
//                            @Override
//                            public boolean canSwipeRight(int position) {
//                                return false;
//                            }
//
//                            @Override
//                            public void onDismissedBySwipeLeft(final RecyclerView recyclerView, int[] reverseSortedPositions) {
//                                for (final int position : reverseSortedPositions) {
//                                    // Save data just in case it was an accident
//                                    String savedNoteTitle = notes.get(position).getTitle();
//                                    String savedNoteDescription = notes.get(position).getDescription();
//                                    final Note note = new Note(savedNoteTitle, savedNoteDescription);
//                                    final int savedNotePosition = position;
//                                    // Remove note object
//                                    notes.remove(position);
//                                    adapter.notifyItemRemoved(position);
//                                    // Undo option
//                                    Snackbar snackbar = Snackbar.make(recyclerView, "Note deleted", Snackbar.LENGTH_LONG)
//                                            .setAction("UNDO", new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View view) {
//                                                    // Restore saved data
//                                                    // TODO Add animation
//                                                    notes.add(savedNotePosition, note);
//                                                    adapter.notifyItemRemoved(position);
//                                                    refreshAdapter();
//                                                    Snackbar snackbar1 = Snackbar.make(recyclerView, "Note restored", Snackbar.LENGTH_SHORT);
//                                                    snackbar1.show();
//                                                }
//                                            });
//
//                                    snackbar.show();
//                                }
//                                adapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
//                                for (int position : reverseSortedPositions) {
//                                    notes.remove(position);
//                                    adapter.notifyItemRemoved(position);
//                                }
//                                adapter.notifyDataSetChanged();
//                            }
//                        });
//
//        recyclerView.addOnItemTouchListener(swipeTouchListener);

    }

    /* Start of RecyclerView ClickListener */
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
    /* End of RecyclerView Click Listener */


    private void prepareTestNotesData() {
        Note note = new Note("Binge on with Benefits", "Video streaming has become the #1 way people use data. And these days, streaming video means you’ll get slammed with overages from those other carriers. Now on qualifying plans, Binge On lets you stream unlimited video for FREE without watching your data. Plus, all T-Mobile customers can enjoy the benefits of Binge On through streaming optimized for mobile devices. So now you can watch up to 3Xs more video, and use the same amount of high-speed data.");
        notes.add(note);
        note = new Note("Free Streaming", "Stream as much video as you want from your favorite providers without using a drop of your high-speed data. Qualifying plan required.");
        notes.add(note);
        note = new Note("Free Streaming", "Stream as much video as you want from your favorite providers without using a drop of your high-speed data. Qualifying plan required.");
        notes.add(note);
        note = new Note("3 times more video", "We’ve optimized video streaming for all T-Mobile customers, so you can now watch up to 3X more video, stretching your high-speed data farther. Included on all Simple Choice Plans.");
        notes.add(note);
        note = new Note("Medium Sized", getResources().getString(R.string.lorem_ipsum_medium));
        notes.add(note);
        note = new Note("Thanksgiving Dinner RSVPs", "Eric Jones. Bryan Smith. Lauren Far. Jovanni Aikens.");
        notes.add(note);
        note = new Note("Gift Ideas", "Bike, candles, coffe mug.");
        notes.add(note);
        note = new Note("Reminder", "Remember to take out the trash");
        notes.add(note);
        note = new Note("Long Size", getResources().getString(R.string.lorem_ipsum_long));
        notes.add(note);
        note = new Note("Book Ideas", "Robert Jordans meets Ian McEwan meets Donna Tartt");
        notes.add(note);
        note = new Note("TC's", "Limited time offers; on qualifying plans; subject to change. Taxes and fees additional. Not all features available on all devices. Unlimited talk & text features for direct communications between 2 people. Minimum 4 lines required; max of 10 lines. Not for extended international use; you must reside in the U.S. and primary usage must occur on our U.S. network. Service may be terminated or restricted for excessive roaming. Communications with premium-rate numbers not included. Data Stash: Up to 20 GB of on-network data from past 12 months carries over to next billing cycle for as long as you maintain qualifying postpaid service. Family Plans: Up to 10 lines with qualifying credit and plan. All lines must be activated in same T-Mobile market with same billing address. Music Streaming from included services does not count towards high speed data allotment on T-Mobile’s network or in Canada/Mexico; music streamed using Smartphone Mobile HotSpot (tethering) service might. Song downloads, video content, and non-music audio content excluded. For included services, see http://www.t-mobile.com/offer/free-music-streaming.html. Binge On: Limited time offers; on qualifying plans and with capable devices. May affect speed of video downloads. May not be compatible with services like game consoles and Apple TV. Video streaming from participating services (see list) does not count toward full-speed data allotment on our U.S. network. Third party content and subscription charges may apply. Some content, e.g. ads, may be excluded. Once full-speed data allotment is reached, all usage slowed to up to 2G speeds until end of bill cycle. See streaming services’ terms & conditions. All marks the property of their respective owners. Up to 30% off for new Sling TV customers on “Best of Live TV” package only; may appear as bill credit or 3rd party discount. After 12-mos, your credit card may be charged at full price. Not avail. in Puerto Rico. Free movie: one SD rental/mo. on selected service via 30-day promo code; must be in good standing on qualifying Unlimited LTE plan. Streaming services’ terms and conditions apply. See T-Mobile.com for details. Coverage not available in some areas. Network Management: Service may be slowed, suspended, terminated, or restricted for misuse, abnormal use, interference with our network or ability to provide quality service to other users, or significant roaming. Customers who use an extremely high amount of data in a bill cycle will have their data usage de-prioritized compared to other customers for that bill cycle at locations and times when competing network demands occur, resulting in relatively slower speeds. See T-Mobile.com/OpenInternet for details. T-Mobile and the magenta color are registered trademarks of Deutsche Telekom AG. ©2016 T-Mobile USA, Inc.");
        notes.add(note);
        refreshAdapter();
    }

    private void refreshBackendNotesData() {
        Backendless.Persistence.of(Note.class).find(new AsyncCallback<BackendlessCollection<Note>>() {
            @Override
            public void handleResponse(BackendlessCollection<Note> foundNotes) {
                // Get first page of data
                List<Note> firstPage = foundNotes.getCurrentPage();
                // // Iterate over the received objects
                Iterator<Note> iterator = firstPage.iterator();
                notes.clear();
                while (iterator.hasNext()) {
                    Note note = iterator.next();
                    Notify.out(note.toString());
                    notes.add(note);
                }
                refreshAdapter();
                // All note instances have been found
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                // An error has occurred
                Log.e(TAG, fault.getCode());
            }
        });
    }

    /* Editing mode toolbar methods start here */
    public void createContextualToolbar() {
        editModeToolbar = new MaterialCab(this, R.id.cab_stub)
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
        // TODO: Clear the selected items
        toolbar.setVisibility(View.VISIBLE);
        toolbarsTitle.setVisibility(View.VISIBLE);
        return true; // allow destruction
    }
    /* End of editing mode toolbar methods */

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Notify.out("onResume();");
        refreshBackendNotesData();
        hideKeyboard();
    }

    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Notify.out("onStart();");
    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Notify.out("onPause();");
    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Notify.out("onStop();");
    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Notify.out("onDestroy();");
    }

    @Override
    public void onBackPressed() {
        if (editModeToolbar != null && editModeToolbar.isActive()) {
            editModeToolbar.finish();
            editModeToolbar = null;
            toolbar.setVisibility(View.VISIBLE);
            toolbarsTitle.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            refreshAdapter();
            Notify.message(getApplicationContext(), "Refreshing data");
            return true;
        }
        if (id == R.id.action_logout) {
            Notify.message(getApplicationContext(), "Wut");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }

    // Called when user clicks the FAB
    public void createNote() {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivity(intent);
    }

    public void hideKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


}