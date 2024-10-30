package com.opensourceFramework.utils.mongoDb;

import com.google.gson.JsonArray;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.json.JsonObject;
import org.bson.types.ObjectId;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;

public class MongoDBService {
    private static final String CONNECTION_URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "testDB";
    private static final String COLLECTION_NAME = "users";

    private MongoCollection<Document> collection;
    private ObjectMapper objectMapper;

    public MongoDBService() {
        MongoClient mongoClient = MongoClients.create(CONNECTION_URI);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        collection = database.getCollection(COLLECTION_NAME);
        objectMapper = new ObjectMapper();
    }

    // Function to read all data from the collection and return it as JSON
    public List<String> getAllUsersAsJson() {
        List<String> jsonList = new ArrayList<>();
        for (Document doc : collection.find()) {
            try {
                jsonList.add(objectMapper.writeValueAsString(doc));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return jsonList;
    }

    // Function to get all data from the collection and return it as a list of User POJOs
    public List<User> getAllUsersAsPOJO() {
        List<User> userList = new ArrayList<>();
        for (Document doc : collection.find()) {
            User user = objectMapper.convertValue(doc, User.class);
            userList.add(user);
        }
        return userList;
    }

    // Function to insert a User into the collection
    public boolean insertUser(User user) {
        try {
            Document doc = new Document("name", user.getName())
                    .append("age", user.getAge());
            InsertOneResult result = collection.insertOne(doc);
            return result.getInsertedId() != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Generic function to get all documents in JSONArray format
    public JsonArray getAllDocumentsAsJSONArray() {
        JsonArray jsonArray = new JsonArray();
        for (Document doc : collection.find()) {
            JsonObject jsonObject = new JsonObject(doc.toJson());
            jsonArray.add(String.valueOf(jsonObject));
        }
        return jsonArray;
    }

    // Generic function to get a document by ID in JSONObject format
    public com.google.gson.JsonObject getDocumentByIdAsJSONObject(String id) {
        Document doc = collection.find(new Document("_id", new ObjectId(id))).first();
        return doc != null ? new com.google.gson.JsonObject() : null;
    }

    // Function to insert a document and return success status
    public boolean insertDocument(com.google.gson.JsonObject jsonObject) {
        try {
            Document doc = Document.parse(jsonObject.toString());
            InsertOneResult result = collection.insertOne(doc);
            return result.getInsertedId() != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        MongoDBService mongoDBService = new MongoDBService();

        // Insert a new user
        User newUser = new User("Alice", 30);
        boolean isInserted = mongoDBService.insertUser(newUser);
        System.out.println("User Inserted: " + isInserted);

        // Fetch all users as JSON
        List<String> jsonUsers = mongoDBService.getAllUsersAsJson();
        System.out.println("All Users (JSON): " + jsonUsers);

        // Fetch all users as POJO
        List<User> pojoUsers = mongoDBService.getAllUsersAsPOJO();
        System.out.println("All Users (POJO): " + pojoUsers);
    }
}
