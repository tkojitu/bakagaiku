package org.jitu.wagtail;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FileArrayAdapter extends ArrayAdapter<File> {
    private Context context;
    private int id;
    private List<File> files;

    public FileArrayAdapter(Context context, int textViewResourceId,
                            List<File> files) {
        super(context, textViewResourceId, files);
        this.context = context;
        id = textViewResourceId;
        this.files = files;
    }

    public File getFile(int i) {
        return files.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }
        TextView t1 = (TextView) v.findViewById(R.id.file_view);
        if (t1 == null) {
            return v;
        }
        if (position == 0) {
            t1.setText("..");
            return v;
        }
        final File file = files.get(position);
        if (file == null) {
            return v;
        }
        String name = file.getName();
        if (file.isDirectory()) {
            name += "/";
        }
        t1.setText(name);
        return v;
    }
}
