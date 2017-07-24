package br.com.iecapoeira.actv;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.utils.ImageUtil;

/**
 * Created by Rafael on 10/08/16.
 */
@EActivity(R.layout.actv_class_schedule_detail)
@OptionsMenu(R.menu.class_detail)
public class ClassScheduleDetailActivity extends AppCompatActivity {

    public static Aula model;
    public boolean IsNotAdmin = false;
    @ViewById
    Toolbar toolbar;


    @ViewById
    ImageView ivTeacher;

    @ViewById
    TextView tvTeacher, tvDescription, tvDays, tvTime, tvPlace;

    @OptionsMenuItem(R.id.delete)
    MenuItem delete;


    @OptionsMenuItem(R.id.edit)
    MenuItem edit;

    @AfterViews
    public void init() {
        setHeader();
        if (model == null) {
            return;
        }

        ImageUtil.setBitmapIntoImageView(model, model.FOTOPROFESSOR, ivTeacher, 80);
        tvTeacher.setText(model.getMestre());
        tvDescription.setText(model.getSobreAula());
        tvTime.setText(model.getHorarioStart()+ " às "+ model.getHorarioEnd());
        tvPlace.setText(model.getNomeAcademia());
        setDaysText();
    }


    public void setHeader() {
        toolbar.setNavigationIcon(R.drawable.logo_voltar);
        toolbar.setTitle(getString(R.string.class_schedule));
        setSupportActionBar(toolbar);
    }

    // makes the back on action bar works as back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.delete)
            ContatoActivity_.intent(this).start();

        if (item.getItemId() == R.id.edit)
            DashboardActivity_.intent(this).start();

        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if(IsNotAdmin)
            menu.getItem(1).setEnabled(false);

        return true;

    }

    public void setDaysText() {
        String days = "";
        if (model.getDiasSemana() != null) {
            for(int i = 0; i < model.getDiasSemana().size(); i++) {
                try {
                    days = days + getResources().getStringArray(R.array.days)[i];
                    if (i + 1 < model.getDiasSemana().size()) {
                        days += ",";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        tvDays.setText(days);
    }


}
