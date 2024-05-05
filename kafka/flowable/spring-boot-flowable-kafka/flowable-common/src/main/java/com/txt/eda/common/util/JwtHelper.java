package com.txt.eda.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtHelper {

    public static String getJWTFromToken(String bearerToken) {
        String jwtToken = bearerToken.replace("Bearer ", "");
        return jwtToken;
    }

}
