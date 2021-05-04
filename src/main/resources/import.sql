INSERT INTO role (id, role_name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO role (id, role_name) VALUES (2, 'ROLE_USER');

INSERT INTO users (password, username, role_id) VALUES ('$2y$12$waSHuZCta/tX.POBvBMWA.4MINkdLQjdhY.C2rGT3z/I2PUlbVoOe', 'user1', 2);
INSERT INTO users (password, username, role_id) VALUES ('$2y$12$iaFgS/IYnuUjbjeK8WCUpeJl6TZcpBXTx9ArEiMhkHJ..XfdNgJTK', 'user2', 2);
INSERT INTO users (password, username, role_id) VALUES ('$2y$12$iyIsd2K8bxkh/16pdYxaX.bPChLkpgoTnOBesLSUyughhjLQVGnsS', 'admin1', 1);

INSERT INTO album (album_name, user_id) VALUES ('random', 1);
INSERT INTO album (album_name, user_id) VALUES ('cats', 2);

INSERT INTO photo (load_date, load_source, photo_name, album_id) VALUES ('2021-04-13 15:56:02', 'device', 'pom.xml', 1);
INSERT INTO photo (load_date, load_source, photo_name, album_id) VALUES ('2021-04-13 15:56:02', 'device', 'README.md', 1);
INSERT INTO photo (load_date, load_source, photo_name, album_id) VALUES ('2021-04-27 16:33:44', 'https://i.pinimg.com/474x/a0/1e/b9/a01eb920157d569f0c214bc48ef1dec4.jpg', 'sleepy cat.jpg', 2);
INSERT INTO photo (load_date, load_source, photo_name, album_id) VALUES ('2021-04-27 17:53:34', 'https://snob.ru/i/indoc/user_32134/6ba5ee528e106b4edbae938462f6b65a.jpg', 'high quality cat.jpg', 2);
