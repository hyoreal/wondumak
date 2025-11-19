package be.domain.notice.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import be.domain.notice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Async
@Component
@RequiredArgsConstructor
public class NotificationEventListener {
	private final NotificationService notificationService;

	@TransactionalEventListener
	public void handleNotification(NotificationEvent notificationEvent) {
		log.info("알림 이벤트 수신");
		notificationService.sendNotificationInternal(
			notificationEvent.getUser(),
			notificationEvent.getIdForNotifyType(),
			notificationEvent.getTitle(),
			notificationEvent.getContent(),
			notificationEvent.getCommenterImage(),
			notificationEvent.getNotificationType()
		);
		log.info("알림 이벤트 처리 완료");
	}
}
