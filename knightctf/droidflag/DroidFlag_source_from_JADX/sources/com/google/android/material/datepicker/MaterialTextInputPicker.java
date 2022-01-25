package com.google.android.material.datepicker;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Iterator;

public final class MaterialTextInputPicker<S> extends PickerFragment<S> {
    private static final String CALENDAR_CONSTRAINTS_KEY = "CALENDAR_CONSTRAINTS_KEY";
    private static final String DATE_SELECTOR_KEY = "DATE_SELECTOR_KEY";
    private static final String THEME_RES_ID_KEY = "THEME_RES_ID_KEY";
    private CalendarConstraints calendarConstraints;
    private DateSelector<S> dateSelector;
    private int themeResId;

    static <T> MaterialTextInputPicker<T> newInstance(DateSelector<T> dateSelector2, int i, CalendarConstraints calendarConstraints2) {
        MaterialTextInputPicker<T> materialTextInputPicker = new MaterialTextInputPicker<>();
        Bundle bundle = new Bundle();
        bundle.putInt(THEME_RES_ID_KEY, i);
        bundle.putParcelable(DATE_SELECTOR_KEY, dateSelector2);
        bundle.putParcelable(CALENDAR_CONSTRAINTS_KEY, calendarConstraints2);
        materialTextInputPicker.setArguments(bundle);
        return materialTextInputPicker;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(THEME_RES_ID_KEY, this.themeResId);
        bundle.putParcelable(DATE_SELECTOR_KEY, this.dateSelector);
        bundle.putParcelable(CALENDAR_CONSTRAINTS_KEY, this.calendarConstraints);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            bundle = getArguments();
        }
        this.themeResId = bundle.getInt(THEME_RES_ID_KEY);
        this.dateSelector = (DateSelector) bundle.getParcelable(DATE_SELECTOR_KEY);
        this.calendarConstraints = (CalendarConstraints) bundle.getParcelable(CALENDAR_CONSTRAINTS_KEY);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.dateSelector.onCreateTextInputView(layoutInflater.cloneInContext(new ContextThemeWrapper(getContext(), this.themeResId)), viewGroup, bundle, this.calendarConstraints, new OnSelectionChangedListener<S>() {
            public void onSelectionChanged(S s) {
                Iterator it = MaterialTextInputPicker.this.onSelectionChangedListeners.iterator();
                while (it.hasNext()) {
                    ((OnSelectionChangedListener) it.next()).onSelectionChanged(s);
                }
            }

            public void onIncompleteSelectionChanged() {
                Iterator it = MaterialTextInputPicker.this.onSelectionChangedListeners.iterator();
                while (it.hasNext()) {
                    ((OnSelectionChangedListener) it.next()).onIncompleteSelectionChanged();
                }
            }
        });
    }

    public DateSelector<S> getDateSelector() {
        DateSelector<S> dateSelector2 = this.dateSelector;
        if (dateSelector2 != null) {
            return dateSelector2;
        }
        throw new IllegalStateException("dateSelector should not be null. Use MaterialTextInputPicker#newInstance() to create this fragment with a DateSelector, and call this method after the fragment has been created.");
    }
}
