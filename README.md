# [Don-t Back Out](dontbackout.com)
### by Daniel Truong, William Martino, Nakul Goel
### Mobile Android Applicatino developed by Daniel Truong

This project was created for **Quarterly Projects (Winter 2019) for iEEE @ UCSD** where we placed **1st** in the showcase out of 20 teams.

## Inspiration
In this modern day and age, bad back posture is a common health issue felt by people of all ages. Most people suffer from this issue because they are just unaware of their bad posture, so we felt that people could improve their posture through a **wearable technology** that uses **statistical machine learning** to inform users when they're slouching.  

## What This Dvice Does
This mobile application is linked to an Arduino device that would ideally be placed on the back of the user, near the collar ([link to Arduino code](https://github.com/wmartino/Dont-Back-Out-Hardware). Essentially, the Android application takes in readings of the back angles by the user. Using this received data, the applicatino would go through a *calibration mode* where the angles of the user's "straight" and "slouched" posture would stored to calculate the initial user's critical angle. From then on, the user is notified, through the app and the vibration motor on the hardware, whenever they are slouching for too long. 

## Other Features
1) Toggling Notfications: User is able to turn on/off notifications if they no longer wants to receive notifications for their back.
2) Recalibration: If the applicaton is unable to read the user's posture correctly, the user is able to click the left recalibration button to reset the initial data entries and receive new data.
3) Time Stored: The application can also store how long the user stays in good posture per day.


