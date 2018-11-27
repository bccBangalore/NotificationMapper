package org.rssb.notification.repository;

import org.rssb.notification.domain.NotificationTracker;
import org.springframework.data.repository.CrudRepository;

public interface NotificationTrackerRepository extends CrudRepository<NotificationTracker,Integer> {
}
