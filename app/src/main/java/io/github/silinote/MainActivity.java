//    The GNU General Public License does not permit incorporating this program
//    into proprietary programs.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.

package io.github.silinote;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity;

import java.io.IOException;

import id.ionbit.ionalert.IonAlert;
import io.github.silinote.tools.ExportImport;
import io.github.silinote.ui.Alert;

public class MainActivity extends CyaneaAppCompatActivity {

	BottomNavigationView mNavView;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Activity activity = this;
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				Uri uri = data.getData();
				IonAlert alert = new Alert(this).exporting();
				alert.show();
				new Thread(() -> {
					try {
						new ExportImport(this).export(uri);
						mNavView.post(() -> {
							alert.dismissWithAnimation();
							new Alert(activity).exported();
						});
					} catch (IOException | NullPointerException i) {
						mNavView.post(() -> {
							alert.dismissWithAnimation();
							new Alert(activity).error(i.toString());
						});
					}
				}).start();
			} else if (requestCode == 2) {
				Uri uri = data.getData();
				IonAlert alert = new Alert(this).importing();
				alert.show();
				new Thread(() -> {
					try {
						new ExportImport(this).import_(uri);
						mNavView.post(() -> {
							alert.dismissWithAnimation();
							new Alert(activity).imported();
						});
					} catch (IOException | NullPointerException i) {
						mNavView.post(() -> {
							alert.dismissWithAnimation();
							new Alert(activity).error(i.toString());
						});
					}
				}).start();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mNavView = findViewById(R.id.nav_view);
		AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_notes,
				R.id.navigation_configuration, R.id.navigation_about).build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
		NavigationUI.setupWithNavController(mNavView, navController);
	}

	public void startExportProcess() {
		Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
		intent.setType("text/plain");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, 1);
	}

	public void startImportProcess() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.setType("text/plain");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, 2);
	}
}