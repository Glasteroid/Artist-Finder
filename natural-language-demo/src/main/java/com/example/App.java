package com.example;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonArray;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class App {
    final static String SPOTIFY_API_URL = "https://api.spotify.com/v1/";
    final static String CLIENT_ID = "ee082a81e0e34ee68f5f8d134be241af";
    final static String CLIENT_SECRET = "75cd3753c96b40449910cb14fa36b059";
    final static String ACCESS_TOKEN = getSpotifyAccessToken();

    public static void main(String[] args) {
        System.out.println("Welcome to the ArtistFinder application!");
        System.out.println("The overall purpose of this application is to recommend new artists based on your interests.");
        System.out.println("Please enter some information about your interests.");
    
        Scanner keyboard = new Scanner(System.in); // Create a Scanner object
    
        try {
            String genre = promptUser("Genre (rock, pop, hip-hop): ", keyboard);
            String tempo = promptUser("Tempo (slow, fast, etc.): ", keyboard);
            String mood = promptUser("Mood/emotion (happy, sad, angry): ", keyboard);
            String era = promptUser("Era/decade: ", keyboard);
            String region = promptUser("Region/culture: ", keyboard);
    
            String input = "Find me three artists whose genre is " + genre + ", tempo is " + tempo + ", mood/emotion is " + mood + ", era/decade is " + era + ", and whose region/culture is " + region;
            String output = chatGPT(input);
    
            String[] artists = output.split("\\d+\\.\\s+");
            List<String> artistsList = getArtists(artists);
    
            int artistCount = 1;
            for (String artist : artistsList) {
                System.out.print("Artist " + artistCount + ": " + artist + ", ");
                artistCount++;
            }
            System.out.println();
    
            System.out.println("Would you like to know more about any of these artists (Y/N): ");
            // keyboard.next();
            char userResponse = keyboard.next().charAt(0);
            keyboard.nextLine(); // Consume newline character after reading char response
    
            if (getResponse(userResponse)) {
                System.out.println("Enter artist name: ");
                String artist = keyboard.nextLine(); // Read artist name using nextLine()
                String reqInfo = getInfo(keyboard);
                String artistInfo = getArtistInformation(artist, reqInfo);
                System.out.println("Requested information about the artist: " + artistInfo);
            } else {
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            keyboard.close(); 
        }
    }

    public static String promptUser(String message, Scanner scanner) {
        System.out.print(message);
        return scanner.next().toLowerCase();
    }

    public static String chatGPT(String message) throws IOException {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";
        String model = "gpt-3.5-turbo";

        URL obj = new URL(url);
        HttpURLConnection con = establishConnection(obj, "POST", apiKey);
        con.setRequestProperty("Content-Type", "application/json; utf-8");

        String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
        con.setDoOutput(true);
        try (OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream())) {
            writer.write(body);
            writer.flush();
        }

        StringBuffer response = getResponse(con);
        return extractContentFromResponse(response.toString());
    }

    public static String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content") + 11;
        int endMarker = response.indexOf("\"", startMarker);
        return response.substring(startMarker, endMarker).replace("\\n", "").trim();
    }

    public static List<String> getArtists(String[] artists) {
        List<String> list = new ArrayList<>();

        for (String s : artists) {
            if (s != null && !s.isEmpty()) {
                list.add(s);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).replaceAll("\\n", ""));
        }
        return list;
    }

    public static boolean getResponse(char response) {
        return Character.toLowerCase(response) == 'y';
    }

    public static String getArtistInformation(String artist, String request) {
        String info = "";
        try {
            String artistId = getArtistId(artist);
            if (artistId == null) {
                System.out.println("Artist ID not found for artist: " + artist);
                return "Artist ID not found";
            }
            
            String urlString = SPOTIFY_API_URL + "artists/" + artistId + "/";
            if (request.equals("albums")) {
                urlString += "albums";
            } else if (request.equals("top tracks")) {
                // Use a default market code, for example "US"
                urlString += "top-tracks?market=US";
            } else if (request.equals("related artists")) {
                urlString += "related-artists";
            } else {
                System.out.println("Invalid request type: " + request);
                return "Invalid request type";
            }
            
            System.out.println("Constructed URL: " + urlString);
            
            URL url = new URL(urlString);
            HttpURLConnection con = establishConnection(url, "GET", ACCESS_TOKEN);
            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuffer responseBuffer = getResponse(con);
                if (responseBuffer != null) {
                    String response = responseBuffer.toString();
                    info = parseJson(response, "name");
                } else {
                    System.out.println("Response buffer is null.");
                }
            } else {
                System.out.println("Failed to get artist information. Response code: " + responseCode);
                info = "Failed to get artist information. Response code: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }    
    
    public static String getInfo(Scanner keyboard) {
        System.out.println("What would you like to know about this specific artist (albums, top tracks, related artists): ");
        return keyboard.nextLine();
    }

    public static void processInfo(String artist, String info, HttpURLConnection con) {
        String artistId = getArtistId(artist);
        if (artistId == null) {
            System.out.println("Artist ID not found.");
            return;
        }
    
        String baseUrl = SPOTIFY_API_URL + "artists/" + artistId + "/";
    
        if (info.equals("albums"))
            baseUrl += "albums";
        else if (info.equals("top tracks"))
            baseUrl += "top-tracks";
        else if (info.equals("related artists"))
            baseUrl += "related-artists";
    
        try {
            URL url = new URL(baseUrl);
            con = establishConnection(url, "GET", ACCESS_TOKEN);
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuffer responseBuffer = getResponse(con);
                if (responseBuffer != null) {
                    String response = responseBuffer.toString();
                    String parsedJson = parseJson(response, "name");
                    System.out.println(parsedJson);

                } else {
                    System.out.println("Response buffer is null.");
                }
            } else {
                System.out.println("Failed to get artist information. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    public static String getArtistId(String artist) {
        String accessToken = getSpotifyAccessToken();
        if (accessToken == null) {
            return null; // Handle failure to get access token
        }

        try {
            String encodedArtist = URLEncoder.encode(artist, StandardCharsets.UTF_8.toString());
            String url = "https://api.spotify.com/v1/search?q=" + encodedArtist + "&type=artist";

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonObject jsonObject = Json.createReader(new StringReader(response.toString())).readObject();
                JsonObject artists = jsonObject.getJsonObject("artists");
                JsonArray items = artists.getJsonArray("items");

                if (items.size() > 0) {
                    JsonObject firstArtist = items.getJsonObject(0);
                    return firstArtist.getString("id");
                } else {
                    return null; // Handle case where no artist was found
                }
            } else {
                System.out.println("Failed to retrieve artist ID. Response code: " + responseCode);
                return null; // Handle HTTP error response
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle IOException
            return null;
        }
    }

    public static HttpURLConnection establishConnection(URL obj, String type, String key) throws IOException {
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(type);
        con.setRequestProperty("Authorization", "Bearer " + key);
        return con;
    }

    public static StringBuffer getResponse(HttpURLConnection con) throws IOException {
        StringBuffer response = new StringBuffer();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response;
    }

    public static String getSpotifyAccessToken() {
        String authUrl = "https://accounts.spotify.com/api/token";
        String accessToken = "";
    
        try {
            URL url = new URL(authUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setDoOutput(true);
    
            String body = "grant_type=client_credentials&client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8")
                    + "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8");
    
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
    
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuffer response = getResponse(con);
                try (JsonReader reader = Json.createReader(new StringReader(response.toString()))) {
                    JsonObject jsonObject = reader.readObject();
                    accessToken = jsonObject.getString("access_token");
                }
            } else {
                System.out.println("Failed to retrieve access token. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessToken;
    }
       
    public static String parseJson(String jsonString, String target) {
        StringBuilder names = new StringBuilder();
        
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            JsonObject jsonObject = jsonReader.readObject();
            
            JsonArray itemsArray = null;
            if (jsonObject.containsKey("items")) {
                itemsArray = jsonObject.getJsonArray("items");
            } else if (jsonObject.containsKey("tracks")) {
                itemsArray = jsonObject.getJsonArray("tracks");
            } else if (jsonObject.containsKey("artists")) {
                itemsArray = jsonObject.getJsonArray("artists");
            }
            
            if (itemsArray != null) {
                for (JsonObject item : itemsArray.getValuesAs(JsonObject.class)) {
                    String name = item.getString("name", null);
                    if (name != null) {
                        names.append(name).append(", ");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing JSON";
        }
        
        return names.length() > 0 ? names.toString() : "No items found";
    }    
}
