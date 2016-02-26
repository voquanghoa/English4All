package voon.truongvan.english_for_all_level.constant;

/**
 * Created by Vo Quang Hoa on 12/20/2015.
 */
public interface AppConstant {
    String JSON_DATA_FILE = "data.json";
    String INDEX_DATA_FILE = "index.txt";
    String SERVER_BASE_PATH = "http://doanit.com/english4all/";

    String GRAMMAR_FOLDER = "grammar/";
    String EXAMINATION_FOLDER = "examination/";
    String ASSET_FOLDER = "assets/";

    String GRAMMAR_JSON_PATH = SERVER_BASE_PATH + GRAMMAR_FOLDER + JSON_DATA_FILE;
    String EXAMINATION_JSON_PATH = SERVER_BASE_PATH + EXAMINATION_FOLDER + JSON_DATA_FILE;

    String MESSAGE_FILE_NAME = "file_name";
    String MESSAGE_FOLDER = "category";

    String USER_RESULT_FILE = "user_result.json";
    String CHARSET = "UTF-8";

    String PACKAGE_NAME = "voon.truongvan.english_for_all_level";
    String APP_PACKAGE = "https://play.google.com/store/apps/details?id=" + PACKAGE_NAME;
    String FACEBOOK_SHARE_PATH = "https://www.facebook.com/sharer/sharer.php?u=";
    String FACEBOOK_SHARE_FULL_PATH = FACEBOOK_SHARE_PATH + APP_PACKAGE;

    String GPLUS_URL_SHARE = "https://plus.google.com/u/0/communities/105959375540598215686";
}
