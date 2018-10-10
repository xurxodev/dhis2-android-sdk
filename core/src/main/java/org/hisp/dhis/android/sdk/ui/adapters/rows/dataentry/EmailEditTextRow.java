package org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry;

import static org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry.AbsEnrollmentDatePickerRow
        .EMPTY_FIELD;

import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.hisp.dhis.android.sdk.R;
import org.hisp.dhis.android.sdk.persistence.Dhis2Application;
import org.hisp.dhis.android.sdk.persistence.models.BaseValue;
import org.hisp.dhis.android.sdk.persistence.models.Event;
import org.hisp.dhis.android.sdk.ui.adapters.rows.AbsTextWatcher;
import org.hisp.dhis.android.sdk.ui.adapters.rows.events.OnDetailedInfoButtonClick;
import org.hisp.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;

public class EmailEditTextRow extends Row {
    private static String rowTypeTemp;
    protected Integer mErrorStringId;

    protected TextView.OnEditorActionListener mOnEditorActionListener;

    public EmailEditTextRow(String label, boolean mandatory, String warning, BaseValue baseValue,
            DataEntryRowTypes rowType, Event event) {
        mLabel = label;
        mMandatory = mandatory;
        mWarning = warning;
        mValue = baseValue;
        mRowType = rowType;
        mEvent = event;


        if (!DataEntryRowTypes.EMAIL.equals(rowType)) {
            throw new IllegalArgumentException("Unsupported row type");
        }
        checkNeedsForDescriptionButton();
    }


    public void setOnEditorActionListener(
            TextView.OnEditorActionListener onEditorActionListener) {
        mOnEditorActionListener = onEditorActionListener;
    }

    @Override
    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView,
            ViewGroup container) {

        View view;
        final EmailEditTextRow.ValueEntryHolder holder;

        if (convertView != null
                && convertView.getTag() instanceof URLEditTextRow.ValueEntryHolder) {
            view = convertView;
            holder = (EmailEditTextRow.ValueEntryHolder) view.getTag();
            holder.listener.onRowReused();
        } else {
            View root = inflater.inflate(R.layout.listview_row_edit_text, container, false);
            TextView label = (TextView) root.findViewById(R.id.text_label);
            TextView mandatoryIndicator = (TextView) root.findViewById(R.id.mandatory_indicator);
            TextView warningLabel = (TextView) root.findViewById(R.id.warning_label);
            TextView errorLabel = (TextView) root.findViewById(R.id.error_label);
            EditText editText = (EditText) root.findViewById(R.id.edit_text_row);
            detailedInfoButton = root.findViewById(R.id.detailed_info_button_layout);

            editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            editText.setHint(R.string.enter_email);
            editText.setSingleLine(true);

            EmailWatcher listener = new EmailWatcher(editText, errorLabel);
            listener.setRow(this);
            rowTypeTemp = mRowType.toString();
            listener.setRowType(rowTypeTemp);
            holder = new EmailEditTextRow.ValueEntryHolder(label, mandatoryIndicator, warningLabel,
                    errorLabel, editText, listener, detailedInfoButton);
            holder.listener.setBaseValue(mValue);
            holder.editText.addTextChangedListener(listener);

            root.setTag(holder);
            view = root;
        }


        if (!isEditable()) {
            holder.editText.setEnabled(false);
        } else {
            holder.editText.setEnabled(true);
        }

        holder.textLabel.setText(mLabel);
        holder.listener.setBaseValue(mValue);

        holder.editText.setText(mValue.getValue());
        holder.editText.setSelection(holder.editText.getText().length());


        if (mWarning == null) {
            holder.warningLabel.setVisibility(View.GONE);
        } else {
            holder.warningLabel.setVisibility(View.VISIBLE);
            holder.warningLabel.setText(mWarning);
        }

        if (mErrorStringId == null) {
            holder.errorLabel.setVisibility(View.GONE);
        } else {
            holder.errorLabel.setVisibility(View.VISIBLE);
            holder.errorLabel.setText(mErrorStringId);
        }

        if (!mMandatory) {
            holder.mandatoryIndicator.setVisibility(View.GONE);
        } else {
            holder.mandatoryIndicator.setVisibility(View.VISIBLE);
        }

        holder.editText.setOnEditorActionListener(mOnEditorActionListener);

        if (isDetailedInfoButtonHidden()) {
            holder.detailedInfoButton.setVisibility(View.INVISIBLE);
        } else {
            holder.detailedInfoButton.setVisibility(View.VISIBLE);
        }

        holder.detailedInfoButton.setOnClickListener(new OnDetailedInfoButtonClick(this));

        return view;
    }

    @Override
    public int getViewType() {
        return DataEntryRowTypes.EMAIL.ordinal();
    }


    public class ValueEntryHolder {
        final TextView textLabel;
        final TextView mandatoryIndicator;
        final TextView warningLabel;
        final TextView errorLabel;
        final EditText editText;
        final EmailEditTextRow.OnTextChangeListener listener;
        final View detailedInfoButton;


        public ValueEntryHolder(TextView textLabel,
                TextView mandatoryIndicator, TextView warningLabel,
                TextView errorLabel, EditText editText,
                EmailEditTextRow.OnTextChangeListener listener, View detailedInfoButton) {
            this.textLabel = textLabel;
            this.mandatoryIndicator = mandatoryIndicator;
            this.warningLabel = warningLabel;
            this.errorLabel = errorLabel;
            this.editText = editText;
            this.listener = listener;
            this.detailedInfoButton = detailedInfoButton;
        }
    }

    private class EmailWatcher extends OnTextChangeListener {
        final private EditText mEditText;
        final private TextView mErrorLabel;


        public EmailWatcher(EditText editText, TextView errorLabel) {
            super();
            mEditText = editText;
            mErrorLabel = errorLabel;
        }

        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            String text = mEditText.getText().toString();
            if (validateEmail(text)) {
                setError(null);
            } else {
                setError(R.string.error_email);
            }
        }

        private void setError(Integer stringId) {
            if (stringId == null) {
                mError = null;
                mErrorLabel.setVisibility(View.GONE);
                mErrorLabel.setText("");
            } else {
                mErrorLabel.setVisibility(View.VISIBLE);
                mErrorLabel.setText(stringId);
                mError = mErrorLabel.getText().toString();
            }
            mErrorStringId = stringId;
        }

    }

    class OnTextChangeListener extends AbsTextWatcher {
        private BaseValue value;
        Row row;
        String rowType;

        public void setRowType(String type) {
            rowType = type;
        }

        public void setRow(Row row) {
            this.row = row;
        }

        public void setBaseValue(BaseValue value) {
            this.value = value;
        }

        public void onRowReused() {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String newValue = s != null ? s.toString() : EMPTY_FIELD;
            if (!newValue.equals(value.getValue()) && validateEmail(newValue)) {
                value.setValue(newValue);
                RowValueChangedEvent rowValueChangeEvent = new RowValueChangedEvent(value, rowType);
                rowValueChangeEvent.setRow(row);
                Dhis2Application.getEventBus().post(rowValueChangeEvent);
            }
        }
    }

    public boolean validateEmail(String url) {
        String regExp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\""
                + "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01"
                + "-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)"
                + "+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:"
                + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
                + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:"
                + "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09"
                + "\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        if (url.matches(regExp) || url.length() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
