package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.WriterDao;
import com.example.demo.model.Writer;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private WriterDao writerDao;
		
	@Override
	public List<Writer> getAllWriters() {
		return writerDao.findAll();
	}

	@Override
	public void saveWriter(Writer writer) {
		writerDao.save(writer);
	}

	@Override
	public Writer getWriterByEmail(String email) {
		Writer writer = null;
		List<Writer> writers = getAllWriters();
		for(Writer curr: writers) {
			if(curr.getEmail().equals(email)) {
				writer = curr;
				break;
			}
		}
		return writer;
	}

	@Override
	public Writer getWriterById(Long id) {
		return writerDao.getById(id);
	}
	
}
