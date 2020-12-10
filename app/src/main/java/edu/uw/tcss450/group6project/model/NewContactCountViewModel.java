package edu.uw.tcss450.group6project.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * @author Chase Alder
 * View Model to track if new contact updates have been sent
 */
public class NewContactCountViewModel extends ViewModel {

    private MutableLiveData<Integer> mNewContactCount;

    public NewContactCountViewModel() {
        mNewContactCount = new MutableLiveData<>();
        mNewContactCount.setValue(0);
    }

    /**
     * Adds an observer to the number of messages.
     * @param owner owner of the current view lifecycle
     * @param observer observer
     */
    public void addContactCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {
        mNewContactCount.observe(owner, observer);
    }

    /**
     * Increases the number of contact updates by 1.
     */
    public void increment() {
        mNewContactCount.setValue(mNewContactCount.getValue() + 1);
    }

    /**
     * Sets the number of new contact updates back to 0.
     */
    public void reset() {
        mNewContactCount.setValue(0);
    }
}
