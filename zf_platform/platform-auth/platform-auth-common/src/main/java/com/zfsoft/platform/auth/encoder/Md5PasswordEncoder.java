package com.zfsoft.platform.auth.encoder;

import cn.hutool.crypto.SecureUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Md5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return SecureUtil.md5((String) charSequence);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        //传输加密 要进行解密
        return SecureUtil.md5((String) rawPassword).equals(encodedPassword);
    }


    public static void main(String[] args) {
        System.out.println(new Md5PasswordEncoder().encode("123"));
        System.out.println(new Md5PasswordEncoder().encode("123456"));
        System.out.println(new Md5PasswordEncoder().matches("123", "202cb962ac59075b964b07152d234b70"));
        System.out.println(new Md5PasswordEncoder().encode("gateway-client:123456"));
    }

}
