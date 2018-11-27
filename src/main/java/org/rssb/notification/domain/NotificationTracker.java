package org.rssb.notification.domain;

import javax.persistence.*;

@Entity
@Table(name = "notification_tracker")

public class NotificationTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String notificationId;
    private int configuredRetries;
    private String notificationPayload;
    private String sourceSystem;
    private int processedRetries;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public int getConfiguredRetries() {
        return configuredRetries;
    }

    public void setConfiguredRetries(int configuredRetries) {
        this.configuredRetries = configuredRetries;
    }

    public String getNotificationPayload() {
        return notificationPayload;
    }

    public void setNotificationPayload(String notificationPayload) {
        this.notificationPayload = notificationPayload;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public int getProcessedRetries() {
        return processedRetries;
    }

    public void setProcessedRetries(int processedRetries) {
        this.processedRetries = processedRetries;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
