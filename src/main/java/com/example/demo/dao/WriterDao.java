package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Writer;

@Repository
public interface WriterDao extends JpaRepository<Writer, Long>{

}
