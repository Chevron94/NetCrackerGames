package gamepub.console;

import gamepub.db.dao.implementation.*;
import gamepub.db.entity.*;
import gamepub.db.service.GameService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by roman on 10.12.15.
 */

public class Main {
    private static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {
        Main loader = new Main();
        String gameUrl = "http://api.steampowered.com/ISteamApps/GetAppList/v2"; //all games
        String newsUrl = "http://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/?appid="; //news by game
        JSONObject jsonObject = new JSONObject((new Main()).sendGet(gameUrl));
        JSONArray jsonArray = jsonObject.getJSONObject("applist").getJSONArray("apps");
        Game game;

        GameDaoImplementation gameDaoImplementation = new GameDaoImplementation();
        PlatformDaoImplementation platformDaoImplementation = new PlatformDaoImplementation();
        GamePlatformDaoImplementation gamePlatformDaoImplementation = new GamePlatformDaoImplementation();
        GameScreenshotDaoImplementation gameScreenshotDaoImplementation = new GameScreenshotDaoImplementation();
        GenreDaoImplementation genreDaoImplementation = new GenreDaoImplementation();
        GameGenreDaoImplementation gameGenreDaoImplementation = new GameGenreDaoImplementation();


        Platform platform = new Platform();
        platform.setName("Windows");
        platformDaoImplementation.create(platform);
        platform = new Platform();
        platform.setName("MAC-OS");
        platformDaoImplementation.create(platform);
        platform = new Platform();
        platform.setName("Linux");
        platformDaoImplementation.create(platform);


        for (int i = 0; i < jsonArray.length(); i++) {
            game = new Game();
            game.setName(jsonArray.getJSONObject(i).getString("name"));
            game.setSteamId(jsonArray.getJSONObject(i).getInt("appid"));
            game.setLinkToSteam("http://store.steampowered.com/app/" + game.getSteamId());
            game.setName(jsonArray.getJSONObject(i).getString("name"));
            JSONObject gameJSON;
            try {
                gameJSON = new JSONObject((new Main()).sendGet("http://store.steampowered.com/api/appdetails?appids=" + game.getSteamId()));
            } catch (Exception e) {
                System.out.println(i);
                e.printStackTrace();
                // Thread.sleep(1000);
                gameJSON = new JSONObject((new Main()).sendGet("http://store.steampowered.com/api/appdetails?appids=" + game.getSteamId()));
            }

            Boolean inStore = gameJSON.getJSONObject(String.valueOf(game.getSteamId())).getBoolean("success");
            if (inStore) {
                gameJSON = gameJSON.getJSONObject(String.valueOf(game.getSteamId())).getJSONObject("data");
                String type = gameJSON.getString("type");
                if (type.equals("game")) {
                    game.setDescription(gameJSON.getString("detailed_description"));
                    game.setPoster(gameJSON.getString("header_image"));
                    game = gameDaoImplementation.create(game);
                   //GENRES
                    try {
                        JSONArray genres = gameJSON.getJSONArray("genres");
                        Genre genre;
                        GameGenre gameGenre;
                        for (int j = 0; j < genres.length(); j++) {
                            String textGenre = genres.getJSONObject(j).getString("description");
                            genre = genreDaoImplementation.getGenreByName(textGenre);
                            if (genre == null) {
                                genre = new Genre();
                                genre.setName(textGenre);
                                genre = genreDaoImplementation.create(genre);
                            }
                            gameGenre = new GameGenre();
                            gameGenre.setGame(game);
                            gameGenre.setGenre(genre);
                            gameGenreDaoImplementation.create(gameGenre);
                        }
                    }catch (JSONException e){

                    }
                   //SCREENSHOTS
                    try {
                        JSONArray screens = gameJSON.getJSONArray("screenshots");
                        GameScreenshot gameScreenshot;
                        for (int j = 0; j < screens.length(); j++) {
                            gameScreenshot = new GameScreenshot();
                            gameScreenshot.setGame(game);
                            gameScreenshot.setLink(Jsoup.parse(screens.getJSONObject(j).getString("path_full")).text());
                            gameScreenshotDaoImplementation.create(gameScreenshot);
                        }
                    }catch (JSONException e){

                    }
                    //SYS REQ
                    GamePlatform gamePlatform;
                    String winReq;
                    String macReq;
                    String linuxReq;
                    try {

                        winReq = Jsoup.parse(gameJSON.getJSONObject("pc_requirements").getString("minimum")).text();
                    } catch (Exception e) {
                        winReq = null;
                    }
                    try {
                        macReq = Jsoup.parse(gameJSON.getJSONObject("mac_requirements").getString("minimum")).text();
                    } catch (Exception e) {
                        macReq = null;
                    }
                    try {
                        linuxReq = Jsoup.parse(gameJSON.getJSONObject("linux_requirements").getString("minimum")).text();
                    } catch (Exception e) {
                        linuxReq = null;
                    }
                    if (winReq != null) {
                        gamePlatform = new GamePlatform();
                        gamePlatform.setGame(game);
                        gamePlatform.setPlatform(platformDaoImplementation.getPlatformByName("Windows"));
                        gamePlatform.setSystemRequirements(winReq);
                        gamePlatformDaoImplementation.create(gamePlatform);
                    }

                    if (macReq != null) {
                        gamePlatform = new GamePlatform();
                        gamePlatform.setGame(game);
                        gamePlatform.setPlatform(platformDaoImplementation.getPlatformByName("MAC-OS"));
                        gamePlatform.setSystemRequirements(macReq);
                        gamePlatformDaoImplementation.create(gamePlatform);
                    }

                    if (linuxReq != null) {
                        gamePlatform = new GamePlatform();
                        gamePlatform.setGame(game);
                        gamePlatform.setPlatform(platformDaoImplementation.getPlatformByName("Linux"));
                        gamePlatform.setSystemRequirements(linuxReq);
                        gamePlatformDaoImplementation.create(gamePlatform);
                    }
                }
            }
            Thread.sleep(1000);

        }
      GameDaoImplementation gameService = new GameDaoImplementation();
        List<Game> games = gameService.findAll();
        NewsDaoImplementation newsDaoImplementation = new NewsDaoImplementation();
        News news;
        for (int i = 0; i < games.size(); i++) {
            game = games.get(i);
            Thread.sleep(1000);
            String json = loader.sendGet(newsUrl + game.getSteamId() + "\"");
            if (json != null) {
                jsonObject = new JSONObject(json);
                jsonArray = jsonObject.getJSONObject("appnews").getJSONArray("newsitems");
                for (int j = 0; j < jsonArray.length(); j++) {
                    news = new News();
                    news.setGame(game);
                    news.setName(jsonArray.getJSONObject(j).getString("title"));
                    news.setLink(jsonArray.getJSONObject(j).getString("url"));
                    news.setDate(new Date(jsonArray.getJSONObject(j).getLong("date") * 1000));
                    newsDaoImplementation.create(news);
                }
            }
        }
        System.out.println("Size = " + newsDaoImplementation.findAll().size());
        System.exit(0);
    }

    private String sendGet(String stringUrl) throws Exception {
        URL url = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = connection.getResponseCode();
        while (responseCode == 429) {
            Thread.sleep(100000);
            responseCode = connection.getResponseCode();
        }
        while (responseCode != 200){
            Thread.sleep(30000);
            responseCode = connection.getResponseCode();
        }
        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        //inputLine = input.toString();
        StringBuffer response = new StringBuffer();
        while ((inputLine = input.readLine()) != null) {
            response.append(inputLine);
        }
        input.close();
        return response.toString();
    }
}
