package io.gmas.fuze.commons.ui;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.gmas.fuze.BuildConfig;
import io.gmas.fuze.commons.utils.ExceptionUtils;

public abstract class YPAdapter<T> extends RecyclerView.Adapter<YPViewHolder> {
    private List<T> items = new ArrayList<>();

    public List<T> items() {
        return items;
    }

    public void clearItems() {
        this.items.clear();
    }

    public void addItem(final @NonNull T item) {
        this.items.add(item);
    }

    public void addItems(final @NonNull List<T> items) {
        this.items.addAll(items);
    }

    public void setItems(final @NonNull List<T> items) {
        this.items = items;
    }

    public void insertItem(final int location, final @NonNull T item) {
        this.items.add(location, item);
    }

    public void deleteItem(final int location) {
        this.items.remove(location);
    }

    public void setItem(final int location, final @NonNull T item) {
        this.items.set(location, item);
    }

    /**
     * Fetch the layout id associated with a sectionRow.
     */
    protected abstract int layout(final @NonNull Object item);

    /**
     * Returns a new YPViewHolder given a layout and view.
     */
    protected abstract @NonNull YPViewHolder viewHolder(final @LayoutRes int layout, final @NonNull View view);

    @Override
    public void onViewDetachedFromWindow(final @NonNull YPViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        // View holders are "stopped" when they are detached from the window for recycling
        holder.lifecycleEvent(ActivityEvent.STOP);

        // View holders are "destroy" when they are detached from the window and no adapter is listening
        // to events, so ostensibly the view holder is being deallocated.
        if (!hasObservers()) {
            holder.lifecycleEvent(ActivityEvent.DESTROY);
        }
    }

    @Override
    public void onViewAttachedToWindow(final @NonNull YPViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        // View holders are "started" when they are attached to the new window because this means
        // it has been recycled.
        holder.lifecycleEvent(ActivityEvent.START);
    }

    @Override
    public final @NonNull YPViewHolder onCreateViewHolder(final @NonNull ViewGroup viewGroup, final @LayoutRes int layout) {
        final View view = inflateView(viewGroup, layout);
        final YPViewHolder viewHolder = viewHolder(layout, view);

        viewHolder.lifecycleEvent(ActivityEvent.CREATE);

        return viewHolder;
    }

    @Override
    public final void onBindViewHolder(final @NonNull YPViewHolder viewHolder, final int position) {
        final Object data = objectFromPosition(position);

        try {
            viewHolder.bindData(data);
            viewHolder.onBind();
        } catch (final Exception e) {
            if (BuildConfig.DEBUG) {
                ExceptionUtils.rethrowAsRuntimeException(e);
            }
        }
    }

    @Override
    public final int getItemViewType(final int position) {
        return layout(items.get(position));
    }

    @Override
    public final int getItemCount() {
        return items.size();
    }

    protected @NonNull Object objectFromPosition(final int position) {
        return items.get(position);
    }

    protected int positionFromObject(final @NonNull T t) {
        return items.indexOf(t);
    }

    private @NonNull View inflateView(final @NonNull ViewGroup viewGroup, final @LayoutRes int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return layoutInflater.inflate(viewType, viewGroup, false);
    }

}
