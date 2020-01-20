package espol.edu.bagtraking.ui.grafica;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GraficViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GraficViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}