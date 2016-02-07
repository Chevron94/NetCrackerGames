package gamepub.scheduler;

/**
 * Created by roman on 24.01.16.
 */

import gamepub.db.dao.implementation.GameDaoImplementation;
import gamepub.db.dao.implementation.NewsDaoImplementation;
import gamepub.db.entity.Game;
import gamepub.db.entity.News;
import gamepub.db.service.GameService;
import gamepub.db.service.NewsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.ejb.EJB;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class SchedulerJob implements Job {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GAME_URL = "http://api.steampowered.com/ISteamApps/GetAppList/v2"; //all games
    private static final String NEWS_URL = "http://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/?appid="; //news by game


    GameDaoImplementation gameService = new GameDaoImplementation();

    NewsDaoImplementation newsService = new NewsDaoImplementation();

    private String sendGet(String stringUrl) throws Exception {
        URL url = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = connection.getResponseCode();
        while (responseCode == 429) {
            Thread.sleep(300000);
            responseCode = connection.getResponseCode();
        }
        while (responseCode != 200) {
            Thread.sleep(300000);
            responseCode = connection.getResponseCode();
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

    public void execute(JobExecutionContext context) throws JobExecutionException {
        News news;
        JSONObject jsonObject;
        JSONArray jsonArray;

        List<Game> games = gameService.findAll();
        for (Game game : games) {
            try {
                String json = sendGet(NEWS_URL + game.getSteamId() + "/");
                List<News> newses = newsService.getNewsByGameId(game.getId());
                if (json != null) {
                    jsonObject = new JSONObject(json);
                    jsonArray = jsonObject.getJSONObject("appnews").getJSONArray("newsitems");
                    if (newses == null || newses.size() == 0) {
                        for (int j = 0; j < jsonArray.length(); j++) {
                            news = new News();
                            news.setGame(game);
                            news.setName(jsonArray.getJSONObject(j).getString("title"));
                            news.setLink(jsonArray.getJSONObject(j).getString("url"));
                            news.setDate(new Date(jsonArray.getJSONObject(j).getLong("date") * 1000));
                            newsService.create(news);
                        }
                    } else {
                        int j = 0;
                        while (!newses.get(0).getDate().equals(new Date(jsonArray.getJSONObject(j).getLong("date") * 1000))) {
                            news = new News();
                            news.setGame(game);
                            news.setName(jsonArray.getJSONObject(j).getString("title"));
                            news.setLink(jsonArray.getJSONObject(j).getString("url"));
                            news.setDate(new Date(jsonArray.getJSONObject(j).getLong("date") * 1000));
                            newsService.create(news);
                            j++;
                        }
                    }
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
