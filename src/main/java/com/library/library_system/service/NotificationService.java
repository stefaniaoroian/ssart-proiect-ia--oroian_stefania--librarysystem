package com.library.library_system.service;

import com.library.library_system.config.RabbitConfig;
import com.library.library_system.entity.Notification;
import com.library.library_system.entity.User;
import com.library.library_system.messaging.NotificationPayload;
import com.library.library_system.repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receive(NotificationPayload payload) {

        Notification n = new Notification(
                payload.getUser(),
                payload.getMessage()
        );

        repo.save(n);

        System.out.println("NOTIFICATION: " + payload.getUser().getName() + " → " + payload.getMessage());
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void notifyUser(User user, String message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.QUEUE_NAME,
                new NotificationPayload(user, message)
        );
    }

}
