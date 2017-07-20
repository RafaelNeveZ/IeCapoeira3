package br.com.iecapoeira.actv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Musica;

/**
 * Created by Felipe Berbert on 16/10/2016.
 */
@EActivity(R.layout.actv_musica_detail)
public class MusicaDetalheActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static final String DEVELOPER_KEY = "AIzaSyASresdn9W52RNjcPtyKEYCf6idSsaldw4";

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView tvLetra;

    @Extra
    Musica musica;

    YouTubePlayerSupportFragment frag;

    @AfterViews
    void init(){
        setHeader();
        frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        frag.initialize(DEVELOPER_KEY, this);
        //youtubeView.initialize(DEVELOPER_KEY, this);
        tvLetra.setText(musica.getLetra());
    }

    public void setHeader() {
        toolbar.setNavigationIcon(R.drawable.logo_voltar);
        toolbar.setTitle(getString(R.string.title_mestres));
        setSupportActionBar(toolbar);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
            if (!wasRestored) {
                youTubePlayer.cueVideo(musica.getLink());
            }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(getString(R.string.error_player), youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            frag.initialize(DEVELOPER_KEY, this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
