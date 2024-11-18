package com.coreoz.plume.db.pagination;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SliceTest {

    @Test
    public void should_map_users() {
        User user = new User(1L, "To fetch");
        Slice<User> page = new Slice<>(
            List.of(user),
            true
        );

        Slice<String> userNames = page.map(User::name);

        Assertions.assertThat(userNames.items().stream().findFirst()).isNotEmpty();
        Assertions.assertThat(userNames.items().stream().findFirst().get()).contains("To fetch");
    }

    private record User(long id, String name) {
    }
}
