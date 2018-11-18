package org.rssb.notification.controller;

import org.rssb.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @RequestMapping("/ping")
    public String alive() {
        return "Notification Wrapper is healthy!";
    }

    @RequestMapping(value = "/notification", method = RequestMethod.POST)
    public String sendNotification(Model model, @RequestBody String notificationRequest) {

        notificationService.processNotification(notificationRequest);

        return "Success";


    }

    @RequestMapping(value = "/mappedNotification", method = RequestMethod.POST)
    public String sendMappedNotification(Model model, @RequestBody String notificationRequest) {

        notificationService.processMappedNotification(notificationRequest);

        return "Success";


    }

    @RequestMapping(value = "/dynamicMappedNotification", method = RequestMethod.POST)
    public String sendDynamicMappedNotification(Model model, @RequestBody String notificationRequest) {

        notificationService.processDynamicMappedNotification(notificationRequest);

        return "Success";


    }

}
