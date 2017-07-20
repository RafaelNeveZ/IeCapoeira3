package br.com.iecapoeira.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.ChatActivity_;
import br.com.iecapoeira.actv.ClassScheduleActivity_;
import br.com.iecapoeira.actv.ContatoActivity_;
import br.com.iecapoeira.actv.DashboardActivity_;
import br.com.iecapoeira.actv.ListaMestreActivity_;
import br.com.iecapoeira.actv.LoginActivity;
import br.com.iecapoeira.actv.LoginActivity_;
import br.com.iecapoeira.actv.MainActivity_;
import br.com.iecapoeira.actv.MestreActivity_;
import br.com.iecapoeira.actv.MusicaActivity_;
import br.com.iecapoeira.actv.ParceirosActivity_;
import br.com.iecapoeira.adapter.LeftMenuAdapter;
import br.com.iecapoeira.model.DashboardEnum;
import br.com.iecapoeira.model.DashboardItem;

/**
 * Created by Rafael on 11/08/16.
 */
@EFragment(R.layout.frag_left_menu)
public class LeftMenuFragment extends Fragment {

    @ViewById
    ListView listView;

    List<DashboardItem> items;

    @AfterViews
    public void init() {
        loadItems();
        listView.setAdapter(new LeftMenuAdapter(items));
    }

    public void loadItems() {
        items = new ArrayList<>();

        DashboardItem events = new DashboardItem();
        events.title = getResources().getStringArray(R.array.dashboard)[0];
        events.titleColor = getResources().getColor(R.color.white);
        events.iconCode = getString(R.string.icon_eventos);
        events.backgroundColor = getResources().getColor(R.color.green);
        events.dashboardEnum = DashboardEnum.EVENTS;
        items.add(events);

        DashboardItem personalSchedule = new DashboardItem();
        personalSchedule.title = getResources().getStringArray(R.array.dashboard)[1];
        personalSchedule.titleColor = getResources().getColor(R.color.white);
        personalSchedule.iconCode = getString(R.string.icon_agenda_pessoal);
        personalSchedule.backgroundColor = getResources().getColor(R.color.yellow);
        personalSchedule.dashboardEnum = DashboardEnum.PERSONAL_SCHEDULE;
        items.add(personalSchedule);

        DashboardItem classesSchedule = new DashboardItem();
        classesSchedule.title = getResources().getStringArray(R.array.dashboard)[2];
        classesSchedule.titleColor = getResources().getColor(R.color.white);
        classesSchedule.iconCode = getString(R.string.icon_aulas);
        classesSchedule.backgroundColor = getResources().getColor(R.color.blue);
        classesSchedule.dashboardEnum = DashboardEnum.CLASSES_SCHEDULE;
        items.add(classesSchedule);

        DashboardItem chat = new DashboardItem();
        chat.title = getResources().getStringArray(R.array.dashboard)[6];
        chat.titleColor = getResources().getColor(R.color.white);
        chat.iconCode = getString(R.string.icon_bate_papo);
        chat.backgroundColor = getResources().getColor(R.color.green);
        chat.dashboardEnum = DashboardEnum.CHAT;
        items.add(chat);

        DashboardItem music = new DashboardItem();
        music.title = getResources().getStringArray(R.array.dashboard)[4];
        music.titleColor = getResources().getColor(R.color.white);
        music.iconCode = getString(R.string.icon_musicas);
        music.backgroundColor = getResources().getColor(R.color.yellow);
        music.dashboardEnum = DashboardEnum.MUSIC;
        items.add(music);

        DashboardItem teachers = new DashboardItem();
        teachers.title = getResources().getStringArray(R.array.dashboard)[3];
        teachers.titleColor = getResources().getColor(R.color.white);
        teachers.iconCode = getString(R.string.icon_mestres);
        teachers.backgroundColor = getResources().getColor(R.color.blue);
        teachers.dashboardEnum = DashboardEnum.TEACHERS;
        items.add(teachers);

        DashboardItem edicts = new DashboardItem();
        edicts.title = getResources().getStringArray(R.array.dashboard)[5];
        edicts.titleColor = getResources().getColor(R.color.white);
        edicts.iconCode = getString(R.string.icon_editais);
        edicts.backgroundColor = getResources().getColor(R.color.green);
        edicts.dashboardEnum = DashboardEnum.EDICTS;
        items.add(edicts);

        DashboardItem partners = new DashboardItem();
        partners.title = getResources().getStringArray(R.array.dashboard)[7];
        partners.titleColor = getResources().getColor(R.color.white);
        partners.iconCode = getString(R.string.icon_parceiros);
        partners.backgroundColor = getResources().getColor(R.color.yellow);
        partners.dashboardEnum = DashboardEnum.PARTNERS;
        items.add(partners);

        DashboardItem history = new DashboardItem();
        history.title = getResources().getStringArray(R.array.dashboard)[9];
        history.titleColor = getResources().getColor(R.color.gray_txt);
        //history.iconCode = String.valueOf((char) 0xe916);
        history.backgroundColor = getResources().getColor(R.color.white);
        history.dashboardEnum = DashboardEnum.HISTORY;
        items.add(history);

       DashboardItem user = new DashboardItem();
        user.title = getResources().getStringArray(R.array.dashboard)[10];
        user.titleColor = getResources().getColor(R.color.gray_txt);
        //user.iconCode = String.valueOf((char) 0xe916);
        user.backgroundColor = getResources().getColor(R.color.white);
        user.dashboardEnum = DashboardEnum.USER;
        items.add(user);


        DashboardItem contact = new DashboardItem();
        contact.title = getResources().getStringArray(R.array.dashboard)[8];
        contact.titleColor = getResources().getColor(R.color.gray_txt);
        //contact.iconCode = String.valueOf((char) 0xe916);
        contact.backgroundColor = getResources().getColor(R.color.white);
        contact.dashboardEnum = DashboardEnum.CONTACT;
        items.add(contact);
    }

    @ItemClick
    void listViewItemClicked(DashboardItem selectedModel) {
        Intent intent = new Intent(getActivity(), ClassScheduleActivity_.class);
        switch (selectedModel.dashboardEnum) {
            case EVENTS:
                intent = new Intent(getActivity(), MainActivity_.class);
                break;
            case PERSONAL_SCHEDULE:
//                intent = new Intent(getActivity(), PersonalScheduleActivity_.class);
                break;
            case CLASSES_SCHEDULE:
                intent = new Intent(getActivity(), ClassScheduleActivity_.class);
                break;
            case TEACHERS:
                intent = new Intent(getActivity(), ListaMestreActivity_.class);
                break;
            case MUSIC:
                intent = new Intent(getActivity(), MusicaActivity_.class);
                break;
            case EDICTS:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://prosas.com.br"));
                break;
            case CHAT:
                intent = new Intent(getActivity(), ChatActivity_.class);
                break;
            case PARTNERS:
                intent = new Intent(getActivity(), ParceirosActivity_.class);
                break;
            case HISTORY:
                intent = new Intent(getActivity(), ListaMestreActivity_.class);
                break;
            case USER:
                intent = new Intent(getActivity(), LoginActivity_.class);
                break;
            case CONTACT:
                intent = new Intent(getActivity(), ContatoActivity_.class);
                break;

        }
        startActivity(intent);
    }

    @Click(R.id.iv_header)
    void clickHeader(){
        DashboardActivity_.intent(this).start();
    }

}
