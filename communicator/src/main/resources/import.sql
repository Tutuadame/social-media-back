-- Insert members with explicit IDs
INSERT INTO MEMBER (id, first_name, last_name, picture) VALUES ('681678f167fbe82d25efc5d7','John','Doe','https://avatar.iran.liara.run/username?username=John+Doe');
INSERT INTO MEMBER (id, first_name, last_name, picture) VALUES ('66fcffa773b84ab38485cd4a','Jane','Smith','https://avatar.iran.liara.run/username?username=Jane+Smith');
INSERT INTO MEMBER (id, first_name, last_name, picture) VALUES ('user8','Ethan','Moore','https://avatar.iran.liara.run/username?username=Ethan+Moore');
INSERT INTO MEMBER (id, first_name, last_name, picture) VALUES ('user9','Sophia','Clark','https://avatar.iran.liara.run/username?username=Sophia+Clark');
INSERT INTO MEMBER (id, first_name, last_name, picture) VALUES ('user10','Liam','Hall','https://avatar.iran.liara.run/username?username=Liam+Hall');
INSERT INTO MEMBER (id, first_name, last_name, picture) VALUES ('user3','Alice','Johnson','https://avatar.iran.liara.run/username?username=Alice+Johnson');
INSERT INTO MEMBER (id, first_name, last_name, picture) VALUES ('user4','Bob','Williams','https://avatar.iran.liara.run/username?username=Bob+Williams');
INSERT INTO MEMBER (id, first_name, last_name, picture) VALUES ('user5','Charlie','Brown','https://avatar.iran.liara.run/username?username=Charlie+Brown');
INSERT INTO MEMBER (id, first_name, last_name, picture) VALUES ('user6','David','Taylor','https://avatar.iran.liara.run/username?username=David+Taylor');
INSERT INTO MEMBER (id, first_name, last_name, picture) VALUES ('user7','Emma','Anderson','https://avatar.iran.liara.run/username?username=Emma+Anderson');

-- Insert conversations with explicit IDs
INSERT INTO CONVERSATION (id, name) VALUES (1, 'General Chat');
INSERT INTO CONVERSATION (id, name) VALUES (2, 'Project Updates');
INSERT INTO CONVERSATION (id, name) VALUES (3, 'Team Coordination');
ALTER TABLE conversation ALTER COLUMN id RESTART WITH 4;

-- Insert member_conversations with explicit IDs
INSERT INTO MEMBER_CONVERSATIONS (member_id, conversation_id) VALUES ('681678f167fbe82d25efc5d7', 1);
INSERT INTO MEMBER_CONVERSATIONS (member_id, conversation_id) VALUES ('66fcffa773b84ab38485cd4a', 1);
INSERT INTO MEMBER_CONVERSATIONS (member_id, conversation_id) VALUES ('681678f167fbe82d25efc5d7', 2);
INSERT INTO MEMBER_CONVERSATIONS (member_id, conversation_id) VALUES ('66fcffa773b84ab38485cd4a', 3);

-- Insert messages with explicit IDs
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '681678f167fbe82d25efc5d7', 'Hi!', '2025-02-20 10:00:00'); -- Alice in General Chat
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Hey!', '2025-02-20 10:01:00'); -- Bob in General Chat
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '681678f167fbe82d25efc5d7', 'Whats up?', '2025-02-20 10:02:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Not much, just working.', '2025-02-20 10:03:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '681678f167fbe82d25efc5d7', 'Anyone else around?', '2025-02-20 10:04:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Not sure, but Im here.', '2025-02-20 10:05:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '681678f167fbe82d25efc5d7', 'I just read an interesting article about AI.', '2025-02-20 10:06:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Oh, what was it about?', '2025-02-20 10:07:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '681678f167fbe82d25efc5d7', 'It talked about how AI is changing everything from automation to creativity.', '2025-02-20 10:08:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Thats fascinating! Do you have a link?', '2025-02-20 10:09:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '681678f167fbe82d25efc5d7', 'Sure, Ill send it over.', '2025-02-20 10:10:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Great, thanks!', '2025-02-20 10:11:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '681678f167fbe82d25efc5d7', 'Im also thinking of learning a new language.', '2025-02-20 10:12:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Which one?', '2025-02-20 10:13:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '681678f167fbe82d25efc5d7', 'Maybe Python or Go. What do you think?', '2025-02-20 10:14:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Both are great! Python is more popular, but Go is very efficient.', '2025-02-20 10:15:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '681678f167fbe82d25efc5d7', 'Good point! I might try both.', '2025-02-20 10:16:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Let me know how it goes!', '2025-02-20 10:17:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '681678f167fbe82d25efc5d7', 'Will do! Also, have you seen the latest tech trends?', '2025-02-20 10:18:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Yeah, AI and quantum computing are making waves!', '2025-02-20 10:19:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '681678f167fbe82d25efc5d7', 'Absolutely, its an exciting time for technology.', '2025-02-20 10:20:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Couldnt agree more!', '2025-02-20 10:21:00');
