# Project 2 - *Flixter*

**Flixter** shows the movies with the best overall rating. The app utilizes the Movie Database API to display images and basic information about these movies to the user.

Time spent: **8** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **scroll through current movies** from the Movie Database API
* [x] Display a nice default [placeholder graphic](https://guides.codepath.org/android/Displaying-Images-with-the-Glide-Library#advanced-usage) for each image during loading
* [x] For each movie displayed, user can see the following details:
  * [x] Title, Poster Image, Overview (Portrait mode)
  * [x] Title, Backdrop Image, Overview (Landscape mode)
* [x] Allow user to view details of the movie including ratings within a separate activity

The following **stretch** features are implemented:

* [x] Improved the user interface by experimenting with styling and coloring.
* [x] Apply rounded corners for the poster or background images using [Glide transformations](https://guides.codepath.org/android/Displaying-Images-with-the-Glide-Library#transformations)
* [x] Apply the popular [View Binding annotation library](http://guides.codepath.org/android/Reducing-View-Boilerplate-with-ViewBinding) to reduce boilerplate code.
* [x] Allow video trailers to be played in full-screen using the YouTubePlayerView from the details screen.

The following **additional** features are implemented:

* [x] Implemented Lazy Loading on main movie cataloge for seamless infinite scrolling

* [x] Implemented catalogue menu to choose between Now Playing, Upcoming and Top Rated movies

* [x] Added information with a second HTTP request:
  * [x] Release date
  * [x] Genre
  * [x] Runtime
  
* [x] Retrieved similar movies for selected title with an HTTP request and added their posters

* [x] Implemented a scrolling view to allow movie information to overflow

## Video Walkthrough

Here's a walkthrough of implemented user stories in portrait mode:

<img src='https://github.com/Esteb37/Flixter/blob/master/Walkthrough.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Amd in landscape mode:

<img src='https://github.com/Esteb37/Flixter/blob/master/Walkthrough_land.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [Kap](https://getkap.co/).

## Notes

The most notable challenges for this assignment was the design aspects of the application. The Android Studio IDE and the language itself have a very poor management of the visual aspects of the application, and require over complicated solutions for simple tasks like rounding corners or adding a border.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright [2021] [Esteban Padilla Cerdio]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
