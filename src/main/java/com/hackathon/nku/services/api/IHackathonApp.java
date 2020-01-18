package com.hackathon.nku.services.api;
import com.hackathon.nku.model.Email;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IHackathonApp {

    void storeHackathonData() throws ClassNotFoundException;

    List<Email> getFlaggedEmails();

    String getHackathonData() throws IOException;

    String getBioData(String id);

    String getImage(String id);

    String postImage(String id, InputStream input, String type) throws FileNotFoundException;

    String postBio(String id, String bio);
}