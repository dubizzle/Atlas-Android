package com.layer.ui.message;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.ViewGroup;

import com.layer.sdk.messaging.Identity;
import com.layer.sdk.messaging.Message;
import com.layer.ui.R;
import com.layer.ui.adapters.ItemViewHolder;
import com.layer.ui.avatar.AvatarView;
import com.layer.ui.avatar.AvatarViewModelImpl;
import com.layer.ui.databinding.UiMessageItemBinding;
import com.layer.ui.identity.IdentityFormatterImpl;
import com.layer.ui.message.messagetypes.CellFactory;
import com.layer.ui.message.messagetypes.MessageStyle;
import com.layer.ui.util.DateFormatter;
import com.layer.ui.util.Util;
import com.layer.ui.util.imagecache.ImageCacheWrapper;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;

public class MessageCellViewHolder extends
        ItemViewHolder<Message, MessageItemViewModel, ViewDataBinding, MessageStyle> {

    protected Message mMessage;

    // Cell
    protected CellFactory.CellHolder mCellHolder;
    protected CellFactory.CellHolderSpecs mCellHolderSpecs;
    protected DateFormat mTimeFormat;
    protected UiMessageItemBinding mUiMessageItemBinding;
    protected ViewGroup mRoot;

    public MessageCellViewHolder(UiMessageItemBinding uiMessageItemBinding, MessageItemViewModel messageItemViewModel,
            ImageCacheWrapper imageCachWrapper) {

        super(uiMessageItemBinding, messageItemViewModel);
        mRoot = uiMessageItemBinding.swipeable;
        mUiMessageItemBinding = uiMessageItemBinding;
        mTimeFormat = android.text.format.DateFormat.getTimeFormat(uiMessageItemBinding.getRoot().getContext());
        uiMessageItemBinding.avatar.init(new AvatarViewModelImpl(imageCachWrapper), new IdentityFormatterImpl());
    }

    public void bind(Message message, int viewVisibilityType, boolean isClusterSpaceVisible,
            boolean shouldDisplayName, boolean shouldBindDateTimeForMessage, String recipientStatus,
            boolean isRecipientStatusVisible, DateFormatter dateFormatter, boolean isCellTypeMe) {


        Context context = mBinding.getRoot().getContext();
        Date receivedAt = mMessage.getReceivedAt();
        if (receivedAt == null) receivedAt = new Date();
        String timeBarDayText =  dateFormatter.formatTimeDay(receivedAt);

        String timeBarTimeText = mTimeFormat.format(receivedAt.getTime());

        Identity identity = mMessage.getSender();
        String sender = identity == null ? context.getString(
                R.string.layer_ui_message_item_unknown_user) : Util.getDisplayName(mMessage.getSender());

        MessageItemViewModel messageItemViewModel = getViewModel();
        messageItemViewModel.setTimeGroupDay(timeBarDayText);
        messageItemViewModel.setSender(sender);
        if (message.getSender() != null) {

            messageItemViewModel.setParticipants(
                    Collections.singleton(message.getSender()));
        }
        messageItemViewModel.setRecipientStatus(recipientStatus);
        messageItemViewModel.setIsRecipientStatusVisible(isRecipientStatusVisible);
        messageItemViewModel.setGroupTime(" " + timeBarTimeText);
        messageItemViewModel.setAvatarViewVisibilityType(viewVisibilityType);
        messageItemViewModel.setIsClusterSpaceVisible(isClusterSpaceVisible);
        messageItemViewModel.setShouldShowDisplayName(shouldDisplayName);
        messageItemViewModel.setShouldBindDateTimeForMessage(shouldBindDateTimeForMessage);
        messageItemViewModel.setParticipants(Collections.singleton(message.getSender()));
        messageItemViewModel.setMessageSent(message.isSent());
        messageItemViewModel.setMyCellType(isCellTypeMe);
        messageItemViewModel.notifyChange();

        mUiMessageItemBinding.setViewModel(messageItemViewModel);
    }

    public ViewGroup getCell() {
        return mUiMessageItemBinding.cell;
    }

    public AvatarView getAvatarView() {
        return mUiMessageItemBinding.avatar;
    }
}
