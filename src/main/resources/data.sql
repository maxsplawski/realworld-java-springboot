INSERT INTO articles (title, slug, description, body, created_at, updated_at)
VALUES ('How to train your dragon', 'how-to-train-your-dragon', 'Ever wonder how?', 'Just stumble upon one!', now(),
        now());
INSERT INTO articles (title, slug, description, body, created_at, updated_at)
VALUES ('Rocket science for dummies', 'rocket-science-for-dummies', 'Ever felt like going to space?',
        'Look mom, I am flying!', now(), now());
INSERT INTO articles (title, slug, description, body, created_at, updated_at)
VALUES ('No-flour bakery tips and tricks', 'no-flour-bakery-tips-and-tricks',
        'I like bread but I do not like kneading doughs :|', 'Fluffy as a stone!', now(), now());

INSERT INTO comments (article_id, body, created_at, updated_at)
VALUES (1, 'Cool movie! Although I was under the impression that this is an article about dragons.', now(), now());
INSERT INTO comments (article_id, body, created_at, updated_at)
VALUES (2, 'Just as I thought, the moon landing was a hoax!', now(), now());
INSERT INTO comments (article_id, body, created_at, updated_at)
VALUES (3, 'As tasty as a pineapple pizza :`)', now(), now());

INSERT INTO tags (name, created_at, updated_at)
VALUES ('dragons', now(), now());
INSERT INTO tags (name, created_at, updated_at)
VALUES ('space', now(), now());
INSERT INTO tags (name, created_at, updated_at)
VALUES ('bakery', now(), now());

INSERT INTO users (email, username, password, bio, image, roles, created_at, updated_at)
VALUES ('hiccup@mail.com', 'Hiccup', '$2a$12$NliWrWjwNQwpQk/TfcAUx.XJMIndmnBCfZ2T2MoNQJ44ue/3AcYXG',
        'Hello, from the skies!', 'https://www.images.com/image', 'ROLE_USER', now(), now());
INSERT INTO users (email, username, password, bio, image, roles, created_at, updated_at)
VALUES ('miro.hermaszewski@mail.com', 'MiroHermaszewski',
        '$2a$12$NliWrWjwNQwpQk/TfcAUx.XJMIndmnBCfZ2T2MoNQJ44ue/3AcYXG',
        'Hello, from space!', 'https://www.images.com/image', 'ROLE_USER', now(), now());
INSERT INTO users (email, username, password, bio, image, roles, created_at, updated_at)
VALUES ('breadfanatic@mail.com', 'iLoveBread', '$2a$12$NliWrWjwNQwpQk/TfcAUx.XJMIndmnBCfZ2T2MoNQJ44ue/3AcYXG',
        'Yum... yum... hello!', 'https://www.images.com/image', 'ROLE_USER', now(), now());

INSERT INTO followers (follower_id, followee_id)
VALUES (1, 2);
INSERT INTO followers (follower_id, followee_id)
VALUES (2, 3);
INSERT INTO followers (follower_id, followee_id)
VALUES (3, 1);