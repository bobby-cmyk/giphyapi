package vttp.batch5.ssf.day16.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vttp.batch5.ssf.day16.services.GiphyService;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private GiphyService giphySvc;
    
    @GetMapping
    public ModelAndView search(
        @RequestParam("query") String query,
        @RequestParam("limit") String limit,
        @RequestParam("rating") String rating)
        {

        ModelAndView mav = new ModelAndView();

        System.out.printf("q = %s, limit = %s, rating = %s\n", query, limit, rating);
        
        List<String> imagesUrl = giphySvc.search(query, limit, rating);

        mav.setViewName("search_results");
        mav.addObject("imagesUrl", imagesUrl);
        mav.addObject("query", query);
        mav.addObject("limit", limit);
        mav.setStatus(HttpStatusCode.valueOf(200));

        return mav;
    }
}
