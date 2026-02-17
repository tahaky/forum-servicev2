CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS threads (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    model_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS subthreads (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    thread_id UUID NOT NULL REFERENCES threads(id)
);

CREATE TABLE IF NOT EXISTS messages_table (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    upvote_count INT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at TIMESTAMP WITH TIME ZONE,
    subthread_id UUID NOT NULL REFERENCES subthreads(id)
);

CREATE TABLE IF NOT EXISTS message_vote (
    message_id UUID NOT NULL REFERENCES messages_table(id),
    user_id VARCHAR(255) NOT NULL,
    upvoted BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (message_id, user_id)
);

-- BMW M3 Thread
INSERT INTO threads (user_id, type, model_id, title)
VALUES ('ahmet', 'car-discussion', 'bmw-m3', 'BMW M3 Genel Yorumlar');

-- Tesla Model S Thread
INSERT INTO threads (user_id, type, model_id, title)
VALUES ('mehmet', 'car-discussion', 'tesla-model-s', 'Tesla Model S Deneyimleri');

-- Subthread ekleme
INSERT INTO subthreads (user_id, title, thread_id)
SELECT 'ahmet', 'Motor Performansı', id FROM threads WHERE model_id = 'bmw-m3';

INSERT INTO subthreads (user_id, title, thread_id)
SELECT 'mehmet', 'Batarya Ömrü', id FROM threads WHERE model_id = 'tesla-model-s';

-- Mesaj ekleme
INSERT INTO messages_table (user_id, body, subthread_id)
SELECT 'ahmet', 'BMW M3 gerçekten yüksek performanslı bir araç.', id
FROM subthreads WHERE title = 'Motor Performansı';

INSERT INTO messages_table (user_id, body, subthread_id)
SELECT 'mehmet', 'Tesla Model S batarya konusunda çok başarılı.', id
FROM subthreads WHERE title = 'Batarya Ömrü';
