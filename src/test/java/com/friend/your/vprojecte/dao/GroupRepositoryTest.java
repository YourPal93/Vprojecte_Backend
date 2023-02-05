package com.friend.your.vprojecte.dao;

import com.friend.your.vprojecte.dto.GroupDto;
import com.friend.your.vprojecte.entity.Group;
import com.friend.your.vprojecte.test_util.TestGroupUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application-h2.properties")
class GroupRepositoryTest {

    @Autowired
    private GroupRepository underTest;

    @Test
    void findAllGroupDtos_GivenValidPageNoAndValidPageSize_ShouldReturnPageOfGroupDtos() {
        List<Group> testGroups = TestGroupUtil.getTestGroupMultipleNoId(10);

        underTest.saveAll(testGroups);

        Page<GroupDto> savedGroupDtos = underTest.findAllGroupDto(PageRequest.of(0, 10));

        assertEquals(savedGroupDtos.getTotalElements(), 10);
    }
}