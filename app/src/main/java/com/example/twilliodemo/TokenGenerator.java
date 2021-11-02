package com.example.twilliodemo;

import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VideoGrant;

public class TokenGenerator {

  public static String generateToken() {
    // Required for all types of tokens
    String twilioAccountSid = "AC9929334a417720f86705175cab855c88";
    String twilioApiKey = "SK0d6a2825443b61210030e4a85fdbc92a";
    String twilioApiSecret = "DmSyIF8tSk88Kyh6pETVmLJvRP8i3698";


    String identity = "user";
    // Create Video grant
    VideoGrant grant = new VideoGrant();
    grant.setRoom("cool room");


    // Create access token
    AccessToken token = new AccessToken.Builder(
      twilioAccountSid,
      twilioApiKey,
      twilioApiSecret
    ).identity(identity).grant(grant).build();

    System.out.println(token.toJwt());
//    return "eyJjdHkiOiJ0d2lsaW8tZnBhO3Y9MSIsInR5cCI6IkpXVCIsImFsZyI6IkhTMjU2In0.eyJpc3MiOiJTSzBkNmEyODI1NDQzYjYxMjEwMDMwZTRhODVmZGJjOTJhIiwiZXhwIjoxNjM1Nzc2NjY5LCJncmFudHMiOnsiaWRlbnRpdHkiOiJhbGljZSIsInZpZGVvIjp7InJvb20iOiJEYWlseVN0YW5kdXAifX0sImp0aSI6IlNLMGQ2YTI4MjU0NDNiNjEyMTAwMzBlNGE4NWZkYmM5MmEtMTYzNTc3MzA1NiIsInN1YiI6IkFDOTkyOTMzNGE0MTc3MjBmODY3MDUxNzVjYWI4NTVjODgifQ.Bf2twVMsQ2WuuKdX7o1FebKVhlKsYpTp6q2wbUmID7E";
    return token.toJwt();

  }
}