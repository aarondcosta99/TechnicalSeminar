package com.example.technicalseminar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technicalseminar.databinding.ActivityMainBinding;
import com.example.technicalseminar.databinding.ContentMainBinding;
import com.example.technicalseminar.fragment.PowFragment;
import com.example.technicalseminar.manager.BlockChainManager;
import com.example.technicalseminar.manager.SharedPreferencesManager;
import com.example.technicalseminar.util.CipherUtils;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ContentMainBinding viewBindingContent;
    private ProgressDialog progressDialog;
    private SharedPreferencesManager prefs;
    private BlockChainManager blockChain;
    private boolean isEncryptionActivated,isDarkActivated;
    private static final String TAG_POW_DIALOG = "proof_of_work_dialog";
    ImageButton button;
    RecyclerView recyclerView;
    TextInputEditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = new SharedPreferencesManager(this);
        isDarkActivated = prefs.isDarkTheme();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean isPowerSaveMode = false;
        if(powerManager!=null){
            isPowerSaveMode = powerManager.isPowerSaveMode();
        }
        if(isPowerSaveMode){
            isPowerSaveMode = powerManager.isPowerSaveMode();
        }else{
            if(isDarkActivated){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
        super.onCreate(savedInstanceState);
        ActivityMainBinding viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        viewBindingContent = ContentMainBinding.bind(viewBinding.contentMain.getRoot());
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.btn_send_data);
        editText = findViewById(R.id.edit_message);
        recyclerView = findViewById(R.id.recycler_content);
        isEncryptionActivated = prefs.getEncryptionStatus();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        new Thread(()->runOnUiThread(()->{
            blockChain = new BlockChainManager(prefs.getPowValue(),this);
            recyclerView.setAdapter(blockChain.adapter);
        })).start();
        button.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void startBlockChain(){

        if(blockChain!=null&&editText.getText()!=null&&recyclerView.getAdapter()!=null){
            String message = editText.getText().toString();
            if(!message.isEmpty()){
                if(!isEncryptionActivated){
                    blockChain.addBlock(blockChain.newBlock(message));
                }else {
                    try{
                        blockChain.addBlock(blockChain.newBlock(CipherUtils.encrypt(message).trim()));
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, "Something", Toast.LENGTH_SHORT).show();
                    }
                }
                recyclerView.scrollToPosition(blockChain.adapter.getItemCount()-1);
                if(blockChain.isBlockChainValid()){
                    recyclerView.getAdapter().notifyDataSetChanged();
                    editText.setText("");
                }else{
                    Toast.makeText(this, "BlockChain Corrupted", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Error Empty Data", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_send_data){
            startBlockChain();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkEncrypt = menu.findItem(R.id.action_encrypt);
        checkEncrypt.setChecked(isEncryptionActivated);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_encrypt:
                isEncryptionActivated = !item.isChecked();
                item.setChecked(isEncryptionActivated);
                if(item.isChecked()){
                    Toast.makeText(this, "Message Encryption On", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Message Encryption Off", Toast.LENGTH_SHORT).show();
                }
                prefs.setEncryptionStatus(isEncryptionActivated);
                return true;
            case R.id.action_exit:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}