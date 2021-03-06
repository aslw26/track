package lta.amazoning.track;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.security.NetworkSecurityPolicy;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UploadFragment extends Fragment {
    private String url;
    private ImageView toUpload;
    private Button upload;
    private ImageButton takePicture;

    private NiceSpinner railLouRSpinner;
    private NiceSpinner tunnelSpinner;
    private NiceSpinner dropMinSpinner;
    private NiceSpinner newCurrentSpinner;
    private NiceSpinner scSpinner;
    private EditText chFrSpinner;
    private EditText chToSpinner;
    private EditText pointSpinner;
    private EditText others;
    private EditText defectSpinner;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int WRITE_PERMISSION_CODE = 101;
    private Uri imageUri;
    private boolean taken_picture = false;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public List<ImageJSONBinder> defectDetails;
    public boolean uploadSuccess = false;

    // Btm app bar
    private FloatingActionButton fab;
    private Button leftButton;
    private Button middleButton;
    private Button rightButton;

    private OnFragmentInteractionListener mListener;


    public UploadFragment() {
        // doesn't do anything special
    }

    @SuppressLint("RestrictedApi")
    public UploadFragment(FloatingActionButton fab, Button leftButton, Button middleButton, Button rightButton, List<ImageJSONBinder> defectDetails) {
        this.fab = fab;
        this.leftButton = leftButton;
        this.middleButton = middleButton;
        this.rightButton = rightButton;
        this.defectDetails = defectDetails;

        fab.hide();
        leftButton.setVisibility(View.INVISIBLE);
        rightButton.setVisibility(View.INVISIBLE);
        middleButton.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        url = getActivity().getResources().getText(R.string.server_url).toString();

        chFrSpinner = view.findViewById(R.id.chFr_spinner);
        chToSpinner = view.findViewById(R.id.chTo_spinner);
        pointSpinner = view.findViewById(R.id.point_spinner);
        others = view.findViewById(R.id.edit_others_txt);
        middleButton.setText(getActivity().getResources().getText(R.string.cr8andUpload));
        toUpload = view.findViewById(R.id.imageHolder);
        defectSpinner = view.findViewById(R.id.desc_spinner);

        String[] arraySpinner = getActivity().getResources().getStringArray(R.array.stations_arrays);

        String[] leftOrRightSpinner = getActivity().getResources().getStringArray(R.array.left_or_right_array);
        railLouRSpinner = view.findViewById(R.id.railLouR_spinner);
        ArrayAdapter<String> rail_adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, leftOrRightSpinner);
        rail_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        railLouRSpinner.setAdapter(rail_adapter);

        String[] tunnelClk = getActivity().getResources().getStringArray(R.array.tunnel_array);
        tunnelSpinner = view.findViewById(R.id.tunnel_spinner);
        ArrayAdapter<String> tunnel_adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, tunnelClk);
        tunnel_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tunnelSpinner.setAdapter(tunnel_adapter);

        String[] dropMin = getActivity().getResources().getStringArray(R.array.dropmin_array);
        dropMinSpinner = view.findViewById(R.id.dropmin_spinner);
        ArrayAdapter<String> dropmin_adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, dropMin);
        dropmin_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropMinSpinner.setAdapter(dropmin_adapter);

        String[] newCurrentArray = getActivity().getResources().getStringArray(R.array.newcurrent_array);
        newCurrentSpinner = view.findViewById(R.id.newcurrent_spinner);
        ArrayAdapter<String> newCurrent_adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, newCurrentArray);
        newCurrent_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newCurrentSpinner.setAdapter(newCurrent_adapter);

        String[] scArray = getActivity().getResources().getStringArray(R.array.sc_array);
        scSpinner = view.findViewById(R.id.sc_spinner);
        ArrayAdapter<String> sc_adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, scArray);
        sc_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scSpinner.setAdapter(sc_adapter);

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        takePicture = view.findViewById(R.id.imageHolder);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    initiateCameraActivity();
                }
            }
        });

        this.middleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!taken_picture) {
                    try {
                        connectServer(view);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                }
            }
        });
    }

    private void initiateCameraActivity() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                initiateCameraActivity();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        taken_picture = true;
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                takePicture.setImageBitmap(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    void connectServer(View view) throws JSONException {
        String postImageUrl= "http://"+ url +":"+"8082"+"/image";
        String postJsonUrl= "http://"+ url +":"+"8082"+"/json";
        final String uid = String.valueOf(System.currentTimeMillis());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // Read BitMap by file path
        Bitmap bitmap = ((BitmapDrawable) toUpload.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        DataJSON jsonObject = new DataJSON(railLouRSpinner.getSelectedItem().toString(), chFrSpinner.getText().toString(),
                                            chToSpinner.getText().toString(), defectSpinner.getText().toString(),
                                            pointSpinner.getText().toString(), tunnelSpinner.getSelectedItem().toString(),
                                            dropMinSpinner.getSelectedItem().toString(), newCurrentSpinner.getSelectedItem().toString(),
                                            scSpinner.getSelectedItem().toString(), others.getText().toString(), uid);
        ImageJSONBinder pass_data_to_inspection = new ImageJSONBinder(jsonObject, bitmap);

        RequestBody postBodyJSON = RequestBody.create(JSON, jsonObject.toString());

        RequestBody postBodyImage = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image" + uid + ".jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
                .build();

        postRequest(postImageUrl, postBodyImage, view);
        postRequest(postJsonUrl, postBodyJSON, view);
        this.defectDetails.add(pass_data_to_inspection);
        this.uploadSuccess = true;
        getActivity().getSupportFragmentManager().popBackStack();
    }

    void postRequest(String postUrl, RequestBody postBody, final View view) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // Cancel the post on failure.
//                call.cancel();
//
//                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i("UploadFragment", "Failed!");
//                        e.printStackTrace();
//                    }
//                });
//            }

//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i("UploadFragment", "Succeeded!");
//                    }
//                });
//            }
//        });
    }
}
