package br.com.agenciacodeplus.socialcron.posts;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.agenciacodeplus.socialcron.photos.Photo;

@Entity
@Table(name = "posts")
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Post {
  
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;
  
  @Column(name = "title")
  @NotNull
  @Size(min = 3, max = 50)
  private String title;
  
  @Column(name = "content")
  @NotNull
  @Size(min = 1, max = 1000)
  private String content;
  
  @OneToMany(mappedBy = "post")
  @JsonIgnore
  private List<Photo> photos;
  
  public Post() {
    this.photos = new LinkedList<Photo>();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
  
  public List<Photo> getPhotos() {
    return photos;
  }

  public void setPhotos(List<Photo> photos) {
    this.photos = photos;
  }
  
  public void addPhoto(Photo photo) {
    photos.add(photo);
  }
  
  public void removePhoto(Photo photo) {
    photos.remove(photo);
  }

  public boolean equals(Object object) {
    if(!object.getClass().equals(Post.class)) return false;
    
    Post post = (Post) object;
    
    if(post.getId() == null) return false;
    
    return post.getId().equals(this.id);
  }
  
}
