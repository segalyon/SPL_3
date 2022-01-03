package bgu.spl.net.api.Messages;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Login extends Message {
    //fields
    private String username;
    private String password;
    private String captcha;

    public Login(List<byte[]> words, byte[] san) {
        super((short) 2);
        username = new String(words.get(0), StandardCharsets.UTF_8);
        password = new String(words.get(1), StandardCharsets.UTF_8);
        captcha= new String(san, StandardCharsets.UTF_8);
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getCaptcha() {
        return captcha;
    }
}
