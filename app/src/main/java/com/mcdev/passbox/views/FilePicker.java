package com.mcdev.passbox.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

import com.mcdev.passbox.R;
import com.mcdev.passbox.utils.Constants;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * File picker extending Dialog
 */
public class FilePicker {
    
    private String[] fileList;
    private File currentPath;

    /**
     * Listeners interfaces
     */
    public interface FileSelectedListener {
        void fileSelected(File file);
    }
    public interface DirectorySelectedListener {
        void directorySelected(File directory);
    }
    
    private ListenerList<FileSelectedListener> fileListenerList = new ListenerList<>();
    private ListenerList<DirectorySelectedListener> dirListenerList = new ListenerList<>();
    private final Context context;
    private boolean selectDirectoryOption;
    private String fileEndsWith = ".db";

    /**
     * Constructor
     * @param context The calling Activity
     * @param path The initial path
     */
    public FilePicker(Context context, File path) {
        this.context = context;
        if (!path.exists()) {
            path = Environment.getExternalStorageDirectory();
        }
        loadFileList(path);
    }

    /**
     * @return file dialog
     */
    public Dialog createFileDialog() {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

//        builder.setTitle(currentPath.getPath());
        builder.setTitle(context.getString(R.string.import_choose_file));
        if (selectDirectoryOption) {
            builder.setPositiveButton(context.getString(R.string.import_choose_directory), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(Constants.TAG_APPLICATION_LOG, currentPath.getPath());
                    fireDirectorySelectedEvent(currentPath);
                }
            });
        }

        builder.setItems(fileList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String fileChosen = fileList[which];
                File chosenFile = getChosenFile(fileChosen);
                if (chosenFile.isDirectory()) {
                    loadFileList(chosenFile);
                    dialog.cancel();
                    dialog.dismiss();
                    showDialog();
                } else {
                    fireFileSelectedEvent(chosenFile);
                }
            }
        });

        dialog = builder.show();
        return dialog;
    }


    public void addFileListener(FileSelectedListener listener) {
        fileListenerList.add(listener);
    }

    public void removeFileListener(FileSelectedListener listener) {
        fileListenerList.remove(listener);
    }

    public void setSelectDirectoryOption(boolean selectDirectoryOption) {
        this.selectDirectoryOption = selectDirectoryOption;
    }

    public void addDirectoryListener(DirectorySelectedListener listener) {
        dirListenerList.add(listener);
    }

    public void removeDirectoryListener(DirectorySelectedListener listener) {
        dirListenerList.remove(listener);
    }

    /**
     * Show file dialog
     */
    public void showDialog() {
        createFileDialog().show();
    }

    private void fireFileSelectedEvent(final File file) {
        fileListenerList.fireEvent(new ListenerList.FireHandler<FileSelectedListener>() {
            public void fireEvent(FileSelectedListener listener) {
                listener.fileSelected(file);
            }
        });
    }

    private void fireDirectorySelectedEvent(final File directory) {
        dirListenerList.fireEvent(new ListenerList.FireHandler<DirectorySelectedListener>() {
            public void fireEvent(DirectorySelectedListener listener) {
                listener.directorySelected(directory);
            }
        });
    }

    private void loadFileList(File path) {
        this.currentPath = path;
        List<String> r = new ArrayList<>();
        if (path.exists()) {
            if (path.getParentFile() != null) {
                r.add(context.getString(R.string.import_choose_file_up));
            }
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    if (sel.isHidden() || !sel.canRead()) {
                        return false;
                    }
                    if (selectDirectoryOption) {
                        return sel.isDirectory();
                    } else {
                        boolean endsWith = fileEndsWith == null || filename.toLowerCase().endsWith(fileEndsWith);
                        return endsWith || sel.isDirectory();
                    }
                }
            };
            String[] fileList1 = path.list(filter);
            Collections.addAll(r, fileList1);
        }
        fileList = r.toArray(new String[r.size()]);
    }

    private File getChosenFile(String fileChosen) {
        if (fileChosen.equals(context.getString(R.string.import_choose_file_up))) {
            return currentPath.getParentFile();
        } else {
            return new File(currentPath, fileChosen);
        }
    }

    public void setFileEndsWith(String fileEndsWith) {
        if (fileEndsWith != null) {
            this.fileEndsWith = fileEndsWith.toLowerCase();
        }
    }
}

class ListenerList<L> {
    private List<L> listenerList = new ArrayList<>();

    public interface FireHandler<L> {
        void fireEvent(L listener);
    }

    public void add(L listener) {
        listenerList.add(listener);
    }

    public void fireEvent(FireHandler<L> fireHandler) {
        List<L> copy = new ArrayList<>(listenerList);
        for (L l : copy) {
            fireHandler.fireEvent(l);
        }
    }

    public void remove(L listener) {
        listenerList.remove(listener);
    }

    public List<L> getListenerList() {
        return listenerList;
    }
}