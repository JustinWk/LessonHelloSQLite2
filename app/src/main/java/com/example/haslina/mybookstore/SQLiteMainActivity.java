package com.example.haslina.mybookstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseInstallation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SQLiteMainActivity extends AppCompatActivity {
    TextView tvDetails, tvShowRecords;
    EditText etBNAME, etBAUTHOR;
    Button btADD, btFIND, btDELETE;
    ListView lv;
    List<Book> bookList;
    ArrayAdapter newBookListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_main);

        Parse.initialize(this, "xANtWGkEHGuLjrFVeWA5zMtm7Kmkhrj484WhDBiC", "5PCZ4Fx7TWOUARB1GAkWKyKplk6I7xeDaIFMl7o1");
        ParseInstallation.getCurrentInstallation().saveInBackground();



        tvDetails = (TextView) findViewById(R.id.textViewDetails);
        tvShowRecords = (TextView) findViewById(R.id.tvShowRecords);
        etBNAME = (EditText) findViewById(R.id.editTextBName);
        etBAUTHOR = (EditText) findViewById(R.id.editTextBAuthor);
        btADD = (Button) findViewById(R.id.buttonADD);
        btFIND = (Button) findViewById(R.id.buttonFind);
        btDELETE = (Button) findViewById(R.id.buttonDelete);
        lv = (ListView) findViewById(R.id.lvShowRecords);


        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy | h:mm a");
        String dateString = sdf.format(date);
        tvDetails.setText(dateString);

        BooksDBHandler dbHandler = new BooksDBHandler(this, null,
                null, BooksDBHandler.DATABASE_VERSION);



        /*dbHandler.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald"));
        dbHandler.addBook(new Book("Anna Karenina", "Leo Tolstoy"));
        dbHandler.addBook(new Book("The Grapes of Wrath", "John Steinbeck"));
        dbHandler.addBook(new Book("Invisible Man", "Ralph Ellison"));
        dbHandler.addBook(new Book("Gone with the Wind", "Margaret Mitchell"));
        dbHandler.addBook(new Book("Pride and Prejudice", "Jane Austen"));
        dbHandler.addBook(new Book("Sense and Sensibility", "Jane Austen"));
        dbHandler.addBook(new Book("Mansfield Park", "Jane Austen"));
        dbHandler.addBook(new Book("The Color Purple", "Alice Walker"));
        dbHandler.addBook(new Book("The Temple of My Familiar", "Alice Walker"));
        dbHandler.addBook(new Book("The waves", "Virginia Woolf"));
        dbHandler.addBook(new Book("Mrs Dalloway", "Virginia Woolf"));
        dbHandler.addBook(new Book("War and Peace", "Leo Tolstoy"));*/



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Write here anything that you wish to do on click of FAB
                // Code to Add an item with default animation
                Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
                //Ends Here
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
        final BooksDBHandler dbHandler = new BooksDBHandler(this, null, null,
                BooksDBHandler.DATABASE_VERSION);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String valueClicked = (String) lv.getItemAtPosition(position);
                Book book = dbHandler.findBookByName(valueClicked);
                if (book != null) {
                    CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
                    Snackbar.make(coordinatorLayout,
                            "Book ID-" + String.valueOf(book.get_id()
                                    + " & The Author is-" + book.getBookAuthor()),
                            Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(findViewById(android.R.id.content),
                            "No Match Found", Snackbar.LENGTH_SHORT).show();
                }
            }
        });


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick
                    (AdapterView<?> parent, View view, int position, long id) {
                String valueClicked = (String) lv.getItemAtPosition(position);
                Book book = dbHandler.findBookByName(valueClicked);
                if (book != null) {
                    //startA new activity to update the data
                    Snackbar.make(findViewById(android.R.id.content),
                            "Record Found", Snackbar.LENGTH_SHORT).show();

                    int clickedID = book.get_id();
                    Intent i = new Intent(SQLiteMainActivity.this, EditDialog.class);
                    i.putExtra("CLICKED_ID", clickedID);
                    startActivity(i);
                } else {
                    Snackbar.make(findViewById(android.R.id.content),
                            "No Match Found", Snackbar.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void refreshList() {
        // get all books
        BooksDBHandler dbHandler = new BooksDBHandler(this, null,
                null, BooksDBHandler.DATABASE_VERSION);
        bookList = dbHandler.getAllBooks();
        if (bookList.size() >= 1) {
            tvShowRecords.setText("Records Available");
        } else tvShowRecords.setText("No Records Available");
        List listTitle = new ArrayList();
        for (int i = 0; i < bookList.size(); i++) {
            listTitle.add(i, bookList.get(i).getBookName());
        }
        newBookListAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, listTitle);
        lv.setAdapter(newBookListAdapter);
    }

    public void addBook(View v) {
        addition();
    }

    private void addition() {
        hideSoftKeyboard(this);
        BooksDBHandler dbHandler = new BooksDBHandler(this,
                null, null, BooksDBHandler.DATABASE_VERSION);
        Book product = new Book(etBNAME.getText().toString(),
                etBAUTHOR.getText().toString());
        dbHandler.addBook(product);
        etBNAME.setText("");
        etBAUTHOR.setText("");
        refreshList();
    }

    public void findBook(View v) {
        searching();
    }

    private void searching() {
        hideSoftKeyboard(this);
        BooksDBHandler dbHandler = new BooksDBHandler(this, null, null,
                BooksDBHandler.DATABASE_VERSION);
        Book book =
                dbHandler.findBookByName(etBNAME.getText().toString());
        if (book != null) {
            Snackbar.make(findViewById(android.R.id.content),
                    "Book Found: ID-" + String.valueOf(book.get_id()
                            + " Author-" + book.getBookAuthor()),
                    Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(android.R.id.content),
                    "No Match Found", Snackbar.LENGTH_SHORT).show();
        }
        refreshList();
    }

    public void deleteBook(View v) {
        deletion();
    }

    private void deletion() {
        hideSoftKeyboard(this);
        final BooksDBHandler dbHandler = new BooksDBHandler(this, null,
                null, BooksDBHandler.DATABASE_VERSION);
        boolean result = dbHandler.deleteBook(
                etBNAME.getText().toString());
        if (result) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
            Snackbar.make(coordinatorLayout,
                    "Record Deleted",
                    Snackbar.LENGTH_LONG).setAction(R.string.action_delete_all, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHandler.clearTable();
                    refreshList();
                    Toast.makeText(getApplicationContext(),
                            "All records are deleted", Toast.LENGTH_LONG);
                }
            }).show();
            etBNAME.setText("");
            etBAUTHOR.setText("");
        } else
            Snackbar.make(findViewById(android.R.id.content),
                    "No Match Found", Snackbar.LENGTH_SHORT).show();
        refreshList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sqlite_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_deleteAll) {
            BooksDBHandler dbHandler = new BooksDBHandler(this, null,
                    null, BooksDBHandler.DATABASE_VERSION);
            dbHandler.clearTable();
            refreshList();
            Toast.makeText(getApplicationContext(),
                    "All records are deleted", Toast.LENGTH_LONG);

            return true;
        } else if (id == R.id.action_add) {
            addition();

            return true;
        } else if (id == R.id.action_delete) {
            deletion();
            return true;
        } else if (id == R.id.action_find) {
            searching();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


}
