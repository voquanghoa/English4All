package voon.truongvan.english_for_all_level.leaderboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import voon.truongvan.english_for_all_level.util.Utils;

/**
 * Created by voqua on 2/22/2016.
 */
public abstract class BaseGameActivity extends Activity implements GameHelper.GameHelperListener{
    protected GameHelper mHelper;

    public static final int CLIENT_GAMES = GameHelper.CLIENT_GAMES;
    public static final int CLIENT_PLUS = GameHelper.CLIENT_PLUS;
    public static final int CLIENT_ALL = GameHelper.CLIENT_ALL;

    // Requested clients. By default, that's just the games client.
    protected int mRequestedClients = CLIENT_GAMES;

    private final static String TAG = "BaseGameActivity";
    protected boolean mDebugLog = false;

    /**
     * Constructs a BaseGameActivity with the requested clients.
     * @param requestedClients The requested clients (a combination of CLIENT_GAMES,
     *         CLIENT_PLUS).
     */
    protected BaseGameActivity(int requestedClients) {
        super();
        if(isGameCircleEnable()) {
            setRequestedClients(requestedClients);
        }
    }

    protected BaseGameActivity() {
        this(CLIENT_GAMES);
    }

    protected boolean isGameCircleEnable(){
        return false;
    }
    /**
     * Sets the requested clients. The preferred way to set the requested clients is
     * via the constructor, but this method is available if for some reason your code
     * cannot do this in the constructor. This must be called before onCreate or getGameHelper()
     * in order to have any effect. If called after onCreate()/getGameHelper(), this method
     * is a no-op.
     *
     * @param requestedClients A combination of the flags CLIENT_GAMES, CLIENT_PLUS
     *         or CLIENT_ALL to request all available clients.
     */
    protected void setRequestedClients(int requestedClients) {
        mRequestedClients = requestedClients;
    }

    public GameHelper getGameHelper() {
        if (mHelper == null) {
            mHelper = new GameHelper(this, mRequestedClients);
            mHelper.enableDebugLog(mDebugLog);
        }
        return mHelper;
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(isGameCircleEnable()) {
            if (mHelper == null) {
                getGameHelper();
            }
            mHelper.setup(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isGameCircleEnable()) {
            mHelper.onStart(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isGameCircleEnable()) {
            mHelper.onStop();
        }
    }

    @Override
    protected void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        if(isGameCircleEnable()) {
            mHelper.onActivityResult(request, response, data);
        }
    }

    protected GoogleApiClient getApiClient() {
        return mHelper.getApiClient();
    }

    protected boolean isSignedIn() {
        return mHelper.isSignedIn();
    }

    protected void beginUserInitiatedSignIn() {
        mHelper.beginUserInitiatedSignIn();
    }

    protected void signOut() {
        mHelper.signOut();
    }

    protected void showAlert(String message) {
        mHelper.makeSimpleDialog(message).show();
    }

    protected void showAlert(String title, String message) {
        mHelper.makeSimpleDialog(title, message).show();
    }

    protected void enableDebugLog(boolean enabled) {
        mDebugLog = true;
        if (mHelper != null) {
            mHelper.enableDebugLog(enabled);
        }
    }

    protected String getInvitationId() {
        return mHelper.getInvitationId();
    }

    protected void reconnectClient() {
        mHelper.reconnectClient();
    }

    protected boolean hasSignInError() {
        return mHelper.hasSignInError();
    }

    protected GameHelper.SignInFailureReason getSignInError() {
        return mHelper.getSignInError();
    }

    protected void showAllLeaderBoard(){
        GoogleApiClient apiClient = getGameHelper().getApiClient();
        if(apiClient.isConnected()) {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(apiClient), 2);
        }
    }

    protected void showLeaderboard(String leaderBoardId){
        GoogleApiClient apiClient = getGameHelper().getApiClient();
        if(apiClient.isConnected()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(apiClient, leaderBoardId), 2);
        }
    }

    protected void showAchievement(){
        GoogleApiClient apiClient = getGameHelper().getApiClient();
        if(apiClient.isConnected()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(apiClient), 2);
        }
    }

}

