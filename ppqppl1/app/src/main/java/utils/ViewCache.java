//package utils;
//
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.ppqppl1.R;
//
//public class ViewCache {
//    private View baseView;
//    private TextView txt_name,txt_info;
//    private ImageView imageView;
//    public ViewCache(View baseView) {
//        this.baseView = baseView;
//    }
//    public TextView getTxt_name() {
//        if (txt_name == null) {
//            txt_name = (TextView) baseView.findViewById(R.id.nametxt);
//        }
//        return txt_name;
//    }
//    public TextView getTxt_info() {
//        if (txt_info == null) {
//            txt_info = (TextView) baseView.findViewById(R.id.infotxt);
//        }
//        return txt_info;
//    }
//    public ImageView getImageView() {
//        if (imageView == null) {
//            imageView = (ImageView) baseView.findViewById(R.id.mainpic);
//        }
//        return imageView;
//    }
//}