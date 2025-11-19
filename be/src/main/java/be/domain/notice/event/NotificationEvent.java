package be.domain.notice.event;

import be.domain.notice.entity.NotificationType;
import be.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationEvent {
	private final User user;
	private final Long idForNotifyType;
	private final String title;
	private final String content;
	private final String commenterImage;
	private final NotificationType notificationType;
}
