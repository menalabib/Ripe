package ripe.ripe.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.sql.Array;
import java.util.ArrayList;

import ripe.ripe.APIUtils.RipeContentService;
import ripe.ripe.R;

public class GridImageAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private String[] imgURLs;

    public GridImageAdapter(Context context, String[] imgURLs) {
        super();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        this.imgURLs = imgURLs;
    }

    @Override
    public int getCount() {
        return imgURLs.length;
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
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.profile_image, null);
            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            final ViewHolder viewHolder = new ViewHolder(imageView);
            view.setTag(viewHolder);
        }
        final ViewHolder viewHolder = (ViewHolder)view.getTag();
        Glide.with(mContext).load(RipeContentService.createBlobURL(imgURLs[i])).into(viewHolder.imageView);
        return view;
    }

    private class ViewHolder {
        private final ImageView imageView;
        public ViewHolder(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}