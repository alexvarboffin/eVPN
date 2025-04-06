package ai.free.vpn.tweeqoldvpn.getvpn.activity.settings;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

import androidx.preference.PreferenceCategory;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import android.widget.TextView;

import ai.free.vpn.tweeqoldvpn.getvpn.R;

import com.walhalla.vpnapp.model.Server;

import ai.free.vpn.tweeqoldvpn.getvpn.OtUtils;
import ai.free.vpn.tweeqoldvpn.getvpn.util.CountriesNames;
import ai.free.vpn.tweeqoldvpn.getvpn.util.PropertiesService;
import nl.invissvenska.numberpickerpreference.NumberDialogPreference;
import nl.invissvenska.numberpickerpreference.NumberPickerPreferenceDialogFragment;

import com.walhalla.vpnapp.repository.ServerRepository;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private static final String DIALOG_FRAGMENT_TAG = "CustomPreferenceDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        // Load the array of strings from resources
        Resources res = getResources();
        String[] randomStrings = res.getStringArray(R.array.random_strings);

        // Select a random string from the array
        String selectedString = OtUtils.getRandomString(randomStrings);
        TextView texts = findViewById(R.id.texts);
        texts.setText(selectedString);

        toolbar = findViewById(R.id.preferenceToolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationOnClickListener(v -> finish());

        //App application = (App) getApplication();

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.preferenceContent, new MyPreferenceFragment());
            transaction.commit();
        }
    }


    public static class MyPreferenceFragment extends PreferenceFragmentCompat {

        private PropertiesService properties;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            properties = new PropertiesService(getActivity());
        }

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
            ListPreference listPreference = findPreference(PropertiesService.SELECTED_COUNTRY);

            ServerRepository dbHelper = ai.free.vpn.tweeqoldvpn.getvpn.database.DBHelper.newInstance(getActivity().getApplicationContext());
            dbHelper.getUniqueCountries(new ServerRepository.DataCallback<List<Server>>() {
                @Override
                public void successResult(List<Server> countryList) {
                    String[] entriesValues = new String[countryList.size()];
                    String[] entries = new String[countryList.size()];

                    for (int i = 0; i < countryList.size(); i++) {
                        entriesValues[i] = countryList.get(i).getCountryLong();
                        String localeCountryName = CountriesNames.getCountries().get(countryList.get(i).getCountryShort()) != null ?
                                CountriesNames.getCountries().get(countryList.get(i).getCountryShort()) :
                                countryList.get(i).getCountryLong();
                        entries[i] = localeCountryName;
                    }

                    if (entries.length == 0) {
                        PreferenceCategory countryPriorityCategory = findPreference("countryPriorityCategory");
                        if (countryPriorityCategory != null) {
                            getPreferenceScreen().removePreference(countryPriorityCategory);
                        }
                    } else {
                        if (listPreference != null) {
                            listPreference.setEntries(entries);
                            listPreference.setEntryValues(entriesValues);
                            if (properties.getSelectedCountry() == null)
                                listPreference.setValueIndex(0);
                        }
                    }
                }

                @Override
                public void showError(String m) {

                }
            });
        }

        @Override
        public void onDisplayPreferenceDialog(@NonNull Preference preference) {
            if (preference instanceof NumberDialogPreference) {
                NumberDialogPreference dialogPreference = (NumberDialogPreference) preference;
                DialogFragment dialogFragment = NumberPickerPreferenceDialogFragment
                        .newInstance(
                                dialogPreference.getKey(),
                                dialogPreference.getMinValue(),
                                dialogPreference.getMaxValue(),
                                dialogPreference.getStepValue(),
                                dialogPreference.getUnitText()
                        );
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(getParentFragmentManager(), DIALOG_FRAGMENT_TAG);
            } else {
                super.onDisplayPreferenceDialog(preference);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
