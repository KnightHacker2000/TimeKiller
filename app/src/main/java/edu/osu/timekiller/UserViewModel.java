package edu.osu.timekiller;

import android.content.ClipData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<List<String>> selectedItem = new MutableLiveData<List<String>>();
    public void selectItem(List list) {
        selectedItem.setValue(list);
    }
    public LiveData<List<String>> getSelectedItem() {
        return selectedItem;
    }
}