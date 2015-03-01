package com.cebecifaruk.repairimei;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.*;
import android.widget.AdapterView.*;
import android.view.View;
import android.widget.ImageButton;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

 

public class MainActivity extends Activity
{
    
    private String[] buttons = 
    {   "Backup NV-Data",
        "Hack This Phone",
        "QPST Port On",
        "Restart Phone",
        "Restore NV-Data"};
    
    
    
    int partitions[] = {10,11,18};
    int partition_number = 2;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
	final ShellExecuter Shell = new ShellExecuter();
        Shell.Executer("su -c clear");
        
        
        
        ImageButton imagebutton = (ImageButton) findViewById(R.id.imagebutton);
            imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cebecifaruk.com"));
                startActivity(browserIntent);
            }
        });
        
        ListView buttonlist=(ListView) findViewById(R.id.listView);
        ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String> 
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, buttons);
        buttonlist.setAdapter(veriAdaptoru);
        
        
        
        buttonlist.setOnItemClickListener(new OnItemClickListener() {
            
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
                switch(position) {    
                    
                    
                    case 0: // Backup Button
                        Shell.Executer("mkdir -p /sdcard/imeibackup/");                        
                        for (int i=0; i<=partition_number ;i++) 
                            Shell.Executer("su -c dd if=/dev/block/mmcblk0p"+partitions[i]+" of=/sdcard/imeibackup/backup"+i+".img");
                        break;
                        
                    case 1: // Hack Button
                        AlertDialog.Builder diyalogOlusturucu =  new AlertDialog.Builder(MainActivity.this);
                          
                        diyalogOlusturucu
				.setMessage("Are You Sure!")
				.setCancelable(true)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {						
                                            dialog.cancel();
                                            for (int i=0; i<=partition_number ;i++) 
                                                Shell.Executer("su -c dd if=/dev/zero of=/dev/block/mmcblk0p"+partitions[i]);
                                            Shell.Executer("su -c reboot"); 
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
					}
				})
                                .show();
                        break;                   
                        
                        
                    case 2: //QPST Port
                        Shell.Executer("su -c setprop sys.usb.config diag,adb");
                        break;
                    
                    case 3: //Restart
                        Shell.Executer("su -c setprop sys.usb.config mtp,adb");
                        Shell.Executer("su -c reboot");
                        break;
                        
                        
                    case 4: //Restore
                        for (int i=0; i<=partition_number ;i++) 
                            Shell.Executer("su -c dd if=/dev/block/mmcblk0p"+i+".img"+" of=/dev/block/mmcblk0p"+partitions[i]);
                        break;
                }
                
            }
        
        });
        
        
        
        	
      
    }
    
    

}
