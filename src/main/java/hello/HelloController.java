package hello;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloController {

    private Facebook facebook;
    private ConnectionRepository connectionRepository;

    public HelloController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }

    @GetMapping
    public String helloFacebook(Model model) {
        Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
        if (connection == null) {
            return "redirect:/connect/facebook";
        } else {
            String token = ""; //TODO token here
                    facebook = new FacebookTemplate(token);
            String[] fields = {"id", "name", "first_name", "email","birthday","hometown", "inspirational_people, work, sports, significant_other, interested_in, favorite_teams, education, devices, verified, religion, relationship_status, political, is_verified    , age_range"};

            User userProfile = facebook.fetchObject("me", User.class, fields);
            model.addAttribute("userProfile", userProfile);
            model.addAttribute("feeds", facebook.feedOperations().getFeed());
            model.addAttribute("albums", facebook.mediaOperations().getAlbums());
            model.addAttribute("games", facebook.likeOperations().getGames() );
           // model.addAttribute("pagesLiked", facebook.likeOperations().getPagesLiked());
            return "hello";
        }
    }
}
