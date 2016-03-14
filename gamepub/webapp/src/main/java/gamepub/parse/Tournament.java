/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamepub.parse;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author fitok
 */
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Singleton
public class Tournament {
     Elements currentTournaments;
        Elements previousTournaments;
        Elements futureTournaments;
        ArrayList<Match> matches;
        ArrayList<Match> csmatches;
        Document doc;
        Document doc2; 
        
    @PostConstruct
    public void setConnections(){
         
        
         try {  
             doc = Jsoup.connect("http://dota2.ru/matches/").get();
             doc2 = Jsoup.connect("http://game-tournaments.com/csgo/matches").get();
         } catch (IOException ex) {
             Logger.getLogger(Tournament.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    public ArrayList<Match> getMatches() {
      matches = new ArrayList<Match>();
  
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
       catch(Exception e){}
      
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
    public ArrayList<Match> getCsMatches(){
        csmatches = new ArrayList<Match>();
        try{
         for(int i=0;i<40;i++){
      Match csmatch = new Match();
      csmatch.setDataScore(doc2.select("td.mtime").get(i).select("span.live-in").text());
      csmatch.setTeam1(doc2.select("a.mlink").get(i).select("span.teamname.c1").text());
      csmatch.setTeam2(doc2.select("a.mlink").get(i).select("span.teamname.c2").text());
      csmatch.setTournament(doc2.select("td.tournament").get(i).select("a.ta.odtip").attr("title"));
      if(doc2.select("td.tournament").get(i).parents().get(4).select("h2").text().contains("Прошедшие матчи")){             
      csmatch.setStatus("Previous match");      
      }
      else {csmatch.setStatus("Future match");
      
      }
         
      csmatches.add(csmatch);
  }
        }catch(Exception e){}
         return csmatches;
    }
    
}