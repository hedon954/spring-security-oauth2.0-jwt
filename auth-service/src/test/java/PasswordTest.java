import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Hedon Wang
 * @create 2020-10-12 17:24
 */
public class PasswordTest {

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123456");
        System.out.println(encode);
    }
}
