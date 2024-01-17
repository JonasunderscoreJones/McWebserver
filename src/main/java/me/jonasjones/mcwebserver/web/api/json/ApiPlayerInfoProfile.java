package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoProfile {
    private ApiPlayerInfoNames names;
    private int age;

    public ApiPlayerInfoProfile(ApiPlayerInfoNames names, int age) {
        this.names = names;
        this.age = age;
    }
}
