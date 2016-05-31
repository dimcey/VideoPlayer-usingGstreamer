# VideoPlayer-usingGstreamer
A project part of a labarotory work within the course Multimedia Systems at Luleå University of Technology

# Introduction
For the accomplishment of the lab tasks, a Gstreamer multimedia framework is used. The framework is very helpful to develop multimedia applications since it has support for many programming languages and OSs. There are various APIs and plugins for developing many applications and it specifically built on pipeline architecture. For our lab task, a java programming language and for writing multimedia application based on java was used, with gstreamer-java.jar and jna.jar libraries.

# Problem Specification
It was required to develop an Audio/Video player with user interface capable of playing a previously recorded media. The user should be able to to forward or rewind while playing and able to pause the play. Also the user should be able to do the play from the spot he choose to play and the application should not crash when non-media files are selected by mistake. The application should also provide a full screen capability.

![alt tag](https://github.com/dimcey/VideoPlayer-usingGstreamer/blob/master/GUI.png)

In addition to those mandatory features, the following extra features for our media player were added.
- Volume up and down buttons, volume level
- Mute button
- Time elapsed information with a progress bar
- Full screen mode

# System description
Short description of each packet with its class inside:
- Icons: gather all the icons layers for a nicer GUI
- GUI: gather our own easy to use GUI;
- Client: gather all the Gstreamer functions, which are accessible from the GUI;
- Main: gather the main class which have to be execute;
This architectural choice for the project structure allows the user to create his own graphical user interface with a specific icons of his choice. Moreover, the application’ core is separated from the Main class to protect the operation of the application when the user want to link is new GUI. And in the same fashion, the Gstreamer’s Pipeline functionalities are separated from the GUI, so that the user has full control of the interface by selecting which Gstreamer’s functions to use in certain time.

# Architecture
![alt tag](https://github.com/dimcey/VideoPlayer-usingGstreamer/blob/master/Architecture.png)

The classes and packages dependencies have been thought to enable a clear development, to facilitate the reusability of our solution and to bring high scalability. As was previously mentioned, the GUI is loosely coupled with the Client’s Pipeline class so that if an end user wants to develop his own GUI, he just needs to call methods from the Pipeline class. The Pipeline class implements the Gstreamer functionality and handles a playbin to which is attached a demultiplexer that first sinks the video and then is separating the audio from the video part by sending both of them to the respective elements for audio and video decoding.

# Algorithm description
For the application development, a special builtin pipeline called playbin2 was used. The pipeline is used to encapsulate a common pipeline making in media player applications such as setting the file source, adding demuxing element and setting appropriate decoders and finally connecting to displaying sinks. All these common operations are already done in PlayBin pipeline and all that had to be done is to set the media source and setting appropriate sinking element to display the media. The architecture of the playBin pipeline is shown below.

![alt tag](https://github.com/dimcey/VideoPlayer-usingGstreamer/blob/master/playbin.png)

In the media player, a video component was created which is a swing component that displays the video that is sent to it. That needed to be attach to the playBin to set it as a sink for displaying the video using setVideoSink() function of the playbin pipeline

# Challenges
The main problem was the synchronisation of the Java Swing GUI with the internal pipelines. Some of the GUI elements run on their own thread and a problem arrise when there is an attempt to exit the fullscreen mode. Once in the fullscreen mode, it is necesarry to hide the panel to render the video truly fullscreen. Now to exit the fullscreen an actionlistener was added to the whole frame as there are no more panels and menus in fullscreen mode, but the response to this frame stimulus was not very rapid and only worked after sometime, as the actionlistener is running in another thread. 

