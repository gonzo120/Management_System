package com.inn.cafe.cominncafe.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.cafe.cominncafe.POJO.User;

public interface UserDao extends JpaRepository<User, Integer> {

}
