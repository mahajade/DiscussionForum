package ca.sheridancollege.mahajade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import ca.sheridancollege.mahajade.beans.Post;
import ca.sheridancollege.mahajade.beans.Reply;
import ca.sheridancollege.mahajade.database.DatabaseAccess;

@SpringBootTest
@AutoConfigureMockMvc
public class TestController {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private DatabaseAccess da;
	
	@Test
	public void deletePost() {
		Post post = new Post();
		post.setId(Long.valueOf(1));
		ArrayList<Post> list = da.getPosts();
		int initialSize = list.size();
		da.deletePost(post.getId().intValue());
		list = da.getPosts();
		int finalSize = list.size();
		assertThat(finalSize).isEqualTo(initialSize-1);
	}
	
	@Test
	public void addPost() {
		Post post = new Post("employee1","Hi",LocalDate.now(),LocalTime.now());
		ArrayList<Post> list = da.getPosts();
		int initialSize = list.size();
		da.addPost(post);
		list = da.getPosts();
		int finalSize = list.size();
		assertThat(finalSize).isEqualTo(initialSize+1);
	}
	
	@Test
	public void addReply() {
		Post post = new Post(Long.valueOf(1),"employee1","Hi",LocalDate.now(),LocalTime.now());
		Reply reply = new Reply(Long.valueOf(1),"employee1","Hello",LocalDate.now(),LocalTime.now());
		ArrayList<Reply> list = da.getReplies(post.getId().intValue());
		int initialSize = list.size();
		da.addReply(reply);
		list = da.getReplies(post.getId().intValue());
		int finalSize = list.size();
		assertThat(finalSize).isEqualTo(initialSize+1);
	}
}
