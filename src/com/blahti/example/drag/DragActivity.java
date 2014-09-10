package com.blahti.example.drag;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import cn.Ragnarok.BitmapFilter;

import com.example.androidimagefilterdemo.Constant;
import com.example.androidimagefilterdemo.CustomArrayAdapter;
import com.example.androidimagefilterdemo.CustomData;
import com.example.androidimagefilterdemo.R;
import com.meetme.android.horizontallistview.HorizontalListView;
import com.soundcloud.android.crop.Crop;

/**
 * This activity presents two images and a text view and allows them to be dragged around.
 * Press and hold on a view initiates a drag. 
 * After clicking the Short button, dragging starts with a short touch rather than a long touch.
 *
 * <p> This activity is derviced from the Android Launcher class.
 * 
 */

@SuppressLint("NewApi")
public class DragActivity extends Activity 
    implements View.OnLongClickListener, View.OnClickListener, View.OnTouchListener
{
	 Bitmap originBitmap;
	  private DrawerLayout mDrawerLayout;
	    private ListView mDrawerList;
	    private ListView mDrawerListR;
	    private ActionBarDrawerToggle mDrawerToggle;
	    private Bitmap changeBitmap = null;
		private ProgressDialog progressDialog;
		EditText tv;
		String image_path;
	    private CharSequence mDrawerTitle;
	    private CharSequence mTitle;
	    private String[] mPlanetTitles;
private DragController mDragController;   // Object that sends out drag-drop events while a view is being moved.
private DragLayer mDragLayer;             // The ViewGroup that supports drag-drop.
private boolean mLongClickStartsDrag = true;    // If true, it takes a long click to start the drag operation.
                                                // Otherwise, any touch event starts a drag.
ImageView i2;
private static final int CHANGE_TOUCH_MODE_MENU_ID = Menu.FIRST;

public static final boolean Debugging = false;
ImageView i1 ;
/**
 * onCreate - called when the activity is first created.
 * 
 * Creates a drag controller and sets up three views so click and long click on the views are sent to this activity.
 * The onLongClick method starts a drag sequence.
 *
 */
private ActionBar actionBar;
private MenuItem refreshMenuItem;
private HorizontalListView mHlvCustomListWithDividerAndFadingEdge;
private CustomData[] mCustomData = new CustomData[] {
        new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.like),
        new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.like),
        new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.like),
        new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.like),
        new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.like),
        new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.like), new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.like),
        new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.like),
        new CustomData(Color.TRANSPARENT, R.drawable.star), new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.like),
        new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.like),
        new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.like), new CustomData(Color.WHITE, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.like),
        new CustomData(Color.TRANSPARENT, R.drawable.star),
        new CustomData(Color.TRANSPARENT, R.drawable.like),
        new CustomData(Color.TRANSPARENT, R.drawable.star)
       
        
};
 @SuppressLint("NewApi")
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    mDragController = new DragController(this);
    mLongClickStartsDrag = !mLongClickStartsDrag;
    setContentView(R.layout.main);
    setupViews ();
    mHlvCustomListWithDividerAndFadingEdge = (HorizontalListView) findViewById(R.id.hlvCustomListWithDividerAndFadingEdge);
    setupCustomLists();
    mHlvCustomListWithDividerAndFadingEdge.setOnItemClickListener(new OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        	for (int i = 0; i < mCustomData.length; i++) {
        	
        		if(i==position)
        		{
        		   	Toast.makeText(getApplicationContext(),mCustomData[i].getText(), 
                            Toast.LENGTH_SHORT).show(); 
        		   	i2.setImageResource(mCustomData[i].getText());
        		   	
        		}
        	}	
        
        }});
    
    
    mTitle = mDrawerTitle = getTitle();
    mPlanetTitles = getResources().getStringArray(R.array.planets_array);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);

    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    // set up the drawer's list view with items and click listener
    mDrawerList.setAdapter(new ArrayAdapter<String>(this,
            R.layout.drawer_list_item, Constant.styles_menu));
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener_menu());
    mDrawerListR = (ListView) findViewById(R.id.right_drawer);

    // set up the drawer's list view with items and click listener
    mDrawerListR.setAdapter(new ArrayAdapter<String>(this,
            R.layout.drawer_list_item,Constant.styles));
    mDrawerListR.setOnItemClickListener(new DrawerItemClickListener());
    // enable ActionBar app icon to behave as action to toggle nav drawer
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setHomeButtonEnabled(true);
    getActionBar().setIcon(R.drawable.filters);
    // ActionBarDrawerToggle ties together the the proper interactions
    // between the sliding drawer and the action bar app icon
    mDrawerToggle = new ActionBarDrawerToggle(
            this,                  /* host Activity */
            mDrawerLayout,         /* DrawerLayout object */
            R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
            R.string.drawer_open,  /* "open drawer" description for accessibility */
            R.string.drawer_close  /* "close drawer" description for accessibility */
            ) {
        @SuppressLint("NewApi")
		public void onDrawerClosed(View view) {
            getActionBar().setTitle(mTitle);
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        public void onDrawerOpened(View drawerView) {
            getActionBar().setTitle(mDrawerTitle);
            switch(drawerView.getId()) {
            case R.id.left_drawer:
            	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerListR);
            	if(drawerOpen){
            		mDrawerLayout.closeDrawer(mDrawerListR);
            	}
              break;
            case R.id.right_drawer:
            	boolean drawerOpenl = mDrawerLayout.isDrawerOpen(mDrawerList);
            	if(drawerOpenl){
            		mDrawerLayout.closeDrawer(mDrawerList);
            	}
              break;
          }
            
            
            
            
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }
    };
    mDrawerLayout.setDrawerListener(mDrawerToggle);

//    if (savedInstanceState == null) {
//        selectItem(0);
//    }

}
 private class DrawerItemClickListener implements ListView.OnItemClickListener {
     @Override
     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         selectItem(position);
     }
 }

 private class DrawerItemClickListener_menu implements ListView.OnItemClickListener {
     private int i;

	@Override
     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       
    	  switch(position) {      
    	   case 0:
    			save_image_gallary(1);
    			// load the data from server
    			   break;
    		case 1:
    			// help action
    			save_image_gallary(2);
    				   break;	
    		case 2:

    			Intent myIntent = new Intent(DragActivity.this, CamTestActivity.class);
    			//myIntent.putExtra("key", value); //Optional parameters
    			 startActivityForResult(myIntent, 6);// Activity is started with requestCode 2  
    				   break;        		
    		case 3:
    			// help action
    			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
    					 "content://media/internal/images/media")); 
    					 startActivity(intent); 
    						Toast.makeText(getApplicationContext(),"Refresh"+position,Toast.LENGTH_SHORT).show(); 
    						   break;			        	
    		case 4:
    			// help action

    	ByteArrayOutputStream stream=new ByteArrayOutputStream();
    	originBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
    	byte[] image=stream.toByteArray();
    			Intent intent_rotate = new Intent(DragActivity.this, CropperActivty.class);
    			intent_rotate.putExtra("image_rotate",image);
    					
    					 startActivityForResult(intent_rotate, 7);
    					   break;
    		case 5:
    			// help action
    			Intent choose_pic = new Intent(Intent.ACTION_PICK);
    			choose_pic.setType("image/*");
    			startActivityForResult(choose_pic,9);
    		
    	   break;
    	   }

         // update the main content by replacing fragments
       
         // update selected item and title, then close the drawer
         mDrawerList.setItemChecked(position, true);
       //  setTitle(Constant.styles[position]);
         
         Toast.makeText(getApplicationContext(),Constant.styles[position], 
                 Toast.LENGTH_SHORT).show(); 
         boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerListR);
     	if(drawerOpen){
     		mDrawerLayout.closeDrawer(mDrawerListR);
     	}
     	boolean drawerOpenl = mDrawerLayout.isDrawerOpen(mDrawerList);
     	if(drawerOpenl){
     		mDrawerLayout.closeDrawer(mDrawerList);
     	}
     	 else {
    		Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_LONG).show();
    		mDrawerLayout.closeDrawers();
    	}
         
     
     }
 }
 private void selectItem(final int position) {
     // update the main content by replacing fragments
   
     // update selected item and title, then close the drawer
     mDrawerList.setItemChecked(position, true);
     setTitle(Constant.styles[position]);
     
     Toast.makeText(getApplicationContext(),Constant.styles[position], 
             Toast.LENGTH_SHORT).show(); 
     boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerListR);
 	if(drawerOpen){
 		mDrawerLayout.closeDrawer(mDrawerListR);
 	}
 	boolean drawerOpenl = mDrawerLayout.isDrawerOpen(mDrawerList);
 	if(drawerOpenl){
 		mDrawerLayout.closeDrawer(mDrawerList);
 	}
 	
     
	if (originBitmap != null) {
		progressDialog = ProgressDialog.show(this, "Please wait", "", true);
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				if (changeBitmap != null && !changeBitmap.isRecycled()) {
					
					//changeBitmap.recycle();
					changeBitmap = null;
					System.gc();
				}
			try{	applyStyle(position + 1);}catch(Exception e){}
				Message msg = Message.obtain();
				msg.what = 1;
				handler.sendMessage(msg);
			}

		}.start();
	} else {
		Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_LONG).show();
		mDrawerLayout.closeDrawers();
	}
     
 }
 
 private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		try{	i1.setImageBitmap(changeBitmap);
			
		}catch(Exception e){}progressDialog.dismiss();
			mDrawerLayout.closeDrawers();
		}
		
	};
	
	
	private void applyStyle(int styleNo) {
		switch (styleNo) {
		case BitmapFilter.AVERAGE_BLUR_STYLE:
			changeBitmap = BitmapFilter.changeStyle(originBitmap, BitmapFilter.AVERAGE_BLUR_STYLE, 5); // maskSize, must odd
			break;
		case BitmapFilter.GAUSSIAN_BLUR_STYLE:
			changeBitmap = BitmapFilter.changeStyle(originBitmap, BitmapFilter.GAUSSIAN_BLUR_STYLE, 1.2); // sigma
			break;
		case BitmapFilter.SOFT_GLOW_STYLE:
			changeBitmap = BitmapFilter.changeStyle(originBitmap, BitmapFilter.SOFT_GLOW_STYLE, 0.6);
			break;
		case BitmapFilter.LIGHT_STYLE:
			int width = originBitmap.getWidth();
			int height = originBitmap.getHeight();
			changeBitmap = BitmapFilter.changeStyle(originBitmap, BitmapFilter.LIGHT_STYLE, width / 3, height / 2, width / 2);
			break;
		case BitmapFilter.LOMO_STYLE:
			changeBitmap = BitmapFilter.changeStyle(originBitmap, BitmapFilter.LOMO_STYLE, 
					(originBitmap.getWidth() / 2.0) * 95 / 100.0);
			break;
		case BitmapFilter.NEON_STYLE:
			changeBitmap = BitmapFilter.changeStyle(originBitmap, BitmapFilter.NEON_STYLE, 200, 100, 50);
			break;
		case BitmapFilter.PIXELATE_STYLE:
			changeBitmap = BitmapFilter.changeStyle(originBitmap, BitmapFilter.PIXELATE_STYLE, 10);
			break;
		case BitmapFilter.MOTION_BLUR_STYLE:
			changeBitmap = BitmapFilter.changeStyle(originBitmap, BitmapFilter.MOTION_BLUR_STYLE, 10, 1);
			break;
		case BitmapFilter.OIL_STYLE:
			changeBitmap = BitmapFilter.changeStyle(originBitmap, BitmapFilter.OIL_STYLE, 5);
			break;
		default:
			changeBitmap = BitmapFilter.changeStyle(originBitmap, styleNo);
			break;
		}
	}

 @Override
 protected void onPostCreate(Bundle savedInstanceState) {
     super.onPostCreate(savedInstanceState);
     // Sync the toggle state after onRestoreInstanceState has occurred.
     mDrawerToggle.syncState();
 }

 @Override
 public void onConfigurationChanged(Configuration newConfig) {
     super.onConfigurationChanged(newConfig);
     // Pass any configuration change to the drawer toggls
     mDrawerToggle.onConfigurationChanged(newConfig);
 }

 @Override
 public void setTitle(CharSequence title) {
     mTitle = title;
     getActionBar().setTitle(mTitle);
 }
 private void setupCustomLists() {
     // Make an array adapter using the built in android layout to render a list of strings
     CustomArrayAdapter adapter = new CustomArrayAdapter(this, mCustomData);

     // Assign adapter to HorizontalListView
   //  mHlvCustomList.setAdapter(adapter);
     mHlvCustomListWithDividerAndFadingEdge.setAdapter(adapter);
//     System.out.println("bingo "+ mCustomData);
 }

/**
 * Build a menu for the activity.
 *
 */ 
public boolean onCreateOptionsMenu (Menu menu) 
{
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.main, menu);

    menu.add (0, CHANGE_TOUCH_MODE_MENU_ID, 0, "Change Touch Mode");
    
    return true;
}

/**
 * Handle a click on a view. Tell the user to use a long click (press).
 *
 */    

public void onClick(View v) 
{
	
    if (mLongClickStartsDrag) {
       // Tell the user that it takes a long click to start dragging.
       toast ("Press and hold to drag an image.");
    }
}

/**
 * Handle a long click.
 * If mLongClick only is true, this will be the only way to start a drag operation.
 *
 * @param v View
 * @return boolean - true indicates that the event was handled
 */    

public boolean onLongClick(View v) 
{
    if (mLongClickStartsDrag) {
       
        //trace ("onLongClick in view: " + v + " touchMode: " + v.isInTouchMode ());

        // Make sure the drag was started by a long press as opposed to a long click.
        // (Note: I got this from the Workspace object in the Android Launcher code. 
        //  I think it is here to ensure that the device is still in touch mode as we start the drag operation.)
        if (!v.isInTouchMode()) {
           toast ("isInTouchMode returned false. Try touching the view again.");
           return false;
        }
       
        return startDrag (v);
    }

    // If we get here, return false to indicate that we have not taken care of the event.
    return false;
}

/**
 * Perform an action in response to a menu item being clicked.
 *
 */

public boolean onOptionsItemSelected (MenuItem item) 
{
//    switch (item.getItemId()) {
//      case CHANGE_TOUCH_MODE_MENU_ID:
//        mLongClickStartsDrag = !mLongClickStartsDrag;
//        String message = mLongClickStartsDrag ? "Changed touch mode. Drag now starts on long touch (click)." 
//                                              : "Changed touch mode. Drag now starts on touch (click).";
//        Toast.makeText (getApplicationContext(), message, Toast.LENGTH_LONG).show ();
//        return true;
//    }
//    return super.onOptionsItemSelected (item);
//    

    // The action bar home/up action should open or close the drawer.
    // ActionBarDrawerToggle will take care of this.
   if (mDrawerToggle.onOptionsItemSelected(item)) {
       return true;
   }
   // Handle action buttons
   switch(item.getItemId()) {
   case R.id.action_websearch:
       // create intent to perform web search for this planet
   	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerListR);
   	if(drawerOpen){
   		mDrawerLayout.closeDrawer(mDrawerListR);
   	}else{
   		mDrawerLayout.openDrawer(mDrawerListR);
   		
   	}
   	
       return true;
   case R.id.action_refresh:
		// refresh
		refreshMenuItem = item;
		save_image_gallary(1);
		// load the data from server
		Toast.makeText(getApplicationContext(),"Refresh",Toast.LENGTH_SHORT).show(); 
		return true;
	case R.id.action_help:
		// help action
		save_image_gallary(2);
		Toast.makeText(getApplicationContext(),"Crop",Toast.LENGTH_SHORT).show(); 
		return true;
	case R.id.action_check_updates:

		Intent myIntent = new Intent(DragActivity.this, CamTestActivity.class);
		//myIntent.putExtra("key", value); //Optional parameters
		 startActivityForResult(myIntent, 6);// Activity is started with requestCode 2  
	//	Toast.makeText(getApplicationContext(),"check updates",Toast.LENGTH_SHORT).show(); 
		return true;
	case R.id.gallery_open:
		// help action
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
				 "content://media/internal/images/media")); 
				 startActivity(intent); 
		return true;
	case R.id.rotate_image:
		// help action

ByteArrayOutputStream stream=new ByteArrayOutputStream();
originBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
byte[] image=stream.toByteArray();
		Intent intent_rotate = new Intent(DragActivity.this, CropperActivty.class);
		intent_rotate.putExtra("image_rotate",image);
				
				 startActivityForResult(intent_rotate, 7);
		return true;
	case R.id.choose_diff_image:
		// help action
		Intent choose_pic = new Intent(Intent.ACTION_PICK);
		choose_pic.setType("image/*");
		startActivityForResult(choose_pic,9);
		return true;
	case R.id.share_image:
		// help action
		try{
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		Uri screenshotUri = Uri.parse(image_path);
		 
		sharingIntent.setType("image/png");
		sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
		startActivity(Intent.createChooser(sharingIntent, "Share image using"));
		}catch(Exception e){ Toast.makeText(getApplicationContext(),"Please save pic first",Toast.LENGTH_SHORT).show();  }
		return true;
	case R.id.write_text:
		// help action
tv.setVisibility(View.VISIBLE);
tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.orangebodboxlg));
		return true;
   default:
       return super.onOptionsItemSelected(item);
   }
}

/**
 * This is the starting point for a drag operation if mLongClickStartsDrag is false.
 * It looks for the down event that gets generated when a user touches the screen.
 * Only that initiates the drag-drop sequence.
 *
 */  
public void save_image_gallary(int flag)
{
//	BitmapDrawable btmpDr = (BitmapDrawable) i1.getDrawable();
//	Bitmap bmp = btmpDr.getBitmap();
	tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_image));
	  View u = findViewById(R.id.drag_layer);
      u.setDrawingCacheEnabled(true); 
   
      MyAbsoluteLayout z = (MyAbsoluteLayout) findViewById(R.id.drag_layer);
 //     int totalHeight = z.getChildAt(0).getHeight();
  //    int totalWidth = z.getChildAt(0).getWidth();
  //    u.layout(0, 0, totalWidth, totalHeight);    
      u.buildDrawingCache(true);
      Bitmap bmp = Bitmap.createBitmap(u.getDrawingCache());             
      u.setDrawingCacheEnabled(false);
      Canvas c= new Canvas(bmp);
	
	/*File sdCardDirectory = Environment.getExternalStorageDirectory();*/
	try
	{
	    File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "MeeguImages");
	    sdCardDirectory.mkdirs();

	    String imageNameForSDCard = "image_" + System.currentTimeMillis() + ".jpg";

	    File image = new File(sdCardDirectory, imageNameForSDCard);
	    FileOutputStream outStream;

	    outStream = new FileOutputStream(image);
	    c.drawBitmap(bmp, 0, 0, null);
	    bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream); 
	    /* 100 to keep full quality of the image */
	    outStream.flush();
	    outStream.close();
	    //Refreshing SD card
//	    sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
	    MediaStore.Images.Media.insertImage(getContentResolver(),bmp, "Screen", "screen");
	    Toast.makeText(DragActivity.this,sdCardDirectory.getAbsolutePath()+"/"+imageNameForSDCard, Toast.LENGTH_LONG).show();
	    image_path=sdCardDirectory.getAbsolutePath()+"/"+imageNameForSDCard;
	    
	   
    	File imgFile = new  File(image_path);
    	if(imgFile.exists()){

    	    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
    	    i1.setImageBitmap(bmp);
    	 i2.setLayoutParams(new MyAbsoluteLayout.LayoutParams(80, 80,20, 20));
    	    
    	    i2.setImageResource(R.drawable.transparent_image);
    	    try{   originBitmap=bmp;}catch(Exception e){/*System.out.println("yo bingo "+image_path);*/}
    	}
  if(flag==2)
  {
	  beginCrop(Uri.fromFile(new File(image_path)));
  }
	}
	catch (Exception e) 
	{
	    e.printStackTrace();
	    Toast.makeText(DragActivity.this, "Image could not be saved : Please ensure you have SD card installed " +
	                                                                            "properly", Toast.LENGTH_LONG).show();
	}
	
	tv.setVisibility(View.GONE);
	tv.setLayoutParams(new MyAbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, 80,20, 20));
}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent result) {
    if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
        beginCrop(result.getData());
    } else if (requestCode == Crop.REQUEST_CROP) {
        handleCrop(resultCode, result);
    }
    
    else if(requestCode==6)  
    {  
    	  try{ 	  String message=result.getStringExtra("image_path");   
    	File imgFile = new  File(message);
    	if(imgFile.exists()){

    	    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
    	    i1.setImageBitmap(myBitmap);
    	    originBitmap=myBitmap;
    	}
    	  }catch(Exception e){}
    }
    else if(requestCode==7)  
    {      try{    	 byte[] byteArray=result.getByteArrayExtra("image_roate_result");   
    	 Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    	if(bmp!=null){

    	  
    	    i1.setImageBitmap(bmp);
    	originBitmap=bmp;
    	}
    }catch(Exception e){/*System.out.println("yo bingo "+image_path);*/}
    }  
    else if (requestCode == 9) {
		Uri selectedImage = result.getData();
		InputStream imageStream;
		try {
			imageStream = getContentResolver().openInputStream(selectedImage);
			
			originBitmap = BitmapFactory.decodeStream(imageStream);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		i1.setImageBitmap(originBitmap);}
}
private void beginCrop(Uri source) {
    Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
    new Crop(source).output(outputUri).asSquare().start(this);
}

private void handleCrop(int resultCode, Intent result) {
    if (resultCode == RESULT_OK) {
        i1.setImageURI(Crop.getOutput(result));
    try{    originBitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),Crop.getOutput(result));System.out.println("yo bingo "+originBitmap.getHeight());}catch(Exception e){/*System.out.println("yo bingo "+image_path);*/}
    
    } else if (resultCode == Crop.RESULT_ERROR) {
        Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
    }
}
public boolean onTouch (View v, MotionEvent ev) 
{
    // If we are configured to start only on a long click, we are not going to handle any events here.
    if (mLongClickStartsDrag) return false;

    boolean handledHere = false;
    if(v.getId()==R.id.Text1)
    {
    	tv.requestFocus();
    	Toast.makeText (getApplicationContext(), "Hip hop", Toast.LENGTH_LONG).show ();
    }
    final int action = ev.getAction();

    // In the situation where a long click is not needed to initiate a drag, simply start on the down event.
    if (action == MotionEvent.ACTION_DOWN) {
       handledHere = startDrag (v);
    }
    
    return handledHere;
}

/**
 * Start dragging a view.
 *
 */    

public boolean startDrag (View v)
{
    Object dragInfo = v;
    mDragController.startDrag (v, mDragLayer, dragInfo, DragController.DRAG_ACTION_MOVE);
    return true;
}

/**
 * Finds all the views we need and configure them to send click events to the activity.
 *
 */
private void setupViews() 
{// Toast.makeText (getApplicationContext(), "bingo", Toast.LENGTH_LONG).show ();
    DragController dragController = mDragController;

    mDragLayer = (DragLayer) findViewById(R.id.drag_layer);
    mDragLayer.setDragController(dragController);
    dragController.addDropTarget (mDragLayer);

    i1 = (ImageView) findViewById (R.id.Image1);
  i2 = (ImageView) findViewById (R.id.Image2);

  byte[] byteArray = getIntent().getByteArrayExtra("image");
  originBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
 try{ i1.setImageBitmap(originBitmap);

 }catch(Exception e){}//    i1.setOnClickListener(this);
//    i1.setOnLongClickListener(this);
//    i1.setOnTouchListener(this);

    i2.setOnClickListener(this);
    i2.setOnLongClickListener(this);
    i2.setOnTouchListener(this);

     tv = (EditText)findViewById (R.id.Text1);
    tv.setOnLongClickListener(this);
    tv.setOnTouchListener(this);
    tv.setOnClickListener(this);
    String message = mLongClickStartsDrag ? "Press and hold to start dragging." 
                                          : "Touch a view to start dragging.";
    Toast.makeText (getApplicationContext(), message, Toast.LENGTH_LONG).show ();

}

/**
 * Show a string on the screen via Toast.
 * 
 * @param msg String
 * @return void
 */

public void toast (String msg)
{
    Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
} // end toast

/**
 * Send a message to the debug log and display it using Toast.
 */

public void trace (String msg) 
{
    if (!Debugging) return;
    Log.d ("DragActivity", msg);
    toast (msg);
}

} // end class
