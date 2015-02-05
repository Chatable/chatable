![][logo]
[logo]: https://avatars0.githubusercontent.com/u/10842034?v=3&s=200 "Chatable"

[![Build Status](https://travis-ci.org/HenryStevens/chatable.svg?branch=master)](https://travis-ci.org/HenryStevens/chatable) [![Code Climate](https://codeclimate.com/github/Chatable/chatable/badges/gpa.svg)](https://codeclimate.com/github/Chatable/chatable)

By Henry Stevens & Jack Gerrits

An untold story of Java and text based chat. This is surely something you have never seen before.
Keep your eyes peeled on this space, this will be the fastest and most beautiful text chat ever.

It will make you WANT to talk to your friends.

##Run Instructions

On a server with [Docker](https://www.docker.com/) installed run:

`docker build -t chatable/server github.com/Chatable/chatable.git`

to create the image, and then:

`docker run -P <image-id>`

to create and run a new container from that image.
