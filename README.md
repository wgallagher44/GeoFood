
<div id="top"></div>
<!-- PROJECT LOGO -->
<br />
<div align="center">
  
<h3 align="center">GeoFood</h3>

  <p align="center">
   GeoFood Is an Android Application that is used to locate restaurants within a certain radius in miles and price range in dollars. You can either find restaurants with yor current location or select a location on the map 
    <br />
    <a href="https://github.com/wgallagher44/GeoFood"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/wgallagher44/GeoFood">View Demo</a>
    ·
    <a href="https://github.com/wgallagher44/GeoFood/issues">Report Bug</a>
    ·
    <a href="https://github.com/wgallagher44/GeoFood/issues">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contact">Contact</a></li>

  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

This was my first major project that I built. First I used android studio to develop all the code for this app and wrote in the languages Java and Kotlin. For the layouts I used xml to specify the layouts. I used the google places api nearby search to get all of the restaurants. And embedded google maps into the app so you can select a location on the map and see where your are trying to search for. I used with kotlin geo-location to get the current location of the user as well 

<p align="right">(<a href="#top">back to top</a>)</p>

### Built With

* [Android Studio](https://developer.android.com/studio/?gclid=CjwKCAjw77WVBhBuEiwAJ-YoJF924pDhXxFs6YqfSkseXtEDgZr5PEbxw-btD2fvRB_lZsQ1WYGiwhoCRsAQAvD_BwE&gclsrc=aw.ds)
* [Kotlin](https://kotlinlang.org/)
* [Google Places Nearby Search Api](https://developers.google.com/maps/documentation/places/web-service/search-nearby)
* [Firebase](https://firebase.google.com/)


<p align="right">(<a href="#top">back to top</a>)</p>

<!-- GETTING STARTED -->

## Getting Started
To run this application your are first going to have to follow the steps below.

### Installation

1. Install [Android Studio](https://developer.android.com/studio/?gclid=CjwKCAjw77WVBhBuEiwAJ-YoJF924pDhXxFs6YqfSkseXtEDgZr5PEbxw-btD2fvRB_lZsQ1WYGiwhoCRsAQAvD_BwE&gclsrc=aw.ds)

2. Create A new project on  google console and get a google api key [Google Console](https://console.cloud.google.com/apis)

3. Clone the repo

   ```sh
   git clone https://github.com/wgallagher44/GeoFood.git
   ```

4. Create A new firebase project and connect it to android studio
 [Firebase Docs](https://firebase.google.com/docs/android/setup) 



5. Enter your API in `strings.xml`

   ```xml
   <string name="apiKey">"Your Api Key"</string>
   ```

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->
## Usage
To get started with this app you will first need to create an account or continue as a guest. Logging in will allow you to have the ability to save restaurants that you like. Next you will either select a location on the map or go to your current location. Once you have the location you want you can change the price range and radius of the search. When you have all that set you can press the search icon to search with those constraints. This Will launch to a new page where you see all the restaurants that you can scroll through.



See the [open issues](https://github.com/wgallagher44/GeoFood/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

* willgallagher0702@gmail.com

## Project Link

 [https://github.com/wgallagher44/GeoFood](https://github.com/wgallagher44/GeoFood)

<p align="right">(<a href="#top">back to top</a>)</p>
