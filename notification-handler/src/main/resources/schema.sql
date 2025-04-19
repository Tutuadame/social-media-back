CREATE TABLE notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message VARCHAR(255),
    created_at TIMESTAMP
);

CREATE TABLE notification_user_ids (
    notification_id BIGINT NOT NULL,
    user_id VARCHAR(255),
    CONSTRAINT fk_notification_users FOREIGN KEY (notification_id) REFERENCES notification(id)
);