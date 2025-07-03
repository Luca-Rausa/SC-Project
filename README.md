# Steger Center Mobile App

## Project Overview
The Steger Center Mobile App is a comprehensive mobile application designed for Virginia Tech's Steger Center for International Scholarship located in Riva San Vitale, Switzerland. The app serves as a digital companion for students, faculty, and visitors to enhance their experience at the center by providing various features related to events, meals, travel, and information about the surrounding area.

## Repository Structure
- **Code/**: Contains all source code
  - **SC-Project-final/**: Android mobile application (Java)
    - Core mobile app with user interface and local database functionality
  - **ChatBot/**: Python-based chatbot server
    - Provides information about the Steger Center, Switzerland, and answers common questions
- **Poster/**: Project poster presentation

## Demo
- [View Application Demo](https://drive.google.com/file/d/1AW5LTffHtN0r4cHr-AC069mzcm7F1U34/view?usp=sharing)

## Features

### Mobile Application
1. **User Authentication**
   - Sign up and login system
   - Different roles for regular users and staff

2. **Event Management**
   - Create, view, and attend events
   - Event categorization and filtering
   - Attendee management
   - Image attachments for events

3. **Meal Planning**
   - Schedule meals during your stay
   - Specify dietary requirements and meal timings
   - Program of study integration

4. **Travel Itinerary**
   - Plan and manage travel schedules
   - Group travel coordination
   - Travel information and recommendations

5. **Feedback System**
   - Submit and view feedback
   - Rating system for services and features

6. **Interactive Chatbot**
   - Information about the Steger Center and surrounding areas
   - Answers to common questions about facilities, transportation, and activities
   - Recommendations for local attractions

## Setup Instructions

### Prerequisites
- Android Studio (latest version)
- JDK 8 or higher
- Python 3.8 or higher
- Android device or emulator (API level 30+)

### Mobile App Setup
1. Clone this repository
2. Open the Code/SC-Project-final directory in Android Studio
3. Sync Gradle files
4. Build and run the application on your device or emulator

### Chatbot Server Setup
1. Navigate to the Code/ChatBot directory
2. Install required Python packages:
   ```
   pip install -r requirements.txt
   ```
3. Run the Flask server:
   ```
   python main.py
   ```
4. The server will start on port 5000

### Connecting Mobile App to Chatbot
- For emulator testing: The mobile app is configured to connect to the chatbot at `10.0.2.2:5000` (Android emulator's alias for localhost)
- For physical device testing: Update the URL in `Chatbot.java` to point to your server's IP address

## Database Schema
The app uses SQLite for local data storage with the following tables:
- `events`: Stores event information
- `images`: Stores images associated with events
- `user`: User account information
- `attendees`: Tracks event attendance
- `meals`: Meal planning information
- `itinerary`: Travel planning details
- `feedback`: User feedback and ratings

## Technologies Used
- **Mobile App**: 
  - Java
  - Android SDK
  - SQLite
  - Volley (for API requests)
- **Chatbot**:
  - Python
  - Flask
  - Transformers (DialoGPT)
  - FuzzyWuzzy (for intent matching)
