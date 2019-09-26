package hello.controller;

import hello.model.ReportClaim;
import hello.service.ReportsComparer;
import hello.utils.CSVUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ReportsComparer reportsComparerService;

    public MainController(ReportsComparer reportsComparer) {
        this.reportsComparerService = reportsComparer;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("compare")
    public ModelAndView compareReports() throws SQLException {
        List<ReportClaim> lostAttachments = lostAttachments();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("compare");
        return mav;
    }

    @ModelAttribute("lostAttachments")
    public List<ReportClaim> lostAttachments() {
        List<ReportClaim> lostAttachments = reportsComparerService.executeComparison();
        return lostAttachments;
    }

    @RequestMapping("saveToFile")
    private ModelAndView saveResult(@ModelAttribute("lostAttachments") List<ReportClaim> attachments) {
        CSVUtils.saveObjectList(attachments, "output");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("export");
        return mav;
    }
}
