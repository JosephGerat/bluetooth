package com.example.bluetooth_;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button button;
	private ListView list;
	private ArrayAdapter adapter;
	private List<String> mList;
	private BluetoothAdapter mBluetoothAdapter;
	private boolean isBluetoothRunning;

	private final static int REQUEST_ENABLE_BT = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);//set the gui this Activity from res/layout/activity_main.xml
		
		list=(ListView)findViewById(R.id.list);
		mList=new ArrayList<String>();
		
		
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//Represents the local device Bluetooth adapter.
                                                                 //Get a handle to the default local Bluetooth adapter.

		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
			Toast.makeText(this, "Your device not support Bluetooth!", Toast.LENGTH_SHORT).show();
			return;
		}else if(mBluetoothAdapter.isEnabled()){
			isBluetoothRunning=true;
		}else{
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
	    //cancel any prior Bluetooth device discovery
	    if (mBluetoothAdapter.isDiscovering()){
	    	mBluetoothAdapter.cancelDiscovery();
	    }

		
		button=(Button)findViewById(R.id.touchMe);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteAllListItems(mList);
				viewDevices();
			}

			private void deleteAllListItems(List<String> mList) {
				while(mList.size()>0){
					mList.remove(0);
				}
				return;
			}

		});

	}

	protected void viewDevices() {
		
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		if (!mBluetoothAdapter.isEnabled()) {
			Toast.makeText(this, "You must turn on bluetooth on your device!", Toast.LENGTH_SHORT).show();
			return;
		}else
			mBluetoothAdapter.startDiscovery();
           		
		
		
		 
		// Create a BroadcastReceiver for ACTION_FOUND
		//if receiver find some device the onReceive(..) method will run
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		    public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();
		        // When discovery finds a device
		        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		            // Get the BluetoothDevice object from the Intent
		            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		            // Add the name and address to an array adapter to show in a ListView
		            
		            if(checkItem(device.getName())==false)//if the device isnt on the list 
		            	mList.add(device.getName());
		            setAdapterDevices();
		        }
		    }
		};
		IntentFilter ifilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	    this.registerReceiver(mReceiver, ifilter);
		
	}

	protected boolean checkItem(String name) {
		
		
		Iterator<String> it=mList.iterator();
		
		while(it.hasNext()){
			if(name==it.next())
				return true;
		}
		
		return false;
	}

	protected void setAdapterDevices() {
		          //ArrayAdapter(Context or Instance of Activity, the way a list looks like,data)
		adapter=new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,mList);
		list.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// bring back bluetooth to previous state
		if(!isBluetoothRunning)
			mBluetoothAdapter.disable();
	}
	////LALALALA
	

}
