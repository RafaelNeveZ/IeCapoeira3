package br.com.iecapoeira.model;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EBean
public class SubscribeHolder {

    @Pref
    Subscribe_ holder;

    public void saveSubscribe(String channel, boolean subscribe) {
        if (subscribe) {
            subscribe(channel);
        } else {
            unsubscribe(channel);
        }
    }

    public boolean isSubscribed(String channel) {
        String[] channels = getChannels();
        for (int i = 0; i < channels.length; i++) {
            if (channels[i].equals(channel)) {
                return true;
            }
        }
        return false;
    }

    private void unsubscribe(String channel) {
        String strChannels = holder.channels().getOr("");
        String[] channels = strChannels.isEmpty() ? new String[0] : strChannels.split(",");
        for (int i = 0; i < channels.length; i++) {
            if (channels[i].equals(channel)) {
                String newValue;
                if (i == 0) {
                    newValue = strChannels.replace(channel, "");
                    if (newValue.startsWith(",")) {
                        newValue = newValue.substring(1);
                    }
                } else {
                    newValue = strChannels.replace("," + channel, "");
                }
                holder.channels().put(newValue);
                return;
            }
        }
    }

    private void subscribe(String channel) {
        if (isSubscribed(channel)) return;
        String channels = holder.channels().getOr("");
        holder.channels().put(channels.isEmpty() ? channel : channels + "," + channel);
    }

    public String[] getChannels() {
        String s = holder.channels().getOr("");
        String[] channels = s.isEmpty() ? new String[0] : s.split(",");
        return channels;
    }
}