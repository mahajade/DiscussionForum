package ca.sheridancollege.mahajade.database;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.mahajade.beans.Post;
import ca.sheridancollege.mahajade.beans.Reply;

@Repository
public class DatabaseAccess {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbc;
	
	public ArrayList<Post> getPosts(){
		String query = "Select id, username, message, date, time from posts";
		HashMap<String, Object> map = new HashMap<String,Object>();
		ArrayList<Post> list = (ArrayList<Post>)jdbc.query(query, map, new BeanPropertyRowMapper<Post>(Post.class));
		return list;
	}

	public Post getPostById(int id) {
		String query = "Select username, message, date, time from posts where id=:id";
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("id", id);
		ArrayList<Post> list = (ArrayList<Post>)jdbc.query(query, map, new BeanPropertyRowMapper<Post>(Post.class));
		return list.get(0);
	}
	
	public void deletePost(int id) {
		String query = "Delete from posts where id = :id";
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("id", id);
		jdbc.update(query, map);
	}
	
	public void addPost(Post post) {
		String query = "Insert into posts(username, message, date, time) values(:username, :message, :date, :time)";
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("username", post.getUsername());
		map.put("message", post.getMessage());
		map.put("date", post.getDate());
		map.put("time", post.getTime());
		jdbc.update(query, map);
	}

	public void addReply(Reply reply) {
		String query = "Insert into replies values(:id, :username, :message, :date, :time)";
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("id", reply.getId());
		map.put("username", reply.getUsername());
		map.put("message", reply.getReply());
		map.put("date", reply.getDate());
		map.put("time", reply.getTime());
		jdbc.update(query, map);
	}
	
	public void edit(int id, Post post) {
		String query = "Update posts set message=:message where id=:id";
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("message", post.getMessage());
		map.put("id", post.getId());
		jdbc.update(query, map);
	}
	
	public ArrayList<Reply> getReplies(int id){
		String query = "Select id, username, reply, date, time from replies where id=:id";
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("id", id);
		ArrayList<Reply> list = (ArrayList<Reply>)jdbc.query(query, map, new BeanPropertyRowMapper<Reply>(Reply.class));
		return list;
	}

}
