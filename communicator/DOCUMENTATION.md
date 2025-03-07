# Communicator

This service is responsible for handling communication between groups of users.
The dedicated functionalities are the following:

- [x] Create conversations between multiple people
- [x] Manage the members of each conversation
- [x] Have messages be available for future changes (deletion, content modification)

### DAO Classes and their responsibilities

**Conversation:**
- Hold the name and the identifier for each conversation

**Member:**
- Hold the name and the identifier of each member

**Member Conversations:**
- Represent any kind of unique connection between a conversation and a member (user)

## API endpoints
 
### conversationApi
- Create, Update, Delete a conversation
- Get conversations by member

### memberApi
- Create, Delete member (used with auth0 registration and account deletion)
- Add or Remove a member from conversation
- Get members by conversation

### messageApi
- Create, Update and Delete messages
- Get messages by conversation

