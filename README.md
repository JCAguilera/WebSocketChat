# WebSocketChat

Web and Websocket Server example for Android. Tested on Android 8.1 (API 27).
Allows you to open a web server to serve an Angular 6 app. Then, opens a Websocket server to comunicate with the app in real time through an easy-to-use Chat App.

## Server

It uses [NanoHttpd](https://github.com/NanoHttpd/nanohttpd) for the Webserver, and [Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket) for the WebSocket Server.
It has a very simple layout and it's pretty easy to use. Just write your username and then start the server.

## Client

The client app is made with Angular 6 and Angular Material. It's stored on the assets folder in the android app.
It's also really easy to use, only username and login. That's it.

## Features

 - [x] Easy to use!
 - [x] Client device can be an Android, iOS or any other device that has a browser (tested on Chrome and Safari).
 - [x] Multi-client Support!
 - [x] Easy to host any Angular apps or any other websites!.
 - [x] JSON support (using [Google's gson](https://github.com/google/gson)).

## License
  
MIT License - see [LICENSE](LICENSE) for more details.
