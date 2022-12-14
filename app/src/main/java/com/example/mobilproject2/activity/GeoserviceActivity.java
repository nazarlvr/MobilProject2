package com.example.mobilproject2.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;

import com.example.mobilproject2.R;
import com.example.mobilproject2.adapter.ContactsAdapter;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class GeoserviceActivity extends AppCompatActivity
{
    Button button;
    EditText editText;
    private static final GeoPoint redBuilding = new GeoPoint(50.442021849127634, 30.51110653557765);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_geoservice);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);


        this.button = findViewById(R.id.geosevice_start_button);
        this.editText = findViewById(R.id.deoservice_edit_text);

        this.button.setOnClickListener(view -> new AlertDialog.Builder(this)
                .setMessage(R.string.geoservice_start_text)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    Uri mapsIntentUri = Uri.parse(
                            String.format("https://www.google.com/maps/dir/Красный+корпус+Киевского+национального+университета/%s/?travelmode=driving", this.editText.getText().toString())
                    );
                    Intent mapsIntent = new Intent(Intent.ACTION_VIEW, mapsIntentUri);
                    mapsIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapsIntent);
                })
                .show());
    }
}