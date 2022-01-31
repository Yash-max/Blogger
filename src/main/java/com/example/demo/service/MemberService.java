package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.User;

@Service
public interface MemberService {
	void saveMember(User user);
	User findByName(String username);
}
