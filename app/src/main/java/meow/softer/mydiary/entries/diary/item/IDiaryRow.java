package meow.softer.mydiary.entries.diary.item;

import android.view.View;

public interface IDiaryRow {
    int TYPE_TEXT = 0;
    int TYPE_PHOTO = 1;
    int TYPE_WEB_BLOCK = 2;

    void setContent(String content);

    String getContent();

    int getType();

    View getView();

    void setEditMode(boolean isEditMode);

    /**
     * For resort after add new item
     *
     * @param position
     */
    void setPosition(int position);

    /**
     * get position for auto save
     * @return
     */
    int getPosition();
}
