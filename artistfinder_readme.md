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
- [API Integration](#api-integration)
- [Application Architecture](#application-architecture)
- [Error Handling](#error-handling)
- [Security Notice](#security-notice)
- [Troubleshooting](#troubleshooting)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)
- [License](#license)

---

## ✅ Features
- 🎤 Prompt-based artist recommendation using OpenAI ChatGPT  
- 📀 Fetch albums, top tracks, and related artists from Spotify  
- 🔁 Interactive console interface  
- 🌍 Filters based on mood, genre, era, region, and tempo  
- 🎯 Personalized music discovery experience  
- 🔍 Detailed artist information retrieval  

---

## 🛠 Technologies Used
- **Java 8+** - Core programming language  
- **Spotify Web API** - Artist metadata and music data  
- **OpenAI GPT-3.5 API** - AI-powered artist recommendations  
- **`javax.json`** - JSON parsing and processing  
- **Java HTTP** (`java.net.HttpURLConnection`) - API communication  
- **Standard Java I/O libraries** - User input/output handling  

---

## 🚀 Getting Started

### Prerequisites
- ☕ Java Development Kit (JDK) 8 or later  
- 🌐 Internet connection  
- 🎵 Spotify API credentials (Client ID & Client Secret)  
- 🤖 OpenAI API key  
- 📦 `javax.json` library  

---

## 🧰 Setup Instructions

### 1. **Clone or download this repository**
```bash
git clone https://github.com/yourusername/artistfinder.git
cd artistfinder
```

### 2. **Replace API keys in `App.java`:**
```java
final static String CLIENT_ID = "your_spotify_client_id";
final static String CLIENT_SECRET = "your_spotify_client_secret";
String apiKey = "your_openai_api_key";
```

### 3. **Obtain API Credentials:**

#### 🎶 Spotify API Setup:
1. Go to [Spotify Developer Dashboard](https://developer.spotify.com/dashboard/)
2. Log in with your Spotify account
3. Click "Create an App"
4. Fill in app details and accept terms
5. Copy your **Client ID** and **Client Secret**

#### 🤖 OpenAI API Setup:
1. Visit [OpenAI Platform](https://platform.openai.com/)
2. Create an account or sign in
3. Navigate to **API Keys** section
4. Click "Create new secret key"
5. Copy your **API key**

### 4. **Add JSON Library:**
Download `javax.json` library or add to your classpath:
```bash
# Using Maven (add to pom.xml)
<dependency>
    <groupId>org.glassfish</groupId>
    <artifactId>javax.json</artifactId>
    <version>1.1.4</version>
</dependency>
```

### 5. **Compile and Run:**
```bash
# Compile
javac -cp "path/to/javax.json.jar" com/example/App.java

# Run
java -cp ".:path/to/javax.json.jar" com.example.App
```

---

## 📖 Usage

### Step-by-Step Process:

1. **🚀 Launch the Application**
   ```bash
   java -cp ".:javax.json.jar" com.example.App
   ```

2. **🎵 Enter Your Music Preferences:**
   - **Genre**: rock, pop, hip-hop, jazz, electronic, etc.
   - **Tempo**: slow, fast, medium, upbeat, etc.
   - **Mood**: happy, sad, energetic, calm, angry, etc.
   - **Era**: 1980s, 1990s, 2000s, 2010s, etc.
   - **Region**: american, british, korean, latin, etc.

3. **🎯 Receive AI-Generated Recommendations:**
   - ChatGPT analyzes your preferences
   - Returns 3 personalized artist suggestions

4. **🔍 Explore Artist Details (Optional):**
   - Choose any recommended artist
   - Get detailed information:
     - 💿 **Albums**: Complete discography
     - 🎵 **Top Tracks**: Most popular songs
     - 👥 **Related Artists**: Similar musicians

---

## 💡 Example Output

```bash
Welcome to the ArtistFinder application!
The overall purpose of this application is to recommend new artists based on your interests.
Please enter some information about your interests.

Genre (rock, pop, hip-hop): rock
Tempo (slow, fast, etc.): fast
Mood/emotion (happy, sad, angry): energetic
Era/decade: 2000s
Region/culture: american

Artist 1: Foo Fighters, Artist 2: Green Day, Artist 3: Linkin Park, 

Would you like to know more about any of these artists (Y/N): Y
Enter artist name: Foo Fighters
What would you like to know about this specific artist (albums, top tracks, related artists): top tracks

Requested information about the artist: Everlong, The Pretender, Learn to Fly, Best of You, My Hero, Times Like These, Monkey Wrench, All My Life, Walk, These Days
```

---

## 🔗 API Integration

### 🤖 OpenAI ChatGPT API
- **Endpoint**: `https://api.openai.com/v1/chat/completions`
- **Model**: `gpt-3.5-turbo`
- **Purpose**: Generate contextual artist recommendations
- **Input**: User preferences (genre, tempo, mood, era, region)
- **Output**: 3 artist suggestions

### 🎵 Spotify Web API
- **Authentication**: Client Credentials flow
- **Base URL**: `https://api.spotify.com/v1/`
- **Endpoints Used**:
  - `/search` - Find artists by name
  - `/artists/{id}` - Get artist details
  - `/artists/{id}/albums` - Get artist albums
  - `/artists/{id}/top-tracks` - Get popular tracks
  - `/artists/{id}/related-artists` - Get similar artists

---

## 🏗️ Application Architecture

### 🔧 Core Components

| Component | Description | Key Methods |
|-----------|-------------|-------------|
| **🎯 Main Controller** | User interaction flow | `main()`, `promptUser()` |
| **🤖 AI Integration** | ChatGPT API communication | `chatGPT()`, `extractContentFromResponse()` |
| **🎵 Spotify Service** | Spotify API operations | `getArtistId()`, `getArtistInformation()` |
| **🔐 Authentication** | API token management | `getSpotifyAccessToken()` |
| **📊 Data Processing** | JSON parsing & formatting | `parseJson()`, `getArtists()` |
| **🌐 HTTP Client** | Network communication | `establishConnection()`, `getResponse()` |

### 🔄 Application Flow
```
User Input → ChatGPT API → Artist Recommendations → User Selection → Spotify API → Artist Details → Display Results
```

---

## ⚠️ Error Handling

The application handles various error scenarios:

- **🔑 Authentication Failures**: Invalid API keys or expired tokens
- **🌐 Network Issues**: Connection timeouts and HTTP errors
- **🎵 Artist Not Found**: Invalid artist names or missing data
- **📊 JSON Parsing Errors**: Malformed API responses
- **⚡ Rate Limiting**: API usage limits exceeded

### Common Error Messages:
```bash
"Artist ID not found for artist: [name]"
"Failed to get artist information. Response code: [code]"
"Failed to retrieve access token. Response code: [code]"
"Error parsing JSON"
```

---

## 🔒 Security Notice

> ⚠️ **Important Security Warning**: The current implementation contains hardcoded API keys in the source code, which is **NOT recommended** for production use.

### 🛡️ Security Best Practices:
- **Environment Variables**: Store API keys in environment variables
- **Configuration Files**: Use external config files (excluded from version control)
- **Key Rotation**: Regularly rotate your API keys
- **Access Control**: Limit API key permissions and usage

### 🔧 Recommended Implementation:
```java
// Use environment variables
final static String CLIENT_ID = System.getenv("SPOTIFY_CLIENT_ID");
final static String CLIENT_SECRET = System.getenv("SPOTIFY_CLIENT_SECRET");
String apiKey = System.getenv("OPENAI_API_KEY");
```

---

## 🔧 Troubleshooting

### Common Issues & Solutions:

| Issue | Possible Causes | Solutions |
|-------|----------------|-----------|
| **"Artist ID not found"** | Misspelled artist name | Check spelling, try variations |
| **Authentication errors** | Invalid/expired API keys | Verify keys, regenerate if needed |
| **Network timeouts** | Poor internet connection | Check connection, retry request |
| **JSON parsing errors** | API response format changed | Update parsing logic |
| **Rate limit exceeded** | Too many API calls | Implement rate limiting, wait before retry |

### 🔍 Debugging Tips:
- Enable detailed logging for API responses
- Check HTTP response codes for error diagnosis
- Verify API endpoints are still valid
- Test with different artist names and genres

---

## 🚀 Future Enhancements

### 🎯 Planned Features:
- **🎨 GUI Interface**: Desktop application with visual interface
- **💾 Local Caching**: Store frequently accessed data locally
- **📱 Mobile Version**: Android/iOS companion app
- **🎵 Playlist Creation**: Generate Spotify playlists from recommendations
- **👤 User Profiles**: Save user preferences and history
- **🔄 Real-time Updates**: Live music trends integration
- **🌐 Multi-language Support**: Internationalization features
- **📊 Analytics Dashboard**: User interaction insights

### 🛠️ Technical Improvements:
- **🏗️ Modular Architecture**: Separate concerns into different classes
- **🧪 Unit Testing**: Comprehensive test coverage
- **📋 Configuration Management**: External configuration system
- **⚡ Performance Optimization**: Async API calls and caching
- **📝 Logging Framework**: Structured logging implementation

---

## 🤝 Contributing

We welcome contributions! Here's how you can help:

### 🔧 Development Setup:
1. **Fork** the repository
2. **Clone** your fork locally
3. **Create** a feature branch
4. **Make** your changes
5. **Test** thoroughly
6. **Submit** a pull request

### 📋 Contribution Guidelines:
- Follow Java coding standards
- Add appropriate comments and documentation
- Include unit tests for new features
- Update README if needed
- Ensure backward compatibility

### 🐛 Bug Reports:
- Use GitHub Issues
- Provide detailed reproduction steps
- Include error messages and logs
- Specify Java version and OS

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

### 📋 Third-Party APIs:
- **OpenAI API**: Subject to OpenAI's terms of service
- **Spotify Web API**: Subject to Spotify's terms of service

### 🎓 Educational Use:
This project is primarily intended for educational purposes. Please ensure compliance with all applicable API terms of service when using this application.

---

## 📞 Support & Contact

- **📧 Issues**: [GitHub Issues](https://github.com/yourusername/artistfinder/issues)
- **📖 Documentation**: Check this README and inline code comments
- **🌐 API Docs**: 
  - [OpenAI API Documentation](https://platform.openai.com/docs)
  - [Spotify Web API Documentation](https://developer.spotify.com/documentation/web-api)

---

<div align="center">

**Made with ❤️ for music lovers**

[⭐ Star this repo](https://github.com/yourusername/artistfinder) | [🐛 Report Bug](https://github.com/yourusername/artistfinder/issues) | [✨ Request Feature](https://github.com/yourusername/artistfinder/issues)

</div>