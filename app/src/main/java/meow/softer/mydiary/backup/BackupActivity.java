package meow.softer.mydiary.backup;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import meow.softer.mydiary.MainActivity;
import meow.softer.mydiary.R;
import meow.softer.mydiary.shared.gui.MyDiaryButton;
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;

public class BackupActivity extends AppCompatActivity implements View.OnClickListener,
ExportAsyncTask.ExportCallBack,ImportAsyncTask.ImportCallBack{
    private final int EXPORT_SRC_PICKER_CODE = 0;
    private final int IMPORT_SRC_PICKER_CODE = 1;
    /*
     * UI
     */
    private TextView TV_backup_export_src, TV_backup_import_src;
    private MyDiaryButton But_backup_export, But_backup_import;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        //UI
        TV_backup_export_src = findViewById(R.id.TV_backup_export_src);
        TV_backup_export_src.setOnClickListener(this);

        TV_backup_import_src = findViewById(R.id.TV_backup_import_src);
        TV_backup_import_src.setOnClickListener(this);

        But_backup_export = findViewById(R.id.But_backup_export);
        But_backup_export.setOnClickListener(this);
        But_backup_export.setEnabled(false);

        But_backup_import = findViewById(R.id.But_backup_import);
        But_backup_import.setOnClickListener(this);
        But_backup_import.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == EXPORT_SRC_PICKER_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = intent.getData();
            if (uri != null) {
                File file = com.nononsenseapps.filepicker.Utils.getFileForUri(uri);
                if (file.canWrite()) {

                    TV_backup_export_src.setText(file.getAbsolutePath());
                    But_backup_export.setEnabled(true);
                } else {
                    Toast.makeText(this, getString(R.string.backup_export_can_not_write),
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == IMPORT_SRC_PICKER_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = intent.getData();
            if (uri != null) {
                File file = com.nononsenseapps.filepicker.Utils.getFileForUri(uri);
                if (file.canRead()) {
                    TV_backup_import_src.setText(file.getAbsolutePath());
                    But_backup_import.setEnabled(true);
                } else {
                    Toast.makeText(this, getString(R.string.backup_import_can_not_read),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TV_backup_export_src:
                Intent exportIntent = new Intent(this, MyDirectoryPickerActivity.class);

                exportIntent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                exportIntent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
                exportIntent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
                exportIntent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                startActivityForResult(exportIntent, EXPORT_SRC_PICKER_CODE);
                break;
            case R.id.TV_backup_import_src:
                Intent importIntent = new Intent(this, MyDirectoryPickerActivity.class);

                importIntent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                importIntent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                importIntent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                importIntent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                startActivityForResult(importIntent, IMPORT_SRC_PICKER_CODE);
                break;
            case R.id.But_backup_export:
                new ExportAsyncTask(this, this, TV_backup_export_src.getText().toString())
                        .execute();
                break;
            case R.id.But_backup_import:
                new ImportAsyncTask(this, this, TV_backup_import_src.getText().toString())
                        .execute();
                break;
        }
    }

    @Override
    public void onImportCompiled(boolean importSuccessful) {
        Intent backMainActivityIntent = new Intent(this, MainActivity.class);
        backMainActivityIntent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(backMainActivityIntent);
    }

    @Override
    public void onExportCompiled(String backupZipFilePath) {
        //Open the shared file intent
        try {
            Intent sendIntent = new Intent();
            if (backupZipFilePath != null) {
                File backupFile = new File(backupZipFilePath);
                Uri backupFileUri = FileProvider.getUriForFile(this,
                        this.getApplicationContext().getPackageName() + ".provider",
                        backupFile);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_STREAM, backupFileUri);
                sendIntent.setType("application/zip");
                startActivity(Intent.createChooser(sendIntent,
                        getResources().getText(R.string.backup_export_share_title)));
            }
        } catch (Exception e) {
            Log.e("Backup", "export share fail", e);
        }
    }
}
