package com.xy.DayTenGoods.util.okhttp.cookie.store;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

import java.util.List;

public interface CookieStore
{

    public void add(HttpUrl uri, List<Cookie> cookie);

    public List<Cookie> get(HttpUrl uri);

    public List<Cookie> getCookies();

    public boolean remove(HttpUrl uri, Cookie cookie);

    public boolean removeAll();


}
