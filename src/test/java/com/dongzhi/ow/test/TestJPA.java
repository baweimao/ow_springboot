package com.dongzhi.ow.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dongzhi.ow.Application;
import com.dongzhi.ow.pojo.Category;
import com.dongzhi.ow.service.CategoryService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestJPA {

	@Autowired
	CategoryService categoryService;
	
	@Test
	public void test(){
		List<Category> beans = categoryService.list();
		for(Category c:beans) {
			System.out.println(c.getId());
		}
	}
}
