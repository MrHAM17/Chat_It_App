This is "master" branch(main) of this repo; And presenting latest version (i.e, version no. 2) of the app. Old version is in "previous_version" branch of this repo.

# Chat It App
It is the Mmessaging Android Application demonstrating how to use Create, Upload, Read & Delete operations & how to deal with Firebase Realtime Database.


## Acknowledgements

We would like to express our sincere gratitude towards our guide Prof. Rohini D. Palve, Mini 
Project Coordinators Prof. Saima Sayyed for their help, guidance and encouragement, they 
provided during the project development. This work would have not been possible without their 
valuable time, patience and motivation. We thank them for making our stint thoroughly 
pleasant and enriching. It was great learning and an honor being their student. 

We are deeply thankful to Dr. Seema Bidey (H.O.D Computer Department) and entire team 
in the Computer Department. They supported us with scientific guidance, advice and 
encouragement, they were always helpful and enthusiastic and this inspired us in our work. 

We take the privilege to express our sincere thanks to Dr. L. K. Ragha our Principal for 
providing the encouragement and much support throughout our work.


## Abstract

This mini project report introduces "Chat !t," an Android-based chatting application that prioritizes 
user privacy and security. The report provides a detailed overview of the development process, 
including its features, objectives, specifications, and necessary conditions for it to work. The report 
also includes a literature survey that reviews existing messaging and chatting applications, the 
development process and architecture of Chat !t, the tools and technologies used for its 
implementation, and recent technological innovations that facilitate its implementation. The report 
concludes with a discussion of the results and user experience of Chat !t and its significance as a social 
networking tool. 

The increasing need for social interaction and communication has led to the development of various 
messaging and chatting applications. However, most of these applications require users to share their 
personal details, which can lead to privacy concerns. To address this issue, we have developed "Chat 
!t," an Android-based chatting application that connects people without requiring them to share their 
personal information. This mini project report provides a detailed description of the development 
process of Chat !t.

##   List of Figures

-       1)  4.1 Software & Hardware Requirements  
-       2)  4.2 Work Flow Of System 
-       3)  4.3 Architecture Diagram Of System 
-       4)  5.1 Profile Verification
-       5)  5.2 Profile Creation
-       6)  5.3 App Interface & Features

## Chapter 1: Introduction

### 1.1 Motivation : 
In today's digital age, communication plays a vital role in our daily lives. With the widespread use of 
smartphones and the internet, people are more connected than ever before. However, many traditional 
communication methods still require the exchange of personal information such as phone numbers or 
email addresses, whichcan be a barrier for those who value their privacy or want to remain anonymous. 

To address this issue, we have developed Chat !t, a chatting application that allowsusers to connect 
with each other without the need for personal information. Chat !toffers a platform for people to chat, 
make new friends, and build relationships withothers, regardless of their location or background. 

Moreover, Chat !t offers a safe and anonymous way for people to connect with others, without the fear 
of unwanted attention or harassment. With its intuitive userinterface and robust features, Chat !t is 
poised to become a leading chatting application in the digital world. 

The motivation behind developing Chat !t was to create a tool that can bring people together, without 
compromising their privacy or security. We believe that Chat !t hasthe potential to revolutionize the 
way people communicate and build relationships inthe digital age, and we are excited to share our 
findings and insights in this report.
 
### 1.1.1 Need Of The Problem : 
The need for the Chat !t chatting application arises due to the growing demand for a platform that can 
connect people without sharing their personal details. In today's digital age, people are often hesitant 
to share their personal information on various social media platforms due to security concerns. 
Moreover, there is a lack of a secure and reliable platform that can facilitate communication between 
individuals of the same work profile for business purposes or to build social connections. The Chat !t 
application addresses these concerns by providing a safe and secure platform for people to connect 
with each other without sharing their personal details. Additionally, the application also helps in 
building social connections and can be used for time pass with strangers. Therefore, the need for a 
platform like Chat !t is crucial in today's world where social interaction has become increasingly 
digitized.

### 1.2 Scope Of The Project : 
The scope of this project is to develop a secure and user-friendly chatting application called "Chat !t" 
that connects people without sharing their personal information. The application aims to provide a 
platform for users to interact with strangers, make new friends, and improve their social skills. 
Additionally, it aims to facilitate business networking by connecting people with similar work profiles. 
The application will be developed using Java, Android Studio, and Firebase, and will include features 
such as secure login and registration, one-on-one chat, group chat, and user profile management. The 
project will focus on creating an easy-to-use interface that provides a seamless chatting experience for 
users.

## Chapter 2: Literature Review 
 
Existing chatting applications usually require users to share personal details, such as phone numbers, 
email addresses, or social media profiles, to connect with other users. However, this raises privacy 
concerns and puts users at risk of receiving unwanted messages or calls from strangers. 
To overcome these limitations, we have developed "Chat !t", an Android-based chatting application 
that connects users without requiring them to share personal information. Our application uses 
Firebase for real-time communication between users, ensuring that messages are delivered instantly. 
In addition to privacy concerns, existing chatting applications often lack features that enhance user 
experience, such as message customization and real-time typing indicators. "Chat !t" addresses these 
limitations by providing a range of customizable options, including text color, font size, and 
background color. The application also provides real-time typing indicators, which notify users when 
the other party is typing. 
Overall, "Chat !t" provides a secure and customizable chatting experience, without compromising on 
user privacy. 

![](https://github.com/MrHAM17/Chat_It_App/blob/master/2.%20Rough%20Work%20&%20Data/Used%20Images/Fig%202.1.png?raw=true)

## Chapter 3: Problem Statement 
 
### 3.1 Problem Statement : 
In today's fast-paced world, communication is an essential aspect of our daily lives. With the 
increasing use of technology and the internet, people are now able to connect with each other from 
different parts of the world. However, many social media platforms require users to share their 
personal information, which can lead to privacy concerns. Moreover, there is a need for a platform 
where people can communicate with each other without any specific purpose, whether it's to pass the 
time or make new friends. 

To address these issues, the Chatting Application: Chat !t was developed. The main objective of this 
project is to create a platform where people can chat with each other without having to share their 
personal information. The app provides a secure and anonymous environment for users to 
communicate with each other. Furthermore, it allows users to connect with people from different 
backgrounds and interests, thereby promoting social interaction and creating opportunities for new 
friendships. 

The project also aims to provide a simple and user-friendly interface for users to navigate. It is 
designed to be easily accessible to everyone, regardless of their technical expertise. By developing 
this application, we hope to create a new way for people to interact with each other and promote a 
more connected world.

### 3.2  Features : 
The "Chat !t" application has several features that make it user-friendly and convenient for users to 
connect with each other. Some of the key features of the application are: 

 -  #### 3.2.1. Anonymous Chatting:

   Users can chat with each other without disclosing their personal information. 
   The application assigns a unique username to each user, which helps maintain privacy and 
   ensures that users feel safe and secure while using the app. 

-   #### 3.2.2. One-to-One Chatting:

   Users can connect with each other on a one-to-one basis, whichhelps facilitate personal conversations and 
   enables users to build meaningful relationships with each other. 

-   #### 3.2.3. Group Chatting:
   
   The application also supports group chatting, which allows users to connect with multiple users at once. 
   Users can create or join groups based on commoninterests, hobbies or professions. 

-   #### 3.2.4. Chat History:
   
   The application keeps a record of all the chats, which enables users torefer back 
   to their previous conversations and continue from where they left off. 

-   #### 3.2.5. Online Status:
   
   The application shows the online status of each user, which helps usersknow 
   when their friends or contacts are available for chatting. 

-   #### 3.2.6. Push Notifications:

    The application sends push notifications to users whenever they receive a new message, 
    which ensures that users do not miss any important messages. 

-   #### 3.2.7. Emojis and Stickers:

    The application has a wide range of emojis and stickers that users can use to express their emotions and 
    make their conversations more fun and engaging. 

These features make the "Chat !t" application a versatile and user-friendly platform for usersto 
connect with each other and build meaningful relationships. 
 
### 3.3 Objectives : 
The main objectives of developing this "Chat !t" application are: 

1. To provide a platform for people to connect with each other without sharing personal details, 
and to communicate with strangers for entertainment and socializing purposes. 

2. To create a user-friendly and intuitive interface for chatting and messaging, with easy-to-use 
features such as real-time message notifications, chat history, and group chat. 

3. To ensure the privacy and security of user data by implementing advanced encryptionalgorithms 
and secure communication protocols. 

4. To create a scalable and robust system that can handle a large number of users andmessages, 
with minimal downtime and fast response times. 

5. To enable users to discover new people and make friends with similar interests orwork 
profiles, and to foster a sense of community and social connectedness. 

By achieving these objectives, this "Chat !t" application can offer users a seamless and securechatting 
experience, and provide a valuable tool for connecting with others, fostering social connections, and 
facilitating communication for entertainment, personal or professional purposes.

### 3.4  Specifications Of The System : 
The Chat !t application is designed to work on Android devices running Android OS version 
5.0 or higher. The following are the detailed specifications of the system: 

-   #### 3.4.1  User Interface: 
       
       The application has a user-friendly and intuitive interface that 
       allow users to easily navigate through the app's features. 

-   #### 3.4.2 Functionalities: 
      
      The Chat !t application provides the following functionalities: 
    
    - User registration and login 
    - Chatting with anonymous users 
    - Block and unblock users 
    - Report users for inappropriate behaviour 
    - View and edit user profile 
    - Notification for new messages 
    - Real-time messaging with the help of Firebase Real-time Database 

-   #### 3.4.3 Technology Stack: 
    
      The Chat !t application is developed using Java programming language and 
      Android Studio as the development environment. Firebase Real-time Databaseis used for real- 
      time messaging and user data storage. 

-   #### 3.4.4 Compatibility: 
     
      The Chat !t application is compatible with Android OS version 5.0 or higher and 
      can be used on any Android device meeting the minimum system requirements. 

-   #### 3.4.5 Security: 
    
      The Chat !t application uses Firebase Authentication to ensure secure user 
      authentication and user data protection. Users are not required to provide their personal details 
      while chatting, ensuring their privacy and security. 

-   #### 3.4.6 Performance: 
    
      The Chat !t application is designed to work efficiently on low-end deviceswith limited 
      resources. The app provides fast and reliable messaging with minimal latency. 

-   #### 3.4.7 Scalability: 
     
      The Chat !t application is designed to handle a large number of users andmessages 
      simultaneously, ensuring smooth performance even during peak usage hours. 

Overall, the Chat !t application is designed to provide a seamless chatting experience for user while 
ensuring their privacy and security

## Chapter 4: Design & Implementation

### 4.1. Tech Stack

**Developer:** Java, Android Stdio SDK

**Server:** FireBase Database

**Client:** Smartphone with minimum 2GB RAM,  minimum API level of 21


### 4.2. Work Flow of System :  


![](https://github.com/MrHAM17/Chat_It_App/blob/master/2.%20Rough%20Work%20&%20Data/Used%20Images/Fig%204,2.png?raw=true)

-   #### 4.2.1. New login:

   The user initiates the login process by providing their login credentials, such as 
   their email address or username and password. 

-   #### 4.2.2. Authentication using OTP:
   
   Once the user enters their login credentials, the system generates a one-time password (OTP) and
   sends it to the user's registered mobile number or email address.
   The user then enters the OTP to verify their identity and authenticate their login. 

-   #### 4.2.3. Profile Setup:
   
   After the user has successfully authenticated their login, the system prompts 
   them to set up their profile. This includes entering personal information such as their name, 
   profile picture, and other relevant details. 

-   #### 4.2.4. Profile created in database:
   
   The system stores the user's profile information in a database, 
   along with their login credentials and other relevant data. 

-   #### 4.2.5. Access to Home Page:
   
   Once the user has completed the profile setup process, the system 
   grants them access to the home page of the application, where they can begin using the chat 
   application to communicate with other users. 

Overall, this workflow ensures that the user's identity is verified and their profile information is 
securely stored in a database, while also providing a smooth and efficient user experience.       

### 4.3. Architecture Diagram Of System : 


![](https://github.com/MrHAM17/Chat_It_App/blob/master/2.%20Rough%20Work%20&%20Data/Used%20Images/Fig%204.3.png?raw=true)

-   #### 4.3.1. Client-side:
   
   The client-side is where users interact with the chat-based application. This 
   can include a web or mobile app, and it is responsible for rendering the user interface 
   and handling user input. The client-side communicates with the server-side through 
   APIs. 

-   #### 4.3.2. Server-side:
   
   The server-side is where the application logic resides, and it is responsible 
   for handling incoming requests from clients, processing data, and sending responses 
   back to clients. It also handles real-time database updates. 

-   #### 4.3.3. Real-time Database:
   
   The real-time database is where all data related to chat messages, 
   user profiles, and other application data is stored. It is designed to handle high-volume, 
   real-time data updates, ensuring that all clients receive the latest data immediately. 

-   #### 4.3.4. Database Management:
   
   The database management component is responsible for 
   maintaining the health of the database, ensuring that it is performing optimally, and 
   monitoring for errors or issues. It should also provide simple, user-friendly tools for 
   database administration, such as a dashboard or API. 

Overall, the goal of this architecture is to provide a seamless and user-friendly chat-based 
application experience, with real-time updates and simple database management. By 
separating the client-side, server-side, real-time database, and database management 
components, the application can be easily maintained and scaled as needed. 

## Chapter 5: Results & Discussions
The Chat !t application was successfully developed and tested on various Android devices.The 
application provides a user-friendly interface for chatting with other users. The main features of 
the application include registration and login, search for nearby users, chat withusers, and the 
ability to report inappropriate behavior. 

The application was tested by a group of users, and their feedback was recorded. The users found the 
application easy to use and appreciated the anonymity feature that allowed them to chat without 
sharing their personal details. The search feature for nearby users was also foundto be useful for 
making new connections. 

The screenshots of the application are included below:

![](https://github.com/MrHAM17/Chat_It_App/blob/master/2.%20Rough%20Work%20&%20Data/Used%20Images/Fig%205.1.png?raw=true)


![](https://github.com/MrHAM17/Chat_It_App/blob/master/2.%20Rough%20Work%20&%20Data/Used%20Images/Fig%205.2.png?raw=true)


![](https://github.com/MrHAM17/Chat_It_App/blob/master/2.%20Rough%20Work%20&%20Data/Used%20Images/Fig%205.3.jpg?raw=true)

-   #### 5.1. Profile Verification :
   
   To ensure the safety and authenticity of the users, the chat-based 
   application can implement a profile verification process using an online DBMS system. This 
   system can verify users' identities by validating their email addresses, phone numbers, or other 
   personal information. By using an online DBMS system, the application can store and manage 
   user data securely, efficiently, and in real-time. 

-   #### 5.2. Profile Creation :
   
   To create a profile on the chat-based application, users need to provide their 
   basic information like name, email address, and phone number, etc. The application can use 
   proper user authentication methods, such as two-factor authentication or OTP verification, to 
   ensure that only genuine users can create profiles. This helps to prevent fake profiles and 
   ensures that all users on the platform are authentic. 

-   #### 5.3. Application Interface & Features :
   
   The chat-based application can provide users with 
   various features and an intuitive interface for group chat. These features may include the ability 
   to create groups, invite friends to groups, send messages, share files and images, and create 
   polls or events. The interface should be user-friendly and visually appealing, allowing users to 
   easily navigate through the application and interact with their friends and groups. 

-   #### 5.4. Status:
   
   The chat-based application can also include a status feature that allows users to update 
   their status and share it with their friends. This feature can be used to let friends know what 
   the user is doing, feeling, or thinking. The status feature can also include customizable options, 
   such as adding emojis or changing the background color, to make it more fun and engaging for users. 

Overall, by implementing profile verification using an online DBMS system, proper user 
authentication, and providing an intuitive interface and features for group chat and status updates, the 
chat-based application can provide a safe, engaging, and enjoyable experience for its users. ## Chapter 6: Conclusion and Future Scope

### 6.1 Conclusion : 
In conclusion, we have successfully developed an android application called "Chat !t" using Java, 
Android Studio, and Firebase. The application is designed to connect people with each other without 
sharing their personal details, helping them to socialize and build connections. We have implemented 
various features such as real-time messaging, group chat, profile management, and security measures 
like end-to-end encryption to ensure the user's privacy. During the development process, we faced 
several challenges such as integrating Firebase with the application, designing a user-friendly 
interface, and ensuring seamless communication between users. However, we overcame these 
challenges and successfully delivered the expected output.

### 6.2 Future Scope : 
There are several potential areas for future research and development of the "Chat !t"application. 
Some of these are: 

1. Enhancing the security features of the application to prevent unauthorized access andprotect 
user data. 

2. Implementing machine learning algorithms to improve the chatbot's responseaccuracy 
and provide a better user experience. 

3. Developing the application for other platforms like iOS, Windows, and Web toincrease 
its user base. 

4. Adding more features like group chat, voice and video call, and file sharing to makethe 
application more versatile and attractive to users. 

5. Integrating the application with social media platforms like Facebook, Twitter, andInstagram 
to increase its reach and user engagement. 

6. Developing a revenue model for the application by adding premium features and in- app purchases. 

In conclusion, the "Chat !t" application has a lot of potential for future development and research. The 
application can be further improved and customized to meet the changing needsand preferences of its 
users. With its user-friendly interface and secure communication, the application can become a 
popular choice for people looking for a safe and convenient way to connect with each other.

## Documents

Project Report

![PDF](https://github.com/MrHAM17/Chat_It_App/blob/master/1.%20All%20Project%20IMP%20Docs/Sem%204%20Mini%20Project%20Report/Project%20Report.pdf)

For more details,
Kindly Check the 1st folder of this repo i.e, "Prject All IMP Docs"

App Logo

![Logo](https://github.com/MrHAM17/Chat_It_App/blob/master/2.%20Rough%20Work%20&%20Data/Used%20Images/CHAT%20IT%20%20LOGO.png?raw=true)

## Installation

Install my-project with:
- Android Stdio SDK

## Reference:

- Dr. Abhay Kasetwar Ritik Gajbhiye., Creating a two-way communication system
  “Development of Chat Application”, May 2022 (SSDM)

- Gaurav Joshi Jatin Bisht., Security for the chating application remains an issue,
  ” Chatting Application with Profanity detection”, May 2022 (IRJMETS)

- Bhadoria Ishani Pavankumar Patel. ,Storage and management of data base using SQL properties,
  ”Android based instant messaging Application using Firebase”, July-2016 (IRJMETS) 
   

- Karthick, R. J. Victor, S. Manikandan and B. Goswami,
  "Professional chat application based on natural language processing,"
  2018 IEEE International Conference on Current Trends in Advanced Computing (ICCTAC)(2018) 

- Ali, Ammar H., and Ali Makki Sagheer. ,
  "Design of a secure android chatting application using end to end encryption."
  Journal ofSoftware Engineering & Intelligent Systems (JSEIS) 2.1.(2021).

- Emmadi, Sai Spandhana Reddy, and Sirisha Potluri.
  "Android based instant messaging application using firebase."
  International Journal Recent Technology and Engneering 7.5 (2019) (IRJTE)


- Youtube
   - https://youtu.be/F_UemS493IM
   - https://youtu.be/j1K5GTDycfg
   - https://youtu.be/asguThv4vkE
   - https://youtu.be/58CM8e80XYw
   - https://youtu.be/mufbi0A6EuI
   - https://youtu.be/I2bEcv_3iMU
   - https://youtu.be/VIulJhGOpQo
   - https://youtu.be/A9rcKZUm0zM
  
- LinkedIn 

- Github 
