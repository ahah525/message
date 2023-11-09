package hello.itemservice.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MessageSourceTest {
    @Autowired
    MessageSource ms;

    @Test
    void helloMessage() {
        String result = ms.getMessage("hello", null, null);
        assertThat(result).isEqualTo("안녕");
    }

    @DisplayName("메시지가 없는 경우에는 NoSuchMessageException 이 발생한다.")
    @Test
    void notFoundMessageCode() {
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @DisplayName("메시지가 없어도 기본 메시지( defaultMessage )를 사용하면 기본 메시지가 반환된다.")
    @Test
    void notFoundMessageCodeDefaultMessage() {
        String result = ms.getMessage("no_code", null, "기본 메시지", null);
        assertThat(result).isEqualTo("기본 메시지");
    }

    @DisplayName("메시지의 {0} 부분은 매개변수를 전달해서 치환할 수 있다.")
    @Test
    void argumentMessage() {
        // Object[]을 넘겨야 한다.
        String result = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        assertThat(result).isEqualTo("안녕 Spring");
    }

    @DisplayName("local이 null인 경우, Locale.getDefault() 값을 사용한다.")
    @Test
    void defaultLang() {
        // 시스템 기본 locale 이 ko_KR -> messages_ko.properties 조회 -> 없으면 messages.properties 조회
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
    }

    @DisplayName("Locale.KOREA인 경우, messages_ko.properties 조회한다.")
    @Test
    void koLang() {
        assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
    }

    @DisplayName("Locale.ENGLISH인 경우, messages_en.properties 조회한다.")
    @Test
    void enLang() {
        assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }
}