/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.parse;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author fitok
 */
public class Tournament {
    public ArrayList<Match> getMatches() throws IOException{
       Elements currentTournaments=null;
        Elements previousTournaments=null;
        Elements futureTournaments=null;
   ArrayList<Match> matches = new ArrayList<Match>();
   //
        Document doc = Jsoup.connect("http://dota2.ru/matches/").get();
        try{
       for(int i=0;i<5;i+=2){
           if(doc.select("div.matches-list").get(0).children().get(i).html().equals("Прошедшие матчи")){
               previousTournaments = doc.select("div.matches-list").get(0).child(i+1).children();
           }
           if(doc.select("div.matches-list").get(0).children().get(i).html().equals("Текущие матчи")){
               currentTournaments = doc.select("div.matches-list").get(0).child(i+1).children();
           }
           if(doc.select("div.matches-list").get(0).children().get(i).html().equals("Будущие матчи")){
               futureTournaments = doc.select("div.matches-list").get(0).child(i+1).children();
           }
       }
        }
       catch(IndexOutOfBoundsException e){}
      
     if(currentTournaments!=null){
       for(int i=0;i<currentTournaments.size();i++){
            Match m = new Match();
        m.setStatus("Current match");
        m.setTournament(currentTournaments.get(i).child(0).child(0).text());
        m.setTeam1(currentTournaments.get(i).child(0).child(1).text());
        m.setTeam2(currentTournaments.get(i).child(0).child(3).text());        
        m.setDataScore(currentTournaments.get(i).child(0).child(2).text());    
        matches.add(m);
       }
   }   
     if(futureTournaments!=null){
       for(int i=0;i<futureTournaments.size();i++){
            Match m = new Match();
        m.setStatus("Future match");
        m.setTournament(futureTournaments.get(i).child(0).child(0).text());
        m.setTeam1(futureTournaments.get(i).child(0).child(1).text());
        m.setTeam2(futureTournaments.get(i).child(0).child(3).text());        
        m.setDataScore(futureTournaments.get(i).child(0).child(2).text());    
        matches.add(m);
       }
   }
    if(previousTournaments!=null){
   for(int i=0;i<previousTournaments.size();i++){
            Match m = new Match();
        m.setStatus("Previous match");
        m.setTournament(previousTournaments.get(i).child(0).child(0).text());
        m.setTeam1(previousTournaments.get(i).child(0).child(1).text());
        m.setTeam2(previousTournaments.get(i).child(0).child(3).text());
        try{
        m.setDataScore(previousTournaments.get(i).child(1).attr("data-score"));
        }
        catch(IndexOutOfBoundsException e){m.setDataScore("finished");}
        matches.add(m);
   
   }}
    
   
   

return matches;    
}
}