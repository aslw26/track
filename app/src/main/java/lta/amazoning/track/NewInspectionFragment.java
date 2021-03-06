package lta.amazoning.track;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class NewInspectionFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FragmentActivity mFrgAct;
    private Intent mIntent;

    private FloatingActionButton fab;
    private FloatingActionButton go;
    private BottomAppBar btm_bar;
    private Button left;
    private Button middle;
    private Button right;
    private NiceSpinner stationstartspinner, stationendspinner, boundspinner, sectorcodespinner, accompaniedbyspinner;
    public InspectionOverview instance;
    public JSONObject inspection_details;

    public NewInspectionFragment() {
    }

    public NewInspectionFragment(FloatingActionButton fab, Button left, Button middle, Button right, BottomAppBar btm_bar) {
        this.fab = fab;
        this.left = left;
        this.middle = middle;
        this.right = right;
        this.btm_bar = btm_bar;

        fab.hide();
        btm_bar.setVisibility(View.INVISIBLE);
        left.setVisibility(View.INVISIBLE);
        right.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFrgAct = getActivity();
        mIntent = mFrgAct.getIntent(); //  Intent intent = new Intent(getActivity().getIntent());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_new_inspection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        go = view.findViewById(R.id.go);
        stationstartspinner = view.findViewById(R.id.stationstartspinner);
        ArrayAdapter<CharSequence> stationstartadapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.stations_arrays, android.R.layout.simple_spinner_item);
        stationstartadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stationstartspinner.setAdapter(stationstartadapter);

        stationendspinner = view.findViewById(R.id.stationendspinner);
        ArrayAdapter<CharSequence> stationendadapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.stations_arrays, android.R.layout.simple_spinner_item);
        stationendadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stationendspinner.setAdapter(stationendadapter);

        boundspinner = view.findViewById(R.id.boundspinner);
        ArrayAdapter<CharSequence> boundadapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.bound_arrays, android.R.layout.simple_spinner_item);
        boundadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boundspinner.setAdapter(boundadapter);

        sectorcodespinner = view.findViewById(R.id.sectorcodespinner);
        ArrayAdapter<CharSequence> sectorcodeadapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.sector_arrays, android.R.layout.simple_spinner_item);
        sectorcodeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectorcodespinner.setAdapter(sectorcodeadapter);

        accompaniedbyspinner = view.findViewById(R.id.accompaniedbyspinner);
        ArrayAdapter<CharSequence> accompaniedbyadapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.accompany_array, android.R.layout.simple_spinner_item);
        accompaniedbyadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accompaniedbyspinner.setAdapter(accompaniedbyadapter);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inspection_details = new JSONObject();
                try {
                    inspection_details.put("Start", stationstartspinner.getSelectedItem().toString());
                    inspection_details.put("End", stationendspinner.getSelectedItem().toString());
                    inspection_details.put("Bound", boundspinner.getSelectedItem().toString());
                    inspection_details.put("Sector", sectorcodespinner.getSelectedItem().toString());
                    inspection_details.put("Accompanied", accompaniedbyspinner.getSelectedItem().toString());

                    left.setVisibility(View.VISIBLE);
                    right.setVisibility(View.VISIBLE);
                    instance = new InspectionOverview(fab, left, middle, right, btm_bar, getContext());
                    instance.setInspectionStatus(inspection_details);
                    FragmentTransaction ft = mFrgAct.getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.homepage, instance);
                    Log.i("NewInspectionFragment", "Back stack count " + Integer.toString(mFrgAct.getSupportFragmentManager().getBackStackEntryCount()));
                    ft.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
