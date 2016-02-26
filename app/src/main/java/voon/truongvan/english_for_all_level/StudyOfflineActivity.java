package voon.truongvan.english_for_all_level;

import voon.truongvan.english_for_all_level.control.FileSelectionActivity;
import voon.truongvan.english_for_all_level.controller.AssetDataController;
import voon.truongvan.english_for_all_level.model.DataItem;

/**
 * Created by Vo Quang Hoa on 12/21/2015.
 */
public class StudyOfflineActivity extends FileSelectionActivity {
    protected DataItem getDataItem() {
        return AssetDataController.getInstance().getGrammarDataItem();
    }

    protected String getFolder() {
        return ASSET_FOLDER;
    }
}
