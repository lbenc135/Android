package com.licoforen.parentalcontrollauncher;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.Time;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.licoforen.parentalcontrollauncher.Adapters.DrawerAdapter;
import com.licoforen.parentalcontrollauncher.Adapters.HomePagerAdapter;
import com.licoforen.parentalcontrollauncher.Adapters.WidgetsListAdapter;
import com.licoforen.parentalcontrollauncher.Animations.CubeInsidePageTransformer;
import com.licoforen.parentalcontrollauncher.Animations.DepthCardsPageTransformer;
import com.licoforen.parentalcontrollauncher.Animations.DepthPageTransformer;
import com.licoforen.parentalcontrollauncher.Animations.FadePageTransformer;
import com.licoforen.parentalcontrollauncher.Animations.RotationPageTransformer;
import com.licoforen.parentalcontrollauncher.Animations.RotationXPageTransformer;
import com.licoforen.parentalcontrollauncher.Animations.ScaleAlphaPageTransformer;
import com.licoforen.parentalcontrollauncher.Animations.ScalePageTransformer;
import com.licoforen.parentalcontrollauncher.Animations.ScaleXPageTransformer;
import com.licoforen.parentalcontrollauncher.Animations.ZoomOutPageTransformer;
import com.licoforen.parentalcontrollauncher.CustomViews.DeleteZone;
import com.licoforen.parentalcontrollauncher.CustomViews.ImageCell;
import com.licoforen.parentalcontrollauncher.CustomViews.ScrollPanel;
import com.licoforen.parentalcontrollauncher.CustomViews.WidgetHostView;
import com.licoforen.parentalcontrollauncher.Helpers.Database;
import com.licoforen.parentalcontrollauncher.Helpers.ResourceLoader;
import com.licoforen.parentalcontrollauncher.Helpers.SortApps;
import com.licoforen.parentalcontrollauncher.Helpers.SortWidgets;
import com.licoforen.parentalcontrollauncher.Interfaces.DragDropPresenter;
import com.licoforen.parentalcontrollauncher.Interfaces.DragSource;
import com.licoforen.parentalcontrollauncher.Interfaces.DropTarget;
import com.licoforen.parentalcontrollauncher.listeners.ClickListener;
import com.licoforen.parentalcontrollauncher.listeners.LocationTracker;
import com.licoforen.parentalcontrollauncher.listeners.TouchListener;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.LinePageIndicator;

public class MainActivity extends Activity implements DragDropPresenter,
		View.OnLongClickListener {

	public static class Pack {
		public Drawable icon;
		public String name, label;
		public String packageName;
	}

	static Pack packs[] = new Pack[0], allowedPacks[] = new Pack[0];
	static List<AppWidgetProviderInfo> widgetList;
	private static AppWidgetProviderInfo allowedWidgets[] = new AppWidgetProviderInfo[100];
	private static PackageManager pm;

	private static GridView drawerGrid;
	private static GridLayout homeGrid[], dockGrid;
	private static RelativeLayout drawer, homeView;
	private static TextView noApps;

	static int home_width, home_height, homeItem_width, homeItem_height,
			dockItem_width, dockItem_height;

	private static DragController mDragController;
	private static DeleteZone mDeleteZone;

	private Database db = new Database(this);
	private Intent homeData[][], dockData[] = new Intent[5],
			widgetData[] = new Intent[50];

	private static int homeItems, animLength;
	private static final int REQUEST_CREATE_SHORTCUT = 134,
			REQUEST_PICK_SHORTCUT = 645;
	private static float scale;

	private static ViewPager viewPager;
	ScrollPanel panel_left, panel_right;

	private BroadcastReceiver packReciever = new PackReceiver();
	private static ContentResolver contentResolver;

	private static SharedPreferences sp;
	private static SharedPreferences.Editor editor;

	private WallpaperManager wallpaperManager;

	private AppWidgetManager mAppWidgetManager;
	private WidgetHost mAppWidgetHost;
	private static final int REQUEST_PICK_APPWIDGET = 303,
			REQUEST_CREATE_APPWIDGET = 5050;

	@Override
	protected void onStart() {
		super.onStart();

		init();

		setWallpaperDimension();

		if (!isMyServiceRunning(Service.class)) {
			new LocationTracker(this);
			startService(new Intent(this, Service.class));
		}

		mAppWidgetHost.startListening();
		set_listeners();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mAppWidgetHost.stopListening();
		unregisterReceiver(packReciever);
	}

	@Override
	public void onBackPressed() {
		if (drawer.getVisibility() == 0) {
			viewPager.setAlpha(0f);
			viewPager.setVisibility(View.VISIBLE);
			viewPager.animate().alpha(1f).setDuration(animLength)
					.setListener(null);
			crossfadeviews(dockGrid, drawer, animLength);
		}
	}

	private void init() {
		setContentView(R.layout.activity_main);

		Resources appRes = getResources();

		scale = appRes.getDisplayMetrics().density;
		home_width = appRes.getDisplayMetrics().widthPixels;
		home_height = appRes.getDisplayMetrics().heightPixels;

		new ResourceLoader(this, home_width, home_height, scale);

		if (ResourceLoader.homeOrientation.equals("auto")
				&& appRes.getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		} else if (ResourceLoader.homeOrientation.equals("portrait")
				&& appRes.getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else if (ResourceLoader.homeOrientation.equals("landscape")
				&& appRes.getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}

		if (home_width < home_height) {
			homeItem_width = home_width / ResourceLoader.numHomeCols;
			homeItem_height = (home_height - dp(ResourceLoader.dockHeight + 20))
					/ ResourceLoader.numHomeRows;
			dockItem_width = home_width / 5;
			dockItem_height = dp(ResourceLoader.dockHeight);
		} else {
			homeItem_width = (home_width - dp(ResourceLoader.dockHeight + 10))
					/ ResourceLoader.numHomeCols;
			homeItem_height = (home_height - dp(20))
					/ ResourceLoader.numHomeRows;
			dockItem_width = dp(ResourceLoader.dockHeight);
			dockItem_height = (home_height - dp(20)) / 5;
		}

		homeItems = ResourceLoader.numHomeCols * ResourceLoader.numHomeRows;
		animLength = ResourceLoader.animLength;

		pm = getPackageManager();
		homeView = (RelativeLayout) findViewById(R.id.home_view);
		drawerGrid = (GridView) findViewById(R.id.content);
		drawer = (RelativeLayout) findViewById(R.id.drawer);
		drawer.setVisibility(View.GONE);
		drawer.setBackgroundColor(ResourceLoader.drawerColor);
		dockGrid = (GridLayout) findViewById(R.id.dock_grid);
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) dockGrid
				.getLayoutParams();
		if (home_width < home_height) {
			lp.setMargins(0, 0, 0, dp(home_height > 600 ? 15 : 0));
			drawerGrid.setPadding(10, 5, 10, 5);
		} else {
			lp.setMargins(0, 0, dp(home_height > 600 ? 15 : 0), 0);
			drawerGrid.setPadding(30, 15, 30, 15);
		}
		dockGrid.setLayoutParams(lp);
		homeGrid = new GridLayout[ResourceLoader.homeScreens];
		for (int i = 0; i < ResourceLoader.homeScreens; i++) {
			homeGrid[i] = new GridLayout(this);
			homeGrid[i].setRowCount(ResourceLoader.numHomeRows);
			homeGrid[i].setColumnCount(ResourceLoader.numHomeCols);
			registerForContextMenu(homeGrid[i]);
		}
		noApps = (TextView) findViewById(R.id.no_apps);

		mDragController = new DragController(this);
		mDeleteZone = (DeleteZone) findViewById(R.id.delete_zone_view);
		mDeleteZone.setOnDragListener(mDragController);
		mDeleteZone.setY(mDeleteZone.getY() - dp(70));

		homeData = new Intent[ResourceLoader.homeScreens][homeItems];

		wallpaperManager = WallpaperManager.getInstance(this);

		mAppWidgetManager = AppWidgetManager.getInstance(this);
		mAppWidgetHost = new WidgetHost(this, R.id.APPWIDGET_HOST_ID);
		widgetList = mAppWidgetManager.getInstalledProviders();
		new SortWidgets().sort(widgetList);

		set_drawer();

		set_home();

		set_dock();

		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(new HomePagerAdapter(homeGrid));
		OnPageChangeListener pageChangeListener = new OnPageScroll();
		viewPager.setOnPageChangeListener(pageChangeListener);
		switch (ResourceLoader.homePageIndicator) {
		case "line":
			LinePageIndicator pageIndicator = new LinePageIndicator(this);
			pageIndicator.setViewPager(viewPager);
			pageIndicator.setOnPageChangeListener(pageChangeListener);
			pageIndicator.setPadding(0, 5, 0, 0);
			homeView.addView(pageIndicator, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			break;
		case "circle":
			CirclePageIndicator pageIndicator2 = new CirclePageIndicator(this);
			pageIndicator2.setViewPager(viewPager);
			pageIndicator2.setOnPageChangeListener(pageChangeListener);
			pageIndicator2.setPadding(0, 5, 0, 0);
			homeView.addView(pageIndicator2, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			break;
		}

		if (ResourceLoader.defaultScreen > ResourceLoader.homeScreens)
			ResourceLoader.defaultScreen = ResourceLoader.homeScreens;
		viewPager.setCurrentItem(ResourceLoader.defaultScreen - 1);
		switch (ResourceLoader.homeAnimation) {
		case "zoomOut":
			viewPager.setPageTransformer(false, new ZoomOutPageTransformer());
			break;
		case "stack":
			viewPager.setPageTransformer(false, new DepthPageTransformer());
			break;
		case "cubeinside":
			viewPager
					.setPageTransformer(false, new CubeInsidePageTransformer());
			break;
		case "cards":
			viewPager
					.setPageTransformer(false, new DepthCardsPageTransformer());
			break;
		case "fade":
			viewPager.setPageTransformer(false, new FadePageTransformer());
			break;
		case "rotation":
			viewPager.setPageTransformer(false, new RotationPageTransformer());
			break;
		case "hrotation":
			viewPager.setPageTransformer(false, new RotationXPageTransformer());
			break;
		case "shrink":
			viewPager
					.setPageTransformer(false, new ScaleAlphaPageTransformer());
			break;
		case "scale":
			viewPager.setPageTransformer(false, new ScalePageTransformer());
			break;
		case "smoothscale":
			viewPager.setPageTransformer(false, new ScaleXPageTransformer());
			break;
		default:
			break;
		}

		switch (ResourceLoader.dockBackground) {
		case "glass":
			if (home_width < home_height) {
				dockGrid.setBackgroundResource(R.drawable.glass_board);
			} else {
				dockGrid.setBackgroundResource(R.drawable.glass_board_land);
			}
			break;
		}

		panel_left = (ScrollPanel) findViewById(R.id.left_panel);
		panel_left.setPager(viewPager);
		panel_left.setOnDragListener(mDragController);
		panel_right = (ScrollPanel) findViewById(R.id.right_panel);
		panel_right.setPager(viewPager);
		panel_right.setOnDragListener(mDragController);

		if (ResourceLoader.passwordHash == "") {
			createPassword();
		}

		sp = getSharedPreferences(
				"com.licoforen.parentalcontrollauncher_preferences",
				Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	public void set_dock() {
		dockData = db.readDockData();

		for (int i = 0; i < 5; i++) {
			GridLayout.LayoutParams lp;
			if (home_width < home_height) {
				lp = new GridLayout.LayoutParams(GridLayout.spec(0),
						GridLayout.spec(i));
			} else {
				lp = new GridLayout.LayoutParams(GridLayout.spec(4 - i),
						GridLayout.spec(0));
			}
			lp.height = dockItem_height;
			lp.width = dockItem_width;

			if (i == 2) {
				ImageView iv = new ImageView(this);
				iv.setImageResource(R.drawable.drawer_open);
				iv.setOnTouchListener(new TouchListener());
				iv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						viewPager.animate().alpha(0f).setDuration(animLength)
								.setListener(new AnimatorListenerAdapter() {
									@Override
									public void onAnimationEnd(
											Animator animation) {
										viewPager.setVisibility(View.INVISIBLE);
									}
								});
						crossfadeviews(drawer, dockGrid, animLength);
					}
				});
				int pad = dp(ResourceLoader.dockPadding);
				iv.setPadding(pad, pad, pad, pad);
				dockGrid.addView(iv, lp);
				continue;
			}

			ImageCell v = new ImageCell(this, lp.width, lp.height, true, false,
					dp(ResourceLoader.dockPadding));

			if (dockData[i] != null) {
				ActivityInfo ai;
				try {
					if (isInPacks(dockData[i].getComponent().getClassName())) {
						ai = pm.getActivityInfo(dockData[i].getComponent(), 0);
						v.icon.setImageDrawable(ai.loadIcon(pm));
						v.text.setText(ai.loadLabel(pm));
						v.name = ai.name;
						v.packageName = ai.packageName;
					}
					v.mEmpty = false;
					v.setOnTouchListener(new TouchListener());
					v.setOnClickListener(new ClickListener(this, pm));
					if (isDragDropEnabled())
						v.setOnLongClickListener((OnLongClickListener) this);
				} catch (NameNotFoundException e1) {
					v.icon.setImageDrawable(null);
					v.text.setText("");
					dockData[i] = null;
					updateDatabase();
				}
			} else {
				v.icon.setImageDrawable(null);
				v.text.setText("");
			}

			v.gridPos = i;
			v.setOnDragListener(mDragController);

			dockGrid.addView(v, lp);
		}
	}

	public void set_drawer() {
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> packsList = pm.queryIntentActivities(mainIntent, 0);
		int len = packsList.size();
		packs = new Pack[len];
		int allowed = 0;
		for (int i = 0; i < len; i++) {
			packs[i] = new Pack();
			packs[i].label = packsList.get(i).loadLabel(pm).toString();
			packs[i].name = packsList.get(i).activityInfo.name;
			packs[i].packageName = packsList.get(i).activityInfo.packageName;
			if (ResourceLoader.allowedApps != null
					&& ResourceLoader.allowedApps
							.contains(packsList.get(i).activityInfo.packageName)) {
				packs[i].icon = packsList.get(i).loadIcon(pm);
				allowed++;
			}
		}
		new SortApps().sort(packs);

		allowedPacks = new Pack[allowed];
		for (int i = 0, it = 0; i < len; i++) {
			if (packs[i].icon != null) {
				allowedPacks[it] = packs[i];
				it++;
			}
		}

		drawerGrid.setAdapter(new DrawerAdapter(this, allowedPacks, pm, dp(60),
				dp(90)));
		if (allowed == 0) {
			noApps.setTextColor(Color.GRAY);
			noApps.setVisibility(View.VISIBLE);
		} else {
			noApps.setVisibility(View.INVISIBLE);
		}
	}

	private void set_home() {
		int widgets = 0;
		homeData = db.readHomeData(homeItems, ResourceLoader.homeScreens);
		for (int k = 0; k < ResourceLoader.homeScreens; k++) {
			for (int i = 0; i < ResourceLoader.numHomeRows; i++) {
				for (int j = 0; j < ResourceLoader.numHomeCols; j++) {
					ImageCell v = new ImageCell(this, homeItem_width,
							homeItem_height, false, true,
							dp(ResourceLoader.homePadding));
					int pos = i * ResourceLoader.numHomeCols + j;

					if (homeData[k][pos] != null) {
						if (homeData[k][pos].getIntExtra("appWidgetId", -1) != -1) {
							widgetData[widgets] = homeData[k][pos];
							widgets++;
						} else if (homeData[k][pos].getBooleanExtra(
								"isShortcut", false) != false) {
							Pack info = infoFromShortcutIntent(this,
									homeData[k][pos]);
							v.icon.setImageDrawable(info.icon);
							v.text.setText(info.label);
							v.packageName = info.packageName;
							v.name = info.name;
						} else {
							try {
								if (isInPacks(homeData[k][pos].getComponent()
										.getClassName())) {
									ActivityInfo ai = pm.getActivityInfo(
											homeData[k][pos].getComponent(), 0);
									v.icon.setImageDrawable(ai.loadIcon(pm));
									if (ResourceLoader.homescrlabels)
										v.text.setText(ai.loadLabel(pm));
									v.name = ai.name;
									v.packageName = ai.packageName;
								} else {
									v.name = null;
									homeData[k][pos] = null;
								}
							} catch (NameNotFoundException e) {
								v.name = null;
								homeData[k][pos] = null;
							}
						}
						v.mEmpty = false;
						v.setOnTouchListener(new TouchListener());
						v.setOnClickListener(new ClickListener(this, pm));
						if (isDragDropEnabled())
							v.setOnLongClickListener((OnLongClickListener) this);
					} else {
						v.icon.setImageDrawable(null);
						v.text.setText("");
					}

					v.setOnDragListener(mDragController);
					int pad = dp(ResourceLoader.homePadding);
					v.setPadding(pad, pad, pad, pad);
					v.gridPos = pos;

					GridLayout.LayoutParams lp = new GridLayout.LayoutParams(
							GridLayout.spec(i), GridLayout.spec(j));
					homeGrid[k].addView(v, lp);
				}
			}
		}
		set_widgets(widgets);
	}

	private void set_widgets(int widgets) {
		for (int i = 0; i < widgets; i++) {
			createWidget(widgetData[i]);
		}

		int it = 0;
		AppWidgetProviderInfo temp[] = new AppWidgetProviderInfo[100];
		for (int i = 0; i < widgetList.size(); i++) {
			if (ResourceLoader.allowedWidgets != null
					&& ResourceLoader.allowedWidgets
							.contains(widgetList.get(i).provider.getClassName())) {
				temp[it] = widgetList.get(i);
				it++;
			}
		}
		allowedWidgets = new AppWidgetProviderInfo[it];
		for (int i = 0; i < it; i++) {
			allowedWidgets[i] = temp[i];
		}
	}

	private void set_listeners() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(packReciever, filter);

		contentResolver = getContentResolver();
		contentResolver.registerContentObserver(Uri.parse("content://sms/"),
				true, new outSmsObserver(new Handler()));
	}

	private boolean isInPacks(String name) {
		boolean res = false;
		int len = allowedPacks.length;
		for (int i = 0; i < len; i++) {
			if (allowedPacks[i].name.equals(name)) {
				res = true;
				break;
			}
		}

		return res;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.addmenu, menu);
		if (!ResourceLoader.shortcutsAllowed)
			menu.removeItem(R.id.Shortcut);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.Wallpaper:
			Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
			startActivity(Intent.createChooser(intent, "Select Wallpaper"));
			break;
		case R.id.Widgets:
			selectWidget();
			break;
		case R.id.Settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		case R.id.Shortcut:
			pickShortcut(REQUEST_PICK_SHORTCUT, "Pick shortcut");
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.appmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.Wallpaper:
			Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
			startActivity(Intent.createChooser(intent, "Select Wallpaper"));
			break;
		case R.id.Widgets:
			selectWidget();
			break;
		case R.id.Settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_PICK_APPWIDGET) {
				configureWidget(data);
			} else if (requestCode == REQUEST_CREATE_APPWIDGET) {
				createWidget(data);
			} else if (requestCode == REQUEST_PICK_SHORTCUT) {
				startActivityForResult(data, REQUEST_CREATE_SHORTCUT);
			} else if (requestCode == REQUEST_CREATE_SHORTCUT) {
				completeAddShortcut(data);
			}
		} else if (resultCode == RESULT_CANCELED && data != null) {
			int appWidgetId = data.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
			if (appWidgetId != -1) {
				mAppWidgetHost.deleteAppWidgetId(appWidgetId);
			}
		}
	}

	void selectWidget() {
		final int appWidgetId = mAppWidgetHost.allocateAppWidgetId();

		final AlertDialog d = new AlertDialog.Builder(this)
				.setTitle("Pick widget")
				.setAdapter(
						new WidgetsListAdapter(this, 0, allowedWidgets, dp(60)),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int pos) {
								Intent data = new Intent(
										AppWidgetManager.ACTION_APPWIDGET_BIND);
								data.putExtra(
										AppWidgetManager.EXTRA_APPWIDGET_ID,
										appWidgetId);
								data.putExtra(
										AppWidgetManager.EXTRA_APPWIDGET_PROVIDER,
										allowedWidgets[pos].provider);
								startActivityForResult(data,
										REQUEST_PICK_APPWIDGET);
							}
						}).create();

		d.show();
	}

	private void configureWidget(Intent data) {
		Bundle extras = data.getExtras();
		int appWidgetId = extras
				.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
		AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager
				.getAppWidgetInfo(appWidgetId);
		if (appWidgetInfo.configure != null) {
			Intent intent = new Intent(
					AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
			intent.setComponent(appWidgetInfo.configure);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
		} else {
			createWidget(data);
		}
	}

	public void createWidget(Intent data) {
		Bundle extras = data.getExtras();
		int appWidgetId = extras
				.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
		if (appWidgetId == -1)
			return;
		AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager
				.getAppWidgetInfo(appWidgetId);
		if (appWidgetInfo == null)
			return;
		WidgetHostView hostView = (WidgetHostView) mAppWidgetHost.createView(
				this, appWidgetId, appWidgetInfo);
		hostView.setAppWidget(appWidgetId, appWidgetInfo);
		if (!ResourceLoader.lockDesktop)
			hostView.setOnLongClickListener(new WidgetLongClickListener());

		int pos, s, w, h;
		if (extras.getString("scrPos") != null) {
			pos = Integer.parseInt(extras.getString("scrPos"));
			s = Integer.parseInt(extras.getString("xPos"))
					+ Integer.parseInt(extras.getString("yPos"))
					* ResourceLoader.numHomeCols;
			w = Integer.parseInt(extras.getString("width"));
			h = Integer.parseInt(extras.getString("height"));
		} else {
			w = (appWidgetInfo.minWidth + homeItem_width - 1) / homeItem_width;
			h = (appWidgetInfo.minHeight + homeItem_height - 1)
					/ homeItem_height;
			pos = viewPager.getCurrentItem();
			s = findFreePlace(w, h);
		}

		while (w > ResourceLoader.numHomeCols)
			w--;
		while (h > ResourceLoader.numHomeRows)
			h--;

		if (w == 0 || h == 0)
			return;

		hostView.h = h;
		hostView.w = w;

		if (s == -1)
			Toast.makeText(this, "No enough room on this screen",
					Toast.LENGTH_SHORT).show();
		else {
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					((ImageCell) homeGrid[pos].getChildAt(s + j + i
							* ResourceLoader.numHomeCols)).mEmpty = false;
			ImageCell ic = (ImageCell) homeGrid[pos].getChildAt(s);
			ic.name = data.toUri(0);
			ic.spanX = w;
			ic.spanY = h;
			GridLayout.LayoutParams lp = new GridLayout.LayoutParams(
					GridLayout.spec(s / 4, h), GridLayout.spec(s % 4, w));
			lp.width = w * homeItem_width;
			lp.height = h * homeItem_height;
			hostView.updateAppWidgetSize(null, lp.width, lp.height, lp.width,
					lp.height);
			homeGrid[pos].addView(hostView, lp);
		}
		if (extras.getString("scrPos") == null)
			updateDatabase();
	}

	public void moveWidget(WidgetHostView v, int pos, int w, int h,
			int startCell, int startScr) {
		int scr = viewPager.getCurrentItem();
		ImageCell source = (ImageCell) homeGrid[startScr].getChildAt(startCell), destination = (ImageCell) homeGrid[scr]
				.getChildAt(pos);
		if (pos != startCell || scr != startScr) {
			destination.name = source.name;
			destination.spanX = source.spanX;
			destination.spanY = source.spanY;
			source.name = null;
			source.spanX = 0;
			source.spanY = 0;
		}
		for (int i = 0; i < h; i++)
			for (int j = 0; j < w; j++)
				((ImageCell) homeGrid[scr].getChildAt(pos + j + i
						* ResourceLoader.numHomeCols)).mEmpty = false;
		GridLayout.LayoutParams lp = new GridLayout.LayoutParams(
				GridLayout.spec(pos / ResourceLoader.numHomeCols, h),
				GridLayout.spec(pos % ResourceLoader.numHomeCols, w));
		lp.width = w * homeItem_width;
		lp.height = h * homeItem_height;

		if (startScr != scr) {
			homeGrid[startScr].removeView(v);
			homeGrid[scr].addView(v, lp);
		} else {
			v.setLayoutParams(lp);
		}

		v.updateAppWidgetSize(null, lp.width, lp.height, lp.width, lp.height);
		updateDatabase();
	}

	public void removeWidget(WidgetHostView v, int startCell) {
		((ImageCell) homeGrid[viewPager.getCurrentItem()].getChildAt(startCell)).name = null;
		((ViewGroup) v.getParent()).removeView(v);
		mAppWidgetHost.deleteAppWidgetId(v.getAppWidgetId());
		updateDatabase();
	}

	@SuppressLint("InflateParams")
	public void resizeWidget(final WidgetHostView v, final int pos) {
		LayoutInflater inflater = this.getLayoutInflater();
		View layout = inflater.inflate(R.layout.widgetsizepicker, null);
		final NumberPicker width, height;
		final AlertDialog d = new AlertDialog.Builder(this)
				.setTitle("Set widget size").setView(layout)
				.setPositiveButton("OK", null).create();

		width = (NumberPicker) layout.findViewById(R.id.width_picker);
		height = (NumberPicker) layout.findViewById(R.id.height_picker);

		d.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View btn) {
						int newW = width.getValue(), newH = height.getValue();
						if (isPlaceFree(pos, newW, newH)) {
							ImageCell ic = (ImageCell) homeGrid[viewPager
									.getCurrentItem()].getChildAt(pos);
							ic.spanX = newW;
							ic.spanY = newH;
							v.h = newH;
							v.w = newW;
							moveWidget(v, pos, newW, newH, pos,
									viewPager.getCurrentItem());
						} else {
							int newPos = findFreePlace(newW, newH);
							if (newPos != -1) {
								ImageCell ic = (ImageCell) homeGrid[viewPager
										.getCurrentItem()].getChildAt(pos);
								ic.spanX = newW;
								ic.spanY = newH;
								v.h = newH;
								v.w = newW;
								moveWidget(v, newPos, newW, newH, pos,
										viewPager.getCurrentItem());
							} else {
								Toast.makeText(getApplicationContext(),
										"No enough room on this screen",
										Toast.LENGTH_SHORT).show();
							}
						}
						d.dismiss();
					}
				});

				width.setMinValue(1);
				height.setMinValue(1);
				width.setMaxValue(ResourceLoader.numHomeCols);
				height.setMaxValue(ResourceLoader.numHomeRows);
				width.setValue(v.w);
				height.setValue(v.h);
			}
		});
		d.show();
	}

	private void pickShortcut(int requestCode, String title) {
		Bundle bundle = new Bundle();
		ArrayList<String> shortcutNames = new ArrayList<String>();
		bundle.putStringArrayList(Intent.EXTRA_SHORTCUT_NAME, shortcutNames);
		ArrayList<ShortcutIconResource> shortcutIcons = new ArrayList<ShortcutIconResource>();
		bundle.putParcelableArrayList(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				shortcutIcons);
		Intent pickIntent = new Intent(Intent.ACTION_PICK_ACTIVITY);
		pickIntent.putExtra(Intent.EXTRA_INTENT, new Intent(
				Intent.ACTION_CREATE_SHORTCUT));
		pickIntent.putExtra(Intent.EXTRA_TITLE, title);
		pickIntent.putExtras(bundle);
		startActivityForResult(pickIntent, requestCode);
	}

	private void completeAddShortcut(Intent data) {
		Pack info = infoFromShortcutIntent(this, data);
		ImageCell ic = null;
		for (int i = 0; i < homeItems; i++) {
			if (((ImageCell) homeGrid[viewPager.getCurrentItem()].getChildAt(i)).mEmpty == true) {
				ic = (ImageCell) homeGrid[viewPager.getCurrentItem()]
						.getChildAt(i);
				break;
			}
		}
		if (ic == null)
			Toast.makeText(this, "No enough room on this screen",
					Toast.LENGTH_SHORT).show();
		else {
			ic.icon.setImageDrawable(info.icon);
			ic.text.setText(info.label);
			ic.name = info.name;
			ic.packageName = info.packageName;
			ic.mEmpty = false;
			ic.setOnTouchListener(new TouchListener());
			ic.setOnClickListener(new ClickListener(this, pm));
			ic.setOnLongClickListener((OnLongClickListener) this);
			updateDatabase();
		}
	}

	@SuppressWarnings("deprecation")
	private Pack infoFromShortcutIntent(Context context, Intent data) {
		Intent intent = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
		String name = null;
		Drawable icon = null;
		ShortcutIconResource iconResource = null;
		byte[] byteArray = null;
		String imageArray = null;

		name = data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);

		if (intent != null) {
			Parcelable extra = data
					.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE);
			if (extra != null)
				iconResource = (ShortcutIconResource) extra;
			else {
				Bitmap bm = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON);
				icon = new BitmapDrawable(bm);
				ByteArrayOutputStream ostream = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.PNG, 100, ostream);
				imageArray = Base64.encodeToString(ostream.toByteArray(),
						Base64.DEFAULT);
			}
		} else {
			if (data.getStringExtra("iconBitmap") != null) {
				byteArray = Base64.decode(data.getStringExtra("iconBitmap"),
						Base64.DEFAULT);
				Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0,
						byteArray.length);
				icon = new BitmapDrawable(bm);
			} else {
				iconResource = new ShortcutIconResource();
				iconResource.packageName = data.getStringExtra("iconResPack");
				iconResource.resourceName = data.getStringExtra("iconResName");
			}
		}
		if (icon == null) {
			try {
				PackageManager pm = context.getPackageManager();
				if (iconResource != null) {
					Resources rs = pm
							.getResourcesForApplication(iconResource.packageName);
					icon = rs.getDrawable(rs.getIdentifier(
							iconResource.resourceName, null, null));
				}
			} catch (Exception e) {
			}
		}
		Pack info = new Pack();
		info.icon = icon;
		info.label = name;
		if (intent == null) {
			info.name = data.toUri(0);
		} else {
			if (iconResource != null) {
				intent.putExtra("iconResName", iconResource.resourceName);
				intent.putExtra("iconResPack", iconResource.packageName);
			} else {
				intent.putExtra("iconBitmap", imageArray);
			}
			intent.putExtra("isShortcut", true);
			intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
					data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME));
			info.name = intent.toUri(0);
		}

		return info;
	}

	@SuppressLint("InflateParams")
	private void createPassword() {
		LayoutInflater inflater = this.getLayoutInflater();
		View layout = inflater.inflate(R.layout.passwordcreate, null);
		final EditText passwordBox, passwordConfirmBox;
		final AlertDialog d = new AlertDialog.Builder(this)
				.setTitle(R.string.create_password).setView(layout)
				.setCancelable(false).setPositiveButton("OK", null).create();

		passwordBox = (EditText) layout.findViewById(R.id.password_box);
		passwordConfirmBox = (EditText) layout
				.findViewById(R.id.password_box_confirm);

		d.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.toggleSoftInput(
						InputMethodManager.SHOW_FORCED, 0);

				Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (passwordBox
								.getText()
								.toString()
								.equals(passwordConfirmBox.getText().toString())) {
							ResourceLoader.setPassword(passwordBox.getText()
									.toString());
							Toast.makeText(getApplicationContext(),
									"Password set", Toast.LENGTH_SHORT).show();
							d.dismiss();
							InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
									.getSystemService(
											Context.INPUT_METHOD_SERVICE);
							inputMethodManager.toggleSoftInput(
									InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

							setDefaultLauncher();
						} else {
							Toast.makeText(getApplicationContext(),
									"Passwords don't match!",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
		d.show();
	}

	@SuppressLint("InflateParams")
	private void setDefaultLauncher() {
		
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_NEUTRAL) {
					final Intent intent = new Intent();
					intent.setAction(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					startActivity(intent);
					
					Handler mHandler = new Handler();
					mHandler.postDelayed(new Runnable() {
			            public void run() {
			            	configSettings();
			            }
			        }, 1000);
					
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.select_launcher)
				.setNeutralButton(R.string.ok, dialogClickListener)
				.setCancelable(false).show();
	}

	private void configSettings() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_POSITIVE)
					startActivity(new Intent(getApplicationContext(),
							ParentalSettingsActivity.class));
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.config_settings)
				.setPositiveButton(R.string.yes, dialogClickListener)
				.setNegativeButton(R.string.no, dialogClickListener).show();
	}

	private void setWallpaperDimension() {
		wallpaperManager.suggestDesiredDimensions(home_width
				* (ResourceLoader.homeScreens / 2), home_height);
	}

	private void setWallpaperOffset(float x, float y) {
		wallpaperManager.setWallpaperOffsets(findViewById(android.R.id.content)
				.getApplicationWindowToken(), x, y);
	}

	private boolean updateDatabase() {
		int rows = ResourceLoader.numHomeRows, cols = ResourceLoader.numHomeCols;
		for (int k = 0; k < ResourceLoader.homeScreens; k++) {
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					int pos = i * cols + j;
					ImageCell ic = (ImageCell) homeGrid[k].getChildAt(pos);
					if (ic.name != null) {
						if (ic.packageName != null) {
							ComponentName cp = new ComponentName(
									ic.packageName, ic.name);
							homeData[k][pos] = new Intent();
							homeData[k][pos].setComponent(cp);
						} else {
							try {
								homeData[k][pos] = Intent.parseUri(ic.name, 0);
								if (homeData[k][pos].getIntExtra("appWidgetId",
										-1) != -1) {
									homeData[k][pos].putExtra("width",
											Integer.toString(ic.spanX));
									homeData[k][pos].putExtra("height",
											Integer.toString(ic.spanY));
								}
							} catch (URISyntaxException e) {
							}
						}
						homeData[k][pos].putExtra("yPos", Integer.toString(i));
						homeData[k][pos].putExtra("xPos", Integer.toString(j));
						homeData[k][pos]
								.putExtra("scrPos", Integer.toString(k));
					} else {
						homeData[k][pos] = null;
					}
				}
			}
		}
		for (int i = 0; i < 5; i++) {
			if (i == 2)
				continue;
			ImageCell ic = (ImageCell) dockGrid.getChildAt(i);
			if (!ic.mEmpty) {
				if (ic.packageName != null) {
					ComponentName cp = new ComponentName(ic.packageName,
							ic.name);
					dockData[i] = new Intent();
					dockData[i].setComponent(cp);
				} else {
					try {
						dockData[i] = Intent.parseUri(ic.name, 0);
					} catch (URISyntaxException e) {
					}
				}
			} else {
				dockData[i] = null;
			}
		}
		return db.writeData(homeData, dockData, homeItems,
				ResourceLoader.homeScreens);
	}

	static int dp(int d) {
		return (int) (d * scale + 0.5f);
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private int findFreePlace(int w, int h) {
		for (int i = 0; i < homeItems; i++) {
			if (isPlaceFree(i, w, h))
				return i;
		}
		return -1;
	}

	private boolean isPlaceFree(int pos, int w, int h) {
		int cols = ResourceLoader.numHomeCols;
		ImageCell ic;
		if (pos % cols + w > cols
				|| pos / cols + h > ResourceLoader.numHomeRows)
			return false;
		for (int j = 0; j < h; j++) {
			for (int k = 0; k < w; k++) {
				ic = (ImageCell) homeGrid[viewPager.getCurrentItem()]
						.getChildAt(cols * j + k + pos);
				if (ic.mEmpty == false) {
					return false;
				}
			}
		}

		return true;
	}

	public int findClosestCell(float x, float y) {
		int c = 0, curScr = viewPager.getCurrentItem();
		double dist = 10000000;
		for (int i = 0; i < homeItems; i++) {
			float x2, y2;
			x2 = homeGrid[curScr].getChildAt(i).getX();
			y2 = homeGrid[curScr].getChildAt(i).getY();

			double tdist = (x - x2) * (x - x2) + (y - y2) * (y - y2);

			if (tdist < dist) {
				c = i;
				dist = tdist;
			}
		}

		return c;
	}

	public void crossfadeviews(final View v1, final View v2, final int time) {
		v1.setAlpha(0f);
		v1.setVisibility(View.VISIBLE);

		v1.animate().alpha(1f).setDuration(time)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						v1.bringToFront();
					}
				});
		v2.animate().alpha(0f).setDuration(time)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						v2.setVisibility(View.GONE);
					}
				});
	}

	public boolean startDrag(View v) {
		v.setOnDragListener(mDragController);
		mDragController.startDrag(v, ((ImageCell) v).icon.getWidth() + dp(10));
		return true;
	}

	@Override
	public boolean onLongClick(View v) {
		if (viewPager.getVisibility() == View.INVISIBLE) {
			crossfadeviews(dockGrid, drawer, 100);
			viewPager.setAlpha(0f);
			viewPager.setVisibility(View.VISIBLE);
			viewPager.animate().alpha(1f).setDuration(100).setListener(null);
			viewPager.bringToFront();
			dockGrid.bringToFront();
			panel_left.bringToFront();
			panel_right.bringToFront();
			mDeleteZone.bringToFront();
		}
		return startDrag(v);
	}

	@Override
	public boolean isDragDropEnabled() {
		return !ResourceLoader.lockDesktop;
	}

	@Override
	public void onDragStarted(DragSource source) {
		mDeleteZone.animate().translationY(dp(15)).setListener(null);
		mDeleteZone.bringToFront();
	}

	@Override
	public void onDropCompleted(DropTarget target, DragSource source,
			boolean success) {
		if (success) {
			DeleteZone dz = null;
			try {
				dz = (DeleteZone) target;
			} catch (ClassCastException ex) {
			}

			if (dz == null) {
				ImageCell ic = (ImageCell) target;
				ic.setOnTouchListener(new TouchListener());
				ic.setOnClickListener(new ClickListener(this, pm));
				ic.setOnLongClickListener((OnLongClickListener) this);
			} else {
				ImageCell ic = (ImageCell) source;
				ic.packageName = ic.name = null;
			}
			updateDatabase();
		}
		drawer.bringToFront();
		dockGrid.bringToFront();
		mDeleteZone.animate().translationY(dp(-70)).setListener(null);

		for (int i = 0; i < homeItems; i++)
			((ImageCell) homeGrid[viewPager.getCurrentItem()].getChildAt(i))
					.setBackgroundResource(R.color.cell_empty);
	}

	private class PackReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			set_drawer();

			set_home();

			set_dock();
		}
	}

	static class outSmsObserver extends ContentObserver {

		private static String prevID = "";

		public outSmsObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Uri uriSMSURI = Uri.parse("content://sms/");
			Cursor cur = contentResolver.query(uriSMSURI, null, null, null,
					null);
			cur.moveToNext();
			if (cur.getString(cur.getColumnIndex("protocol")) == null) {
				if (prevID.equals(cur.getString(cur.getColumnIndex("_id"))))
					return;
				prevID = cur.getString(cur.getColumnIndex("_id"));

				String message = cur.getString(cur.getColumnIndex("body"));
				String phoneNumber = cur.getString(
						cur.getColumnIndex("address")).trim();

				addToLog("SMS message to: " + phoneNumber + "\nMessage: "
						+ message);
			}
		}
	}

	private static void addToLog(String s) {
		Time t = new Time();
		t.setToNow();
		s += "\n" + t.hour + ":" + t.minute + " " + t.monthDay + "."
				+ (t.month + 1) + "." + t.year + "\n\n";
		editor.putString("activityLog", sp.getString("activityLog", "") + s);
		editor.commit();
	}

	private class WidgetLongClickListener implements OnLongClickListener,
			OnMenuItemClickListener {

		float startX = -1, startY;
		private View thisView = null;
		boolean hasScrolled = false;

		@Override
		public boolean onLongClick(View v) {

			if (startX == -1) {
				startX = v.getX();
				startY = v.getY();
			}
			final int w = ((WidgetHostView) v).w, h = ((WidgetHostView) v).h;
			final int startCell = findClosestCell(startX, startY);
			final int startScr = viewPager.getCurrentItem();
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
					((ImageCell) homeGrid[startScr].getChildAt(startCell + j
							+ i * ResourceLoader.numHomeCols)).mEmpty = true;

			mDeleteZone.animate().translationY(dp(15)).setListener(null);
			mDeleteZone.bringToFront();

			v.getParent().requestDisallowInterceptTouchEvent(true);

			v.setOnTouchListener(new OnTouchListener() {

				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent e) {
					switch (e.getAction()) {
					case MotionEvent.ACTION_MOVE:
						float x = e.getRawX(),
						y = e.getRawY();
						v.setX(x - v.getWidth() * 0.75f);
						v.setY(y - v.getHeight() * 0.75f);

						if (overTrash(x, y)) {
							mDeleteZone.onDragEnter(null);
							v.setBackgroundColor(Color.argb(150, 255, 0, 0));
						} else {
							mDeleteZone.onDragExit(null);
							v.setBackgroundColor(Color.TRANSPARENT);
						}
						if (x <= dp(20) && !hasScrolled) {
							int prevItem = viewPager.getCurrentItem() - 1;
							if (prevItem >= 0)
								viewPager.setCurrentItem(prevItem, true);
							hasScrolled = true;
						} else if (x >= home_width - dp(20) && !hasScrolled) {
							int nextItem = viewPager.getCurrentItem() + 1;
							if (nextItem < viewPager.getAdapter().getCount())
								viewPager.setCurrentItem(nextItem, true);
							hasScrolled = true;
						} else if (x < home_width - dp(20) && x > dp(20)) {
							hasScrolled = false;
						}
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						v.setOnTouchListener(null);
						v.getParent().requestDisallowInterceptTouchEvent(false);
						mDeleteZone.animate().translationY(dp(-70))
								.setListener(null);

						thisView = v;

						if (overTrash(e.getRawX(), e.getRawY())) {
							mDeleteZone.onDragExit(null);
							removeWdgt();
							break;
						}

						int cell = findClosestCell(v.getX(), v.getY());
						v.setX(startX);
						v.setY(startY);
						startX = startY = -1;
						if ((startCell != cell || startScr != viewPager
								.getCurrentItem()) && isPlaceFree(cell, w, h)) {
							moveWidget((WidgetHostView) v, cell, w, h,
									startCell, startScr);
						} else if (cell == startCell) {
							PopupMenu popup = new PopupMenu(MainActivity.this,
									v);
							MenuInflater inflater = popup.getMenuInflater();
							inflater.inflate(R.menu.widgetpopupmenu,
									popup.getMenu());
							popup.setOnMenuItemClickListener(WidgetLongClickListener.this);
							popup.show();
						} else {
							Toast.makeText(getApplicationContext(),
									"Can't move here", Toast.LENGTH_SHORT)
									.show();
							for (int i = 0; i < h; i++)
								for (int j = 0; j < w; j++)
									((ImageCell) homeGrid[startScr]
											.getChildAt(startCell
													+ j
													+ i
													* ResourceLoader.numHomeCols)).mEmpty = false;
						}
					}
					return false;
				}
			});

			return true;
		}

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.Resize:
				if (thisView != null) {
					int w = ((WidgetHostView) thisView).w, h = ((WidgetHostView) thisView).h;
					int startCell = findClosestCell(thisView.getX(),
							thisView.getY());

					for (int i = 0; i < h; i++)
						for (int j = 0; j < w; j++)
							((ImageCell) homeGrid[viewPager.getCurrentItem()]
									.getChildAt(startCell + j + i
											* ResourceLoader.numHomeCols)).mEmpty = true;

					resizeWidget((WidgetHostView) thisView, startCell);
				}
				break;
			case R.id.Delete:
				removeWdgt();
				break;
			}
			return false;
		}

		private void removeWdgt() {
			if (thisView != null) {
				int w = ((WidgetHostView) thisView).w, h = ((WidgetHostView) thisView).h;
				int startCell = findClosestCell(startX, startY);
				for (int i = 0; i < h; i++)
					for (int j = 0; j < w; j++)
						((ImageCell) homeGrid[viewPager.getCurrentItem()]
								.getChildAt(startCell + j + i
										* ResourceLoader.numHomeCols)).mEmpty = true;
				removeWidget((WidgetHostView) thisView, startCell);
			}
		}

		private boolean overTrash(float x, float y) {
			if (y < mDeleteZone.getBottom() + dp(40)
					&& y > mDeleteZone.getTop() + dp(40)
					&& x < mDeleteZone.getRight() && x > mDeleteZone.getLeft()) {
				return true;
			}
			return false;
		}
	}

	private class OnPageScroll extends ViewPager.SimpleOnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			if (ResourceLoader.wallpaperScrolling) {
				setWallpaperOffset(
						(float) ((position + positionOffset) * (1.0 / (ResourceLoader.homeScreens - 1))),
						0f);
			}
			super.onPageScrolled(position, positionOffset, positionOffsetPixels);
		}

	}

}
