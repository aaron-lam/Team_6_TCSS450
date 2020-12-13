package edu.uw.tcss450.group6project.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * @author Charles Bryan
 * View Model to track if new messages have been sent
 */
public class NewMessageCountViewModel extends ViewModel {
    private MutableLiveData<Integer> mNewMessageCount;

    public NewMessageCountViewModel() {
        mNewMessageCount = new MutableLiveData<>();
        mNewMessageCount.setValue(0);
    }

    /**
     * Adds an observer to the number of messages.
     * @param owner owner of the current view lifecycle
     * @param observer observer
     */
    public void addMessageCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {
        mNewMessageCount.observe(owner, observer);
    }

    /**
     * Increases the number of messages by 1.
     */
    public void increment() {
        mNewMessageCount.setValue(mNewMessageCount.getValue() + 1);
    }


    /**
     * Decreases the number of messages by 1.
     */
    public void decrement() {
        mNewMessageCount.setValue(mNewMessageCount.getValue() - 1);
    }

    /**
     * Sets the number of new messages back to 0.
     */
    public void reset() {
        mNewMessageCount.setValue(0);
    }
}