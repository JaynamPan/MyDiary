package meow.softer.mydiary.contacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import meow.softer.mydiary.R;
import meow.softer.mydiary.shared.ThemeManager;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.TopicViewHolder> {
    private FragmentActivity mActivity;

    private ContactsDetailDialogFragment.ContactsDetailCallback callback;
    private long topicId;

    private List<ContactsEntity> contactsNamesList;

    public ContactsAdapter(FragmentActivity activity, List<ContactsEntity> contactsNamesList, long topicId,
                           ContactsDetailDialogFragment.ContactsDetailCallback callback) {
        this.mActivity = activity;
        this.contactsNamesList = contactsNamesList;
        this.topicId = topicId;
        this.callback = callback;
    }
    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_contacts_item, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return contactsNamesList.size();
    }
    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, final int position) {
        if (showHeader(position)) {
            holder.getHeader().setVisibility(View.VISIBLE);
            holder.getHeader().setText(contactsNamesList.get(position).getSortLetters());
        } else {
            holder.getHeader().setVisibility(View.GONE);
        }

        holder.getTVName().setText(contactsNamesList.get(position).getName());
        holder.getTVPhoneNumber().setText(contactsNamesList.get(position).getPhoneNumber());
        holder.setItemPosition(position);
    }
    public int getPositionForSection(char section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = contactsNamesList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;

    }
    private boolean showHeader(final int position) {
        if (position == 0) {
            return true;
        } else {
            if (!contactsNamesList.get(position - 1).getSortLetters().equals(
                    contactsNamesList.get(position).getSortLetters())) {
                return true;
            } else {
                return false;
            }
        }
    }
    protected class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        //Header
        private TextView TV_contacts_item_header;

        //Item
        private LinearLayout LL_contacts_item_contant;
        private ImageView IV_contacts_photo;
        private TextView TV_contacts_name;
        private TextView TV_contacts_phone_number;
        private int itemPosition;

        protected TopicViewHolder(View view) {
            super(view);
            this.TV_contacts_item_header = view.findViewById(R.id.TV_contacts_item_header);
            this.LL_contacts_item_contant = view.findViewById(R.id.LL_contacts_item_contant);

            this.LL_contacts_item_contant.setOnClickListener(this);
            this.LL_contacts_item_contant.setOnLongClickListener(this);
            this.IV_contacts_photo = view.findViewById(R.id.IV_contacts_photo);
            this.TV_contacts_name = view.findViewById(R.id.TV_contacts_name);
            this.TV_contacts_phone_number = view.findViewById(R.id.TV_contacts_phone_number);
            this.TV_contacts_name.setTextColor(ThemeManager.getInstance().getThemeMainColor(mActivity));
        }

        public TextView getHeader() {
            return TV_contacts_item_header;
        }

        public ImageView getIVPhoto() {
            return IV_contacts_photo;
        }

        public TextView getTVName() {
            return TV_contacts_name;
        }

        public TextView getTVPhoneNumber() {
            return TV_contacts_phone_number;
        }


        public void setItemPosition(int itemPosition) {
            this.itemPosition = itemPosition;
        }

        @Override
        public void onClick(View v) {
            CallDialogFragment callDialogFragment =
                    CallDialogFragment.newInstance(contactsNamesList.get(itemPosition).getName(),
                            contactsNamesList.get(itemPosition).getPhoneNumber());
            callDialogFragment.show(mActivity.getSupportFragmentManager(), "callDialogFragment");
        }

        @Override
        public boolean onLongClick(View v) {
            ContactsDetailDialogFragment contactsDetailDialogFragment =
                    ContactsDetailDialogFragment.newInstance(contactsNamesList.get(itemPosition).getContactsId(),
                            contactsNamesList.get(itemPosition).getName(), contactsNamesList.get(itemPosition).getPhoneNumber(),
                            topicId);
            contactsDetailDialogFragment.show(mActivity.getSupportFragmentManager(), "contactsDetailDialogFragment");
            return true;
        }
    }
}
