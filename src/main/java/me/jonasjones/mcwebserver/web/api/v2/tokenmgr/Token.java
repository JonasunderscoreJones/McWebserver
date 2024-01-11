package me.jonasjones.mcwebserver.web.api.v2.tokenmgr;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Token {
    private String name;
    private String tokenHash;
    private String tokenStart;
    private long expires;

    public Token(String name, String tokenHash, String tokenStart, long expires) {
        this.name = name;
        this.tokenHash = tokenHash;
        this.tokenStart = tokenStart;
        this.expires = expires;
    }
}
