package com.pluralsight.courseinfo.cli.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CourseRetrievalService {
    private static final String PS_URI = "https://app.pluralsight.com/profile/data/author/%s/all-content";

    private static final HttpClient CLIENT = HttpClient
            .newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    public static List<PluralsightCourse> getCoursesFor(String authorId){ // passé manuellement en static
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(PS_URI.formatted(authorId)))
                .GET()
                .build();
         try {
             HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
             return switch(response.statusCode()) {
                 case 200 -> null;
                 case 404 -> List.of();
                 default -> throw new RuntimeException("Pluralsight API call failed with status code " + response.statusCode());
             };
         } catch (IOException | InterruptedException e){
             throw new RuntimeException("Ne peut pas appeler Pluralsight API", e);
         }
    }
}
