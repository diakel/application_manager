package ui;

import model.Application;
import model.ApplicationList;
import model.Requirement;

public class Main {
    public static void main(String[] args) {
        /*
        Application app1 = new Application("test1");
        app1.setDeadline("09-11-2022 11:59 PM");
        //System.out.println(app1.getDeadline());
        Requirement req1 = new Requirement("req1");
        //req1.uploadDocument("/Users/el/Desktop/FormulaSheet.pdf");
        //req1.uploadDocument("/Users/el/Desktop/schedule.png");
        //req1.openUploadedDocument();
        Requirement req2 = new Requirement("req1");
        Requirement req3 = new Requirement("req3");
        Application app2 = new Application("test2");
        app2.setDeadline("12-12-2022 11:00 AM");
        Application app3 = new Application("test3");
        app3.setDeadline("12-12-2022 10:30 AM");
        ApplicationList applist = new ApplicationList();
        applist.addApplication(app1);
        applist.addApplication(app2);
        applist.addApplication(app3);
        applist.sortByDeadlines();
        System.out.println(applist.getApplicationList()); */
        new ApplicationManager();
    }
}
