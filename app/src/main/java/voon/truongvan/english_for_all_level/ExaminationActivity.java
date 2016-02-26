package voon.truongvan.english_for_all_level;

import voon.truongvan.english_for_all_level.control.FileSelectionActivity;
import voon.truongvan.english_for_all_level.controller.OnlineDataController;
import voon.truongvan.english_for_all_level.model.DataItem;

/**
 * Created by Vo Quang Hoa on 12/26/2015.
 */
public class ExaminationActivity extends FileSelectionActivity {
    protected DataItem getDataItem() {
        return OnlineDataController.getInstance().getExaminationDataItem();
    }

    protected String getFolder() {
        return EXAMINATION_FOLDER;
    }
}
