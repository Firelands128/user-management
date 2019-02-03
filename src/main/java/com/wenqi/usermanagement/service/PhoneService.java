/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service;

public interface PhoneService {
    void sendSMS(String token, String phone);
}
