# ChatServer
Simple TCP chat server in java and Spring supporting multiple users and rooms. 

> **Note:** This project currently implements only the base structure.

## How to use
1. Start the server

2. Connect to the server using your favorite TCP clients such as **telnet**:

```bash
telnet <server_ip> <port>
```
> **Warning:** Currently the project does not fully support telnet in **character mode**, so input may behave unexpectedly  

3. Enter [commands](#Commands)

## Commands
- `/conenct <user>` Connect a new user to the server
- `/create <room>` Create a new chat room
- `/help` - Displays all available commands
- `/logout` - Disconnect the current user


## Features
- Multiple TCP connections
- (BASIC) support for user and room management

## Planned Improvements
- Basic messaging commands
- Private messages
- Private Rooms
- Message history persistence