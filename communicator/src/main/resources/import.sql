-- Insert members with explicit IDs
INSERT INTO MEMBER (id, first_name, last_name, picture) VALUES ('6771a216a6339380c80b75a1','Alice','McKenzy', 'https://avatar.iran.liara.run/public');
INSERT INTO MEMBER (id, first_name, last_name, picture)  VALUES ('66fcffa773b84ab38485cd4a','Bob', 'Douglas', 'https://avatar.iran.liara.run/public');

-- Insert conversations with explicit IDs
INSERT INTO CONVERSATION (id, name) VALUES (1, 'General Chat');
INSERT INTO CONVERSATION (id, name) VALUES (2, 'Project Updates');
INSERT INTO CONVERSATION (id, name) VALUES (3, 'Team Coordination');

-- Insert member_conversations with explicit IDs
INSERT INTO MEMBER_CONVERSATIONS (member_id, conversation_id) VALUES ('6771a216a6339380c80b75a1', 1); -- Alice in General Chat
INSERT INTO MEMBER_CONVERSATIONS (member_id, conversation_id) VALUES ('66fcffa773b84ab38485cd4a', 1); -- Bob in General Chat
INSERT INTO MEMBER_CONVERSATIONS (member_id, conversation_id) VALUES ('6771a216a6339380c80b75a1', 2); -- Alice in Project Updates
INSERT INTO MEMBER_CONVERSATIONS (member_id, conversation_id) VALUES ('66fcffa773b84ab38485cd4a', 3); -- Bob in Team Coordination

-- Insert messages with explicit IDs
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '6771a216a6339380c80b75a1', 'Hi!', '2025-02-20 10:00:00'); -- Alice in General Chat
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Hey!', '2025-02-20 10:01:00'); -- Bob in General Chat

-- Generate 20 messages for General Chat
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '6771a216a6339380c80b75a1', 'Whats up?', '2025-02-20 10:02:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Not much, just working.', '2025-02-20 10:03:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '6771a216a6339380c80b75a1', 'Anyone else around?', '2025-02-20 10:04:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Not sure, but Im here.', '2025-02-20 10:05:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '6771a216a6339380c80b75a1', 'I just read an interesting article about AI.', '2025-02-20 10:06:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Oh, what was it about?', '2025-02-20 10:07:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '6771a216a6339380c80b75a1', 'It talked about how AI is changing everything from automation to creativity.', '2025-02-20 10:08:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Thats fascinating! Do you have a link?', '2025-02-20 10:09:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '6771a216a6339380c80b75a1', 'Sure, Ill send it over.', '2025-02-20 10:10:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Great, thanks!', '2025-02-20 10:11:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '6771a216a6339380c80b75a1', 'Im also thinking of learning a new language.', '2025-02-20 10:12:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Which one?', '2025-02-20 10:13:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '6771a216a6339380c80b75a1', 'Maybe Python or Go. What do you think?', '2025-02-20 10:14:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Both are great! Python is more popular, but Go is very efficient.', '2025-02-20 10:15:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '6771a216a6339380c80b75a1', 'Good point! I might try both.', '2025-02-20 10:16:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Let me know how it goes!', '2025-02-20 10:17:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '6771a216a6339380c80b75a1', 'Will do! Also, have you seen the latest tech trends?', '2025-02-20 10:18:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Yeah, AI and quantum computing are making waves!', '2025-02-20 10:19:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '6771a216a6339380c80b75a1', 'Absolutely, its an exciting time for technology.', '2025-02-20 10:20:00');
INSERT INTO MESSAGE (conversation_id, member_id, content, sent_at) VALUES (1, '66fcffa773b84ab38485cd4a', 'Couldnt agree more!', '2025-02-20 10:21:00');
