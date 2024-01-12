package com.example.musicapp.realm;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class LiveRealmResults<T extends RealmModel > extends MutableLiveData<List<T>> {
    private final RealmResults<T> results;

    private final Realm realm = Realm.getDefaultInstance();

    private OrderedRealmCollectionChangeListener<RealmResults<T>> listener = (results, changeSet) -> LiveRealmResults.this.setValue((List<T>) realm.copyFromRealm(results));

    @MainThread
    public LiveRealmResults(@NonNull RealmResults<T> results) {
        if (results == null) {
            throw new IllegalArgumentException("Results cannot be null!");
        }
        if (!results.isValid()) {
            throw new IllegalArgumentException("The provided RealmResults is no longer valid, the Realm instance it belongs to is closed. It can no longer be observed for changes.");
        }
        this.results = results;
        if (results.isLoaded()) {
            setValue((List<T>) realm.copyFromRealm(results));
        }
    }


    @Override
    protected void onActive() {
        super.onActive();
        if (results.isValid()) {
            results.addChangeListener(listener);
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (results.isValid()) {
            results.removeChangeListener(listener);
        }
    }
}
