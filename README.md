# DungeonCrawler

Final Product
DungeonCrawler is an online interface for players to play 5e dnd. The interface allows connection between a dungeon master
as well as up to 4 players. The dungeon master can load in a pre coded dungeon using CRAWL code style. The players may create new 
characters or load old characters into the dungeon. Then guided by different typed commands they work their way through the dungeon
with the dungeon master. The dungeon will be automatically populated by the CRAWL documents with objects, text prompts, and encounters.
Most normal events in encounters will be handled by the engine automatically, however at anytime the dungeon master may override events
and respond as they see fit. A language recognition engine will be implemented to increase the number of events that can be responded to. 
The dungeon master will be able to enter CRAWL code on the fly into the interface to add to the dungeon as they see fit. Dungeons state as 
well as characters can be saved and loaded back into the game as well as into other dungeons.

Current Features
Socket connectivity between a dungeon master and up to 4 players.
A fully implemented character creation, saving and loading system.
Character information sharing between all members of a party.
Full implementation of CRAWL code for dungeons.
Ability to load CRAWL dungeons into the interface.
Response to basic exploration commands within the dungeon by dungoen master.
CRAWL dungeon states are shared between whole game party.
Interactivity between objects and players within the dungeon through commands.
Low level language recognition program to correct misspelled inputs.

Upcoming Features
Handle disconnect bugs as well as information transfer bugs.
Encounter support within dungeons.
Increased command handling for encounters and exploration.
Dungeon master CRAWL coding on the fly.
Moving to server style connection rather than sockets.
Rework of information transfer between users.

Current Bugs
Socket connectivity does not work for many routers.
Insufficient error handling for CRAWL compilation.
Small bugs in information transfer between users.
Errors caused by incomplete encounter code with object lists.
Errors disconecting.
