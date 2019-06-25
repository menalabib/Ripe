package ripe.ripe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GridImageAdapter extends BaseAdapter {

    private Context context;

    public GridImageAdapter(Context c) {
        context = c;
    }

    @Override
    public int getCount() {
        return thumbIds.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.profile_image, null);
        }
        imageView = view.findViewById(R.id.imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(thumbIds[i]);
        return imageView;
    }

    private Integer[] thumbIds = {
            R.drawable.leader_1_trav,
            R.drawable.leader_2_ye,
            R.drawable.leader_3_kenny,
            R.drawable.leader_4_drake,
            R.drawable.leader_5_rocky,
            R.drawable.leader_6_rashad,
            R.drawable.leader_7_skepta,
            R.drawable.like_logo,
            R.drawable.dislike_logo,
            R.drawable.apple,
            R.drawable.yeet,
            R.drawable.fast,
            R.drawable.help
    };
}