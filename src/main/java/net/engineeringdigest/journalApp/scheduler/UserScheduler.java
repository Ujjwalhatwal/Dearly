package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.repository.UserRepo;
import net.engineeringdigest.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AppCache appCache;

    // to schedule a particular task , cron : means time
//    @Scheduled(cron = "0 0 9 ? * SUN *")
//    public void fetchUserAndSendMail(){
//        List<User> users = userRepo.getUserForSA();
//        for(User user : users){
//            List<JournalEntry> journalEntries = user.getJournalEntries();
//            List<String> filteredEntries = journalEntries.stream()
//                    .filter(x -> x.getDate().isAfter(
//                            LocalDateTime.now().minusDays(7)))
//                    .map(JournalEntry::getContent)
//                    .collect(Collectors.toList());
//            String finalEntry = String.join(" ", filteredEntries);
//            String sentiment = sentimentAnalysisService.getSentiment(finalEntry);
//
//            emailService.sendMail(user.getEmail(),"Sentiment For This Week",sentiment);
//        }
//    }

//    @Scheduled(cron = "0 0 9 ? * SUN")
    public void fetchUserAndSendSentimentMail(){
        List<User> users = userRepo.findValidUsersForEmail();
        for(User user : users){
            List<JournalEntry> journalEntries = new java.util.ArrayList<>(user.getJournalEntries());
            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(x -> x.getDate() != null && x.getDate().isAfter(
                            LocalDateTime.now().minusDays(7)))
                    .map(JournalEntry::getSentiment).collect(Collectors.toList());

            Map<Sentiment,Integer> sentimentCount = new HashMap<>();
            for(Sentiment sentiment : sentiments){
                if(sentiment != null)
                    sentimentCount.put(sentiment,sentimentCount.getOrDefault(sentiment,0)+1);
            }
            Sentiment mostFreqSenti = null;
            int maxCount = 0;
            for(Map.Entry<Sentiment,Integer> entry : sentimentCount.entrySet()){
                if(entry.getValue() > maxCount){
                    maxCount = entry.getValue();
                    mostFreqSenti = entry.getKey();
                }
            }

            if(mostFreqSenti != null){
                emailService.sendMail(user.getEmail(),"Sentiments For This Week",mostFreqSenti.toString());
            }
        }
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void refreshAppCache(){
        appCache.refresh();
    }
}
