# Conference Scheduler Project 

This project is spring based web application which schedules conferences. 
Its written by using Spring-Boot web framework. So any idea with compatible with spring boot will help you run it.

**Post API:**  it expects a list of conference sessions which has duration and title attributes. 
* validates title against empty string. 
* parses duration integer from input 
(Duration value examples -> Duration : "45min Duration : "45" ) 
* schedules sessions assigns start-end dates
* sets common programId for all sessions  

  
**Search API:** Search API works based on programId parameter. it retrieves conference presentation list from
 database based on program id  


You can use following link to test the application.  
**Postman Link** [Link](https://www.getpostman.com/collections/f513406c9bfa9b1b9698)
