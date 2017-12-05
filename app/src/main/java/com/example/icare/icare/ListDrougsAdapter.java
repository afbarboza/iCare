/*
 * Copyright (c) 2017.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.icare.icare;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Giovanni on 03/12/2017.
 */

public class ListDrougsAdapter extends BaseAdapter {
    private final Activity act;
    private final List<Droug> drougs;

    public ListDrougsAdapter(Activity act, List<Droug> drougs) {
        this.drougs = drougs;
        this.act = act;
    }
    @Override
    public int getCount() {
        return drougs.size();
    }

    @Override
    public Object getItem(int i) {
        return drougs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = act.getLayoutInflater().inflate(R.layout.activity_caregiver_dashboard_list_drougs_adapter, parent, false);


        ImageView oldPhoto = (ImageView) rowView.findViewById(R.id.iv_old_portrait);
        TextView name = (TextView) rowView.findViewById(R.id.tv_droug_name);
        TextView period = (TextView) rowView.findViewById(R.id.drougs_period);
        TextView observations = (TextView) rowView.findViewById(R.id.drougs_observation);
        TextView dosage = (TextView) rowView.findViewById(R.id.drougs_dosage);

        name.setText(drougs.get(position).getName());
        period.setText(drougs.get(position).getPeriod());
        observations.setText(drougs.get(position).getObservations());
        dosage.setText(drougs.get(position).getDosage());

        return rowView;
    }


}
