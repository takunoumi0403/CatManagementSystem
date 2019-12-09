package joshisen.abcc.aso.nekokan.ui.toilet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ToiletViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ToiletViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is toilet fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}