/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os.assistant;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.FlowLayout;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;

/**
 *
 * @author Daniel
 */
public class OSAssistant {

    private static HashMap<String, String> params = new HashMap<>();
    private static String src = getPageSource();
    private static String gamepackURL = getCurrentGamepackJar();
    private static final String WORLD_URL = "http://oldschool65.runescape.com/";
    private static Thread hookThread;

    private static void loadParams() throws Exception {
        Pattern pattern = Pattern.compile("<param name=\"([^\\s]+)\"\\s+value=\"([^>]*)\">");
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            params.put(matcher.group(1), matcher.group(2));
        }
    }

    private static String getCurrentGamepackJar() {

        Pattern pattern = Pattern.compile("archive=.+\\.jar");
        Matcher matcher = pattern.matcher(src);
        if (matcher.find()) {
            String x = WORLD_URL + src.substring(matcher.start(), matcher.end()).replace("archive=", "");
            System.out.println("Downloading " + x + "");
            return x;
        } else {
            return "";
        }
    }

    private static String getPageSource() {

        try (InputStream stream = new URL(WORLD_URL + "j1").openStream()) {
            return getString(stream);
        } catch (Exception e) {
            return "";
        }
    }

    private static String getString(InputStream is) {
        try {
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = is.read()) != -1) {
                sb.append((char) ch);
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        loadParams();
        final ClientWindow clientFrame = new ClientWindow();
        final Overlay overlay = new Overlay();
        final Applet clientApplet;
        final URLClassLoader loader = new URLClassLoader(new URL[]{new URL(gamepackURL)});
        clientApplet = (Applet) loader.loadClass("client").newInstance();
        clientApplet.setStub(new AppletStub() {

            @Override
            public boolean isActive() {
                return false;
            }

            @Override
            public URL getDocumentBase() {
                try {
                    return new URL(gamepackURL);
                } catch (MalformedURLException ex) {
                    return null;
                }
            }

            @Override
            public URL getCodeBase() {
                try {
                    return new URL(gamepackURL);
                } catch (MalformedURLException ex) {
                    return null;
                }
            }

            @Override
            public String getParameter(String name) {
                return params.get(name);
            }

            @Override
            public AppletContext getAppletContext() {
                return null;
            }

            @Override
            public void appletResize(int width, int height) {

            }
        });
        clientApplet.add(overlay);
        clientApplet.setLayout(null);
        clientApplet.init();
        clientApplet.start();
        clientApplet.setSize(800, 800);
        final SkillList skillList = new SkillList();
        skillList.setVisible(true);
        skillList.setSize(400, 800);
        clientFrame.setLayout(new FlowLayout());
        clientFrame.setExtendedState(clientFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        clientFrame.add(skillList);
        clientFrame.add(clientApplet);
        clientFrame.pack();
        clientFrame.setVisible(true);

        hookThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Player player = new Player(clientApplet);
                    int total = 0;
                    while (!hookThread.isInterrupted()) {
                        int diff = 0;
                        for(int i = 0; i < 23; i++) {
                            
                            diff += player.getXPDropOf(i);
                            total += diff;
                            
                        }
                        overlay.setXPDropValue(diff);
                        overlay.setTotalXP(total);
                        skillList.addSkillAll(player.getActiveSkills());
                        player.updatePlayerInformation();
                        Thread.sleep(600);
                        //skillList.clear();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        hookThread.start();

        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
