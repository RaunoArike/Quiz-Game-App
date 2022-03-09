
package server.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class LeaderboardController {

    @GetMapping("/api/leaderboard")
    public String getLeaderboard() {
        return null;
    }
}