package joshisen.abcc.aso.nekokan.ui.aircon;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AirconViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AirconViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("35℃");
    }

    public LiveData<String> getText() {
        return mText;
    }
}