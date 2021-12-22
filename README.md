# Applications Dev Diary App

This repo contains the source code for an application which was originally created for an App Dev module in Uni (Grade: 89%). It is the first Android App I have created properly. 

This app was submitted in a fairly completed state, however there are a number of improvements that could be made, which are listed below. I may updated
this app in the future to satisfy those points.

Additionally, starting from the heading "Top Level Requirements" is the original module assignment requirements, as well as the basic and advanced features
which I chose to implement.

### Things that could be improved
1. The current communication between fragments uses the Fragment Result API, which was chosen for quickness. However, this goes against Android Design Principles
   and it would be better as a Shared ViewModel.
2. The landscape views do not work very well when entries are added on the third screen. In general these could be improved. 
3. The current implementation of adding an image to an entry is very rudimentary and not the best, this was more to demonstrate the idea rather a proper implementation of
   adding media other than text which was not possible in the time constraint of the module assignment.
4. Support for phones @ 320 not yet supported.

### Top level requirements
1. Support fragments
2. Be able to accept user input for the date (on the first fragment), as well as a text entry (on the second fragment).
3. Store  the  diary  entries  (possibly  up  to  a  certain  max  number  of  entries).  This  may  be  implemented in a simplistic way (internal data structure, Android shared preferences)
4. Display diary entries (on the third fragment).

### Task list

#### Basic Features
- [x] 1. The first screen should allow the user to select a date in a user friendly way, ideally using a spinner or picker.
- [x] 2. The second screen is expected to offer the user the functionality to input the text for the diary entry. There  should  also  be  a button to  clear any previous input and perhaps a second button to action the insertion into the diary.
- [x] 3. The second screen should also state the date that was selected on the first page.
- [x] 4. The third screen should display previously added diary entries.

#### Advanced Features
- [x] 1. Persistent data storage
- [x] 2. Filters and search for diary entries
- [x] 3. Export diary entries to file (either in HTML or text form)
- [x] 4. Support small, medium, large and extra large screens including tablets
- [x] 5. Be responsive and allow support for landscape orientation
- [x] 6. Light Mode Toggle
- [x] 7. Add image to entries
