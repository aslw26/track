package lta.amazoning.track;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomepageFragment extends Fragment {

    // True iff the shadow view between the card header and the RecyclerView
    // is currently showing.
    public boolean navigated_inspection = false;
    public boolean navigated_upload = false;

    private InspectionOverview instance_inspection;
    private NewInspectionFragment new_inspection;

    private FragmentActivity mFrgAct;
    private Button button;
    private Intent mIntent;
    private FloatingActionButton fab;
    private Button left_button;
    private Button middle_button;
    private Button right_button;
    private BottomAppBar btm_bar;

    public HomepageFragment() {
        // doesn't do anything special
    }

    public HomepageFragment(FloatingActionButton fab, Button left_button, Button middle_button, Button right_button, BottomAppBar btm_bar) {
        this.fab = fab;
        this.left_button = left_button;
        this.middle_button = middle_button;
        this.right_button = right_button;
        this.btm_bar = btm_bar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_homepage3, null);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        button = view.findViewById(R.id.inspection_button);

        if (new_inspection != null && new_inspection.inspection_details != null) {
            left_button.setText("Settings");
            button.setTextColor(getResources().getColor(android.R.color.black));
            button.setBackgroundTintList(getResources().getColorStateList(R.color.btn_available));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openInspectionOverview();
                }
            });
        }
        button = view.findViewById(R.id.logout_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View cv){
                openMainActivity();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewInspectionFragment();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFrgAct = getActivity();
        mIntent = mFrgAct.getIntent();
    }

    public void openNewInspectionFragment(){
        new_inspection = new NewInspectionFragment(this.fab, this.left_button, this.middle_button, this.right_button, this.btm_bar);
        FragmentTransaction ft = mFrgAct.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.homepage, new_inspection);
        ft.addToBackStack("NewInspection");
        ft.commit();
        navigated_inspection = true;
    }

    public void openInspectionOverview() {
        instance_inspection = new_inspection.instance;
        FragmentTransaction ft = mFrgAct.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.homepage, instance_inspection);
        ft.addToBackStack("Inspection");
        ft.commit();
        Log.i("HomepageFragment", "Back stack count " + Integer.toString(mFrgAct.getSupportFragmentManager().getBackStackEntryCount()));
        navigated_inspection = true;
        navigated_upload = instance_inspection.navigated_upload;
    }

    public void openMainActivity(){
        Intent intent = new Intent(mFrgAct.getApplicationContext(), MainActivity.class);
        mFrgAct.startActivity(intent);
    }

}