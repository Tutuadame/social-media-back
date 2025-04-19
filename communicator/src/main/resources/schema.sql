CREATE TABLE conversation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE MEMBER (
    id VARCHAR(50) PRIMARY KEY, -- Auth0 user ID (sub claim)
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    picture TEXT NOT NULL
);

CREATE TABLE MESSAGE (
    conversation_id BIGINT NOT NULL,
    member_id VARCHAR(50) NOT NULL,
    PRIMARY KEY(member_id, conversation_id),
    content TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE MEMBER_CONVERSATIONS (
    member_id VARCHAR(50) NOT NULL,
    conversation_id BIGINT NOT NULL,
    PRIMARY KEY(member_id, conversation_id),
    UNIQUE(member_id, conversation_id),
    CONSTRAINT MEMBER_REF_MC FOREIGN KEY(member_id) REFERENCES MEMBER (id),
    CONSTRAINT CONV_REF_MC FOREIGN KEY(conversation_id) REFERENCES CONVERSATION (id)
);

CREATE INDEX idx_messages_conversation_id ON MESSAGE(conversation_id);
CREATE INDEX idx_messages_conversation_sender ON MESSAGE(conversation_id, member_id);
CREATE INDEX idx_member_conversation ON MEMBER_CONVERSATIONS(member_id, conversation_id);
