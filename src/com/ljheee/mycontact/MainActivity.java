package com.ljheee.mycontact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 *读取系统联系人
 */
public class MainActivity extends Activity {

	ListView contactsView;
	List<String> contactsList = new ArrayList<String>();
	Set<String> to1ContactsList = new HashSet<String>();
	ArrayAdapter<String> adapter;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        contactsView = (ListView) findViewById(R.id.contacts_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactsList);
        contactsView.setAdapter(adapter);
        getContacts();
    }

    /**
     * 读取系统联系人
     * 借助ContentResolver
     */
	private void getContacts() {
		Cursor cursor = null;
		try{
			cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
			while(cursor.moveToNext()){
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				String num = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				if(!TextUtils.isEmpty(num)&&!TextUtils.isEmpty(name)){
					contactsList.add(name+"\n"+num);
					to1ContactsList.add(name+"\n"+num);//集合“唯一性”，过滤重复联系人
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			if(cursor!=null){
				cursor.close();
			}
		}
	}
	
	
	
	/**
     * 创建选项菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * 选项菜单-选项监听
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
		case R.id.action_filter:
			Toast.makeText(MainActivity.this , "过滤重复", Toast.LENGTH_SHORT).show();
	        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  to1ContactsList.toArray(new String[to1ContactsList.size()]));
	        contactsView.setAdapter(adapter);
			break;
		case R.id.action_exit:
			finish();
			break;

		default:
			break;
		}
    	return super.onOptionsItemSelected(item);
    }
 

}
