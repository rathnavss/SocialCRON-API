package br.com.agenciacodeplus.socialcron.controllers;

import br.com.agenciacodeplus.socialcron.acl.ACLPermissions;
import br.com.agenciacodeplus.socialcron.exceptions.EmptyFileException;
import br.com.agenciacodeplus.socialcron.photos.Photo;
import br.com.agenciacodeplus.socialcron.photos.PhotosService;
import br.com.agenciacodeplus.socialcron.posts.Post;
import br.com.agenciacodeplus.socialcron.posts.PostsService;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/v2/photos")
public class PhotosController {
  
  private PhotosService service;
  
  private PostsService postsService;
  
  @Autowired
  private ACLPermissions permissions;
  
  @Autowired
  public PhotosController(PhotosService service, PostsService postsService) {
    this.service = service;
    this.postsService = postsService;
  }
  
  /**
   * Save a photo and bind it to a post
   * @param id ID of post
   * @param file A org.springframework.web.multipart.MultipartFile, with uploaded image
   * @param authentication org.springframework.security.core.Authentication object to retrieve user 
   * to ACL operations
   * @return ResponseEntity<Void> with status code
   * @throws EmptyFileException Throw this exception in case of a empty upload
   * @throws IOException Throw this exception when isn't possible to save an image file
   * @throws URISyntaxException If path to save is invalid
   */
  @CrossOrigin
  @PostMapping
  @PreAuthorize("hasPermission(#id, 'br.com.agenciacodeplus.socialcron.posts.Post', 'write')")
  public @ResponseBody ResponseEntity<Void> save(
                            @RequestParam("postId") Long id,
                            @RequestParam("file") MultipartFile file,
                                                 Authentication authentication) 
                                                                        throws EmptyFileException,
                                                                               IOException,
                                                                               URISyntaxException {
    Post post = postsService.findOne(id);
    
    if(post != null) {
      Photo photo = new Photo();
      photo.setPost(post);
      photo.setFile(file);
      photo.saveFile();
      service.save(photo);
      permissions.add(authentication, photo);
      
      post.addPhoto(photo);
      postsService.save(post);
      
      return ResponseEntity.created(new URI("/v2/photos/" + post.getId())).build();
    }
    
    return ResponseEntity.badRequest().build();
    
  }
  
  /**
   * Retrieve the photo data
   * @param id ID of photo
   * @return ResponseEntity<Photo> with photo info JSON and status code
   * @throws IOException Throw this exception when isn't possible to retrieve an image file
   */
  @CrossOrigin
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @PreAuthorize(
            "hasPermission(#id, 'br.com.agenciacodeplus.socialcron.photos.Photo', 'read')")
  public @ResponseBody ResponseEntity<Photo> findOne(@PathVariable Long id) throws IOException {
    Photo photo = service.findOne(id);
    
    if(photo == null) {
      return new ResponseEntity<Photo>(HttpStatus.NOT_FOUND);
    }
    
    return ResponseEntity.ok(photo);
    
  }
  
  /**
   * Return all the photos of a post
   * @param id ID of post
   * @return  ResponseEntity<List<Photo>> with a list of photo info JSON and status code
   * @throws IOException Throw this exception when isn't possible to retrieve an image file
   */
  @CrossOrigin
  @RequestMapping(value = "/post/{id}", method = RequestMethod.GET)
  @PreAuthorize("hasAuthority('ADMIN') or "
              + "hasPermission(#id, 'br.com.agenciacodeplus.socialcron.posts.Post', 'read')")
  public ResponseEntity<List<Photo>> findAll(@PathVariable Long id) throws IOException {
    Post post = postsService.findOne(id);
    
    if(post == null) {
      return new ResponseEntity<List<Photo>>(HttpStatus.NOT_FOUND);
    }
    
    List<Photo> photos = service.findByPost(post);
    
    return ResponseEntity.ok(photos);
    
  }
  
}
