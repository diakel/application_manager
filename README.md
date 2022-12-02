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
Run ApplicationManagerUI (don't press enter button if you can help it)
- You can generate the first required event by typing in the name of the new application and pressing "Add Application" button
- You can generate the second required event by typing in the name of the requirement for the selected application and pressing "Add Requirement"
- You can locate my visual component by right-clicking a requirement and pressing "completed" button in the popup menu, the progress bar should change
- You can save the state of my application by pressing menu -> save
- You can reload the state of my application by pressing menu -> load
- You can upload a file to the requirement by pressing the right button and choosing upload file
- You can open the file (if you uploaded it) by pressing "open file" in the popup menu
- You can delete the file by pressing "delete file"
- You can remove application or requirement by selecting it and pressing remove application/requirement
- You can set the deadline for the selected application in the spinner at the top of the requirements pane.
- You can set the category by typing it in the text field and pressing "Set category" button
- You can search by both name and category in the search pane at the top of the applications pane
- You can sort the applications by deadlines or return to the original order by pressing "Sort by" menu

  ### Phase 4: Task 2
Thu Dec 01 17:47:55 PST 2022
Added application: UBC

Thu Dec 01 17:48:27 PST 2022
Added requirement Transcript for UBC

Thu Dec 01 17:48:36 PST 2022
Added requirement References for UBC

Thu Dec 01 17:48:42 PST 2022
Set category Grad schools for UBC

Thu Dec 01 17:49:06 PST 2022
Set the deadline 10-01-23 23:59 PM for UBC

Thu Dec 01 17:49:16 PST 2022
Changed status of Transcript to completed

Thu Dec 01 17:49:24 PST 2022
Uploaded file Unofficial_transcript.pdf for Transcript

Thu Dec 01 17:49:32 PST 2022
Opened file Unofficial_transcript.pdf for Transcript

Thu Dec 01 17:49:37 PST 2022
Deleted file Unofficial_transcript.pdf for Transcript

Thu Dec 01 17:49:44 PST 2022
Changed status of Transcript to incomplete

Thu Dec 01 17:50:03 PST 2022
Removed requirement References for UBC

Thu Dec 01 17:50:14 PST 2022
Removed application: UBC

