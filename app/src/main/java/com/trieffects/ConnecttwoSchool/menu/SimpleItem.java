package com.trieffects.ConnecttwoSchool.menu;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trieffects.ConnecttwoSchool.Interface.ApiUtils;
import com.trieffects.ConnecttwoSchool.Other.PrefrencesUtils;
import com.trieffects.ConnecttwoSchool.R;


/**
 * Created by yarolegovich on 25.03.2017.
 */
public class SimpleItem extends DrawerItem<SimpleItem.ViewHolder> {

    private int selectedItemIconTint;
    private int selectedItemTextTint;

    private int normalItemIconTint;
    private int normalItemTextTint;

    private Drawable icon;
    private String title;
    private int pos;

    public SimpleItem(Drawable icon, String title,int position) {
        this.icon = icon;
        this.title = title;
       pos=position;
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_option, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void bindViewHolder(ViewHolder holder,int position) {


            holder.title.setText(title);
            holder.icon.setImageDrawable(icon);
            holder.badge_notification_1.setVisibility(View.GONE);
            holder.title.setTextColor(isChecked ? selectedItemTextTint : normalItemTextTint);
            holder.icon.setColorFilter(isChecked ? selectedItemIconTint : normalItemIconTint);
            if (pos == 2 &&!PrefrencesUtils.getUserMode().equalsIgnoreCase("1")) {
                if (!ApiUtils.isEmptyString(PrefrencesUtils.getHomecount())) {
                    holder.badge_notification_1.setVisibility(View.VISIBLE);
                    holder.badge_notification_1.setText(PrefrencesUtils.getHomecount());
                }
            }


    }

    public SimpleItem withSelectedIconTint(int selectedItemIconTint) {
        this.selectedItemIconTint = selectedItemIconTint;
        return this;
    }

    public SimpleItem withSelectedTextTint(int selectedItemTextTint) {
        this.selectedItemTextTint = selectedItemTextTint;
        return this;
    }

    public SimpleItem withIconTint(int normalItemIconTint) {
        this.normalItemIconTint = normalItemIconTint;
        return this;
    }

    public SimpleItem withTextTint(int normalItemTextTint) {
        this.normalItemTextTint = normalItemTextTint;
        return this;
    }

    static class ViewHolder extends DrawerAdapter.ViewHolder {

        private ImageView icon;
        private TextView title,badge_notification_1;
        LinearLayout mainl;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            title = (TextView) itemView.findViewById(R.id.title);
            mainl=itemView.findViewById(R.id.main);
            badge_notification_1= itemView.findViewById(R.id.badge_notification_1);
        }
    }
}
