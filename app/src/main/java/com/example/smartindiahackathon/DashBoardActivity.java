package com.example.smartindiahackathon;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.smartindiahackathon.localData.UserManager;
import com.example.smartindiahackathon.login.emailLogin.LoginWithEmailActivity;
import com.example.smartindiahackathon.login.emailLogin.RegisterActivity;
import com.example.smartindiahackathon.model.UserDataModel;
import com.example.smartindiahackathon.ui.epubReader.EpubReaderMainActivity;
import com.example.smartindiahackathon.ui.pdfReader.pdfdigitalsignature.DigitalSignatureActivity;
import com.example.smartindiahackathon.utils.Utils;
import com.google.android.datatransport.runtime.logging.Logging;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartindiahackathon.databinding.ActivityDashBoardBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.List;

import ru.bartwell.exfilepicker.data.ExFilePickerResult;

public class DashBoardActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDashBoardBinding binding;
    NavigationView navigationView;
    DrawerLayout drawer;
    Switch switchBtn;
    Boolean isClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDashBoard.toolbar);
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dash_board);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            //   int id=menuItem.getItemId();
            //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
            switch (menuItem.getItemId()) {

                case R.id.Sign_Up: {
                    Intent intent = new Intent(this, RegisterActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.Login: {
                    Intent intent = new Intent(this, LoginWithEmailActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.Logout: {
                    openLogoutDialog();
                    break;
                }
            }
            //This is for maintaining the behavior of the Navigation view
            NavigationUI.onNavDestinationSelected(menuItem, navController);
            //This is for closing the drawer after acting on it
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
        checkUserFullyRegisterOrNot();
        switchBtn = findViewById(R.id.switch4);
        switchBtn.setChecked(accessibilityEnable(DashBoardActivity.this));
        switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isClick) {
                isClick=false;
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_dash_board);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        isClick=true;
        switchBtn.setChecked(accessibilityEnable(DashBoardActivity.this));
        checkUserFullyRegisterOrNot();
    }

    private void checkUserFullyRegisterOrNot() {
        if (Utils.isNetworkConnected(this)) {
            UserDataModel profileData = UserManager.INSTANCE.getQuestionariesData();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu nav_Menu = navigationView.getMenu();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.userName);
            TextView txtNumber = (TextView) headerView.findViewById(R.id.txtNumber);
            navUsername.setText("Guest User");
            txtNumber.setText("");
            if (profileData == null) {
                nav_Menu.findItem(R.id.Logout).setVisible(false);
                nav_Menu.findItem(R.id.Sign_Up).setVisible(true);
                nav_Menu.findItem(R.id.Login).setVisible(true);
            } else {
                nav_Menu.findItem(R.id.Logout).setVisible(true);
                nav_Menu.findItem(R.id.Sign_Up).setVisible(false);
                nav_Menu.findItem(R.id.Login).setVisible(false);
                //**************set header data******************//
                navUsername.setText(profileData.getName());
                txtNumber.setText(profileData.getPhoneNo());
            }
        }
    }

    private void openLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Are you sure you want to Logout?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // Continue with delete operation
                    FirebaseAuth.getInstance().signOut();
                    //  LoginManager.getInstance().logOut();  facebook authcode use in future
                    UserManager.INSTANCE.deleteQuestionaries();
                    Intent i = new Intent(this, DashBoardActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            ExFilePickerResult result = ExFilePickerResult.getFromIntent(data);
            if (result != null && result.getCount() > 0) {
//                Intent I = new Intent(this, EpubReaderMainActivity.class);
//                I.putExtra("doc_location", result.getPath() + result.getNames().get(0));
//                startActivity(I);
//                File targetFile = new File( result.getPath()+ result.getNames().get(0));
//                Uri targetUri = Uri.fromFile(targetFile);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(targetUri, "application/*");
//                startActivityForResult(intent, 100);
               // openDocument(result.getPath());
            }
        }
        if (requestCode == 3) {
            ExFilePickerResult result = ExFilePickerResult.getFromIntent(data);
            if (result != null && result.getCount() > 0) {
                Intent I = new Intent(this, EpubReaderMainActivity.class);
                I.putExtra("epub_location", result.getPath() + result.getNames().get(0));
                startActivity(I);
            }
        }
    }

    public void openDocument(String name) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        File file = new File(name);
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (extension.equalsIgnoreCase("") || mimetype == null) {
            // if there is no extension or there is no definite mimetype, still try to open the file
            intent.setDataAndType(Uri.fromFile(file), "text/*");
        } else {
            intent.setDataAndType(Uri.fromFile(file), mimetype);
        }
        // custom message for the intent
        startActivity(Intent.createChooser(intent, "Choose an Application:"));
    }

    private static final String TALKBACK_SETTING_ACTIVITY_NAME = "com.android.talkback.TalkBackPreferencesActivity";

    public static boolean accessibilityEnable(Context context) {
        boolean enable = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            try {
                AccessibilityManager manager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
                List<AccessibilityServiceInfo> serviceList = manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN);
                for (AccessibilityServiceInfo serviceInfo : serviceList) {
                    String name = serviceInfo.getSettingsActivityName();
                    if (!TextUtils.isEmpty(name) && name.equals(TALKBACK_SETTING_ACTIVITY_NAME)) {
                        enable = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return enable;
    }

    private void updateTalkBackState(boolean enableTalkBack) {
        if (enableTalkBack) {
            enableAccessibilityService(TALKBACK_SETTING_ACTIVITY_NAME);
        } else {
            disableAccessibilityServices();
        }
    }

    private void enableAccessibilityService(String name) {
        Settings.Secure.putString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, name);
        Settings.Secure.putString(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, "1");
    }

    private void disableAccessibilityServices() {
        Settings.Secure.putString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "");
        Settings.Secure.putString(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, "0");
    }

}