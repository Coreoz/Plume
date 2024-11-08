package com.coreoz.plume.db.pagination;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PageTest {

    @Test
    public void should_map_users() {
        User user = new User(1L, "To fetch");
        Page<User> page = new Page<>(
            List.of(user),
            1,
            1,
            1,
            true
        );

        Page<String> userNames = page.map(User::name);

        Assertions.assertThat(userNames.items().stream().findFirst()).isNotEmpty();
        Assertions.assertThat(userNames.items().stream().findFirst().get()).contains("To fetch");
    }

    private record User(long id, String name) {
    }
}
