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
- [ ] User can sign up their user account based on method they choose
- [ ] User user can login and log out
- [ ] User can post photos of dogs only
- [ ] User can get the current feed of dogs based on their physical location
- [ ] User can reply to specific dog post
- [ ] User can see their own feed at the Profile tab


**Optional Nice-to-have Stories**
- [ ] User can identify if it is a lost dog by “Scanning” the dog with user’s camera.(Image processing that only allows dog images)
- [ ] User can send a direct message to user who creates the post.
- [ ] video processing for uploading short videos.
- [ ] Users can favorite different dog pictures with an option to save to library.


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
Note: Activities/ Fragments in green are required Activities/ Fragments; Arrows in Orange show the nagvigation flow between different Screens.
![IMG_0611](https://user-images.githubusercontent.com/67621407/163572838-55f3eb02-c0d8-40cd-895c-429f92b58a4b.jpg)


### [BONUS] Digital Wireframes & Mockups
![android-group](https://user-images.githubusercontent.com/48068956/161396769-7ae533e8-f759-4e51-84c1-c103d9cceed4.gif)

![image](https://user-images.githubusercontent.com/48068956/161396825-32277c7e-1ba8-49aa-a9e0-6f0036b83d16.png)
![image](https://user-images.githubusercontent.com/48068956/161396834-0f978325-d4a7-4dde-bcd1-66f27ec2e1e1.png)
![image](https://user-images.githubusercontent.com/48068956/161396845-ff959752-cc1f-4352-bfde-c2ebb65a35d4.png)
![image](https://user-images.githubusercontent.com/48068956/161396859-db3a91d7-1cd2-4c5d-b5cb-a9f1224c3673.png)


### [BONUS] Interactive Prototype

## Schema 
### Models

#### User

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | userID	   | String   | unique id ofthe user (key attribute) |
   | password      | String   | hashed password |
   | caption       | String   | image caption by author |
      

#### Post

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | userID	   | String   | id ofthe user  |
   | postId        | String   | unique id of the post (key attribute)|
   | image         | File     | image that user posts |
   | caption       | String   | image caption by author |
   | likeCount	   | Number   | number of likes given by viewers.|
   | isFound       | Boolean  | boolean value indicating the dog is found or not(false by default).|
   | isFollowed    | Boolean  | boolean value showing whether a user has followed a post(false by default).|
   | vote          | String   | id of related vote table entry|
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |

#### Vote

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | voteId	   | String   | unique id specific vote (key attribute) |
   | userId        | String   | id of vote source |
   | postId        | String   | id of voted post  |
### Networking
#### List of network requests by screen
   - Feed Screen
      - (Read/GET) Query all posts 
          ```kotlin
          val query: ParseQuery<post> = ParseQuery.getQuery("Posts")
          query.order(byDescending: "createdAt")
	        query.findInBackground( //callback that loads results into recyclerview)
          ```
     - (Create/POST) Vote
       ```kotlin
	      val parseObject: ParseObject = ParseObject("zeVote")
	      parseObject.put("userID", ParseUser.getCurrentUser.getString("userID"))
	      parseObject.put("postID", postIDString)
	      parseObject.saveInBackground();
        ```
   - Create Post Screen
      - (Create/POST) Create a new post object
	     ```kotlin
          val parseObject: ParseObject = ParseObject("zePost")
	        parseObject.put("userId",ParseUser.getCurrentUser.getString("userID"))
	        parseObject.put("image", imageFileObject)
	        parseObject.put("caption", captionText)
	        parseObject.saveInBackground()
       ```
   - Profile Screen
      - (Read/GET) Query user created posts
	    ```kotlin
        val query: ParseQuery<post> = ParseQuery.getQuery("Posts")
        query.order(byDescending: "createdAt")
	      query.whereEqualTo("userID",ParseUser.getCurrentUser.getString("userID")
	      query.findInBackground( //callback that loads results into recyclerview)
      ```
   - Login Screen
      - (Read/Get) authenticate user
	    ```kotlin
        ParseUser.logInInBackground(userName,password, loginCallback)
      ```  
      
## Sprint 1 build process Walkthrough

Here's a walkthrough of first build process:

<img src='https://i.imgur.com/QNOZNg5.gif' title='Video Walkthrough' width='450' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).
