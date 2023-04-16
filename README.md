# Sportify Mobile Version

Sportify is an application that enables users to create or participate in football-related events in their local area. Football is a highly popular sport in Morocco, particularly among young people who enjoy playing with friends or neighbors. However, it can be difficult to organize a game due to a lack of available pitches or limited spots. Additionally, newcomers to a neighborhood may not know people to play with. Sportify addresses these challenges by allowing users to create a profile, organize events (with prior pitch reservation), or join existing games.

## Features

- User authentication (login, register)
- User profile creation and editing
- Event creation, modification and deletion
- Joining and leaving events
- Real-time event notifications using WebSocket
- Payment processing with PayPal Android SDK
- Responsive UI design
- Multi-language support
- Asynchronous communication with web server using Retrofit
- Dependency injection with Dagger Hilt
- MVVM architecture with LiveData and View Binding
- Single Activity architecture with Navigation Component
- Data storage using Data Store
- Coroutines for asynchronous programming
- Agile methodology

## Dependencies

- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Provides lifecycle-aware data observation.
- [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) - A library that helps implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer.
- [Data Store](https://developer.android.com/topic/libraries/architecture/datastore) - A data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.
- [MVVM](https://developer.android.com/jetpack/guide#recommended-app-arch) - A software architectural pattern that separates an application into three main components: Model, View, and ViewModel.
- [Single Activity Architecture](https://proandroiddev.com/the-real-reasons-to-use-a-single-activity-on-android-bacf97b966fe) - An architectural pattern where the entire app UI is hosted inside a single Activity.
- [Data Binding](https://developer.android.com/topic/libraries/data-binding) - A support library that allows binding of UI components to data sources.
- [Coroutines](https://developer.android.com/kotlin/coroutines) - A concurrency design pattern that you can use on Android to simplify code that executes asynchronously.
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - A dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.
- [Paypal Android SDK](https://developer.paypal.com/docs/business/mobile-sdk/get-started/) - A software development kit that enables businesses to accept payments from customers in a mobile app.

## Modules

The app has been divided into the following modules:

* **auth** - This module handles the authentication functionality of the app.
* **networking** - This module is responsible for handling the network operations, such as communicating with the API and parsing the response.
* **user** - This module manages the user's profile and settings.
* **navigation** - This module handles the navigation flow between different screens of the app.
* **entity** - This module contains the data classes that represent the objects used in the app.

## Additional Features

* **Multilanguage** - The app supports multiple languages to provide a better experience to users who speak different languages.
* **Asynchronous Web Requests** - The app makes use of coroutines to make asynchronous network requests to the server.
* **Dark Mode** - The app supports a dark mode option for users who prefer it.

## Screenshots

![image](https://user-images.githubusercontent.com/71185753/232261999-e020ab12-5a55-46a2-a5a5-b59ad3ec26dc.jpg)

## Contributing

Contributions are always welcome! If you'd like to contribute to this project, please fork the repository and create a pull request.

## Agile Methodology

The development of this app followed the Agile methodology, which emphasizes flexibility and continuous improvement throughout the development process. The Agile approach encourages collaboration between team members and stakeholders, and it prioritizes delivering working software that meets the needs of the end-users. This approach allowed us to build an app that meets the needs of the users while being adaptable to changing requirements and feedback.
