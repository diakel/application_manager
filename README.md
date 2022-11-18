## My Personal Project

### *Proposal*

<p> I am planning to design an <strong>application manager</strong> 
which 
would store and track user's various applications. A 
user can input applications, their 
deadlines, and a list of the requirements for each one. 
They can also fill out applications inside the program
by uploading files from their computer. The program 
will show deadlines and progress on each one.
For example, a user is applying to many grad schools.
They can store and keep track of all their applications 
for each one of them as well as some required documents
(e.g. motivational essays, transcripts, etc.) </p>

<p> The program might be useful for people applying to
jobs, schools, visas, tax returns. It allows to manage
and collect documents for different applications in 
one place. Personally, I often have to apply for visas 
and having a program to store many required documents 
for them would be useful.</p>

### *User Stories*

For now, 4 user stories that will be available in the console (outside of console, much more is implemented and tested):
As a user, I want to be able to:
- Add an application to my list of applications
- Remove an application from my list
- Choose a deadline for an application
- Sort all application by their deadlines
- Create a category for an application
- Filter applications by their categories
- Add a required document to an application
- Remove a required document
- Upload a document for an application
- Open a document for an application
- Save my application list (with all the underlying requirements, files, etc.) to a file
- Load my application list (with all the structure intact) from a file

  ### Instructions for Grader
- You can generate the first required event by typing in the name of the new application and pressing "Add Application" button
- You can generate the second required event by typing in the name of the requirement for the selected application and pressing "Add Requirement"
- You can locate my visual component by...
- You can save the state of my application by pressing menu -> save
- You can reload the state of my application by pressing menu -> load