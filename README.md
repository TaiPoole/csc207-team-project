# Team Project

Please keep this up-to-date with information about your project throughout the term.

The readme should include information such as:
- a summary of what your application is all about
- a list of the user stories, along with who is responsible for each one
- information about the API(s) that your project uses 
- screenshots or animations demonstrating current functionality

By keeping this README up-to-date,
your team will find it easier to prepare for the final presentation
at the end of the term.


COPY OF BLUEPRINT:
Project Specification for Group TUT401-22
Team Name: 🐸🐸🐸
Domain:
Messaging/File Transfer service
User Stories: 

User story 1: As a user, I’d like to be able to send and receive a message, so that I can have conversations 
User story 2: As a user, I’d like to be able to join / leave channels, so that I can have a more private conversation
User story 3: As a user, I’d like to be able to store / search my message history, so that I can recall information
User story 4: As a user, I’d like to be able to have permissions on channels, so that only certain people can read/write to a given channel
User story 5: As a user, I’d like to be able to send small files along with messages so that I can show people images along with text
User story 6: As a user, I’d like to be able to create a channel with a name for the channel, so that I can have new discussions with other people.
User story 7: As a user, I’d like to change my UI’s theme (dark/light mode), so that my graphical interface suits my preference.
User story 8:As a user, I’d like to view and regenerate my random nickname at any time so I can control how others identify me in channels.
User story 9: As a user, I’d like to download the file/ images in the chatroom locally, so that I can review it offline and edit it.




Use Cases:

Use Case for story 1: Send and receive messages
Main Flow: 
User, in a channel, types out a message, and hits send
System packages message and sends it across the network by UDP to all members in the channel

Use Case for story 2:
Main Flow: 
User hits button to create new server
User selects which users to invite
System creates new server/chatroom with only the invited users and creator

Use Case for story 3:
Main flow:
Application stores messages locally on the users device
User clicks search bar in app
User enters keywords to search
Application searches local message stores and returns matching results

Use Case for story 4:
Main flow:
The creator of a server/channel has permissions to modify per-user server/channel permissions
A user with permission to modify permissions clicks on the permissions button
The user selects another user and modifies their permissions (note that being able to modify a permission is a permission)
Note: The server’s overall permissions can be set on the application server via a configuration file

Use Case for story 5:
Main flow:
User clicks attach file
User selects a file
Application uploads and packages the file for upload with the message
User sends messages + attachment to all members of the channel
Other users in channel can view or download the images

Use Case for story 6: create channel with a unique name
Main flow:
User hits button to create new channel, enters a unique name
Channel created
Other people can join the channel using the channel’s name

Use Case for story 7:
Main flow:
User can click on a little button on the top right which lets them cycle between themes

Use Case for story 8:
Main flow:
System assigns a random nickname when the user first joins.
The nickname appears at the top-right of the UI with a “Regenerate” button.
User clicks Regenerate.
System generates a new random nickname and updates it immediately.
New outgoing messages show the new nickname, but old messages retain the previous one.
UI refreshes the displayed nickname in all relevant views.

Use Case for story 9: Download from chatroom
Main flow:
User click the “download” button when they open the file/image
The file/image is download to user’s computer locally
Download message will be shown in the chat



MVP:

Lead
Use Case
User Story
Loago
Use Case #1
User Story #1
Tiger
Use Case #2
User Story #2
Lucas
Use Case #5/4
User Story #4/#5
Billy
Use Case #6
User Story #6
Tai
Use Case #3/7
User Story #3/7
Jiawei
Use Case #8
User Story #8
Billy
Use Case #9
User Story #9




Proposed Entities for the Domain:
Server:
	Attributes:
		+ channels: List<Channel>
		+ clients: List<Client>
		+ names: List<List<String>, List<String>>
	Methods:
		+ createChannel(name: string): Channel
		+ closeChannel(c: Channel): void
		+ connectClient(client: Client): void
		+ assignPseudonym(): String
		+ regeneratePseudonym(): String
Permission:
Attributes:
		+ Name: String
		+ Value: String
		+ Permitted: Boolean
Channel:
	Attributes:
		+ channelID: String
		+ clients: List <Clients>
		+ messages: List<Messages>
		+ permissions: Map<User, List<Permission>>
	Methods:
		+ addClient(client: Client):void
		+ addMessage(message: Message): void
Client:
	Attributes:
		+ clientID: String
		+ channel: List<Channel>
		+ user: User
	Methods:
		+ disconnect(): void
	
User:
	Attributes:
		+ pseudonym: String
Methods:
		+ changePseudonym(newName:String): void
		+ sendMessages(channel: Channel, content: String): Message


Message:
	Attributes:
		+ sender: User
		+ content: String
		+ timestamp: DateTime
		+ attachment: File



Proposed API for the project:

Use the Java.net Socket APIs (UDP) to facilitate communication between the server and clients and ensure message delivery 

