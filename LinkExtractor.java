import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

import com.trigonic.jrobotx.RobotExclusion;

/**
 * Created by berda on 1/1/15.
 */
public class LinkExtractor {

    public static void main(String[] args){
        int MAX = 1000;
        RobotExclusion robotExclusion = new RobotExclusion();
        HashMap<String, String> inspectedMap = new HashMap<String, String>();
        HashMap<String, String> inspectionMap = new HashMap<String, String>();
        Elements imports;
        String url = "https://en.wikipedia.org/wiki/Main_Page";
        if (args.length > 1) {
            try {
                MAX = Integer.parseInt(args[0]);
                url = args[1];
            } catch (Exception ex) {
            }

            for (String str : args) {
                inspectionMap.putIfAbsent(str, "");
            }
            inspectionMap.remove(args[0]);
        }
        else {
            inspectionMap.putIfAbsent(url, "");
        }
        while (!inspectionMap.isEmpty() && inspectedMap.size() < MAX) {
            try {
                System.out.println("Inspected URL's size= " + inspectedMap.size());
                System.out.println("Inspection URL's size= " + inspectionMap.size());
                url = inspectionMap.keySet().iterator().next();
                System.out.println("The URL: " + url + " under inspection");
                inspectedMap.putIfAbsent(url, "");
                inspectionMap.remove(url);
                if (!robotExclusion.allows(new java.net.URL(url), "")) {
                    System.out.println("The URL:" + url + " not allowed");
                    continue;
                }
                Document doc = Jsoup.connect(url).get();
                imports = doc.select("link[href]");
                for (Element element : imports) {
                    String href = element.attr("abs:href");
                    if (!inspectedMap.containsKey(href))
                        inspectionMap.putIfAbsent(href, "");
                }
            } catch (IOException ex) {
                System.out.println("The URL:" + url + "was skipped");
                continue;
            }
        }
    }
}

