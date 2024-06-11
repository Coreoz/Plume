package com.coreoz.plume.db.querydsl.pagination;

import com.coreoz.plume.db.querydsl.db.User;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PageTest {

    @Test
    public void should_map_users() {
        User user = new User();
        user.setId(0L);
        user.setName("To fetch");
        Page<User> page = new Page<>(
            List.of(user),
            1,
            1,
            1,
            1,
            true
        );

        Page<String> userNames = page.mapElements(User::getName);

        assertThat(userNames.getElements().stream().findFirst()).isNotEmpty();
        assertThat(userNames.getElements().stream().findFirst().get()).contains("To fetch");
    }
}
