package com.amazonaws.samples;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Movies")

public class MovieObjectMapper {
    private String name;
    private int year;
    private String rating;
    private String fans;

    @DynamoDBHashKey(attributeName="name")
    public String getName() { return name;}
    public void setName(String name) {this.name = name;}

    @DynamoDBAttribute(attributeName = "year")
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    @DynamoDBAttribute(attributeName = "rating")
    public String getRating() { return rating;}
    public void setRating(String rating) {this.rating = rating;}

    @DynamoDBAttribute(attributeName="fans")
    public String getFans() { return fans;}
    public void setFans(String fans) {this.fans = fans;}

}
