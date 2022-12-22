package com.amigoscode.customer;
//record is used to maintain immutability of data
public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email) {
}
