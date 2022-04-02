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
* User can see and select user’s submitted dogs
* User can set up back end to allow users to post their dogs details
Image processing that only allows dog images
* User can identify if it is a lost dog by “Scanning” the dog with user’s camera.
* User can create profiles of their dogs


**Optional Nice-to-have Stories**
* Chat messaging
* video processing for uploading short videos
* * Users can favorite different dog pictures with an option to save to library

### 2. Screen Archetypes

*Login Screen
   * users must be able to login and logout
   * enable OAUTH to sign in with Google
* Register Screen
   * User signs up or logs into their account
*Feed
   * Create recyclerview and adapter to display data
   * Profile

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
 *

## Wireframes


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
