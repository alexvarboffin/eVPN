package com.walhalla.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.walhalla.lib.pojo.VCountry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyClass {


    //static String base = "C:\\Program Files\\OpenVPN Connect\\config";
    static String base = "C:\\Users\\combo\\Desktop\\Lucky Jet\\assets";


    public static void main(String[] args) {

        CountryPresenter cp = new CountryPresenter();
        File[] files = new File(base).listFiles(pathname -> pathname.getName().endsWith(".ovpn"));
        //prepareList(files, cp);
        generateJson(files, cp);
    }

    private static void prepareList(File[] files, CountryPresenter cp) {
        for (File file : files) {
            String part0 = file.getName().split("\\.")[0].replace("config_", "").toLowerCase();
            VCountry vc = cp.recognition(part0);
            System.out.println(vc);
        }
    }

    public static void loadVpnProfileFromAsset(Server profile, String configFileName) {

        //System.out.println("==============================================");

        //OpenVpnProfile profile = new OpenVpnProfile();
        try {
            InputStream inputStream = new FileInputStream(new File(configFileName));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                StringBuilder total_lines = new StringBuilder();
                String line;

                StringBuilder caCertBuilder = new StringBuilder();
                StringBuilder tlsAuthKeyBuilder = new StringBuilder();
                StringBuilder clientCertBuilder = new StringBuilder();
                StringBuilder privateKeyBuilder = new StringBuilder();

                String protocol = "";
                String remoteHost = "";
                int remotePort = -1;

                while ((line = reader.readLine()) != null) {

                    total_lines.append(line).append("\n");

                    if (line.startsWith("remote ")) {
                        String[] parts = line.split(" ");
                        remoteHost = parts[1];
                        remotePort = Integer.parseInt(parts[2]);

                        //@@    profile.setRemoteHost(remoteHost);
                        //@@    profile.setRemotePort(remotePort);
                        profile.setIp(remoteHost);

                    } else if (line.startsWith("proto ")) {
                        String[] parts = line.split(" ");
                        protocol = parts[1];
                        //@@    profile.setProtocol(protocol);
                    } else if (line.startsWith("auth-user-pass")) {
                        //@@    profile.setAuthUserPass(true);
                    } else if (line.startsWith("<ca>")) {
//                        line = reader.readLine();
//                        while (!line.equals("</ca>")) {
//                            caCertBuilder.append(line);
//                            line = reader.readLine();
//                        }
                        //@@     profile.setCaCertificate(caCertBuilder.toString());
                    } else if (line.startsWith("<tls-auth>")) {
//                        line = reader.readLine();
//                        while (!line.equals("</tls-auth>")) {
//                            tlsAuthKeyBuilder.append(line);
//                            line = reader.readLine();
//                        }
                        //@@    profile.setTlsAuthKey(tlsAuthKeyBuilder.toString());
                    } else if (line.startsWith("<cert>")) {
//                        line = reader.readLine();
//                        while (!line.equals("</cert>")) {
//                            clientCertBuilder.append(line);
//                            line = reader.readLine();
//                        }
                        //@@    profile.setClientCertificate(clientCertBuilder.toString());
                    } else if (line.startsWith("<key>")) {
//                        line = reader.readLine();
//                        while (!line.equals("</key>")) {
//                            privateKeyBuilder.append(line);
//                            line = reader.readLine();
//                        }
                        //@@  profile.setPrivateKey(privateKeyBuilder.toString());
                    }
                }

                //System.out.println("@@@@@@@@@@" + remoteHost + " " + remotePort + " " + protocol);
                String mm = total_lines.toString()
                        .replace("auth-user-pass", "#auth-user-pass")
                        //.replace("auth-user-pass", "auth-user-pass credentials.txt")

                        ;
                //System.out.println("-->" + mm + "<--");

                String b64response = Base64.getEncoder().encodeToString(mm.getBytes(Charset.forName("UTF-8")));
                profile.setConfigData(b64response);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void generateJson(File[] files, CountryPresenter cp) {
        Random random = new Random();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<Object, Object> map = new HashMap<>();
        List<Server> aa = new ArrayList<>();
        Map<String, List<Server>> total = new LinkedHashMap<>();

        int index = 0;

        for (File file : files) {

            ++index;

            String part0 = file.getName().split("\\.")[0].replace("config_", "").toLowerCase();
            //System.out.println("@@@" + part0);
            Server server = new Server("vpn" + String.format("%09d", random.nextInt(1000000000)));


            VCountry vc = cp.recognition(part0);


            server.id = index;
            server.setCountryLong(vc.countryLong);//capitalize(countryLong0)
            server.configFileName = file.getName();

            server.setCity(null);
            server.setRegionName(null);

            server.setPing("2");
            server.setScore("555");
            server.setSpeed("555");
            server.setUptime("555");
            server.setTotalUsers("555");
            server.setQuality(4);

            server.setCountryShort(vc.countryShort);


            //FROM OVPN FILE
            //"C:\\Program Files\\OpenVPN Connect\\config\\config_usa.ovpn"
            loadVpnProfileFromAsset(server, file.getAbsolutePath());
            //FROM OVPN FILE


            server.mUsername = "vpnuser219";
            server.mPassword = "vpnpassword219";

//            for (String code : codes) {
//                if (part0.startsWith(code)) {
//                    List<Server> mm = total.get(code);
//                    if (mm == null) {
//                        mm = new ArrayList<>();
//                        mm.add(server);
//                        total.put(code, mm);
//                    } else {
//                        mm.add(server);
//                        total.put(code, mm);
//                    }
//                    break;
//                }
//            }

            for (String code : cp.countryCodes) {
                if (server.getCountryShort().startsWith(code)) {
                    List<Server> mm = total.get(code);
                    if (mm == null) {
                        mm = new ArrayList<>();
                        mm.add(server);
                        total.put(code, mm);
                    } else {
                        mm.add(server);
                        total.put(code, mm);
                    }
                    break;
                }
            }
            if(server.getCountryShort().length()!=2){
                System.out.println(server.configFileName + " @@ " + server.getCountryShort()+" @@");
            }

            aa.add(server);
        }

        map.put("countries", cp.smms);
        map.put("servers", aa);

        String filePath = "C:\\Users\\combo\\Desktop\\Lucky Jet\\assets\\FirebaseDatabase.json";

        try {
            FileWriter writer = new FileWriter(filePath);
            String jsonString = gson.toJson(map);
            //System.out.println(gson.toJson(map));
            // Write JSON string to file
            writer.write(jsonString);

            // Close the writer
            writer.close();

            System.out.println("Data saved to file: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}