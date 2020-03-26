package xyz.lnews.covid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import xyz.lnews.covid.model.LocStats;
import xyz.lnews.covid.service.CovidServiceImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

@Controller
public class CovidController {

    @Autowired
    CovidServiceImpl covidService;

    @GetMapping("/")
    public String homePage(Model model){

        List<LocStats> allStats = covidService.getAllStats();
        allStats.sort(Comparator.comparing(LocStats::getLatestTotalCases));
        int totalReportedCases = allStats.stream().mapToInt(s->s.getLatestTotalCases()).sum();
        int totalLatestCases = allStats.stream().mapToInt(s->s.getDiffFromPrevDay()).sum();

        int lastDayCount = totalReportedCases-totalLatestCases;

        double percentChange = (1-1.0*(lastDayCount-totalLatestCases)/lastDayCount)*100;



        System.out.println(totalLatestCases);
        System.out.println(totalReportedCases);
        System.out.println(percentChange);

        model.addAttribute("title","Covid-19 viiruse haigestunute arv");
        model.addAttribute("totalReportedCases",totalReportedCases);
        model.addAttribute("totalLatestCases",totalLatestCases);
        model.addAttribute("percentChange",percentChange);
        model.addAttribute("locStats",allStats);

        return "homepage";
    }

}
