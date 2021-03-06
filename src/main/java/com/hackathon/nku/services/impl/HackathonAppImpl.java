package com.hackathon.nku.services.impl;

import com.hackathon.nku.model.Email;
import com.hackathon.nku.services.api.IHackathonApp;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.*;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class HackathonAppImpl implements IHackathonApp {

    private Connection con;
    @Override
    public String getHackathonData() throws IOException{
        String apiURL = "https://s3rdf9bxgg.execute-api.us-east-2.amazonaws.com/deploy/all";
        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "UTF-8")
        );
        String jsonString = rd.lines().collect(Collectors.joining());
        return jsonString;
    }



    @Override
        public List<Email> getFlaggedEmails() {
        List<Email> emailList = new ArrayList<>();
        try{
                Statement statement = con.createStatement();
                String query = ("Select * from email_data");
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()){
                    Email email = new Email(rs.getString("sender"), Collections.singletonList(rs.getString("recipient")),
                            rs.getString("subject"), rs.getString("body"), rs.getDate("send_date"), rs.getString("attachment"));
                     emailList.add(email);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        return emailList;

        }


    @Override
    public void storeHackathonData() throws IOException{
        try{
            con = getConnection();
            Statement statement = con.createStatement();

            JSONObject obj = new JSONObject(getHackathonData());
            String pageName = obj.getJSONObject("data").getString("subject");

            JSONArray arr = obj.getJSONArray("data");
            for (int i = 0; i < arr.length(); i++) {
                String post_id = arr.getJSONObject(i).getString("subject");
                statement.executeUpdate("INSERT INTO email_data VALUES(" + post_id + " )");
            }
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("msql");
        String jdbcUrl = "jdbc:mysql:// gehackathon-theextras.cfer0hpjyt6u.us-east-2.rds.amazonaws.com:3306";
        return DriverManager.getConnection(jdbcUrl);
    }

    @Override
    public String getBioData(String id) {
        FileReader file = null;
        String bio = "";
        try {
            File pathFile = new File("src/main/java/com/hackathon/nku/services/bios/" + id + ".txt");
            file = new FileReader(pathFile.getCanonicalPath());
            BufferedReader reader = new BufferedReader(file);
            String line = reader.readLine();
            while(line != null) {
                bio += line + "\n";
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return bio;
    }

    @Override
    public String getImage(String id) {
        //This is where the image would be read in and stringified to send back image data.
        return "this is image data" + id;
    }

    @Override
    public String postImage(String id, InputStream input, String type) throws FileNotFoundException, IOException {
        OutputStream out = null;
        int read = 0;
        byte[] bytes = new byte[1024];
        out = new FileOutputStream(new File("src/main/java/com/hackathon/nku/services/images/" + id + "." + type));
        while((read = input.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
        return "Successful image post";
    }

    @Override
    public String postBio(String id, String bio) {
        File pathFile = new File("src/main/java/com/hackathon/nku/services/bios/" + id + ".txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathFile.getCanonicalPath()));
            writer.write(bio);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Successful bio post";
    }
}