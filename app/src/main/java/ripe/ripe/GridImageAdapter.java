package ripe.ripe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class GridImageAdapter extends BaseAdapter {

    private Context context;
    private int[] thumbIds;

    public GridImageAdapter(Context c, int[] images) {
        context = c;
        this.thumbIds = images;
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private String mAppend;
    private ArrayList<String> imgURLs;

    public GridImageAdapter(Context context, int layoutResource, String append, ArrayList<String> imgURLs) {
        super();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        this.layoutResource = layoutResource;
        mAppend = append;
        this.imgURLs = imgURLs;
    }

    @Override
    public int getCount() {
        return thumbIds.length;
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) { return null; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.profile_image, null);
            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            final ViewHolder viewHolder = new ViewHolder(imageView);
            view.setTag(viewHolder);
        }
        final ViewHolder viewHolder = (ViewHolder)view.getTag();
        viewHolder.imageView.setImageResource(thumbIds[i]);
        return view;
    }

    private class ViewHolder {
        private final ImageView imageView;
        public ViewHolder(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}