## Overview
### Description
App where users can only post pictures of dogs and create profiles and focuses on their locality

### App Evaluation
- **Category:** Social Media/ Photo Sharing
- **Mobile:** mobile focus, uses device camera and photo library
- **Story:** Get to know local pups in your area and share your own globally or locally
- **Market:**Everyone loves a good pupper, I believe all age groups and demographics would enjoy this app
- **Habit:** Keeping up with the popularity of your pup can be habit forming
- **Scope:** Narrow, focused on photo sharing dogs in a local area

## Product Spec
### 1. User Stories 
* User can see and select user’s submitted lost dogs.
* User can set up back end to allow users to post their dogs details.
* User can create profiles of their demographics and dogs they own.
* User can reply to posts.
* Once user's dog is found, user can mark as found.


**Optional Nice-to-have Stories**
* User can identify if it is a lost dog by “Scanning” the dog with user’s camera.(Image processing that only allows dog images)
* User can send a direct message to user who creates the post.
* video processing for uploading short videos.
* Users can favorite different dog pictures with an option to save to library.


### 2. Screen Archetypes

*Login Screen
   * users must be able to login and logout
   * enable OAUTH to sign in with Google
* Register Screen
   * User signs up or logs into their account
*Feed Screen
   * Create recyclerview and adapter to display data
   * Allows users to view and reply to posts.
* Profile Screen
   * Allow user to view and/ or modify posts that user creates 

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Feed/Timeline
* Create/Compose
* Profile/Setting

**Flow Navigation** (Screen to Screen)

* Login Activity
   * default when opening app if not already signed in
   * Successful login → go to main activity
* Main Activity
   * shows nav bar for the different tabs/fragment
   * Notice Selection -->go to specific lost dog notice 
   * On logout → go to login activity

## Wireframes
![android-group](https://user-images.githubusercontent.com/48068956/161396769-7ae533e8-f759-4e51-84c1-c103d9cceed4.gif)

![image](https://user-images.githubusercontent.com/48068956/161396825-32277c7e-1ba8-49aa-a9e0-6f0036b83d16.png)
![image](https://user-images.githubusercontent.com/48068956/161396834-0f978325-d4a7-4dde-bcd1-66f27ec2e1e1.png)
![image](https://user-images.githubusercontent.com/48068956/161396845-ff959752-cc1f-4352-bfde-c2ebb65a35d4.png)
![image](https://user-images.githubusercontent.com/48068956/161396859-db3a91d7-1cd2-4c5d-b5cb-a9f1224c3673.png)



### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
