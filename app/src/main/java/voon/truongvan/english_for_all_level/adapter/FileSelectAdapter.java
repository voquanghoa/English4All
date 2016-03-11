package voon.truongvan.english_for_all_level.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import voon.truongvan.english_for_all_level.R;
import voon.truongvan.english_for_all_level.controller.UserResultController;
import voon.truongvan.english_for_all_level.model.DataItem;
import voon.truongvan.english_for_all_level.model.UserResult;

/**
 * Created by Vo Quang Hoa on 12/20/2015.
 */
public class FileSelectAdapter extends BaseAdapter {
    private Context context;
    private DataItem dataItem;
    private List<DataItem> children;
    private Stack<DataItem> pathStack;
    private FileSelectFeedback selectFeedback;

    public FileSelectAdapter(Context context, DataItem dataItem, FileSelectFeedback selectFeedback){
        this.context = context;
        this.pathStack = new Stack<DataItem>();
        this.selectFeedback = selectFeedback;
        setDisplayDataItem(dataItem);
    }

    public boolean showParent(){
        if(pathStack.size()>0){
            setDisplayDataItem(pathStack.pop());
            return true;
        }
        return false;
    }

    public void setDisplayDataItem(DataItem dataItem){
        this.dataItem = dataItem;
        this.children = dataItem.getChildren();
        selectFeedback.onShowItem(dataItem);
        if(this.children == null){
            this.children = new ArrayList<DataItem>();
        }
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return children.size();
    }

    public Object getItem(int position) {
        return children.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final DataItem rowDataItem = (DataItem) getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.data_item_layout, null);
        }

        View inUsedView = getInUsedView(convertView, rowDataItem);

        TextView button =  (TextView)inUsedView.findViewById(R.id.button_main_menu);
        View.OnClickListener onClickListener = new View.OnClickListener(){
            public void onClick(View v) {
                List<DataItem> localChildren = rowDataItem.getChildren();

                if(localChildren!=null && localChildren.size()>0){
                    FileSelectAdapter.this.pathStack.push(dataItem);
                    FileSelectAdapter.this.setDisplayDataItem(rowDataItem);
                }else if(selectFeedback!=null){
                    if(rowDataItem.isFileTest()){
                        selectFeedback.openFile(getCurrentPath() + rowDataItem.getFileName());
                    }else{
                        selectFeedback.customCommand(rowDataItem);
                    }
                }
            }
        };

        if(rowDataItem.isFileTest()){
            setUserResult(rowDataItem, inUsedView);
        }

        button.setOnClickListener(onClickListener);
        inUsedView.setOnClickListener(onClickListener);

        button.setText(rowDataItem.getDisplay());
        return convertView;
    }

    private void setUserResult(DataItem dataItem, View displayView){
        String filePath = getCurrentPath() + dataItem.getFileName();
        UserResult userResult = UserResultController.getInstance().getResult(filePath);
        TextView tvResult = (TextView)displayView.findViewById(R.id.text_badge);

        if(userResult == null){
            tvResult.setVisibility(View.INVISIBLE);
        }else{
            tvResult.setVisibility(View.VISIBLE);
            if(userResult.getCorrect()*2 >= userResult.getTotal()){
                tvResult.setBackgroundResource(R.drawable.green_badge);
            }
            else {
                tvResult.setBackgroundResource(R.drawable.gray_badge);
            }

            tvResult.setText(userResult.getDisplay());
        }
    }

    private String getCurrentPath(){
        StringBuilder sb = new StringBuilder();
        for(int i=1; i<pathStack.size(); i++){
            sb.append(pathStack.get(i).getFileName()).append("/");
        }
        sb.append(dataItem.getFileName()).append("/");
        return sb.toString();
    }

    private View getInUsedView(View convertView, DataItem dataItem){
        View fileView = convertView.findViewById(R.id.file_data_item_select_layout);
        View folderView = convertView.findViewById(R.id.folder_data_item_select_layout);

        if(dataItem.isFileTest()){
            fileView.setVisibility(View.VISIBLE);
            folderView.setVisibility(View.GONE);
            return fileView;
        }else{
            folderView.setVisibility(View.VISIBLE);
            fileView.setVisibility(View.GONE);
            return folderView;
        }
    }

    public interface FileSelectFeedback {
        void customCommand(DataItem dataItem);

        void onShowItem(DataItem dataItem);
        void openFile(String filePath);
    }
}
