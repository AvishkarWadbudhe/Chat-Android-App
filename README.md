# Android Real-Time Chat App

This Android Real-Time Chat app allows users to create an account using a unique username and password. Authenticated users can chat with other registered users in real-time. The app utilizes Firebase for user authentication, Firestore for real-time database storage, Retrofit for API communication, and FCM (Firebase Cloud Messaging) for sending notifications.

## Features

- User Authentication: Allow users to create an account using a unique username and password through Firebase Authentication.

- Real-Time Messaging: Implement real-time chat functionality using Firebase Firestore. Users can send and receive messages instantly.

- Push Notifications: Utilize FCM (Firebase Cloud Messaging) to send push notifications for new messages and other relevant events.

## Screenshots


## Installation

To run the app locally, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/AvishkarWadbudhe/Chat-App.git
   ```

2. Open the project in Android Studio.

3. Replace the Firebase configurations in the `google-services.json` file with your own Firebase project configurations. Set up Firebase Authentication, Firestore, and FCM in the Firebase console.

4. Build and run the app on an Android device or emulator.

## Dependencies

The following dependencies are used in this project:

- Firebase Authentication: For user authentication. More information can be found in the [Firebase Authentication documentation](https://firebase.google.com/docs/auth).

- Firebase Firestore: For real-time database storage. More information can be found in the [Firebase Firestore documentation](https://firebase.google.com/docs/firestore).

- Retrofit: A type-safe HTTP client for API communication. Used for sending notifications through FCM. More information can be found on the [Retrofit GitHub page](https://github.com/square/retrofit).

- Firebase Cloud Messaging (FCM): For sending push notifications. More information can be found in the [FCM documentation](https://firebase.google.com/docs/cloud-messaging).

## Contributions

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgements

- [Firebase](https://firebase.google.com/)
- [Retrofit](https://github.com/square/retrofit)
