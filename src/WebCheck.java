
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class WebCheck extends Frame {

    static WebCheck app;
    TextField website;
    TextArea good_url, broken_url;
    PageProbe pageProbe;

    public static void main(String args[]) {
        app = new WebCheck();
        app.setBounds(80, 80, 800, 700);
        app.show();
    }

    WebCheck() {

        super("Web Site Broken Link Handler");
        setBounds(80, 80, 800, 700);
        setFont(new Font("TimesRoman", Font.BOLD, 16));
        setup_menu();
        setup_component();
        addWindowListener(new whandler());
    }

    public void setup_menu() {
        MenuBar mb;
        Menu m;
        MenuItem mi;
        mb = new MenuBar();
        m = new Menu("File\t\t");
        mi = new MenuItem("Open");
        mi.addActionListener(new handler());
        m.add(mi);
        mi = new MenuItem("Save");
        mi.addActionListener(new handler());
        m.add(mi);
        m.addSeparator();
        mi = new MenuItem("Exit");
        mi.addActionListener(new handler());
        m.add(mi);
        mb.add(m);
        m = new Menu("Edit\t\t");
        mi = new MenuItem("Clear");
        mi.addActionListener(new handler());
        m.add(mi);
        mb.add(m);
        m = new Menu("Action");

        mi = new MenuItem("Check");
        mi.addActionListener(new handler());
        m.add(mi);
        mi = new MenuItem("Cancel");
        mi.addActionListener(new handler());
        m.add(mi);
        mb.add(m);
        setMenuBar(mb);
        setVisible(true);
    }

    public void setup_component() {
//Set up buttons
        Button check = new Button("Check");
        Button cancel = new Button("Cancel");
        Button clear = new Button("Clear");
        check.setActionCommand("Check");
        cancel.setActionCommand("Cancel");
        clear.setActionCommand("Clear");
        check.addActionListener(new handler());
        cancel.addActionListener(new handler());
        clear.addActionListener(new handler());
        check.setBounds(190, 630, 120, 40);
        cancel.setBounds(330, 630, 120, 40);
        clear.setBounds(470, 630, 120, 40);
        add(check);
        add(cancel);
        add(clear);
//Set up labels
        Label site = new Label("Web Site", Label.LEFT);
        Label good = new Label("Good Links", Label.LEFT);
        Label broken = new Label("Broken Links", Label.LEFT);
        site.setBounds(50, 80, 80, 20);
        good.setBounds(50, 160, 440, 20);
        broken.setBounds(520, 160, 440, 20);
        add(site);
        add(good);
        add(broken);
//Set up textfield and textareas
        website = new TextField("http://");
        good_url = new TextArea(20, 20);
        broken_url = new TextArea(20, 10);
        website.setBounds(50, 100, 600, 40);
        good_url.setBounds(50, 180, 340, 430);
        broken_url.setBounds(420, 180, 340, 430);
        add(website);
        add(good_url);
        add(broken_url);
        add(new Label(""));
    }

    class whandler extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

    private class handler implements ActionListener, ItemListener {

        public void actionPerformed(ActionEvent e) {
            String cmd;
            int pos;
            cmd = e.getActionCommand();
            if (cmd.equals("Open")) {
                read_file();
            } else if (cmd.equals("Save")) {
                save_file();
            } else if (cmd.equals("Clear")) {
                clear();
            } else if (cmd.equals("Exit")) {
                System.exit(0);
            } else if (cmd.equals("Check")) {
                pageProbe = new PageProbe();
                pageProbe.start();
            } else if (cmd.equals("Cancel")) {
                pageProbe.suspend();
            }
        }

        public void itemStateChanged(ItemEvent e) {
        }
    }

    public void read_file() {
        String dir = null, fn = null;
        String inLine;
        try {
            clear();
            FileDialog flg = new FileDialog(app, "Open a file", FileDialog.LOAD);
            flg.show();
            dir = flg.getDirectory();
            fn = flg.getFile();
            if (fn != null) {
                try {
                    BufferedReader inStream = new BufferedReader(new FileReader(dir + fn));
                    website.setText(inStream.readLine());
                    if (!inStream.readLine().equals("Good Links------")) {
                        return;
                    }

                    while (!(inLine = inStream.readLine()).equals("Broken Links------")) {
                        good_url.append(inLine + "\n");
                    }
                    while ((inLine = inStream.readLine()) != null) {
                        broken_url.append(inLine + "\n");
                    }
                } catch (IOException ex) {
                    System.out.println("Error reading " + dir + fn);
                }
            }
        } catch (NullPointerException np) {
            System.out.println("NullPointerException Error: " + np);
        }
    }

    public void save_file() {
        String dir = null, fn = null, inLine;
        try {
            FileDialog dlg = new FileDialog(app, "Save to File", FileDialog.SAVE);
            dlg.show();
            dir = dlg.getDirectory();
            fn = dlg.getFile();
            try {
                File outFile = new File(dir + fn);
                PrintWriter outStream = new PrintWriter(new FileWriter(outFile), true);
                inLine = website.getText();
                outStream.println(inLine);
                outStream.println("Good Links------");
                inLine = good_url.getText();
                outStream.print(inLine);
                outStream.println("Broken Links------");
                inLine = broken_url.getText();
                outStream.print(inLine);
                outStream.close();
            } catch (IOException ex) {
                System.out.println("IOException Error: " + ex);
            }
        } catch (NullPointerException np) {
            System.out.println("NullPointerException Error: " + np);
        }
    }

    public void clear() {
        website.setText("");
        good_url.setText("");
        broken_url.setText("");
    }
//---------------------------------------------------------------------

//Class URLConnectionReader
    private class URLConnectionReader {

        private String m_currUrl;

        public URLConnectionReader(String currUrl) {
            m_currUrl = currUrl;
        }

        public int test() {
//For situation of http:/HTML
            if (m_currUrl.length() < 12) {
                return 1;
            }
            try {
                URL page = new URL(m_currUrl);
                BufferedReader dis = new BufferedReader(
                        new InputStreamReader(page.openStream()));
                dis.close();
                return 0;
            } catch (IOException me) {
                System.out.println(me);
                return 1;
            }
        }
    }

//---------------------------------------------------------------------
//Class PageProbe
    private class PageProbe extends Thread {

        private String M_StartURL;
        private String M_BaseURL;
        private String url;

        public PageProbe() {
            //To check connection is available or not
            try {
                URL url2 = new URL("http://www.google.com");
                System.out.println(url2.getHost());
                HttpURLConnection con = (HttpURLConnection) url2.openConnection();
                con.connect();
            } catch (Exception exception) {
                System.out.println("No Connection");
                JOptionPane.showMessageDialog(app, "No Connection");
                good_url.setText("");
                broken_url.setText("");
                pageProbe.suspend();
            }
            url = new String(website.getText());
            if (url.length() == 0) {
//Default URL is "http://www.cse.unl.edu"
                M_StartURL = new String("http://www.cse.unl.edu/");
                M_BaseURL = new String("cse.unl.edu");

                website.setText("http://www.cse.unl.edu/");
            } else {
                int i;
                String tmpstr;
                M_StartURL = new String(url);
                if (url.endsWith("/")) {
                    tmpstr = new String(url);
                } else if (url.endsWith("html") || url.endsWith("HTML")
                        || url.endsWith("htm") || url.endsWith("HTM")) {
                    i = url.lastIndexOf("/");
                    tmpstr = new String(url.substring(0, i));
                } else {
                    tmpstr = new String(url + "/");
                }
                i = tmpstr.lastIndexOf("/");
                if (tmpstr.indexOf("www") != -1) {
                    M_BaseURL = new String(tmpstr.substring(11, i));
                } else {
                    M_BaseURL = new String(tmpstr.substring(7, i));
                }
            }
        }

        private String MakeAbsoluteUrl(String baseUrl, String relativeUrl) {
            int slashBase = baseUrl.lastIndexOf('/');
            int slashRelative = relativeUrl.indexOf('/');
            String absUrl;
            if (relativeUrl.startsWith("http://")) {
                if (relativeUrl.indexOf(baseUrl) != -1) {
                    absUrl = new String(relativeUrl);
                } else {
                    absUrl = new String(baseUrl + relativeUrl.substring(7));
                }
            } else {
                if (slashBase == 6) {
                    baseUrl = new String(baseUrl + "/");
                    slashBase = baseUrl.lastIndexOf('/');
                }
                if (slashRelative != 0) // need slash for the baseUrl
                {
                    absUrl = new String(baseUrl.substring(0, slashBase + 1) + relativeUrl);
                } else // don't need the slash for the baseUrl
                {
                    absUrl = new String(baseUrl.substring(0, slashBase) + relativeUrl);
                }

                if (slashRelative != 0 && relativeUrl.startsWith("http:")) {
                    absUrl = new String(relativeUrl);
                }
            }
//System.out.println(baseUrl+" "+relativeUrl+" "+absUrl);
            return absUrl;
        }

        public boolean IsLinkAbsolute(String linkUrl) {
            if (linkUrl.startsWith("http://")) {
                return true;
            } else {
                return false;
            }
        }

        public void run() {
            Vector VisitedVector = new Vector();
            Vector TestedVector = new Vector();
            RecurFindBrokenLinks(M_StartURL, VisitedVector, TestedVector);
            return;
        }

        public void RecurFindBrokenLinks(String url, Vector VisitedVector, Vector TestedVector) {
            Vector brokenUrlVector = new Vector(10, 10);
            Vector OkUrlVector = new Vector(10, 10);
            int pos1, pos2, pos;
            String substr, NewURL;
// if the url has been visited, do nothing
            if (VisitedVector.contains(url)) {
                return;
            } else {
                VisitedVector.addElement(url.trim());
                TestedVector.addElement(url.trim());
                System.out.println("Visiting URL: " + url);
            }
            try {

                URL yahoo = new URL(url);
                BufferedReader dis = new BufferedReader(
                        new InputStreamReader(yahoo.openStream()));
                String inputLine;
                while ((inputLine = dis.readLine()) != null) {
                    yield();
//System.out.println(inputLine);
                    pos = 10;

                    substr = new String(inputLine);
                    while (pos > 0) {
                        pos1 = substr.indexOf("href");
                        pos2 = substr.indexOf("HREF");
                        if (pos1 != -1 || pos2 != -1) {
                            if (pos1 == -1) {
                                pos = pos2;
                            } else if (pos2 == -1) {
                                pos = pos1;
                            } else if (pos1 < pos2) {
                                pos = pos1;
                            } else {
                                pos = pos2;
                            }
                            substr = substr.substring(pos + 4);
                            pos = substr.indexOf("\"");
                            substr = substr.substring(pos + 1);
                            pos = substr.indexOf("\"");
                            if (pos != -1) {
                                NewURL = new String(substr.substring(0, pos).trim());
                                substr = substr.substring(pos + 1);
                                System.out.println("New URL: " + NewURL);
//Do not test email address
                                if (!IsLinkAbsolute(NewURL)) {
                                    NewURL = MakeAbsoluteUrl(url,
                                            NewURL);
                                }
                                if (NewURL.indexOf("mailto:") == -1 && NewURL.indexOf("ftp:") == -1 && NewURL.indexOf("Mailto:") == -1 && !TestedVector.contains(NewURL)) {
                                    int testValue;
                                    URLConnectionReader testReader = new URLConnectionReader(NewURL);
                                    testValue = testReader.test();
                                    NewURL = new String(NewURL.trim());
                                    TestedVector.addElement(NewURL);
                                    if (testValue != 0) {
                                        brokenUrlVector.addElement(NewURL);
                                    } else {
                                        OkUrlVector.addElement(NewURL);
//System.out.println(testValue);
//System.out.println("1111111111111 "+NewURL);
                                    }
                                }
                            }
                        } else {
                            pos = 0;
                        }
                    }
                }
                dis.close();
                if (brokenUrlVector.size() > 0) {
                    System.out.println("Broken Links---");
                    broken_url.append("In " + url + "\n");
                }
                for (int i = 0; i < brokenUrlVector.size(); i++) {
                    System.out.println("\t" + brokenUrlVector.elementAt(i));
                    broken_url.append("\t" + brokenUrlVector.elementAt(i) + "\n");
                }
//Recursively call each URL in okUrlVector
                if (OkUrlVector.size() > 0) {
                    System.out.println("\nGood links---");
                    good_url.append("In " + url + "\n");
                }
                for (int i = 0; i < OkUrlVector.size(); i++) {
                    String linkUrl = (String) (OkUrlVector.elementAt(i));
                    System.out.println("\t" + linkUrl);
                    good_url.append("\t" + linkUrl + "\n");
                }
                for (int i = 0; i < OkUrlVector.size(); i++) {
                    String linkUrl = (String) (OkUrlVector.elementAt(i));
                    if (linkUrl.indexOf(M_BaseURL) != -1) {
                        RecurFindBrokenLinks(linkUrl, VisitedVector,
                                TestedVector);
                    }
                }
                return;
            } catch (IOException ioe) {
                System.out.println("IOException: " + ioe.toString());

            }
        }
    }
}
