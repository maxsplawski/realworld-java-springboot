package github.maxsplawski.realworld.domain.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import github.maxsplawski.realworld.domain.article.Article;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followee_id")
    )
    @JsonManagedReference
    private Set<User> followedUsers = new HashSet<>();

    @ManyToMany(mappedBy = "followedUsers")
    @JsonBackReference
    private Set<User> followers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "favorite_articles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    @JsonManagedReference
    private Set<Article> favouriteArticles = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @JsonManagedReference
    private List<Article> articles;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String password;

    private String bio;

    private String image;

    private String roles;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public User() {
    }

    public User(String email, String username, String password, String bio, String image, String roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.image = image;
        this.roles = roles;
        this.createdAt = Instant.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Set<User> getFollowedUsers() {
        return followedUsers;
    }

    public void setFollowedUsers(Set<User> followedUsers) {
        this.followedUsers = followedUsers;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public void follow(User user) {
        this.followedUsers.add(user);
        user.getFollowers().add(this);
    }

    public void unfollow(User user) {
        this.followedUsers.remove(user);
        user.getFollowers().remove(this);
    }

    public Set<Article> getFavouriteArticles() {
        return favouriteArticles;
    }

    public void setFavouriteArticles(Set<Article> favoriteArticles) {
        this.favouriteArticles = favoriteArticles;
    }

    public void favoriteArticle(Article article) {
        this.favouriteArticles.add(article);
        article.getUsersWhoFavourited().add(this);
    }

    public void unfavouriteArticle(Article article) {
        this.favouriteArticles.remove(article);
        article.getUsersWhoFavourited().remove(this);
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public void addArticle(Article article) {
        this.articles.add(article);
        article.setAuthor(this);
    }

    public void removeArticle(Article article) {
        this.articles.remove(article);
        article.setAuthor(null);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
