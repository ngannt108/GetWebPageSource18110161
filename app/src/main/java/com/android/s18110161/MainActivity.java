package com.android.s18110161;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, AdapterView.OnItemSelectedListener {

    private String stringSpinner;
    private EditText editTextUrl;
    private TextView textViewSourceCodePage;

    private static final String QUERY = "queryString";
    private static final String PROTOCOL = "transferProtocol";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializationUI();

        initializationAdapterSpinner();
    }

    public void initializationUI() {

        editTextUrl = findViewById(R.id.url);
        textViewSourceCodePage = findViewById(R.id.page_source_code);

    }

    public void initializationAdapterSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.http_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.http_spinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
            spinner.setAdapter(adapter);
        }

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        stringSpinner = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        String[] values = getResources().getStringArray(R.array.http_array);
        stringSpinner = values[0];
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {

        String queryString = "";
        String transferProtocol = "";

        if (args != null) {
            queryString = args.getString(QUERY);
            transferProtocol = args.getString(PROTOCOL);
        }
        return new Resource(queryString, transferProtocol, this);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        try {
            textViewSourceCodePage.setText(data);
        } catch (Exception e) {
            e.printStackTrace();
            textViewSourceCodePage.setText("No Response !!!");
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public void getSourceCode(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        String queryString = editTextUrl.getText().toString();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected() && (queryString.length() != 0)) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString(QUERY, queryString);
            queryBundle.putString(PROTOCOL, stringSpinner);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            textViewSourceCodePage.setText("Getting source code...");
        } else {
            if (queryString.length() == 0) {
                Toast.makeText(this, "Url is not provided !", Toast.LENGTH_LONG).show();
            } else if (!URLUtil.isValidUrl(queryString)) {
                Toast.makeText(this, "URL is invalid !", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Internet is not connecting...", Toast.LENGTH_LONG).show();
            }
        }
    }
}