GravityControls
===============
A simple application for controlling your device's music stream
with the built-in accelerometer. Simply flip over your device to
mute your music. Flip the device back upright to resume.

Great for when you're listening to music at your desk and someone
stops over to ask you a quick question. No need to fumble with a 
lock screen or unplug your headphones.

Originally I wanted this app to actually pause the current track, but
since there is no standard way to interact with the variety of
media player applications, this has proved to be extremely difficult.

[Download from the Android Market][market-link]

Building
========
If you want to build this app from source you can do so using the basic
ant commands.

    $ cd GravityControls
    $ ant clean debug
    $ adb install bin/GravityControls-debug.apk

Contributing
============
If you would like to contribute to this app, please feel free to fork
the repository and submit a pull request.

[market-link]: https://market.android.com/details?id=com.tristanwaddington.gravitycontrols
