package com.lu.user.authentication.model.pojo;

import com.lu.user.authentication.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode
public class Users {
    private Integer totalCount;
    private List<User> users = new ArrayList<>();
}
