CREATE TABLE PROFILE (
    id VARCHAR(50) PRIMARY KEY,
    picture VARCHAR(255) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    introduction TEXT
);

CREATE TABLE CONNECTIONS (
    initiator_id VARCHAR(50) NOT NULL,
    target_id VARCHAR(50) NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'pending',
    PRIMARY KEY(initiator_id, target_id),
    UNIQUE(initiator_id, target_id),
    CONSTRAINT PROFILE_REF_1 FOREIGN KEY(initiator_id) REFERENCES PROFILE(id) ON DELETE CASCADE,
    CONSTRAINT PROFILE_REF_2 FOREIGN KEY(target_id) REFERENCES PROFILE(id) ON DELETE CASCADE
);

CREATE TABLE POST (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    content TEXT NOT NULL,
    profile_id VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT USER_REF FOREIGN KEY(profile_id) REFERENCES PROFILE(id) ON DELETE CASCADE
);

CREATE TABLE VOTES (
    post_id BIGINT NOT NULL,
    profile_id VARCHAR(50) NOT NULL,
    vote BOOLEAN NOT NULL,
    PRIMARY KEY(post_id, profile_id),
    CONSTRAINT FK_VOTES_POST FOREIGN KEY(post_id) REFERENCES POST(id) ON DELETE CASCADE,
    CONSTRAINT FK_VOTES_USER FOREIGN KEY(profile_id) REFERENCES PROFILE(id) ON DELETE CASCADE
);