package com.todo.device.notification;

import android.app.Notification;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;

public final class NotificationsImpl implements Notifications {

    private final NotificationManagerCompat notificationManagerCompat;

    public NotificationsImpl(@NonNull final NotificationManagerCompat notificationManagerCompat) {
        this.notificationManagerCompat = notificationManagerCompat;
    }

    @Override
    public void showNotification(final int notificationId, @NonNull final Notification notification) {
        notificationManagerCompat.notify(notificationId, notification);
    }

    @Override
    public void updateNotification(final int notificationId, @NonNull final Notification notification) {
        notificationManagerCompat.notify(notificationId, notification);
    }

    @Override
    public void hideNotification(final int notificationId) {
        notificationManagerCompat.cancel(notificationId);
    }

    @Override
    public void hideNotifications() {
        notificationManagerCompat.cancelAll();
    }
}
