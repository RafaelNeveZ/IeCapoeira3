package br.com.iecapoeira.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;

import br.com.hemobile.BaseActivity;
import br.com.hemobile.MyApplication;
import br.com.iecapoeira.IEApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.ChatActivity;
import br.com.iecapoeira.actv.ChatActivity_;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.UserDetails;
import br.com.iecapoeira.utils.HETextUtil;

@EFragment(R.layout.frag_event_detail)
@OptionsMenu(R.menu.event_detail)
public class EventDetailFragment extends Fragment {

    @ViewById
    TextView textName;

    @ViewById
    TextView textDate;

    @ViewById
    TextView textLocation;

    @ViewById
    TextView textQuantity;

    @ViewById
    TextView textDesc;

    private Event obj;

    @ViewById
    ImageView img;

    @ViewById
    ImageView profileImg;

    private final GetDataCallback callbackProfilePicture = new GetDataCallback() {
        @Override
        public void done(byte[] bytes, ParseException e) {
            if(e == null)
                setProfilePicture(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            else
                e.printStackTrace();

        }
    };

    private final GetDataCallback callback = new GetDataCallback() {
        @Override
        public void done(byte[] bytes, ParseException e) {
            if(e == null)
                setImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            else
                e.printStackTrace();
        }
    };

    @OptionsMenuItem(R.id.menu_go)
    MenuItem menuGo;

    @OptionsMenuItem(R.id.menu_delete)
    MenuItem menuDelete;

    @UiThread
    public void setImage(Bitmap picture) {
        img.setImageBitmap(picture);
    }

    @UiThread
    public void setProfilePicture(Bitmap picture) {
        profileImg.setImageBitmap(picture);
    }

    @AfterViews
    public void init() {
        String id = getActivity().getIntent().getStringExtra("id");
        obj = ParseObject.createWithoutData(Event.class, id);
        obj.fetchIfNeededInBackground(new GetCallback<Event>() {
            @Override
            public void done(Event event, ParseException e) {
                obj = event;
                try {
                    obj.setOwner((UserDetails) obj.getOwner().fetchIfNeeded());
                } catch (ParseException e1) {}
                if (e == null) {
                    update();
                }
            }
        });
    }

    @UiThread
    void update() {
        textName.setText(obj.getName());
        String pattern = getString(R.string.date_hour_pattern);
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        textDate.setText(sdf.format(obj.getDate()));
        textLocation.setText(String.format("%s\n%s, %s - %s", HETextUtil.toTitleCase(obj.getAddress()), HETextUtil.toTitleCase(obj.getCity()), obj.getState().toUpperCase(), obj.getCountry()));
        int howManyIsGoing = obj.getHowManyIsGoing();
        textQuantity.setText(getResources().getQuantityString(R.plurals.x_pessoas_irao, howManyIsGoing, howManyIsGoing));
        UserDetails owner = null;
        try {
            owner = obj.getOwner();
            textDesc.setText(obj.getDescription() + "\n\n- " + owner.getName());
        } catch (Exception ex) {
            textDesc.setText(obj.getDescription());
        }

        Bitmap eventImage = obj.getProfilePicture(callback);
        if (eventImage != null)
            setImage(eventImage);

        if (owner != null) {
            Bitmap profilePicture = owner.getProfilePicture(callbackProfilePicture);
            if (profilePicture != null)
                setProfilePicture(profilePicture);
        }




    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        try {
            menuGo.setIcon(obj.isUserGoing(IEApplication.getUserDetails()) ? R.drawable.ic_eventdetail_action_unselect : R.drawable.ic_eventdetail_action_select);
        } catch (Exception e) {
            setIconLater();
        }
        try {
            if (obj.getOwner().equals(IEApplication.getUserDetails())) {
                menuDelete.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void setIconLater() {
        try {
            menuGo.setIcon(obj.isUserGoing(IEApplication.getUserDetails()) ? R.drawable.ic_eventdetail_action_unselect : R.drawable.ic_eventdetail_action_select);
        } catch (Exception e) {
        }
        try {
            if (obj.getOwner().equals(IEApplication.getUserDetails())) {
                menuDelete.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OptionsItem
    public void menuChat() {
        if (MyApplication.hasInternetConnection()) {
            startActivity(new Intent(getActivity(), ChatActivity_.class).putExtra(ChatActivity.EXTRA_ID, obj.getObjectId()).putExtra(ChatActivity.EXTRA_CHAT_NAME, obj.getName()));
        } else {
            Toast.makeText(getActivity(), R.string.msg_erro_no_internet, Toast.LENGTH_LONG).show();
        }
    }

    @OptionsItem
    public void menuGo() {
        UserDetails user = IEApplication.getUserDetails();
        boolean isGoing;
        try {
            isGoing = ! obj.isUserGoing(user);
        } catch (ParseException e) {
            isGoing = true;
        }

        try {
            obj.setUserGoing(user, isGoing);
            menuGo.setIcon(isGoing ? R.drawable.ic_eventdetail_action_unselect : R.drawable.ic_eventdetail_action_select);
        } catch (Exception e) {
        }
        update();
    }


    @OptionsItem
    public void menuDelete() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.titulo_delete_event)
                .setPositiveButton(R.string.menu_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteEvent();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null).show();
    }

    @Background
    public void deleteEvent() {
        ((BaseActivity)getActivity()).showProgress(getString(R.string.aguarde));
        try {
            obj.delete();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((BaseActivity)getActivity()).dismissProgress();
        getActivity().finish();
    }

    @OptionsItem
    public void menuDenunciar() {
        final EditText edit = new EditText(getActivity());
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.titulo_denuncia)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String string = edit.getText().toString();
                        if (!string.isEmpty()) {
                            ParseObject denuncia = new ParseObject("Denuncia");
                            denuncia.put("user", IEApplication.getUserDetails());
                            denuncia.put("text", string);
                            denuncia.put("sala", obj.getObjectId());
                            denuncia.saveEventually();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setView(edit).show();
    }

}