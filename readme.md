# 🎵 ArtistFinder

**ArtistFinder** is a Java-based console application that helps users discover new music artists based on their preferences for genre, tempo, mood, era, and region. It uses the OpenAI API to generate artist recommendations and the Spotify Web API to fetch artist metadata such as albums, top tracks, and related artists.

---

## 📚 Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Setup Instructions](#setup-instructions)
- [Usage](#usage)
- [Example Output](#example-output)
- [API References](#api-references)
- [Security Notice](#security-notice)
- [License](#license)

---

## ✅ Features

- 🎤 Prompt-based artist recommendation using OpenAI ChatGPT  
- 📀 Fetch albums, top tracks, and related artists from Spotify  
- 🔁 Interactive console interface  
- 🌍 Filters based on mood, genre, era, region, and tempo  

---

## 🛠 Technologies Used

- Java 8+  
- Spotify Web API  
- OpenAI GPT-3.5 API  
- `javax.json` for JSON parsing  
- Java HTTP (`java.net.HttpURLConnection`)  
- Standard Java I/O libraries  

---

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or later  
- Internet connection  
- Spotify API credentials  
- OpenAI API key  

---

## 🧰 Setup Instructions

1. **Clone or download this repository**

2. **Replace API keys in `App.java`:**

   ```java
   final static String CLIENT_ID = "your_spotify_client_id";
   final static String CLIENT_SECRET = "your_spotify_client_secret";
   String apiKey = "your_openai_api_key";
