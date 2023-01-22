# Movies app
by
Karam

## Table of Contents

- [About](#about)
- [Hints](#hints)


## About 

-The application is a simple tool for browsing movies with search functionality


1. Listing:the movies are rendered in lists (grid) filtered by genre into 
tabs     
         

        . All is an extra tab in the beginning where no filtering is applied
        . The user can select any tab or swipe to the adjacent tabs
        . On Selecting each tab the movies list is loaded and filtered using the  corresponding genre
        . Show the image, name & year of production for each movies item
        . As the user scrolls down the next page is loaded until they reach the end. I.e. Implement infinite scrolling

 <a target="blank"><img align="center"
      src="https://github.com/KaramZero/SimpleMoviesApp/blob/master/home.jpg"
      alt="Home screen " height="30" width="40" />





2. Details: The user can view the movie details in a separate page.
        
        a. If the user clicks on a movie item from the list, they are redirected to a new page where the details of that movies are shown

        b. The page shows all the movie’s image/ poster, name, year of production, rating, description, cast (if available)
        c. A toolbar bar is shown on top, and has a back arrow to navigate back, and the movie title is shown too.

 <a target="blank"><img align="center"
      src="https://github.com/KaramZero/SimpleMoviesApp/blob/master/movie_details1.jpg"
      alt="Details screen " height="30" width="40" />
       <a target="blank"><img align="center"
      src="https://github.com/KaramZero/SimpleMoviesApp/blob/master/movie_details1.jpg"
      alt="Details screen " height="30" width="40" />



3. Search: search the movies db and filter using a search query

        a. The user can write in the search box and click the search icon or the searchaction on their keyboard. The user then is redirected to a second page that contains the search bar and the resulting movies list.
        b. The user can repeat the same search sequence from this screen


<a target="blank"><img align="center"
      src="https://github.com/KaramZero/SimpleMoviesApp/blob/master/search.jpg"
      alt="Search screen " height="30" width="40" />

## Hints 

- ROOM Database
- Navigation component
- Clean Architecture.
- Hilt Injection.
- Rely on Fragments (Single Activity ).
- Offline Mode.
- Retrofit

