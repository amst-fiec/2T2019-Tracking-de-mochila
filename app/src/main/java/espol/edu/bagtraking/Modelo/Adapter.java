package espol.edu.bagtraking.Modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import espol.edu.bagtraking.R;

public class Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String,String>> array;
    public Adapter(Context context, ArrayList<HashMap<String,String>> array){
        this.context = context;
        this.array = array;
    }
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item,null);
        }
        TextView textView = view.findViewById(R.id.lstContent);
        Iterator it = array.get(i).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            if(pair.getKey().equals("user_id_maleta") ){
                textView.setText(pair.getValue().toString());
            }

            // avoids a ConcurrentModificationException
        }

        return view;
    }
}
