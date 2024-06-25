INSERT INTO article (title, slug, description, body, created_at, updated_at)
VALUES ('How to train your dragon', 'how-to-train-your-dragon', 'Ever wonder how?', 'This is how', now(), now());

INSERT INTO comment (article_id, body, created_at, updated_at)
VALUES (1, 'Cool movie!', now(), now());

INSERT INTO tag (name, created_at, updated_at)
VALUES ('dragons', now(), now());

INSERT INTO users (email, username, password, bio, image, roles, created_at, updated_at)
VALUES ('john.doe@mail.com', 'John Doe', '{bcrypt}$2a$12$NliWrWjwNQwpQk/TfcAUx.XJMIndmnBCfZ2T2MoNQJ44ue/3AcYXG',
        'Hello, there!', 'https://www.images.com/image', 'ROLE_USER', now(), now());