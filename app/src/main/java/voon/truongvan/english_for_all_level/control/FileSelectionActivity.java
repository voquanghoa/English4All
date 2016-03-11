package voon.truongvan.english_for_all_level.control;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import voon.truongvan.english_for_all_level.QuestionListActivity;
import voon.truongvan.english_for_all_level.R;
import voon.truongvan.english_for_all_level.adapter.FileSelectAdapter;
import voon.truongvan.english_for_all_level.constant.AppConstant;
import voon.truongvan.english_for_all_level.controller.UserResultController;
import voon.truongvan.english_for_all_level.model.DataItem;


public abstract class FileSelectionActivity extends BaseActivity implements FileSelectAdapter.FileSelectFeedback {
    private FileSelectAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_select_layout);
        adapter = new FileSelectAdapter(this, getDataItem(), this);
        ((ListView)findViewById(R.id.list_item)).setAdapter(adapter);
    }

    public void onBackPressed() {
        if(!adapter.showParent()){
            super.onBackPressed();
        }
    }

    public void onResume() {
        super.onResume();
        if(UserResultController.getInstance().isRequestUpdate()){
            adapter.notifyDataSetChanged();
        }
    }

    protected abstract DataItem getDataItem();

    public void customCommand(DataItem dataItem) {

    }

    public void onShowItem(DataItem dataItem) {
        appTitle.setText(dataItem.getDisplay());
    }

    public void openFile(String filePath) {
        Intent intent = new Intent(this, QuestionListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.MESSAGE_FILE_NAME, filePath);
        bundle.putString(AppConstant.MESSAGE_FOLDER, getFolder());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected abstract String getFolder();
}
