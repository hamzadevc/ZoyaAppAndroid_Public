package me.crosswall.photo.pick.presenters;

import java.lang.ref.WeakReference;

import me.crosswall.photo.pick.saferxjava.WeakSubscriberDecorator;
import me.crosswall.photo.pick.views.BaseView;
import rx.Subscriber;

/**
 * Created by yuweichen on 15/12/10.
 */
public abstract class SafePresenter<T extends BaseView> {

    private WeakReference<T> weakReference;


    abstract void initialized(Object... objects);

    public SafePresenter(T t) {
        if (t == null) {
            throw new NullPointerException("view is null, check it");
        }
        this.weakReference = new WeakReference<>(t);
    }

    public T getView() {
        T t = weakReference.get();
        return t;
    }

    public WeakSubscriberDecorator safeSubscriber(Subscriber subscriber) {
        return new WeakSubscriberDecorator(subscriber);
    }

    public boolean isViewEmpty(T t) {
        return t == null;
    }

}