# League of Legends TFT Rank Checker

League of Legends TFT Rank Checker is an Android app that allows users to check the rank of League of Legends Teamfight Tactics (TFT) players. Users can search for players, view their ranks, and add them to favorites.

## Table of Contents

- [Description](#description)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [API Keys](#api-keys)
- [Contributing](#contributing)
- [License](#license)

## Description

The League of Legends TFT Rank Checker app provides an intuitive interface for users to quickly look up the ranks of their favorite TFT players. Users can search for players using their in-game names and view information such as rank, LP, and tier. Additionally, users can mark players as favorites for easy access.

## Features

- Search for TFT players by in-game name.
- View player ranks, LP, and tier.
- Add players to favorites for quick access.

## Getting Started

### Prerequisites

Before you begin, make sure you have the following:

- Android Studio installed on your computer.
- A valid League of Legends API key for accessing TFT player data.

### Installation

1. Clone this repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

## Usage

1. Open the app on your Android device.
2. Enter the in-game name of the TFT player you want to search for.
3. View the player's rank information.
4. Optionally, add the player to your favorites for quick access.

## API Keys

To make API requests to the League of Legends servers, you need to provide your own API key. You must replace the placeholder API keys in the `LolApi.java` and `TftApi.java` files with your actual API key.

```java
// LolApi.java
public class LolApi {
    private static final String API_KEY = "YOUR_LOL_API_KEY";
    // ...
}

// TftApi.java
public class TftApi {
    private static final String API_KEY = "YOUR_TFT_API_KEY";
    // ...
}
```

## Contributing

Contributions are welcome! If you find any issues or want to enhance the app, feel free to submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).

---
