# xPhoto

xPhoto is an web application that is meant to replicate google photos on your own NAS server.
its meant to  be run in a home environment behind a firewall.

It was created because I had a need to share photos within my household at full resolution, in a fast matter.

It uses rapidoid-http-fast to serve the image files, which is the fastest http server I ever experienced. And it serves 42 MegaPixel images from a Sony A7R2 within a second (20-30MB per picture). 

---


## Features

- Scans your photo folder, generates thumbnails and indexes all photos/albums
- Works with your own folders, each folder is an album
- Extremely lightweight
- Serve huge images, very fast.

---


## Setup

- Install Java 8 and make sure its on your path
- Modify xphoto.properties and change "photos" and "server.port" to suit your needs
- run "java -jar xphoto.jar"

---

## FAQ

- How do I add/re-index my photos ?

> Since the autoscanning feature is not yet implemented, you have to delete "albums.ser" and re-run the program in order to rescan.

- How do I remake the thumbnails ?

> Delete the contents of the "thumbnail" folder


## TODO

- Option for scanning of SD cards and auto-import them once they get plugged in
- Cold storage & Hot storage mechanism (only scan cold storage once)
- Poll hot storage for new files, and auto add them.
- Video support
- Edit images (contrast, brightness, colors, etc) and place them in a new file (leave original)
- Ability to hide images & albums
- Face detection 
- Delete thumbnails for deleted photos

## Screenshots

### View Albums
![SS1](https://i.imgur.com/koijIJO.jpg)

### View Pictures inside album
![SS2](https://imgur.com/H7bdIwb.jpg)

### View Picture
![SS3](https://imgur.com/HW5kMnY.jpg)