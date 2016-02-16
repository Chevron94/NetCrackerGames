package gamepub.scheduler;

/**
 * Created by roman on 24.01.16.
 */

import gamepub.db.dao.implementation.*;
import gamepub.db.entity.*;
import gamepub.db.service.GameService;
import gamepub.db.service.NewsService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.ejb.EJB;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class SchedulerJob implements Job {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GAME_URL = "http://api.steampowered.com/ISteamApps/GetAppList/v2"; //all games
    private static final String NEWS_URL = "http://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/?appid="; //news by game

    private String sendGet(String stringUrl) throws Exception {
        URL url = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = connection.getResponseCode();
        while (responseCode == 429) {
            Thread.sleep(10000);
            responseCode = connection.getResponseCode();
        }
        if (responseCode == 404){
            Thread.sleep(10000);
            responseCode = connection.getResponseCode();
            if (responseCode == 404){
                return null;
            }
        }

        while (responseCode != 200) {
            Thread.sleep(10000);
            responseCode = connection.getResponseCode();
            System.out.println(responseCode);
        }
        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = input.readLine()) != null) {
            response.append(inputLine);
        }
        input.close();
        return response.toString();
    }

    public List<Game> getNamesAndLinks(String platform) throws Exception {
        GameDaoImplementation gameDaoImplementation = new GameDaoImplementation();
        GamePlatformDaoImplementation gamePlatformDaoImplementation = new GamePlatformDaoImplementation();
        PlatformDaoImplementation platformDaoImplementation = new PlatformDaoImplementation();
        List<Game> result = new ArrayList<Game>();
        String baseLink = "http://www.metacritic.com";
        String link = "http://www.metacritic.com/browse/games/release-date/available/" + platform + "/date?page=";
        for (int page = 0; ; page++) {
            String res = sendGet(link + page);
            Document document = Jsoup.parse(res);
            Element element = document.select("li.game_product:nth-child(1) > div:nth-child(1) > div:nth-child(1) > a:nth-child(1)").first();
            if (element == null){
                Collections.reverse(result);
                return result;
            }
            int i = 1;
            while (element != null) {
                List<Game> tmp = gameDaoImplementation.getGamesByName(element.text());
                Game game;
                if (tmp!=null && tmp.size()>0){
                    game=tmp.get(0);
                    Platform platform1;
                    if (platform.equals("vita"))
                        platform1 = platformDaoImplementation.getPlatformByName("PS Vita");
                    else if (platform.equals("pc"))
                        platform1 = platformDaoImplementation.getPlatformByName("Windows");
                    else platform1 = platformDaoImplementation.getPlatformByName(platform.toUpperCase());
                    if (gamePlatformDaoImplementation.getGamePlatformByGameIdAndPlatformId(game.getId(),platform1.getId()) != null){
                        Collections.reverse(result);
                        return result;
                    }
                }
                game = new Game();
                game.setName(element.text());
                game.setLinkToSteam(baseLink + element.attr("href") + "/details");
                result.add(game);
                i++;
                element = document.select("li.game_product:nth-child(" + i + ") > div:nth-child(1) > div:nth-child(1) > a:nth-child(1)").first();
            }
        }
    }

    public List<Game> getFullInformation(List<Game> games, Platform platform) throws Exception {
        GamePlatformDaoImplementation gamePlatformDaoImplementation = new GamePlatformDaoImplementation();
        GameDaoImplementation gameDaoImplementation = new GameDaoImplementation();
        GameGenreDaoImplementation gameGenreDaoImplementation = new GameGenreDaoImplementation();
        GenreDaoImplementation genreDaoImplementation = new GenreDaoImplementation();
        GamePlatform gamePlatform;
        GameGenre gameGenre;
        for (int i = 0; i<games.size(); i++){
            try {
                Game g = games.get(i);
                gamePlatform = new GamePlatform();
                gamePlatform.setPlatform(platform);
                String result = sendGet(g.getLinkToSteam());
                Document document = Jsoup.parse(result);
                List<Game> tmp = gameDaoImplementation.getGamesByName(g.getName());
                if (tmp != null && tmp.size() > 0) {
                    gamePlatform.setGame(tmp.get(0));
                } else {
                    g.setPoster(document.select("img.product_image").attr("src").replace("-98", ""));
                    g.setDescription(document.select("div.summary_detail > span:nth-child(2)").text());
                    String genreText;
                    if (document.select("div.product_details:nth-child(4) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(4) > th:nth-child(1)").text().equals("Genre(s):")) {
                        genreText = document.select("div.product_details:nth-child(4) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(2)").get(0).text();
                    } else if (document.select("tr.alt:nth-child(3) > th:nth-child(1)").text().equals("Genre(s):")) {
                        genreText = document.select("tr.alt:nth-child(3) > td:nth-child(2)").get(0).text();
                    } else if (document.select("div.product_details:nth-child(4) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(3) > th:nth-child(1)").text().equals("Genre(s):")) {
                        genreText = document.select("div.product_details:nth-child(4) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(2)").get(0).text();
                    } else if (document.select("div.product_details:nth-child(4) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > th:nth-child(1)").text().equals("Genre(s):")) {
                        genreText = document.select("div.product_details:nth-child(4) > table:nth-child(1) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2)").get(0).text();
                    } else
                        genreText = document.select("tr.alt > td:nth-child(2)").get(0).text();
                    String[] genres = genreText.trim().split(" ");
                    for (int j = 0; j < genres.length; j++) {
                        Genre genre = genreDaoImplementation.getGenreByName(genres[j]);
                        if (genre == null) {
                            genre = new Genre();
                            genre.setName(genres[j]);
                            genre = genreDaoImplementation.create(genre);
                        }
                        g = gameDaoImplementation.create(g);
                        gamePlatform.setGame(g);
                        games.set(i, g);
                        gameGenre = new GameGenre();
                        gameGenre.setGame(g);
                        gameGenre.setGenre(genre);
                        gameGenreDaoImplementation.create(gameGenre);
                    }
                }
                //.release_data > span:nth-child(2)
                //.release_data > span:nth-child(2)
                String releaseDate = document.select(".release_data > span:nth-child(2)").text();
                String metascore = document.select(".metascore_wrap > a:nth-child(2) > div:nth-child(1) > span:nth-child(2)").text();
                gamePlatform.setReleaseDate(new Date(releaseDate));
                if (metascore.equals("")) {
                    gamePlatform.setMetacritic(0);
                } else {
                    gamePlatform.setMetacritic(Integer.parseInt(metascore));
                }
                gamePlatformDaoImplementation.create(gamePlatform);
            }catch (Exception e){

            }
        }
        System.out.println(platform.getName()+": "+games.size());
        return games;
    }

    public void initPlatforms() {
        PlatformDaoImplementation platformDaoImplementation = new PlatformDaoImplementation();

        Platform platform;
        if (platformDaoImplementation.getPlatformByName("PS4") == null) {
            platform = new Platform();
            platform.setName("PS4");
            platformDaoImplementation.create(platform);
        }
        if (platformDaoImplementation.getPlatformByName("PS3") == null) {
            platform = new Platform();
            platform.setName("PS3");
            platformDaoImplementation.create(platform);
        }
        if (platformDaoImplementation.getPlatformByName("PS Vita") == null) {
            platform = new Platform();
            platform.setName("PS Vita");
            platformDaoImplementation.create(platform);
        }

        if (platformDaoImplementation.getPlatformByName("Windows") == null) {
            platform = new Platform();
            platform.setName("Windows");
            platformDaoImplementation.create(platform);
        }

        if (platformDaoImplementation.getPlatformByName("Linux") == null) {
            platform = new Platform();
            platform.setName("Linux");
            platformDaoImplementation.create(platform);
        }

        if (platformDaoImplementation.getPlatformByName("MAC-OS") == null) {
            platform = new Platform();
            platform.setName("MAC-OS");
            platformDaoImplementation.create(platform);
        }
    }

    public void steamNews(Game game) throws Exception {
        JSONObject jsonObject;
        News news;
        NewsDaoImplementation newsDaoImplementation = new NewsDaoImplementation();
        JSONArray newsJson;
        String json = sendGet(NEWS_URL + game.getSteamId() + "\"");
        List<News> newses = newsDaoImplementation.getNewsByGameId(game.getId());
        if (json != null) {
            jsonObject = new JSONObject(json);
            newsJson = jsonObject.getJSONObject("appnews").getJSONArray("newsitems");
            if (newses == null || newses.size() == 0) {
                for (int j = 0; j < newsJson.length(); j++) {
                    news = new News();
                    news.setGame(game);
                    news.setName(newsJson.getJSONObject(j).getString("title"));
                    news.setLink(newsJson.getJSONObject(j).getString("url"));
                    news.setDate(new Date(newsJson.getJSONObject(j).getLong("date") * 1000));
                    newsDaoImplementation.create(news);
                }
            } else {
                int j = 0;
                while (!newses.get(0).getDate().equals(new Date(newsJson.getJSONObject(j).getLong("date") * 1000))) {
                    news = new News();
                    news.setGame(game);
                    news.setName(newsJson.getJSONObject(j).getString("title"));
                    news.setLink(newsJson.getJSONObject(j).getString("url"));
                    news.setDate(new Date(newsJson.getJSONObject(j).getLong("date") * 1000));
                    newsDaoImplementation.create(news);
                    j++;
                }
            }
        }
    }

    public void linkToSteam(List<Game> games) throws Exception {
        PlatformDaoImplementation platformDaoImplementation = new PlatformDaoImplementation();
        GamePlatformDaoImplementation gamePlatformDaoImplementation = new GamePlatformDaoImplementation();
        GameScreenshotDaoImplementation gameScreenshotDaoImplementation = new GameScreenshotDaoImplementation();
        GamePlatform gamePlatform;


        JSONObject jsonObject = new JSONObject(sendGet(GAME_URL));
        JSONArray jsonArray = jsonObject.getJSONObject("applist").getJSONArray("apps");
        HashMap<String, Integer> steam = new HashMap<String, Integer>();
        for (int i = 0; i < jsonArray.length(); i++) {
            steam.put(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getInt("appid"));
        }

        for (int i = 0; i < games.size(); i++) {
            Game g = games.get(i);
            if (g.getSteamId() != 0) {
                if (steam.get(g.getName()) != null) {
                    g.setSteamId(steam.get(g.getName()));
                    JSONObject gameJSON;
                    try {
                        gameJSON = new JSONObject(sendGet("http://store.steampowered.com/api/appdetails?appids=" + g.getSteamId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Thread.sleep(100);
                        gameJSON = new JSONObject(sendGet("http://store.steampowered.com/api/appdetails?appids=" + g.getSteamId()));
                    }
                    Boolean inStore = gameJSON.getJSONObject(String.valueOf(g.getSteamId())).getBoolean("success");
                    if (inStore) {
                        gameJSON = gameJSON.getJSONObject(String.valueOf(g.getSteamId())).getJSONObject("data");
                        g.setPoster(gameJSON.getString("header_image"));
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
                            gamePlatform = gamePlatformDaoImplementation.getGamePlatformByGameIdAndPlatformId(g.getId(), platformDaoImplementation.getPlatformByName("Windows").getId());
                            gamePlatform.setGame(g);
                            gamePlatform.setPlatform(platformDaoImplementation.getPlatformByName("Windows"));
                            gamePlatform.setSystemRequirements(winReq);
                            gamePlatformDaoImplementation.create(gamePlatform);
                        }

                        if (macReq != null) {
                            gamePlatform = new GamePlatform();
                            gamePlatform.setGame(g);
                            gamePlatform.setPlatform(platformDaoImplementation.getPlatformByName("MAC-OS"));
                            gamePlatform.setSystemRequirements(macReq);
                            gamePlatformDaoImplementation.create(gamePlatform);
                        }

                        if (linuxReq != null) {
                            gamePlatform = new GamePlatform();
                            gamePlatform.setGame(g);
                            gamePlatform.setPlatform(platformDaoImplementation.getPlatformByName("Linux"));
                            gamePlatform.setSystemRequirements(linuxReq);
                            gamePlatformDaoImplementation.create(gamePlatform);
                        }
                        steamNews(g);
                        try {
                            JSONArray screens = gameJSON.getJSONArray("screenshots");
                            GameScreenshot gameScreenshot;
                            for (int j = 0; j < screens.length(); j++) {
                                gameScreenshot = new GameScreenshot();
                                gameScreenshot.setGame(g);
                                gameScreenshot.setLink(Jsoup.parse(screens.getJSONObject(j).getString("path_full")).text());
                                gameScreenshotDaoImplementation.create(gameScreenshot);
                            }
                        } catch (JSONException e) {

                        }
                    }
                    Thread.sleep(1000);

                }
            }
        }

    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            PlatformDaoImplementation platformDaoImplementation = new PlatformDaoImplementation();
            initPlatforms();
            getFullInformation(getNamesAndLinks("vita"), platformDaoImplementation.getPlatformByName("PS Vita"));
            linkToSteam(getFullInformation(getNamesAndLinks("pc"), platformDaoImplementation.getPlatformByName("Windows")));
            getFullInformation(getNamesAndLinks("ps4"), platformDaoImplementation.getPlatformByName("PS4"));
            getFullInformation(getNamesAndLinks("ps3"), platformDaoImplementation.getPlatformByName("PS3"));
            //getFullInformation(getNamesAndLinks("vita"), platformDaoImplementation.getPlatformByName("PS Vita"));
        }catch (Exception e) {

        }
    }

}
